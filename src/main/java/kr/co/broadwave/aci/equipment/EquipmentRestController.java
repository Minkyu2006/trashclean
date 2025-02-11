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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                                   MasterCodeService masterCodeService,
                                   EquipmentService equipmentService, ACIAWSIoTDeviceService aciawsIoTDeviceService) {
        this.accountService = accountService;
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
        CodeType codeType = CodeType.valueOf("C0015");
        Optional<MasterCode> state = masterCodeService.findByCoAndCodeTypeAndCode(codeType,"TS01");
        //장비타입/국가/지역코드가 존재하지않으면
        if (!optionalEmType.isPresent() || !optionalEmCountry.isPresent() || !optionalEmLocation.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E016.getCode(),ResponseErrorCode.E016.getDesc()));
        }else{
            if(optionalEmType.get().getCode().equals("ISOL")){
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
            }else if(optionalEmType.get().getCode().equals("ITAI")){
                if(equipment.getVMqttInterval().equals("")){
                    equipment.setVMqttInterval("3600");
                }
                if(equipment.getVLoraInterval().equals("")){
                    equipment.setVLoraInterval("10");
                }
                if(equipment.getVScaleSafeInterval().equals("")){
                    equipment.setVScaleSafeInterval("65");
                }
                if(equipment.getVShutterIdleInterval().equals("")){
                    equipment.setVShutterIdleInterval("60");
                }
                if(equipment.getVWastePressInterval().equals("")){
                    equipment.setVWastePressInterval("3");
                }
                if(equipment.getVWasteCapacity().equals("")){
                    equipment.setVWasteCapacity("5000000");
                }
                if(equipment.getVOzonTime().equals("")){
                    equipment.setVOzonTime("7");
                }
                if(equipment.getVPayPreamt().equals("")){
                    equipment.setVPayPreamt("8000");
                }
                if(equipment.getVPayUnitPrice().equals("")){
                    equipment.setVPayUnitPrice("25");
                }
                if(equipment.getVPayUnitWeight().equals("")){
                    equipment.setVPayUnitWeight("100");
                }
                if(equipment.getVPayMethod().equals("")){
                    equipment.setVPayMethod("");
                }
            }else{
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(),ResponseErrorCode.E004.getDesc()));
            }

            // 장비타입/국가/지역 저장/설비상태
            equipment.setEmType(optionalEmType.get());
            equipment.setEmCountry(optionalEmCountry.get());
            equipment.setEmLocation(optionalEmLocation.get());
            if(!state.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E030.getCode(),ResponseErrorCode.E030.getDesc()));
            }else{
                equipment.setEmState(state.get());
            }

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
        Optional<IModel>  iModels = iModelService.findByModel(equipmentMapperDto.getiModel());
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

        CodeType codeType = CodeType.valueOf("C0013");
        List<MasterCodeDto> masterCodes= masterCodeService.findCodeList(codeType);
        double vInterval;
        double vPresstime;
        double vInputtime;
        double vSolenoidtime;
        double vYellowstart;
        double vRedstart;

        for (EquipmentBaseDto baseDto : equipmentBaseDto) {
            equipment.setId(baseDto.getId());
            equipment.setEmNumber(baseDto.getEmNumber());
            equipment.setEmCerealNumber(baseDto.getEmCerealNumber());
            equipment.setEmDesignation(baseDto.getEmDesignation());
            equipment.setEmType(baseDto.getEmTypeId());
            equipment.setEmAwsNumber(baseDto.getEmAwsNumber());
            equipment.setEmInstallDate(baseDto.getEmInstallDate());
            equipment.setEmCountry(baseDto.getEmCountryId());
            equipment.setEmLocation(baseDto.getEmLocationId());
            equipment.setEmEmbeddedNumber(baseDto.getEmEmbeddedNumber());
            equipment.setCompany(baseDto.getCompany());
            equipment.setEmDashboard(baseDto.getEmDashboard());
            equipment.setEmSubName(baseDto.getEmSubName());
            equipment.setMdId(baseDto.getMdId());
            equipment.setEmLatitude(baseDto.getEmLatitude());
            equipment.setEmLongitude(baseDto.getEmLongitude());
            equipment.setEmCertificationNumber(baseDto.getEmCertificationNumber());

            if (equipment.getVInterval() == null) {
                vInterval = Double.parseDouble(masterCodes.get(0).getName());
                equipment.setVInterval(vInterval);
            }
            if (equipment.getVPresstime() == null) {
                vPresstime = Double.parseDouble(masterCodes.get(1).getName());
                equipment.setVPresstime(vPresstime);
            }
            if (equipment.getVInputtime() == null) {
                vInputtime = Double.parseDouble(masterCodes.get(2).getName());
                equipment.setVInputtime(vInputtime);
            }
            if (equipment.getVSolenoidtime() == null) {
                vSolenoidtime = Double.parseDouble(masterCodes.get(3).getName());
                equipment.setVSolenoidtime(vSolenoidtime);
            }
            if (equipment.getVYellowstart() == null) {
                vYellowstart = Double.parseDouble(masterCodes.get(4).getName());
                equipment.setVYellowstart(vYellowstart);
            }
            if (equipment.getVRedstart() == null) {
                vRedstart = Double.parseDouble(masterCodes.get(5).getName());
                equipment.setVRedstart(vRedstart);
            }

            equipment.setInsert_id(baseDto.getInsert_id());
            equipment.setInsertDateTime(baseDto.getInsertDateTime());
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
                                                            @RequestParam (value="emLocation", defaultValue="")String emLocation){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        Long emTypeId = null;
        Long emCountryId = null;
        Long emLocationId = null;

//        if(!emType.equals("")){
//            Optional<MasterCode> emTypes = masterCodeService.findByCode(emType);
//            emTypeId = emTypes.map(MasterCode::getId).orElse(null);
//        }
        if(!emCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(emCountry);
            emCountryId = emCountrys.map(MasterCode::getId).orElse(null);
        }
        if(!emLocation.equals("")){
            Optional<MasterCode> emLocations = masterCodeService.findByCode(emLocation);
            emLocationId = emLocations.map(MasterCode::getId).orElse(null);
        }

        List<EquipmentBaseListDto> equipmentListDtos =
                equipmentService.findByBaseEquipmentSearch(emNumber,emLocationId,emType,emCountryId);
//        log.info("equipmentListDtos : "+equipmentListDtos);

        data.put("equipmentListDtos",equipmentListDtos);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 장비 리스트
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> equipmentList(@RequestParam (value="emNumber", defaultValue="") String emNumber,
                                                            @RequestParam (value="emDesignation", defaultValue="") String  emDesignation,
                                                            @RequestParam (value="emType", defaultValue="")String emType,
                                                            @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                                            @RequestParam (value="emDashboard", defaultValue="")String emDashboard,
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
                equipmentService.findByEquipmentSearch(emNumber,emDesignation,emTypeId,emCountryId,emDashboard,pageable);

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
            List<IModelChangeDto> ref = iModelService.findByEmTypeQuerydsl(optionalEmType.get());
//            log.info("ref : "+ref);
            if(ref.size()==0){
                data.put("dataselect",null);
            }else{
                data.put("dataselect",ref);
                data.put("mdType",ref.get(0).getMdName().substring(0,4));
            }
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E021.getCode(), ResponseErrorCode.E021.getDesc()));
        }

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    //장비 고유아이디값 리스트부르기
    @PostMapping("devicelist")
    public ResponseEntity<Map<String,Object>> devicelist(@RequestParam(value="codeType", defaultValue="") String codeType) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<EquipmentEmNumberListDto> equipment = equipmentService.findByDeviceNumber(codeType);

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
            log.info("deviceList : "+deviceList);
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

    // 장비확인 버튼(devicereg : 타임스탬프)
    @PostMapping("deviceCheck")
    public ResponseEntity<Map<String,Object>> deviceCheck(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                          @RequestParam(value="timestamp", defaultValue="") String timestamp) throws Exception {
        AjaxResponse res = new AjaxResponse();

//        log.info("장비코드 : "+deviceid);
//        log.info("timestamp : "+timestamp);
        //Shadow Isolarbin LED 점멸 (IoT) -> param :  디바이스 아이디, 타임스탬프
        aciawsIoTDeviceService.setRegComplete(deviceid,timestamp);

        return ResponseEntity.ok(res.success());
    }

    //Shadow 아이테이너 오존살포
    @PostMapping("ozone")
    public ResponseEntity<Map<String,Object>> ozone(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                    @RequestParam(value="timestamp", defaultValue="") String timestamp,
                                                    @RequestParam(value="ozoneVal", defaultValue="") String ozoneVal) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();
        if(ozoneVal.equals("")){
            ozoneVal="0";
        }

        try{
            aciawsIoTDeviceService.setOzone(deviceid,timestamp,ozoneVal);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 카드제거
    @PostMapping("cardremove")
    public ResponseEntity<Map<String,Object>> cardremove(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                    @RequestParam(value="timestamp", defaultValue="") String timestamp) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setCardremove(deviceid,timestamp);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 결제도어
    @PostMapping("doorpay")
    public ResponseEntity<Map<String,Object>> doorpay(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                         @RequestParam(value="doorVal", defaultValue="") String doorVal) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setDoorpay(deviceid,doorVal);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 폐기물투입문
    @PostMapping("doorinput")
    public ResponseEntity<Map<String,Object>> doorinput(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                         @RequestParam(value="doorVal", defaultValue="") String doorVal) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setDoorinput(deviceid,doorVal);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 저울뒤집기
    @PostMapping("saleTurn")
    public ResponseEntity<Map<String,Object>> saleTurn(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                         @RequestParam(value="timestamp", defaultValue="") String timestamp) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setSaleTurn(deviceid,timestamp);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 화재제어
    @PostMapping("firlCtrl")
    public ResponseEntity<Map<String,Object>> firlCtrl(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                       @RequestParam(value="timestamp", defaultValue="") String timestamp,
                                                       @RequestParam(value="firl_ctrl", defaultValue="") String firl_ctrl) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setFirlCtrl(deviceid,timestamp,firl_ctrl);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 악취제어
    @PostMapping("stinkCtrl")
    public ResponseEntity<Map<String,Object>> stinkCtrl(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                       @RequestParam(value="timestamp", defaultValue="") String timestamp,
                                                       @RequestParam(value="stink_ctrl", defaultValue="") String stink_ctrl) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setStinkCtrl(deviceid,timestamp,stink_ctrl);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 비상정지
    @PostMapping("emergCtrl")
    public ResponseEntity<Map<String,Object>> emergCtrl(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                       @RequestParam(value="timestamp", defaultValue="") String timestamp,
                                                       @RequestParam(value="emerg_ctrl", defaultValue="") String emerg_ctrl) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setEmergCtrl(deviceid,timestamp,emerg_ctrl);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 시스템제어
    @PostMapping("systemCtrl")
    public ResponseEntity<Map<String,Object>> systemCtrl(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                       @RequestParam(value="timestamp", defaultValue="") String timestamp,
                                                       @RequestParam(value="system_ctrl", defaultValue="") String system_ctrl) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setSystemCtrl(deviceid,timestamp,system_ctrl);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 엑추에이터제어
    @PostMapping("actuatorAct")
    public ResponseEntity<Map<String,Object>> actuatorAct(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                       @RequestParam(value="timestamp", defaultValue="") String timestamp,
                                                       @RequestParam(value="actuator_act", defaultValue="") String actuator_act) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setActuatorAct(deviceid,timestamp,actuator_act);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

    //Shadow 아이테이너 LED제어
    @PostMapping("ledCtrl")
    public ResponseEntity<Map<String,Object>> ledCtrl(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                       @RequestParam(value="timestamp", defaultValue="") String timestamp,
                                                       @RequestParam(value="led_type", defaultValue="") String led_type,
                                                       @RequestParam(value="led_ctrl", defaultValue="") String led_ctrl) {
        AjaxResponse resLocal = new AjaxResponse();
        HashMap<String, Object> dataLocal = new HashMap<>();

        try{
            aciawsIoTDeviceService.setLedCtrl(deviceid,timestamp,led_type,led_ctrl);
            resLocal.addResponse("data",dataLocal);
            return ResponseEntity.ok(resLocal.success());
        }catch(Exception e){
            return ResponseEntity.ok(resLocal.fail(ResponseErrorCode.E020.getCode(),ResponseErrorCode.E020.getDesc()));
        }
    }

}
