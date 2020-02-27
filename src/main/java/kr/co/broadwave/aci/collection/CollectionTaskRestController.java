package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.accounts.*;
import kr.co.broadwave.aci.awsiot.ACIAWSIoTDeviceService;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.equipment.EquipmentCollectionListDto;
import kr.co.broadwave.aci.equipment.EquipmentCollectionRegDto;
import kr.co.broadwave.aci.equipment.EquipmentService;
import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import kr.co.broadwave.aci.vehicle.Vehicle;
import kr.co.broadwave.aci.vehicle.VehicleDto;
import kr.co.broadwave.aci.vehicle.VehicleListDto;
import kr.co.broadwave.aci.vehicle.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
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
    private final KeyGenerateService keyGenerateService;

    @Autowired
    public CollectionTaskRestController(ModelMapper modelMapper,
                                        KeyGenerateService keyGenerateService,
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
        this.keyGenerateService = keyGenerateService;
    }

    // 수거관리 저장
    @PostMapping ("reg")
    public ResponseEntity<Map<String,Object>> collectionTaskReg(@ModelAttribute CollectionTaskMapperDto collectionTaskMapperDto, HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();
        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);

        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(), ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        //장비아이디,장비코드,모델타입 리스트에 넣기
        List<EquipmentCollectionRegDto> equipment = equipmentService.findByRoutingEmNumberQuerydsl(collectionTaskMapperDto.getStreetRouting());
        List<Equipment> equipmentId = new ArrayList<>();
        List<String> equipmentEmNumber = new ArrayList<>();
        List<MasterCode> equipmentEmType = new ArrayList<>();
        for(int i=0; i<collectionTaskMapperDto.getCollectionSeq(); i++){
            for(int j=0; j<collectionTaskMapperDto.getCollectionSeq(); j++) {
                if (collectionTaskMapperDto.getStreetRouting().get(i).contains(equipment.get(j).getEmNumber())) {
                    equipmentId.add(equipment.get(j).getId());
                    equipmentEmNumber.add(equipment.get(j).getEmNumber());
                    equipmentEmType.add(equipment.get(j).getEmType());
                }
            }
        }

        //기본값넣기 수거처리단계(확정)
        ProcStatsType cl02 = ProcStatsType.valueOf("CL02");
        // 유저아이디/배차차량 가져오기
        Optional<Account> optionalUserId = accountService.findByUserid(collectionTaskMapperDto.getUserid());
        Optional<Vehicle> optionalVehicleNumber = vehicleService.findByVcNumber(collectionTaskMapperDto.getVehicleNumber());

        String collectionTaskctCode = collectionTaskMapperDto.getCtCode();

        int z = 0;
        if(!collectionTaskctCode.equals("")) {
            //log.info("수정작성");
            List<CollectionDto> optionalCollectionTask = collectionTaskService.findByCtCodeSeqQuerydsl(collectionTaskMapperDto.getCtCode());
            //log.info("optionalCollectionTask : "+optionalCollectionTask);
            for (int i = 1; i < collectionTaskMapperDto.getCollectionSeq()+1; i++) {
                CollectionTask collectionTask = modelMapper.map(collectionTaskMapperDto, CollectionTask.class);

                //유저아이디/배차차량이 존재하지않으면
                if (!optionalUserId.isPresent() || !optionalVehicleNumber.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.E024.getCode(),
                            ResponseErrorCode.E024.getDesc()));
                } else {
                    collectionTask.setAccountId(optionalUserId.get());
                    collectionTask.setVehicleId(optionalVehicleNumber.get());
                }
                collectionTask.setId(optionalCollectionTask.get(z).getId());
                collectionTask.setEmId(equipmentId.get(z));
                collectionTask.setDeviceid(equipmentEmNumber.get(z));
                collectionTask.setDevicetype(equipmentEmType.get(z));

                //완료시간은 Null 처리함.
                collectionTask.setProcStats(cl02);
                collectionTask.setCtSeq(i);

                collectionTask.setInsert_id(optionalCollectionTask.get(z).getInsert_id());
                collectionTask.setInsertDateTime(optionalCollectionTask.get(z).getInsertDateTime());
                collectionTask.setModify_id(currentuserid);
                collectionTask.setModifyDateTime(LocalDateTime.now());

                collectionTaskService.save(collectionTask);

                z++;
            }

        }else{
            //log.info("신규작성");
            SimpleDateFormat todayFormat = new SimpleDateFormat("yyMMdd");
            Date time = new Date();
            String today = todayFormat.format(time);
            String ctCode = keyGenerateService.keyGenerate("bs_collection", "TS"+today, currentuserid);

            for (int i = 1; i < collectionTaskMapperDto.getCollectionSeq()+1; i++) {
                CollectionTask collectionTask = modelMapper.map(collectionTaskMapperDto, CollectionTask.class);

                //유저아이디/배차차량이 존재하지않으면
                if (!optionalUserId.isPresent() || !optionalVehicleNumber.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.E024.getCode(),
                            ResponseErrorCode.E024.getDesc()));
                } else {
                    collectionTask.setAccountId(optionalUserId.get());
                    collectionTask.setVehicleId(optionalVehicleNumber.get());
                }

                collectionTask.setCtCode(ctCode);
                collectionTask.setEmId(equipmentId.get(z));
                collectionTask.setDeviceid(equipmentEmNumber.get(z));
                collectionTask.setDevicetype(equipmentEmType.get(z));

                //완료시간은 Null 처리함.
                collectionTask.setProcStats(cl02);
                collectionTask.setCtSeq(i);

                collectionTask.setInsert_id(currentuserid);
                collectionTask.setInsertDateTime(LocalDateTime.now());
                collectionTask.setModify_id(currentuserid);
                collectionTask.setModifyDateTime(LocalDateTime.now());

                collectionTaskService.save(collectionTask);

                z++;
            }
        }

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

        Page<CollectionListDto> collectionListDtos = collectionTaskService.findByCollectionList(ctCode,fromDate,toDate,emTypeId,userName,vehicleNumber,pageable);

        return CommonUtils.ResponseEntityPage(collectionListDtos);

    }

    // 수거업무 정보 보기
    @PostMapping ("collectionTaskInfo")
    public ResponseEntity<Map<String,Object>> collectionTaskInfo(@RequestParam(value="ctCode", defaultValue="")String ctCode){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<CollectionInfoDto> collectionInfo = collectionTaskService.findByCollectionInfoQueryDsl(ctCode);
//        log.info("받아온 관리코드 : "+ctCode);
//        log.info("collectionInfo : "+collectionInfo);

        data.put("collection",collectionInfo);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 수거업무 삭제
    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> collectionDel(@RequestParam(value="ctCode", defaultValue="") String ctCode,
                                                            @RequestParam(value="collectionSeq", defaultValue="") Integer collectionSeq){
        AjaxResponse res = new AjaxResponse();
//        log.info("삭제할 관리번호 : "+ctCode);
//        log.info("해당 시퀀스 : "+collectionSeq);
        List<CollectionDto> listCollectionTask = collectionTaskService.findByCtCodeSeqQuerydsl(ctCode);
//        log.info("listCollectionTask : "+listCollectionTask);
        for(int i=0; i<collectionSeq; i++){
            Optional<CollectionTask> optionalCollectionTask = collectionTaskService.findById2(listCollectionTask.get(i).getId());
            if (!optionalCollectionTask.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
            }
            collectionTaskService.delete(optionalCollectionTask.get());
        }
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

    // 거리 라우팅계산
    @PostMapping("streetRouting")
    public ResponseEntity<Map<String,Object>> streetRouting(@RequestParam(value="deviceids", defaultValue="") String deviceids, HttpServletRequest request) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String currentuserid = CommonUtils.getCurrentuser(request); // 현재로그인한 사용자아이디가져오기
        AccountDtoCollection collectionAccount = accountService.findByCollectionLanLon(currentuserid);
//        log.info("소속사 : "+collectionAccount.getOperator());
//        log.info("출발점 위도 : "+collectionAccount.getCompanyLatitude());
//        log.info("출발점 경도 : "+collectionAccount.getCompanyHardness());

        // 장비 거리간 순서짜기
        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids);
        List<String> streetdevicenameList = new ArrayList<>();
        List<String> street_gps_laList = new ArrayList<>();
        List<String> street_gps_loList = new ArrayList<>();
        List<String> streetdevice = new ArrayList<>();
        List<String> passdevice = new ArrayList<>();
        if(collectionAccount.getCompanyLatitude()==null||collectionAccount.getCompanyLatitude()==null){
            //소속사에 위도나 경도가 존재하지 않을때,
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E027.getCode(),ResponseErrorCode.E027.getDesc()));
        }else{
            streetdevicenameList.add(collectionAccount.getOperator());
            street_gps_laList.add(collectionAccount.getCompanyLatitude());
            street_gps_loList.add(collectionAccount.getCompanyHardness());
        }

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수

        for (int i = 0; i < number; i++) {
            HashMap datamap = (HashMap) resData.get("data").get(i);;

            String gps_laData = String.valueOf(datamap.get("gps_la"));
            String gps_loData = String.valueOf(datamap.get("gps_lo"));
            if(gps_loData.equals("na") || gps_loData == null || gps_loData.equals("") || gps_laData.equals("na") || gps_laData == null || gps_laData.equals("")) {
                //log.info("위도경도데이터없음 장비코드 : "+datamap.get("deviceid"));
                //장비에 위도나 경도가 존재하지 않을때,
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E029.getCode(),"ㅤ"+datamap.get("deviceid")+ResponseErrorCode.E029.getDesc()));
            }else {
                streetdevicenameList.add((String)datamap.get("deviceid"));

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
//        log.info("재료준비완료 여기서부터 라우팅시작");
//        System.out.println();
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
            streetdevice.add(streetdevicenameList.get(result));
//            log.info("최소값의 인덱스번호 : "+result);
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
    //장비 간의 거리계산
    public static double haversine(double x1, double y1, double x2, double y2, String fordeviceid, List<String> passdevice, int size) {
        boolean a;
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

    // 수거업무 DirectionAPI 맵생성
    @PostMapping("collectionDirection")
    public ResponseEntity<Map<String,Object>> collectionDirection(@RequestParam(value="deviceids", defaultValue="") String deviceids,
                                                                  @RequestParam(value="deviceArray[]", defaultValue="") List<String> deviceArray,
                                                                  HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<String> street_gps_laList = new ArrayList<>();
        List<String> street_gps_loList = new ArrayList<>();

        String currentuserid = CommonUtils.getCurrentuser(request); // 현재로그인한 사용자아이디가져오기
        AccountDtoCollection collectionAccount = accountService.findByCollectionLanLon(currentuserid);

        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids);
        //log.info("resData : "+resData);
        int streetSize = Integer.parseInt(String.valueOf(resData.get("datacounts")));

        for (String deviceid : deviceArray){
            for (int i = 0; i < streetSize; i++) {
                HashMap map = (HashMap) resData.get("data").get(i);
                if (map.get("deviceid").equals(deviceid)) {
                    street_gps_laList.add((String) map.get("gps_la")); //맵 데이터 차트
                    street_gps_loList.add((String) map.get("gps_lo")); //맵 데이터 차트
                }
            }
        }

//        log.info("장비배열 : "+deviceArray);
//        log.info("streetSize : "+streetSize);
//        log.info("street_gps_laList : "+street_gps_laList);
//        log.info("street_gps_loList : "+street_gps_loList);

        List<String> direction_gps_devicename = new ArrayList<>();
        List<String> direction_gps_laList = new ArrayList<>();
        List<String> direction_gps_loList = new ArrayList<>();

        List<String> direction_noGps_device = new ArrayList<>();

        String htmlStart = null;
        String htmlGoal = null;
        String startName = null; //시작점이름(소속사명)
        String start = null; //시작점
        String goalName = null; //도착점이름(도착점장비)
        String goal = null; //도착점

        for (int i = 0; i < streetSize; i++) {
            String gps_laData = street_gps_laList.get(i);
            String gps_loData = street_gps_loList.get(i);
            if (gps_loData.equals("na") || gps_loData == null || gps_loData.equals("") || gps_laData.equals("na") || gps_laData == null || gps_laData.equals("")) {
                direction_noGps_device.add(deviceArray.get(i));
            }else {
                if (gps_laData.substring(0, 1).equals("N")) {
                    String gps_laSubStirng = gps_laData.replace("N", "");
                    direction_gps_laList.add(gps_laSubStirng);
                } else if (gps_laData.substring(0, 1).equals("S")) {
                    String gps_laSubStirng = gps_laData.replace("S", "-");
                    direction_gps_laList.add(gps_laSubStirng);
                }
                if (gps_loData.substring(0, 1).equals("E")) {
                    String gps_loSubStirng = gps_loData.replace("E", "");
                    direction_gps_loList.add(gps_loSubStirng);
                } else if (gps_loData.substring(0, 1).equals("W")) {
                    String gps_loSubStirng = gps_loData.replace("W", "-");
                    direction_gps_loList.add(gps_loSubStirng);
                }
                direction_gps_devicename.add(deviceArray.get(i));
            }
        }

//        log.info("direction_gps_devicename : "+direction_gps_devicename);
//        log.info("direction_gps_laList : "+direction_gps_laList);
//        log.info("direction_gps_loList : "+direction_gps_loList);
//        log.info("direction_gps_devicename.size() : "+direction_gps_devicename.size());
        if(direction_gps_devicename.size()!=0){
            int directionSize = direction_gps_devicename.size()-1;
            goalName = direction_gps_devicename.get(directionSize);
            goal = direction_gps_loList.get(directionSize)+","+direction_gps_laList.get(directionSize);
            htmlGoal =  direction_gps_laList.get(directionSize)+","+direction_gps_loList.get(directionSize);
            direction_gps_devicename.remove(directionSize);
            direction_gps_laList.remove(directionSize);
            direction_gps_loList.remove(directionSize);
        }

        int directionSize = direction_gps_devicename.size();

        if(collectionAccount.getCompanyLatitude() != null || collectionAccount.getCompanyHardness() != null){
            startName = collectionAccount.getOperator();
            start = collectionAccount.getCompanyHardness()+","+collectionAccount.getCompanyLatitude(); //출발점
            htmlStart = collectionAccount.getCompanyLatitude()+","+collectionAccount.getCompanyHardness(); //자바스크립트용 출발점
        }else{
            startName = "경도위도없음";
            start = "126.82752,37.568666";
            htmlStart = "37.568666,126.82752";
        }

//        log.info("start : "+start);
//        log.info("goal : "+goal);
//        log.info("시작점이름 : "+startName);
//        log.info("도착점이름 : "+goalName);
//        log.info("directionSize : "+directionSize);
        StringBuilder url = null;
//        log.info("direction_gps_devicename.size() : "+direction_gps_devicename.size());

        //driving 옵션
        //trafast 실시간 빠른길
        //tracomfort 실시간 편한길
        //traoptimal 실시간 최적
        //traavoidtoll 무료 우선
        //traavoidcaronly 자동차 전용도로 회피 우선
        if(directionSize > 0 && directionSize < 16) {
            //log.info("경우지점있음");
            if(directionSize<=6){
                url = new StringBuilder("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=" + start + "&goal=" + goal + "&option=traoptima&waypoints=");
                for (int i = 0; i < directionSize; i++) {
                    if(i==directionSize-1) {
                        url.append(direction_gps_loList.get(i)).append(",").append(direction_gps_laList.get(i)).append("|");
                    }else{
                        url.append(direction_gps_loList.get(i)).append(",").append(direction_gps_laList.get(i));
                    }
                }
            }else{
                url = new StringBuilder("https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving?start=" + start + "&goal=" + goal + "&option=traoptima&waypoints=");
                for (int i = 0; i < directionSize; i++) {
                    if(i==directionSize-1) {
                        url.append(direction_gps_loList.get(i)).append(",").append(direction_gps_laList.get(i)).append("|");
                    }else{
                        url.append(direction_gps_loList.get(i)).append(",").append(direction_gps_laList.get(i));
                    }
                }
            }

            //Rest URL (Open Api Test)
            String clientId = "n1xv5m63g5";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "PJoblCQiihINMwQD5pX3WNQVLZuw6wvIZv0ec6DM";//애플리케이션 클라이언트 시크릿값";

            //ex
//            String via = "127.050374,37.546808"; //경유지 1번장치
//            String via2 = "127.028192,37.558203"; //경유지2 대림아파트
//            String via3 = "127.063791,37.557296"; //경유지3 장한평역
//            String via4 = "127.085515,37.555643"; //경유지4
//            String via5 = "127.081469,37.580941"; //경유지5
//            String via6 = "127.058825,37.579615"; //경유지6
//
//            // Direction 15적용
//            final String test = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving?start=" + start + "&goal=" + goal + "&option=traoptimal&waypoints=" + via2 + "|" + via3 + "|" + via4 + "|" + via5 + "|" + via6 + "|" + via + "";

            RestTemplate restTemplate = new RestTemplate();

            //header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-NCP-APIGW-API-KEY-ID", clientId);
            headers.add("X-NCP-APIGW-API-KEY", clientSecret);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(headers);
            URI uri = UriComponentsBuilder.fromUriString(String.valueOf(url)).build().toUri();
            ResponseEntity<String> apiResult = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
//            log.info("유알엘 : "+url);
//            log.info("유알엘 : "+test);
//            log.info("apiResult : " + apiResult);
//            log.info("direction_gps_laList : " + direction_gps_laList);
//            log.info("direction_gps_loList : " + direction_gps_loList);

            data.put("direction_noGps_device",direction_noGps_device);
            data.put("direction_noGps_deviceSize",direction_noGps_device.size());
            data.put("direction_gps_laList",direction_gps_laList);
            data.put("direction_gps_loList",direction_gps_loList);
            data.put("directionSize", directionSize);
            data.put("htmlStart", htmlStart);
            data.put("htmlGoal", htmlGoal);
            data.put("apiResultBody", apiResult.getBody());
        }else if(goal!=null){
            //log.info("골인지점만있음");
            url = new StringBuilder("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=" + start + "&goal=" + goal + "&option=traoptima");

            String clientId = "n1xv5m63g5";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "PJoblCQiihINMwQD5pX3WNQVLZuw6wvIZv0ec6DM";//애플리케이션 클라이언트 시크릿값";

            RestTemplate restTemplate = new RestTemplate();

            //header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-NCP-APIGW-API-KEY-ID", clientId);
            headers.add("X-NCP-APIGW-API-KEY", clientSecret);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(headers);
            URI uri = UriComponentsBuilder.fromUriString(String.valueOf(url)).build().toUri();
            ResponseEntity<String> apiResult = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            //log.info("유알엘 : "+url);
            //log.info("apiResult : " + apiResult);

            data.put("direction_noGps_device",direction_noGps_device);
            data.put("direction_noGps_deviceSize",direction_noGps_device.size());
            data.put("direction_gps_laList",direction_gps_laList);
            data.put("direction_gps_loList",direction_gps_loList);
            data.put("directionSize", directionSize);
            data.put("htmlStart", htmlStart);
            data.put("htmlGoal", htmlGoal);
            data.put("apiResultBody", apiResult.getBody());
        }else {
//            log.info("도착점이 없습니다(에러)");
//            log.info("유알엘 : "+url);
            data.put("apiResultBody", null);
        }
//        log.info("htmlStart : "+htmlStart);
//        log.info("htmlGoal : "+htmlGoal);

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





    //////////////////////// 여기서부턴 모바일 ////////////////////////

    // 모바일 - 수거업무 리스트 : 수거예정일
    @PostMapping("collectionTaskListDate")
    public ResponseEntity<Map<String,Object>> collectionTaskListDate(@PageableDefault Pageable pageable,
                                                                 HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        ProcStatsType procStatsType = ProcStatsType.valueOf("CL02");
        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        Page<CollectionTaskListDateDto> collection;
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(), ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }else{
            collection = collectionTaskService.findByCollectionsTaskDateList(currentuserid,optionalAccount.get().getRole(),procStatsType,pageable);
        }
//        log.info("collection : "+collection.getTotalElements());
//        log.info("collection : "+collection.getContent());

        if(collection.getTotalElements()> 0 ){
            data.put("datalist",collection.getContent());
            res.addResponse("data",data);
        }else{
            res.addResponse("data",data);
        }

        return ResponseEntity.ok(res.success());
    }

    // 모바일 - 수거업무 리스트 : 장비코드
    @PostMapping("collectionTaskListDevice")
    public ResponseEntity<Map<String,Object>> collectionTaskListDevice(@RequestParam (value="ctCode", defaultValue="") String ctCode,
                                                                     HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        //log.info("ctCode : "+ctCode);

        List<CollectionTaskListDeviceDto> collection;
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(), ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }else{
            collection = collectionTaskService.findByCollectionsTaskDeviceList(ctCode,currentuserid,optionalAccount.get().getRole());
        }
        //log.info("collection : "+collection);

        if(!collection.isEmpty()){
            data.put("collection",collection);
            data.put("collectionSize",collection.size());
            res.addResponse("data",data);
        }else{
            res.addResponse("data",data);
        }

        return ResponseEntity.ok(res.success());
    }

    // 모바일 - 수거업무 리스트 : 정보보기
    @PostMapping("collectionTaskListInfo")
    public ResponseEntity<Map<String,Object>> collectionTaskListInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        //log.info("id : "+id);

        CollectionTaskListDto collection = collectionTaskService.findByCollectionsTaskInfoList(id);
        log.info("collection : "+collection);


        if(collection != null){
            data.put("collection",collection);
            data.put("awss3url",AWSS3URL);
            res.addResponse("data",data);
        }else{
            res.addResponse("data",data);
        }
        return ResponseEntity.ok(res.success());
    }

    // 모바일 - 장비확인(라이트점멸)버튼
    @PostMapping("collectionCheck")
    public ResponseEntity<Map<String,Object>> collectionCheck(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                              @RequestParam(value="timestamp", defaultValue="") String timestamp) throws Exception {
        AjaxResponse res = new AjaxResponse();

        //log.info("장비코드 : "+deviceid);

        //Shadow Isolarbin LED 점멸 (IoT) -> param :  디바이스 아이디, 타임스탬프
        aciawsIoTDeviceService.setLightFlicker(deviceid,timestamp);

        return ResponseEntity.ok(res.success());
    }

    // 모바일 - 수거시작버튼
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

    // 모바일 - 수거완료버튼
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
