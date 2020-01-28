package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountDtoCollectionList;
import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.equipment.EquipmentDto;
import kr.co.broadwave.aci.equipment.EquipmentListDto;
import kr.co.broadwave.aci.equipment.EquipmentService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import kr.co.broadwave.aci.vehicle.Vehicle;
import kr.co.broadwave.aci.vehicle.VehicleDto;
import kr.co.broadwave.aci.vehicle.VehicleListDto;
import kr.co.broadwave.aci.vehicle.VehicleService;
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
 * Date : 2020-01-23
 * Time :
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/collection")
public class CollectionTaskRestController {

    private final ModelMapper modelMapper;
    private final EquipmentService equipmentService;
    private final AccountService accountService;
    private final MasterCodeService masterCodeService;
    private final VehicleService vehicleService;
    private final CollectionTaskService collectionTaskService;

    @Autowired
    public CollectionTaskRestController(ModelMapper modelMapper,
                                        CollectionTaskService collectionTaskService,
                                        VehicleService vehicleService,
                                        AccountService accountService,
                                        MasterCodeService masterCodeService,
                                        EquipmentService equipmentService) {
        this.accountService = accountService;
        this.collectionTaskService = collectionTaskService;
        this.masterCodeService = masterCodeService;
        this.vehicleService = vehicleService;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
    }

    // 수거관리 저장
    @PostMapping ("reg")
    public ResponseEntity<Map<String,Object>> collectionTaskReg(@ModelAttribute CollectionTaskMapperDto collectionTaskMapperDto, HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();

        CollectionTask collectionTask = modelMapper.map(collectionTaskMapperDto, CollectionTask.class);

        //기본값넣기 수거처리단계(확정), 수거처리순번
        ProcStatsType cl02 = ProcStatsType.valueOf("CL02");
        collectionTask.setProcStats(cl02);
        collectionTask.setCtSeq(1);

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(), ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        // 장비번호/유저아이디/배차차량 가져오기
        Optional<Equipment> optionalDeviceid = equipmentService.findByEmNumber(collectionTaskMapperDto.getDeviceid());
        Optional<Account> optionalUserId = accountService.findByUserid(collectionTaskMapperDto.getUserid());
        Optional<Vehicle> optionalVehicleNumber = vehicleService.findByVcNumber(collectionTaskMapperDto.getVehicleNumber());

        //장비번호/유저아이디/배차차량이 존재하지않으면
        if (!optionalDeviceid.isPresent()  || !optionalUserId.isPresent() ||!optionalVehicleNumber.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E024.getCode(),
                    ResponseErrorCode.E024.getDesc()));
        }else{
            collectionTask.setEmId(optionalDeviceid.get());
            collectionTask.setDevicetype(optionalDeviceid.get().getEmType());
            collectionTask.setDeviceid(optionalDeviceid.get().getEmNumber());
            collectionTask.setAccountId(optionalUserId.get());
            collectionTask.setVehicleId(optionalVehicleNumber.get());
        }

        // 수거관리코드 가져오기(고유값)
        CollectionDto optionalCollectionTask = collectionTaskService.findByCtCode(collectionTask.getCtCode());
        //신규 및 수정여부
        if (optionalCollectionTask != null) {
            //수정
            collectionTask.setId(optionalCollectionTask.getId());
            collectionTask.setInsert_id(optionalCollectionTask.getInsert_id());
            collectionTask.setInsertDateTime(optionalCollectionTask.getInsertDateTime());
            collectionTask.setModify_id(currentuserid);
            collectionTask.setModifyDateTime(LocalDateTime.now());
        }else{
            //신규
            collectionTask.setInsert_id(currentuserid);
            collectionTask.setInsertDateTime(LocalDateTime.now());
            collectionTask.setModify_id(currentuserid);
            collectionTask.setModifyDateTime(LocalDateTime.now());
        }

        collectionTaskService.save(collectionTask);

        return ResponseEntity.ok(res.success());
    }

    // 수거업무 리스트
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> collectionList(@RequestParam (value="ctCode", defaultValue="") String ctCode,
                                                            @RequestParam (value="dateFrom", defaultValue="") String  dateFrom,
                                                            @RequestParam (value="dateTo", defaultValue="")String dateTo,
                                                            @RequestParam (value="modelType", defaultValue="")String modelType,
                                                            @RequestParam (value="userName", defaultValue="")String userName,
                                                            @RequestParam (value="vehicleNumber", defaultValue="")String vehicleNumber,
                                                            @PageableDefault Pageable pageable){
        String fromDate = null;
        String toDate = null;

//        log.info("dateFrom : "+dateFrom);
//        log.info("dateTo : "+dateTo);

        if(!dateFrom.equals("")){
            fromDate = dateFrom.substring(0,10).replace("-","");
        }
        if(!dateTo.equals("")){
            toDate = dateTo.substring(0,10).replace("-","");
        }

        Long emTypeId = null;

        if(!modelType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(modelType);
            emTypeId = emTypes.map(MasterCode::getId).orElse(null);
        }

//        log.info("ctCode : "+ctCode);
//        log.info("fromDate : "+fromDate);
//        log.info("toDate : "+toDate);
//        log.info("emTypeId : "+emTypeId);
//        log.info("userId : "+userId);
//        log.info("vehicleNumber : "+vehicleNumber);
        Page<CollectionListDto> collectionListDtos = collectionTaskService.findByCollectionList(ctCode,fromDate,toDate,emTypeId,userName,vehicleNumber,pageable);

        return CommonUtils.ResponseEntityPage(collectionListDtos);

    }



    // 수거업무 정보 보기
    @PostMapping ("info")
    public ResponseEntity<Map<String,Object>> collectionInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        CollectionInfoDto collectionInfo = collectionTaskService.findByCollectionInfoQueryDsl(id);
