package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountDtoCollectionList;
import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.awsiot.ACIAWSIoTDeviceService;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.equipment.*;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import kr.co.broadwave.aci.vehicle.Vehicle;
import kr.co.broadwave.aci.vehicle.VehicleDto;
import kr.co.broadwave.aci.vehicle.VehicleListDto;
import kr.co.broadwave.aci.vehicle.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

    private final ModelMapper modelMapper;
    private final EquipmentService equipmentService;
    private final AccountService accountService;
    private final MasterCodeService masterCodeService;
    private final VehicleService vehicleService;
    private final CollectionTaskService collectionTaskService;
    private final ACIAWSIoTDeviceService aciawsIoTDeviceService;
    private final DashboardService dashboardService;

    @Autowired
    public CollectionTaskRestController(ModelMapper modelMapper,
                                        ACIAWSIoTDeviceService aciawsIoTDeviceService,
                                        CollectionTaskService collectionTaskService,
                                        VehicleService vehicleService,
                                        AccountService accountService,
                                        MasterCodeService masterCodeService,
                                        EquipmentService equipmentService,
                                        DashboardService dashboardService) {
        this.accountService = accountService;
        this.aciawsIoTDeviceService = aciawsIoTDeviceService;
        this.collectionTaskService = collectionTaskService;
        this.masterCodeService = masterCodeService;
        this.vehicleService = vehicleService;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
        this.dashboardService = dashboardService;
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

    // 수거업무에 등록할 장비리스트
    @PostMapping("equipmentList")
    public ResponseEntity<Map<String,Object>> equipmentList(@RequestParam (value="emNumber", defaultValue="") String emNumber,
                                                       @RequestParam (value="emType", defaultValue="")String emType,
                                                       @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                                       @RequestParam (value="emLocation", defaultValue="")String emLocation,
                                                            @PageableDefault Pageable pageable){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

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

        Page<EquipmentCollectionListDto> equipmentCollectionListDtos =
                equipmentService.findByEquipmentCollectionQuerydsl(emNumber,emTypeId,emCountryId,emLocationId,pageable);
        //log.info("equipmentCollectionListDtos : "+equipmentCollectionListDtos.getContent());

        if(equipmentCollectionListDtos.getTotalElements()> 0 ){
            List<String> emNumbers = new ArrayList<>();
            HashMap<String,List<String>> deviceids = new HashMap<>();

            for(int i=0; i<equipmentCollectionListDtos.getTotalElements(); i++){
                emNumbers.add('"'+equipmentCollectionListDtos.getContent().get(i).getEmNumber()+'"');
            }
            deviceids.put('"'+"deviceids"+'"',emNumbers);
            String aswDeviceids = deviceids.toString().replace("=",":").replace(" ","");

            HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(aswDeviceids);
//            log.info("emNumbers : "+emNumbers);
//            log.info("aswDeviceids : "+aswDeviceids);
//            log.info("AWS 장비 data : "+resData.get("data"));

            List<String> sortDevice = new ArrayList<>();
            for (int i = 0; i < emNumbers.size(); i++) {
                HashMap map = (HashMap) resData.get("data").get(i);
                sortDevice.add((String) map.get("deviceid"));
            }
            sortDevice.sort(Comparator.naturalOrder());

            List<String> deviceLevel = new ArrayList<>(); // 쓰레기량
            List<String> deviceTempBrd = new ArrayList<>(); // 온도
            List<String> deviceBattLevel = new ArrayList<>(); // 배터리잔량
            List<String> deviceSolarCurrent = new ArrayList<>(); //태양광판넬 전류
            List<String> deviceSolarVoltage = new ArrayList<>(); //태양관판넬 전압

            for (String deviceid : sortDevice) {
                for (int i = 0; i < emNumbers.size(); i++) {
                    HashMap map = (HashMap) resData.get("data").get(i);
                    if (map.get("deviceid")==deviceid) {
                        deviceLevel.add((String) map.get("level"));
                        deviceTempBrd.add((String) map.get("temp_brd"));
                        deviceBattLevel.add((String) map.get("batt_level"));
                        deviceSolarCurrent.add((String) map.get("solar_current"));
                        deviceSolarVoltage.add((String) map.get("solar_voltage"));
                    }
                }
            }

            data.put("deviceLevel",deviceLevel);
            data.put("deviceTempBrd",deviceTempBrd);
            data.put("deviceBattLevel",deviceBattLevel);
            data.put("deviceSolarCurrent",deviceSolarCurrent);
            data.put("deviceSolarVoltage",deviceSolarVoltage);
            data.put("datalist",equipmentCollectionListDtos.getContent());
            data.put("total_rows",equipmentCollectionListDtos.getTotalElements());

            res.addResponse("data",data);
        }else{
            data.put("total_rows",equipmentCollectionListDtos.getTotalElements());

            res.addResponse("data",data);
        }
        return ResponseEntity.ok(res.success());
    }

    @PostMapping("streetRouting")
    public ResponseEntity streetRouting(@RequestParam(value="deviceids", defaultValue="") String deviceids) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 장비 거리간 순서짜기
        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids);
        List<String> streetdevicenameList = new ArrayList<>();
        List<String> street_gps_laList = new ArrayList<>();
        List<String> street_gps_loList = new ArrayList<>();
        List<String> streetdevice = new ArrayList<>();
        List<String> passdevice = new ArrayList<>();
        streetdevicenameList.add("Agency");
        street_gps_laList.add("37.547611");
        street_gps_loList.add("127.048871"); // 로그인

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수

        for (int i = 0; i < number; i++) {
            HashMap testmap = (HashMap) resData.get("data").get(i);
            streetdevicenameList.add((String)testmap.get("deviceid"));

            String gps_laData = String.valueOf(testmap.get("gps_la"));
            String gps_loData = String.valueOf(testmap.get("gps_lo"));
            if(gps_loData.equals("na") || gps_loData == null || gps_loData.equals("") || gps_laData.equals("na") || gps_laData == null || gps_laData.equals("")) {
                String gps_laSubStirng = "0.0";
                String gps_loSubStirng = "0.0";

                street_gps_laList.add(gps_laSubStirng);
                street_gps_loList.add(gps_loSubStirng);
            }else {
                if (gps_laData.substring(0, 1).equals("N")) {
                    String gps_laSubStirng = gps_laData.replace("N", "+");
                    street_gps_laList.add(gps_laSubStirng);
                } else if (gps_laData.substring(0, 1).equals("S")) {
                    String gps_laSubStirng = gps_laData.replace("S", "-");
                    street_gps_laList.add(gps_laSubStirng);
                }
                if (gps_loData.substring(0, 1).equals("E")) {
                    String gps_loSubStirng = gps_loData.replace("E", "+");
                    street_gps_loList.add(gps_loSubStirng);
                } else if (gps_loData.substring(0, 1).equals("W")) {
                    String gps_loSubStirng = gps_loData.replace("W", "-");
                    street_gps_loList.add(gps_loSubStirng);
                }
            }
        }
        int streetSize = streetdevicenameList.size();

