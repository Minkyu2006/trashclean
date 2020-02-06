package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.awsiot.ACIAWSIoTDeviceService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.company.CompanyDto;
import kr.co.broadwave.aci.company.CompanyListDto;
import kr.co.broadwave.aci.company.CompanyService;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.imodel.IModelChangeDto;
import kr.co.broadwave.aci.imodel.IModelService;
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

    private final DashboardService dashboardService;
    private final ModelMapper modelMapper;
    private final EquipmentService equipmentService;
    private final AccountService accountService;
    private final CompanyService companyService;
    private final MasterCodeService masterCodeService;
    private final ACIAWSIoTDeviceService aciawsIoTDeviceService;
    private final IModelService iModelService;
    @Autowired
    public EquipmentRestController(ModelMapper modelMapper,
                                   AccountService accountService,
                                   IModelService iModelService,
                                   CompanyService companyService,
                                   DashboardService dashboardService,
                                   MasterCodeService masterCodeService,
                                   EquipmentService equipmentService, ACIAWSIoTDeviceService aciawsIoTDeviceService) {
        this.accountService = accountService;
        this.dashboardService = dashboardService;
        this.companyService = companyService;
        this.iModelService = iModelService;
        this.masterCodeService = masterCodeService;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
        this.aciawsIoTDeviceService = aciawsIoTDeviceService;
    }

    // 장비 저장
    @PostMapping ("reg")
    public ResponseEntity<Map<String,Object>> equipmentReg(@ModelAttribute EquipmentMapperDto equipmentMapperDto, HttpServletRequest request) throws Exception {
        AjaxResponse res = new AjaxResponse();

        Equipment equipment = modelMapper.map(equipmentMapperDto, Equipment.class);

        if(equipment.getVInterval()==null){
            equipment.setVInterval(60.0);
        }
        if(equipment.getVPresstime()==null){
            equipment.setVPresstime(3.0);
        }
        if(equipment.getVInputtime()==null){
            equipment.setVInputtime(10.0);
        }
        if(equipment.getVSolenoidtime()==null){
            equipment.setVSolenoidtime(5.0);
        }
        if(equipment.getVYellowstart()==null){
            equipment.setVYellowstart(61.0);
        }
        if(equipment.getVRedstart()==null){
            equipment.setVRedstart(81.0);
        }

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

        //장비타입/국가/지역코드가 존재하지않으면
        if (!optionalEmType.isPresent() || !optionalEmCountry.isPresent() || !optionalEmLocation.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E016.getCode(),
                    ResponseErrorCode.E016.getDesc()));
        }else{
            // 장비타입/국가/지역 저장
            equipment.setEmType(optionalEmType.get());
            equipment.setEmCountry(optionalEmCountry.get());
            equipment.setEmLocation(optionalEmLocation.get());
        }

        // 장비번호 가져오기(고유값)
        Optional<Equipment> optionalEquipment = equipmentService.findByEmNumber(equipment.getEmNumber());
        //신규 및 수정여부
        if (optionalEquipment.isPresent()) {
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

        // 모델저장
        Optional<IModel>  iModels = iModelService.findByModel(equipmentMapperDto.getMdId());
        //log.info("iModelDto : "+iModelDto);

        //모델이 존재하지않으면
        if (!iModels.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E021.getCode(), ResponseErrorCode.E021.getDesc()));
        }else{
            IModel iModel = iModels.get();
            equipment.setMdId(iModel);
        }

        equipmentService.save(equipment);

        return ResponseEntity.ok(res.success());
    }

    // 장비기본값 저장
    @PostMapping ("basereg")
    public ResponseEntity<Map<String,Object>> baseReg(@ModelAttribute EquipmentBaseMapperDto equipmentBaseMapperDto,
                                                      HttpServletRequest request) throws Exception {
        AjaxResponse res = new AjaxResponse();

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        Equipment equipment = modelMapper.map(equipmentBaseMapperDto, Equipment.class);

        List<EquipmentBaseDto> equipmentBaseDto = equipmentService.EquipmentBaseSettingQuerydsl(equipmentBaseMapperDto.getEmNumbers());
        //log.info("equipmentBaseDto : "+equipmentBaseDto);

        CodeType codeType = CodeType.valueOf("C0013");
        List<MasterCodeDto> masterCodes= masterCodeService.findCodeList(codeType);
        Double vInterval = Double.parseDouble(masterCodes.get(0).getName());
        Double vPresstime = Double.parseDouble(masterCodes.get(1).getName());
        Double vInputtime = Double.parseDouble(masterCodes.get(2).getName());
        Double vSolenoidtime = Double.parseDouble(masterCodes.get(3).getName());
        Double vYellowstart = Double.parseDouble(masterCodes.get(4).getName());
        Double vRedstart = Double.parseDouble(masterCodes.get(5).getName());

        for(int i=0; i<equipmentBaseDto.size(); i++){
            equipment.setId(equipmentBaseDto.get(i).getId());
            equipment.setEmNumber(equipmentBaseDto.get(i).getEmNumber());
            equipment.setEmCerealNumber(equipmentBaseDto.get(i).getEmCerealNumber());
            equipment.setEmDesignation(equipmentBaseDto.get(i).getEmDesignation());
            equipment.setEmType(equipmentBaseDto.get(i).getEmTypeId());
            equipment.setEmAwsNumber(equipmentBaseDto.get(i).getEmAwsNumber());
            equipment.setEmInstallDate(equipmentBaseDto.get(i).getEmInstallDate());
            equipment.setEmCountry(equipmentBaseDto.get(i).getEmCountryId());
            equipment.setEmLocation(equipmentBaseDto.get(i).getEmLocationId());
            equipment.setEmEmbeddedNumber(equipmentBaseDto.get(i).getEmEmbeddedNumber());
            equipment.setCompany(equipmentBaseDto.get(i).getCompany());
            equipment.setEmSubName(equipmentBaseDto.get(i).getEmSubName());
            equipment.setMdId(equipmentBaseDto.get(i).getMdId());
            equipment.setEmLatitude(equipmentBaseDto.get(i).getEmLatitude());
            equipment.setEmHardness(equipmentBaseDto.get(i).getEmHardness());

            if(equipment.getVInterval()==null){
                equipment.setVInterval(vInterval);
            }
            if(equipment.getVPresstime()==null){
                equipment.setVPresstime(vPresstime);
            }
            if(equipment.getVInputtime()==null){
                equipment.setVInputtime(vInputtime);
            }
            if(equipment.getVSolenoidtime()==null){
                equipment.setVSolenoidtime(vSolenoidtime);
            }
            if(equipment.getVYellowstart()==null){
                equipment.setVYellowstart(vYellowstart);
            }
            if(equipment.getVRedstart()==null){
                equipment.setVRedstart(vRedstart);
            }

            equipment.setInsert_id(equipmentBaseDto.get(i).getInsert_id());
            equipment.setInsertDateTime(equipmentBaseDto.get(i).getInsertDateTime());
            equipment.setModify_id(currentuserid);
            equipment.setModifyDateTime(LocalDateTime.now());

            equipmentService.save(equipment);
        }
        return ResponseEntity.ok(res.success());
    }

    // 기본값셋팅페이지의 장비리스트
    @PostMapping("baselist")
    public ResponseEntity<Map<String,Object>> baselist(@RequestParam (value="emNumber", defaultValue="") String emNumber,
                                                            @RequestParam (value="emType", defaultValue="")String emType,
                                                            @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                                            @RequestParam (value="emLocation", defaultValue="")String emLocation,
                                                            @PageableDefault Pageable pageable){

        Long emTypeId = null;
        Long emCountryId = null;
        Long emLocationId = null;

        if(!emType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(emType);
            emTypeId = emTypes.map(MasterCode::getId).orElse(null);
        }
        if(!emCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(emCountry);
            emCountryId = emCountrys.map(MasterCode::getId).orElse(null);
        }
        if(!emLocation.equals("")){
            Optional<MasterCode> emLocations = masterCodeService.findByCode(emLocation);
            emLocationId = emLocations.map(MasterCode::getId).orElse(null);
        }

        Page<EquipmentBaseListDto> equipmentListDtos =
                equipmentService.findByBaseEquipmentSearch(emNumber,emLocationId,emTypeId,emCountryId,pageable);

        return CommonUtils.ResponseEntityPage(equipmentListDtos);
    }

    // 장비 리스트
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> equipmentList(@RequestParam (value="emNumber", defaultValue="") String emNumber,
                                                        @RequestParam (value="emDesignation", defaultValue="") String  emDesignation,
                                                        @RequestParam (value="emType", defaultValue="")String emType,
                                                        @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                                        @PageableDefault Pageable pageable){

        Long emTypeId = null;
        Long emCountryId = null;

        if(!emType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(emType);
            emTypeId = emTypes.map(MasterCode::getId).orElse(null);
        }
        if(!emCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(emCountry);
            emCountryId = emCountrys.map(MasterCode::getId).orElse(null);
        }

        Page<EquipmentListDto> equipmentListDtos =
                equipmentService.findByEquipmentSearch(emNumber,emDesignation,emTypeId,emCountryId,pageable);

        return CommonUtils.ResponseEntityPage(equipmentListDtos);
    }

    // 운영사 리스트
    @PostMapping("agencyList")
    public ResponseEntity<Map<String,Object>> agencyList(@RequestParam (value="csNumber", defaultValue="") String csNumber,
                                        @RequestParam (value="csOperator", defaultValue="") String  csOperator,
                                        @PageableDefault Pageable pageable){

        Page<CompanyListDto> companyListDtos = equipmentService.findByAgencySearch(csNumber,csOperator,pageable);

        return CommonUtils.ResponseEntityPage(companyListDtos);
    }

    // 장비 정보 보기
    @PostMapping ("info")
    public ResponseEntity<Map<String,Object>> equipmentInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        EquipmentDto equipment = equipmentService.findById(id);