//        log.info("collectionInfo : "+collectionInfo);
//        log.info("받아온 아이디값 : "+id);

        data.put("collection",collectionInfo);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 수거업무 삭제
    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> collectionDel(@RequestParam(value="ctCode", defaultValue="") String ctCode){
        AjaxResponse res = new AjaxResponse();

        Optional<CollectionTask> optionalCollectionTask = collectionTaskService.findByCtCodeDel(ctCode);

        if (!optionalCollectionTask.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }
        collectionTaskService.delete(optionalCollectionTask.get());
        return ResponseEntity.ok(res.success());
    }

    // 등록장비 리스트
    @PostMapping("equipmentList")
    public ResponseEntity<Map<String,Object>> equipmentList(@RequestParam (value="equipmentNumber", defaultValue="") String equipmentNumber,
                                                             @RequestParam (value="equipmentCountry", defaultValue="") String  equipmentCountry,
                                                            @RequestParam (value="equipmentType", defaultValue="") String equipmentType,
                                                             @PageableDefault Pageable pageable){

        Long emTypeId = null;
        Long emCountryId = null;

        if(!equipmentType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(equipmentType);
            emTypeId = emTypes.map(MasterCode::getId).orElse(null);
        }
        if(!equipmentCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(equipmentCountry);
            emCountryId = emCountrys.map(MasterCode::getId).orElse(null);
        }

        Page<EquipmentListDto> equipmentListDtos =
                equipmentService.findByEquipmentSearch(equipmentNumber,null,emTypeId,emCountryId,pageable);

        return CommonUtils.ResponseEntityPage(equipmentListDtos);
    }
    // 등록장비 정보따오기
    @PostMapping ("equipmentInfo")
    public ResponseEntity<Map<String,Object>> equipmentInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        EquipmentDto equipmentDto = equipmentService.findById(id);

        data.put("equipment",equipmentDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 수거원 리스트
    @PostMapping("collectionList")
    public ResponseEntity<Map<String,Object>> collectionList(@RequestParam (value="collectionId", defaultValue="") String collectionId,
                                                             @RequestParam (value="collectionName", defaultValue="") String  collectionName,
                                                             @PageableDefault Pageable pageable){
        AccountRole role = AccountRole.valueOf("ROLE_COLLECTOR");
        Page<AccountDtoCollectionList> accounts = accountService.findByCollection(collectionId, collectionName,role, pageable);
        return CommonUtils.ResponseEntityPage(accounts);
    }
    // 수거원 정보따오기
    @PostMapping ("collectionInfo")
    public ResponseEntity<Map<String,Object>> collectionInfo(@RequestParam (value="userid", defaultValue="") String userid){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Optional<Account> account = accountService.findByUserid(userid);

        data.put("account",account);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 등록차량 리스트
    @PostMapping("vehicleList")
    public ResponseEntity<Map<String,Object>> vehicleList(@RequestParam (value="vcNumber", defaultValue="") String vcNumber,
                                                          @RequestParam (value="vcShape", defaultValue="")String vcShape,
                                                          @RequestParam (value="vcUsage", defaultValue="")String vcUsage,
                                                          @RequestParam (value="vcState", defaultValue="")String vcState,
                                                          @PageableDefault Pageable pageable){

        Long vcShapeId = null;
        Long vcUsageId = null;
        Long vcStateId = null;

        if(!vcShape.equals("")){
            Optional<MasterCode> vcShapes = masterCodeService.findByCode(vcShape);
            vcShapeId = vcShapes.map(MasterCode::getId).orElse(null);
        }
        if(!vcUsage.equals("")){
            Optional<MasterCode> vcUsages = masterCodeService.findByCode(vcUsage);
            vcUsageId = vcUsages.map(MasterCode::getId).orElse(null);
        }
        if(!vcState.equals("")){
            Optional<MasterCode> vcStates = masterCodeService.findByCode(vcState);
            vcStateId = vcStates.map(MasterCode::getId).orElse(null);
        }

        Page<VehicleListDto> vehicleListDtos =
                vehicleService.findByVehicleSearch(vcNumber,null,vcShapeId,vcUsageId,vcStateId,pageable);

        return CommonUtils.ResponseEntityPage(vehicleListDtos);
    }
    // 등록차량 정보따오기
    @PostMapping ("vehicleInfo")
    public ResponseEntity<Map<String,Object>> vehicleInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        VehicleDto vehicleDto = vehicleService.findById(id);

        data.put("vehicle",vehicleDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

}
