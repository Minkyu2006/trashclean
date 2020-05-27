package kr.co.broadwave.aci.position;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.mastercode.MasterCode;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/position")
public class PositionRestController {

    private final ModelMapper modelMapper;
    private final PositionService positionService;
    private final MasterCodeService masterCodeService;
    private final AccountService accountService;

    @Autowired
    public PositionRestController(ModelMapper modelMapper,
                                  PositionService positionService,
                                  MasterCodeService masterCodeService,
                                  AccountService accountService) {
        this.modelMapper = modelMapper;
        this.positionService = positionService;
        this.masterCodeService = masterCodeService;
        this.accountService = accountService;
    }

    // 장비 저장
    @PostMapping ("reg")
    public ResponseEntity<Map<String,Object>> positionReg(@ModelAttribute PositionMapperDto positionMapperDto, HttpServletRequest request) {

        AjaxResponse res = new AjaxResponse();

        Position position = modelMapper.map(positionMapperDto, Position.class);
        position.setDeviceid(null);
        position.setInstalldate(null);

        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        // 로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        // 국가/지역 가져오기
        Optional<MasterCode> optionalPsCountry = masterCodeService.findById(positionMapperDto.getPsCountry());
        Optional<MasterCode> optionalPsLocation = masterCodeService.findById(positionMapperDto.getPsLocation());

        // 국가/지역코드가 존재하지않으면
        if (!optionalPsCountry.isPresent() || !optionalPsLocation.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E032.getCode(),ResponseErrorCode.E032.getDesc()));
        }else{
            // 국가/지역 저장
            position.setPsCountry(optionalPsCountry.get());
            position.setPsLocation(optionalPsLocation.get());
        }

        // 장비번호 가져오기(고유값)
        Optional<Position> optionalPosition = positionService.findByPsZoneCode(position.getPsZoneCode());
        //신규 및 수정여부
        if (optionalPosition.isPresent()) {
            //수정
            position.setId(optionalPosition.get().getId());
            position.setInsert_id(optionalPosition.get().getInsert_id());
            position.setInsertDateTime(optionalPosition.get().getInsertDateTime());
            position.setModify_id(currentuserid);
            position.setModifyDateTime(LocalDateTime.now());
        }else{
            //신규
            position.setInsert_id(currentuserid);
            position.setInsertDateTime(LocalDateTime.now());
            position.setModify_id(currentuserid);
            position.setModifyDateTime(LocalDateTime.now());
        }

        positionService.save(position);

        return ResponseEntity.ok(res.success());
    }

    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> positionList(@RequestParam (value="psZoneCode", defaultValue="") String psZoneCode,
                                                       @RequestParam (value="psZoneName", defaultValue="")String psZoneName,
                                                       @RequestParam (value="psCountry", defaultValue="")String psCountry,
                                                       @RequestParam (value="psLocation", defaultValue="")String psLocation,
                                                       @PageableDefault Pageable pageable){
        Long psCountryId = null;
        Long psLocationId = null;

        if(!psCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(psCountry);
            psCountryId = emCountrys.map(MasterCode::getId).orElse(null);
        }
        if(!psLocation.equals("")){
            Optional<MasterCode> emLocations = masterCodeService.findByCode(psLocation);
            psLocationId = emLocations.map(MasterCode::getId).orElse(null);
        }

        Page<PositionListDto> positionListDtos = positionService.findByPositionSearch(psZoneCode,psZoneName,psLocationId,psCountryId,pageable);

        return CommonUtils.ResponseEntityPage(positionListDtos);
    }

    // 거점 정보 보기
    @PostMapping ("info")
    public ResponseEntity<Map<String,Object>> positionInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        PositionDto positionDto = positionService.findByPositionInfo(id);
//        log.info("positionDto : "+positionDto);
//        log.info("받아온 아이디값 : "+id);

        data.put("positionDto",positionDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

//    // 장비 삭제
//    @PostMapping("del")
//    public ResponseEntity<Map<String,Object>> equipmentDel(@RequestParam(value="emNumber", defaultValue="") String emNumber){
//        AjaxResponse res = new AjaxResponse();
//
//        Optional<Equipment> optionalEquipment = equipmentService.findByEmNumber(emNumber);
//
//        if (!optionalEquipment.isPresent()){
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
//        }
//        equipmentService.delete(optionalEquipment.get());
//        return ResponseEntity.ok(res.success());
//    }


}
