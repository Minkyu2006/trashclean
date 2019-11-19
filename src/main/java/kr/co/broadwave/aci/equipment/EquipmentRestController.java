package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.bscodes.CommonCode;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.company.*;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 14:22
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/equipment")
public class EquipmentRestController {

    private AjaxResponse res = new AjaxResponse();
    private HashMap<String, Object> data = new HashMap<>();

    private final ModelMapper modelMapper;
    private final EquipmentService equipmentService;
    private final AccountService accountService;
    private final CompanyService companyService;
    private final MasterCodeService masterCodeService;

    @Autowired
    public EquipmentRestController(ModelMapper modelMapper,
                                   AccountService accountService,
                                   CompanyService companyService,
                                   MasterCodeService masterCodeService,
                                   EquipmentService equipmentService) {
        this.accountService = accountService;
        this.companyService = companyService;
        this.masterCodeService = masterCodeService;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
    }

    // 업체 저장
    @PostMapping ("reg")
    public ResponseEntity equipmentReg(@ModelAttribute EquipmentMapperDto equipmentMapperDto,HttpServletRequest request){

        Equipment equipment = modelMapper.map(equipmentMapperDto, Equipment.class);

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        // 장비타입/국가/지역 가져오기
        Optional<MasterCode> optionalEmType = masterCodeService.findById(equipmentMapperDto.getEmType());
        Optional<MasterCode> optionalEmCountry = masterCodeService.findById(equipmentMapperDto.getEmCountry());
        Optional<MasterCode> optionalEmLocation = masterCodeService.findById(equipmentMapperDto.getEmLocation());
        Optional<MasterCode> optionalEmUnit = masterCodeService.findById(equipmentMapperDto.getEmUnit());

        //장비타입/국가/지역코드가 존재하지않으면
        if (!optionalEmType.isPresent() || !optionalEmCountry.isPresent() || !optionalEmLocation.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E016.getCode(),
                    ResponseErrorCode.E016.getDesc()));
        }else{
            // 장비타입/국가/지역 저장
            equipment.setEmType(optionalEmType.get());
            equipment.setEmCountry(optionalEmCountry.get());
            equipment.setEmLocation(optionalEmLocation.get());
            equipment.setEmUnit(optionalEmUnit.get());
        }

        // 장비번호 가져오기(고유값)
        Optional<Equipment> optionalEquipment = equipmentService.findByEmNumber(equipment.getEmNumber());
        //신규 및 수정여부
        if (optionalEquipment.isPresent()){
            //수정
            equipment.setId(optionalEquipment.get().getId());
            equipment.setInsert_id(optionalEquipment.get().getInsert_id());
            equipment.setInsertDateTime(optionalEquipment.get().getInsertDateTime());
            equipment.setModify_id(currentuserid);
            equipment.setModifyDateTime(LocalDateTime.now());
        }else{
            //신규
            equipment.setInsert_id(currentuserid);
            equipment.setInsertDateTime(LocalDateTime.now());
            equipment.setModify_id(currentuserid);
            equipment.setModifyDateTime(LocalDateTime.now());
        }

        // 소속운영사 아이디저장하기
        Optional<Company> optionalCompany = companyService.findByCsOperator(equipmentMapperDto.getOperator());
        //운영사가 존재하지않으면
        if (!optionalCompany.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E005.getCode(), ResponseErrorCode.E005.getDesc()));
        }else{
            Company company = optionalCompany.get();
            equipment.setCompany(company);
        }

        Equipment save = equipmentService.save(equipment);

        log.info("장비등록 데이터 : "+save.toString());
        return ResponseEntity.ok(res.success());
    }

    // 업체 리스트
    @PostMapping("list")
    public ResponseEntity equipmentList(@RequestParam (value="emNumber", defaultValue="") String emNumber,
                                                        @RequestParam (value="emDesignation", defaultValue="") String  emDesignation,
                                                        @RequestParam (value="emType", defaultValue="")String emType,
                                                        @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                                        @PageableDefault Pageable pageable){

        Long emTypeId = null;
        Long emCountryId = null;

        if(!emType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(emType);
            emTypeId = emTypes.get().getId();
        }
        if(!emCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(emCountry);
            emCountryId = emCountrys.get().getId();
        }

        Page<EquipmentListDto> equipmentListDtos =
                equipmentService.findByEquipmentSearch(emNumber,emDesignation,emTypeId,emCountryId,pageable);

        return CommonUtils.ResponseEntityPage(equipmentListDtos);
    }

    // 운영사 리스트
    @PostMapping("agencyList")
    public ResponseEntity agencyList(@RequestParam (value="csNumber", defaultValue="") String csNumber,
                                        @RequestParam (value="csOperator", defaultValue="") String  csOperator,
                                        @PageableDefault Pageable pageable){

        Page<CompanyListDto> companyListDtos = equipmentService.findByAgencySearch(csNumber,csOperator,pageable);

        return CommonUtils.ResponseEntityPage(companyListDtos);
    }

    // 업체 정보 보기
    @PostMapping ("info")
    public ResponseEntity equipmentInfo(@RequestParam (value="id", defaultValue="") Long id){

        EquipmentDto equipment = equipmentService.findById(id);
        log.info("받아온 아이디값 : "+id);

        data.clear();
        data.put("equipment",equipment);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 소속운영사 관리코드-운영사명 따오기
    @PostMapping ("agencyInfo")
    public ResponseEntity agencyInfo(@RequestParam (value="id", defaultValue="") Long id){

        CompanyDto companyDto = companyService.findById(id);

        data.clear();
        data.put("company",companyDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 업체 삭제
    @PostMapping("del")
    public ResponseEntity equipmentDel(@RequestParam(value="emNumber", defaultValue="") String emNumber){

        Optional<Equipment> optionalEquipment = equipmentService.findByEmNumber(emNumber);

        if (!optionalEquipment.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }
        equipmentService.delete(optionalEquipment.get());
        return ResponseEntity.ok(res.success());
    }

    @PostMapping("location")
    public ResponseEntity location(@RequestParam(value="emCountry", defaultValue="") Long emCountry){
        Optional<MasterCode> optionalCountry= masterCodeService.findById(emCountry);
        CodeType codeType = CodeType.valueOf("C0005");

        List<MasterCode> ref = masterCodeService.findAllByCodeTypeEqualsAndBcRef1(codeType,optionalCountry.get().getCode());

        data.clear();
        data.put("dataselect",ref);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

}