//        log.info("equipment : "+equipment);
//        log.info("받아온 아이디값 : "+id);

        data.put("equipment",equipment);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 소속운영사 관리코드-운영사명 따오기
    @PostMapping ("agencyInfo")
    public ResponseEntity<Map<String,Object>> agencyInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        CompanyDto companyDto = companyService.findById(id);

        data.put("company",companyDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 장비 삭제
    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> equipmentDel(@RequestParam(value="emNumber", defaultValue="") String emNumber){
        AjaxResponse res = new AjaxResponse();

        Optional<Equipment> optionalEquipment = equipmentService.findByEmNumber(emNumber);

        if (!optionalEquipment.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }
        equipmentService.delete(optionalEquipment.get());
        return ResponseEntity.ok(res.success());
    }

    // 국가 셀렉트 선택시 지역변경
    @PostMapping("location")
    public ResponseEntity<Map<String,Object>> location(@RequestParam(value="emCountry", defaultValue="") Long emCountry){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Optional<MasterCode> optionalCountry= masterCodeService.findById(emCountry);
        CodeType codeType = CodeType.valueOf("C0005");
        String conuntyCode = optionalCountry.map(MasterCode::getCode).orElse(null);
        List<MasterCodeDto> ref = masterCodeService.findAllByCodeTypeEqualsAndBcRef1(codeType,conuntyCode);

        data.put("dataselect",ref);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 타입 셀렉트 선택시 모델변경
    @PostMapping("modelchange")
    public ResponseEntity<Map<String,Object>> modelchange(@RequestParam(value="emType", defaultValue="") Long emType){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Optional<MasterCode> optionalEmType= masterCodeService.findById(emType);

        if(optionalEmType.isPresent()){
            List<IModelChangeDto> ref = iModelService.findByEmType(optionalEmType.get());
            data.put("dataselect",ref);
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E021.getCode(), ResponseErrorCode.E021.getDesc()));
        }

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    //장비 고유아이디값 리스트부르기
    @PostMapping("devicelist")
    public ResponseEntity<Map<String,Object>> devicelist() {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<Equipment> equipment = dashboardService.findAll();

        data.put("equipment",equipment);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    //aws deivce Shadow 상태
    @PostMapping("awsdevicestatus")
    public ResponseEntity<Map<String,Object>> awsDeviceStatus(@RequestParam(value="deviceid", defaultValue="") String deviceid) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            HashMap deviceList = aciawsIoTDeviceService.getDeviceStatus(deviceid);
            dataLocal.put("datastate",deviceList.get("state"));

            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 문열기 단기 요청
    @PostMapping("shdoor")
    public ResponseEntity<Map<String,Object>> awsSetDoorOpen(@RequestParam(value="deviceid", defaultValue="") String deviceid
                                        ,@RequestParam(value="door", defaultValue="") String door) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setDeviceDoor(deviceid,door);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }

    }

    //Shadow 데이터 요청
    @PostMapping("shdatareq")
    public ResponseEntity<Map<String,Object>> awsSetDataReq(@RequestParam(value="deviceid", defaultValue="") String deviceid
            ,@RequestParam(value="ts", defaultValue="") String ts) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setDataRequest(deviceid,ts);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }

    }

    // 액츄에이터리셋버튼
    @PostMapping("actuatorReset")
    public ResponseEntity<Map<String,Object>> actuatorReset(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                            @RequestParam(value="timestamp", defaultValue="") String timestamp) throws Exception {
        AjaxResponse res = new AjaxResponse();

//        log.info("deviceid : "+deviceid);
//        log.info("timestamp : "+timestamp);

        //Shadow Isolarbin 액츄에이터리셋(IoT) -> param : 디바이스 아이디, 타임스탬프
        aciawsIoTDeviceService.setActuatorReset(deviceid,timestamp);

        return ResponseEntity.ok(res.success());
    }

}