//        log.info("streetdevicenameList : "+streetdevicenameList);
//        log.info("street_gps_laList : "+street_gps_laList);
//        log.info("street_gps_loList : "+street_gps_loList);
//        log.info("streetSize : "+streetSize);

        List<HashMap<String,String>> streetListData = new ArrayList<>();
        HashMap<String,String> streetListDataMap;
        ArrayList<Boolean> BoleanStreet = new ArrayList<>();
        for(int i=0; i<streetSize; i++){
            streetListDataMap = new HashMap<>();
            streetListDataMap.put("deviceid",streetdevicenameList.get(i));
            streetListDataMap.put("lanData",street_gps_laList.get(i));
            streetListDataMap.put("lonData",street_gps_loList.get(i));
            BoleanStreet.add(false);
            streetListData.add(streetListDataMap);
        }
//        log.info("streetListData : "+streetListData);
//        log.info("BoleanStreet : "+BoleanStreet);
//        log.info("재료준비완료 여기서부터 다시시작");
        System.out.println();
        int result = 0;
        for(int i=0; i<streetSize; i++){
            ArrayList<Double> minStreet = new ArrayList<>();
            //log.info("result : "+result);
            // 첫번째값은 본부이므로 0번째는 true로 변경한다.
            BoleanStreet.set(result, true);
            //log.info("result 값으로 true로 변경 : "+BoleanStreet);
            passdevice.add(streetListData.get(result).get("deviceid"));
            //log.info("계산 할 경위도 : "+streetListData.get(result).get("lanData")+" , "+streetListData.get(result).get("lonData"));
            for(int j=0; j<streetListData.size(); j++){
                if(BoleanStreet.get(result)){
                    if(!streetListData.get(result).get("lanData").equals(street_gps_laList.get(j)) && !streetListData.get(result).get("lonData").equals(street_gps_loList.get(j))){
                        double haversine = haversine(Double.parseDouble(streetListData.get(result).get("lanData")), Double.parseDouble(streetListData.get(result).get("lonData")), Double.parseDouble(street_gps_laList.get(j)), Double.parseDouble(street_gps_loList.get(j)),streetListData.get(j).get("deviceid"),passdevice,passdevice.size());
                        minStreet.add(haversine);
                    }else{
                        minStreet.add(9999.0);
                    }
                }
            }
//            log.info("streetListData : "+streetListData);
//            log.info("최소값 비교하는 리스트 : "+minStreet);
            for(int a=0; a<minStreet.size(); a++){
                if(minStreet.get(result) > minStreet.get(a)){
                    result = a;
                }
            }
            //log.info("최소값의 인덱스번호 : "+result);
            streetdevice.add(streetdevicenameList.get(result));
//            log.info("넣은 장비리스트 : "+streetdevice);
//            log.info("BoleanStreet : "+BoleanStreet);
            if(i==streetSize-1){
                streetdevice.remove(i);
            }
            //System.out.println();
        }
        //log.info("결과 : "+streetdevice);

        data.put("streetdevice", streetdevice);

        res.addResponse("data", data);

        return ResponseEntity.ok(res.success());
    }

    //솔라빈 간의 거리
    public static double haversine(double x1, double y1, double x2, double y2, String fordeviceid, List<String> passdevice, int size) {
        boolean a = false;
        for(int i=0; i<size; i++){
            a = passdevice.get(i).contains(fordeviceid);
            if(a){
                return 9999.0;
            }
        }

        double distance;
        double radius = 6371; // 지구 반지름(km)
        double toRadian = Math.PI / 180;

        double deltaLatitude = Math.abs(x1 - x2) * toRadian;
        double deltaLongitude = Math.abs(y1 - y2) * toRadian;

        double sinDeltaLat = Math.sin(deltaLatitude / 2);
        double sinDeltaLng = Math.sin(deltaLongitude / 2);
        double squareRoot = Math.sqrt(sinDeltaLat * sinDeltaLat + Math.cos(x1 * toRadian) * Math.cos(x2 * toRadian) * sinDeltaLng * sinDeltaLng);

        distance = 2 * radius * Math.asin(squareRoot);

        return distance;
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


    // 수거업무 리스트
    @PostMapping("collectionTaskList")
    public ResponseEntity<Map<String,Object>> collectionTaskList(@PageableDefault Pageable pageable,
                                                                 HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        ProcStatsType procStatsType = ProcStatsType.valueOf("CL02");
        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        Page<CollectionTaskListDto> collection;
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(), ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }else{
            collection = collectionTaskService.findByCollectionsTaskList(currentuserid,optionalAccount.get().getRole(),procStatsType,pageable);
        }

        if(collection.getTotalElements()> 0 ){
            data.put("datalist",collection.getContent());
            data.put("awss3url",AWSS3URL);
            res.addResponse("data",data);
        }else{
            res.addResponse("data",data);
        }

        return ResponseEntity.ok(res.success());
    }

    // 장비확인(라이트점멸)버튼
    @PostMapping("collectionCheck")
    public ResponseEntity<Map<String,Object>> collectionCheck(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                              @RequestParam(value="timestamp", defaultValue="") String timestamp) throws Exception {
        AjaxResponse res = new AjaxResponse();

        //log.info("장비코드 : "+deviceid);

        //Shadow Isolarbin LED 점멸 (IoT) -> param :  디바이스 아이디, 타임스탬프
        aciawsIoTDeviceService.setLightFlicker(deviceid,timestamp);

        return ResponseEntity.ok(res.success());
    }

    // 수거시작버튼
    @PostMapping("collectionStart")
    public ResponseEntity<Map<String,Object>> collectionStart(@RequestParam(value="receiveId", defaultValue="") Long receiveId,
                                                              @RequestParam(value="ctCode", defaultValue="") String ctCode,
                                                              @RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                              @RequestParam(value="ctSeq", defaultValue="") Integer ctSeq,
                                                           HttpServletRequest request) throws Exception {
        AjaxResponse res = new AjaxResponse();

//        log.info("수거업무 : "+ctCode);
//        log.info("장비코드 : "+deviceid);
//        log.info("수거처리순번 : "+ctSeq);

        //Shadow Isolarbin 수거관리시작 (IoT) -> param :  디바이스 아이디, 수거관리번호
        aciawsIoTDeviceService.setCollectStart(deviceid,ctCode+ctSeq);

        CollectionTask collectionTask = new CollectionTask();

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);

        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            //log.info("수거완료한 사람 아이디 미존재 : '" + currentuserid + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        Optional<CollectionTask> optionalCollectionTask = collectionTaskService.findById2(receiveId);
        ProcStatsType procStatsType = ProcStatsType.valueOf("CL03");

        if(!optionalCollectionTask.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E025.getCode(), ResponseErrorCode.E025.getDesc()));
        }else{
            collectionTask.setProcStats(procStatsType);
            collectionTask.setId(optionalCollectionTask.get().getId());
            collectionTask.setCtCode(optionalCollectionTask.get().getCtCode());
            collectionTask.setCtSeq(optionalCollectionTask.get().getCtSeq());
            collectionTask.setYyyymmdd(optionalCollectionTask.get().getYyyymmdd());
            collectionTask.setEmId(optionalCollectionTask.get().getEmId());
            collectionTask.setDeviceid(optionalCollectionTask.get().getDeviceid());
            collectionTask.setDevicetype(optionalCollectionTask.get().getDevicetype());
            collectionTask.setAccountId(optionalCollectionTask.get().getAccountId());
            collectionTask.setVehicleId(optionalCollectionTask.get().getVehicleId());
            collectionTask.setInsert_id(optionalCollectionTask.get().getInsert_id());
            collectionTask.setInsertDateTime(optionalCollectionTask.get().getInsertDateTime());
            collectionTask.setModify_id(currentuserid);
            collectionTask.setModifyDateTime(LocalDateTime.now());
            collectionTask.setCompleteDateTime(null);
        }

        collectionTaskService.save(collectionTask);

        return ResponseEntity.ok(res.success());
    }

    // 수거완료버튼
    @PostMapping("collectionEnd")
    public ResponseEntity<Map<String,Object>> collectionEnd(@RequestParam(value="receiveId", defaultValue="") Long receiveId,
                                                            @RequestParam(value="ctCode", defaultValue="") String ctCode,
                                                            @RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                            @RequestParam(value="ctSeq", defaultValue="") Integer ctSeq,
                                                           HttpServletRequest request) throws Exception {
        AjaxResponse res = new AjaxResponse();

//        log.info("수거업무 : "+ctCode);
//        log.info("장비코드 : "+deviceid);
//        log.info("수거처리순번 : "+ctSeq);
        //Shadow Isolarbin 수거관리종료 (IoT) -> param :  디바이스 아이디, 수거관리번호
        aciawsIoTDeviceService.setCollectEnd(deviceid,ctCode+ctSeq);
        CollectionTask collectionTask = new CollectionTask();

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);

        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            //log.info("수거완료한 사람 아이디 미존재 : '" + currentuserid + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        Optional<CollectionTask> optionalCollectionTask = collectionTaskService.findById2(receiveId);
        ProcStatsType procStatsType = ProcStatsType.valueOf("CL04");

        if(!optionalCollectionTask.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E025.getCode(), ResponseErrorCode.E025.getDesc()));
        }else{
            collectionTask.setProcStats(procStatsType);
            collectionTask.setId(optionalCollectionTask.get().getId());
            collectionTask.setCtCode(optionalCollectionTask.get().getCtCode());
            collectionTask.setCtSeq(optionalCollectionTask.get().getCtSeq());
            collectionTask.setYyyymmdd(optionalCollectionTask.get().getYyyymmdd());
            collectionTask.setEmId(optionalCollectionTask.get().getEmId());
            collectionTask.setDeviceid(optionalCollectionTask.get().getDeviceid());
            collectionTask.setDevicetype(optionalCollectionTask.get().getDevicetype());
            collectionTask.setAccountId(optionalCollectionTask.get().getAccountId());
            collectionTask.setVehicleId(optionalCollectionTask.get().getVehicleId());
            collectionTask.setInsert_id(optionalCollectionTask.get().getInsert_id());
            collectionTask.setInsertDateTime(optionalCollectionTask.get().getInsertDateTime());
            collectionTask.setModify_id(currentuserid);
            collectionTask.setModifyDateTime(LocalDateTime.now());
            collectionTask.setCompleteDateTime(LocalDateTime.now());
        }

        collectionTaskService.save(collectionTask);

        return ResponseEntity.ok(res.success());
    }


}
