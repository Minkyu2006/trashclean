package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountMapperDto;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.awsiot.ACIAWSIoTDeviceService;
import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.devicestats.DevicestatsDto;
import kr.co.broadwave.aci.devicestats.DevicestatusService;
import kr.co.broadwave.aci.equipment.EquipmentDto;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author InSeok
 * Date : 2019-10-17
 * Remark :
 */
@RestController
@RequestMapping("/api/dashboard")
@Slf4j
public class DashboardRestController {

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

    private final DashboardService dashboardService;
    private final MasterCodeService masterCodeService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final DevicestatusService devicestatusService;
    private final ACIAWSLambdaService aciawsLambdaService;
    private final ACIAWSIoTDeviceService aciawsIoTDeviceService;

    @Autowired
    public DashboardRestController(DashboardService dashboardService,
                                   ModelMapper modelMapper,
                                   AccountService accountService,
                                   MasterCodeService masterCodeService,
                                   DevicestatusService devicestatusService,
                                   ACIAWSLambdaService aciawsLambdaService,
                                   ACIAWSIoTDeviceService aciawsIoTDeviceService) {
        this.modelMapper = modelMapper;
        this.dashboardService = dashboardService;
        this.accountService = accountService;
        this.masterCodeService = masterCodeService;
        this.devicestatusService = devicestatusService;
        this.aciawsLambdaService = aciawsLambdaService;
        this.aciawsIoTDeviceService = aciawsIoTDeviceService;
    }

    @PostMapping("monitering")
    public ResponseEntity<Map<String,Object>> monitering(@RequestParam(value="deviceids", defaultValue="") String deviceids){
        //log.info("모니터링 조회 시작");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        //Sample of deviceids variable : {"deviceids":["ISOL-KR-SEOUL-0001","ISOL-KR-SEOUL-0002"]}
        HashMap<String, Object> resData = dashboardService.getDeviceLastestState(deviceids);
        //log.info("resData : "+resData);

        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        //log.info("모니터링 조회 성공 ");
        return ResponseEntity.ok(res.success());

    }

    @PostMapping("devicelist")
    public ResponseEntity<Map<String,Object>> devicelist(){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        //log.info("Device목록가져오기 시작");

        HashMap<String, Object> resData = dashboardService.getDeviceList("ISOL");

        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        log.info("Device목록가져오기 성공 ");
        return ResponseEntity.ok(res.success());

    }

    @PostMapping("devicehitory")
    public ResponseEntity<Map<String,Object>> devicehistory(@RequestParam(value="deviceid", defaultValue="") String deviceid
            ,@RequestParam(value="intervaltime", defaultValue="") String intervaltime){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        //log.info("Device History 가져오기 시작");

        HashMap<String, Object> resData = dashboardService.getDeviceHistory(deviceid,intervaltime);
        log.info("data : "+resData.get("data"));
        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        //log.info("Device History 가져오기 성공 ");
        return ResponseEntity.ok(res.success());

    }

    //장비 리스트 뿌리기
    @PostMapping ("deviceInfoList")
    public ResponseEntity<Map<String,Object>> deviceInfoList(@RequestParam (value="emNumber", defaultValue="") String emNumber,
                                                             @RequestParam (value="emType", defaultValue="")String emType,
                                                             @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                                             @RequestParam (value="emLocation", defaultValue="")String emLocation,
                                                             @PageableDefault Pageable pageable){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Long emTypeId = null;
        Long emCountryId = null;
        Long emLocationId = null;

//        log.info("emNumber : "+emNumber);
//        log.info("emType : "+emType);
//        log.info("emCountry : "+emCountry);
//        log.info("emLocation : "+emLocation);

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

        List<DashboardDeviceListViewDto> deviceInfoListDtos =
                dashboardService.findByDashboardListView(emNumber, emTypeId, emCountryId, emLocationId, pageable);
//        log.info("deviceInfoListDtos : "+deviceInfoListDtos);

        data.put("deviceInfoListDtos",deviceInfoListDtos);
        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // ASW 장치 데이터리스트 뿌리기
    @PostMapping("deviceAWSListView")
    public ResponseEntity deviceAWSListView(@RequestParam(value="deviceids", defaultValue="") String deviceids,
                                            @RequestParam(value="deviceIdList[]", defaultValue="") List<String> deviceIdList) throws ParseException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<String> devices = new ArrayList<>(); //장비값넣기
        List<String> status = new ArrayList<>(); // 상태값리스트
        List<String> temp_brd = new ArrayList<>(); // 온도리스트
        List<String> level = new ArrayList<>(); // 배출량리스트
        List<String> batt_voltage = new ArrayList<>(); // 배터리잔량리스트
        List<String> solar_current = new ArrayList<>(); // 전류리스트
        List<String> solar_voltage = new ArrayList<>(); // 전압리스트
        List<String> gps_laDatas = new ArrayList<>();// 위도리스트
        List<String> gps_loDatas = new ArrayList<>();  // 경도리스트
        List<String> gps_laDatas2 = new ArrayList<>(); // 위도리스트
        List<String> gps_loDatas2 = new ArrayList<>();  // 경도리스트

        List<Integer> rsrp = new ArrayList<>(); // 안테나

        List<String> frontDoor_sol = new ArrayList<>(); // 전면도어
        List<String> inputDoor = new ArrayList<>();  // 투입구

        // ITAI
        List<String> s_tmp2 = new ArrayList<>(); // ITAI 온도
        List<String> s_actuator_level = new ArrayList<>(); // ITAI 배출량(%)
        List<String> s_dis_info_level = new ArrayList<>(); // ITAI 배출무게(%)
        List<String> s_dis_info_weight = new ArrayList<>(); // ITAI 배출무게(g)
        List<String> s_language = new ArrayList<>();// ITAI 사용언어

//        log.info("deviceids : " + deviceids);
//        log.info("deviceIdList : " + deviceIdList);
        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids);
        log.info("resData : " + resData);
        List<String> sortDevice = new ArrayList<>();

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수

        int deviceIdListSize = deviceIdList.size();
//        log.info("deviceIdListSize : " + deviceIdListSize);
        for (int i = 0; i < number; i++) {
            HashMap map = (HashMap) resData.get("data").get(i);
            sortDevice.add((String) map.get("deviceid"));
        }
        sortDevice.sort(Comparator.naturalOrder());
//        log.info("sortDevice : " + sortDevice);
//        log.info("sortDevice.size() : " + sortDevice.size());
        int x=0;
        int y=0;
        for (int i=0; i<deviceIdListSize; i++) {
//            log.info("x : " + x);
//            log.info("y : " + y);
            String deviceList = deviceIdList.get(y);
            String deviceid = "";
            if(sortDevice.size()!=x){
                deviceid = sortDevice.get(x);
            }
//            log.info("deviceList : " + deviceList);
//            log.info("deviceid : " + deviceid);
            if (deviceid.equals(deviceList)) {
                for (int j = 0; j < number; j++) {
                    HashMap map = (HashMap) resData.get("data").get(j);
                    String resDeviceid = (String) map.get("deviceid");
                    String subDevice = resDeviceid.substring(0, 4);
//                    log.info("subDevice : "+subDevice);
                    if (resDeviceid.equals(deviceid)) {
                        x++;
                        y++;
                        if (subDevice.equals("ISOL")) {
                            if (map.get("status").equals("normal")) {
                                map.replace("status", "정상");
                            } else if (map.get("status").equals("caution")) {
                                map.replace("status", "주의");
                            } else if (map.get("status").equals("severe")) {
                                map.replace("status", "심각");
                            }

                            devices.add((String) map.get("deviceid")); // 장비값넣기
                            status.add((String) map.get("status")); // 상태값넣기
                            temp_brd.add((String) map.get("temp_brd")); // 온도값넣기
                            level.add((String) map.get("level")); // 배출량값넣기
                            batt_voltage.add((String) map.get("batt_voltage")); // 배터리잔량값넣기
                            solar_current.add((String) map.get("solar_current")); // 전류값넣기
                            solar_voltage.add((String) map.get("solar_voltage")); // 전압값넣기
                            gps_laDatas.add((String) map.get("gps_la")); // 위도값넣기
                            gps_loDatas.add((String) map.get("gps_lo")); // 경도값넣기

                            if (!String.valueOf(map.get("rsrp")).equals("")) {
                                rsrp.add(Integer.parseInt(String.valueOf(map.get("rsrp"))));
                            } else {
                                rsrp.add(null);
                            }

                            if (!String.valueOf(map.get("frontdoor_sol")).equals("") || !String.valueOf(map.get("frontdoor_sol")).equals("na")) {
                                String front = (String) map.get("frontdoor_sol");
                                switch (front) {
                                    case "close":
                                        frontDoor_sol.add("닫힘");
                                        break;
                                    case "open":
                                        frontDoor_sol.add("열림");
                                        break;
                                    case "jamming":
                                        frontDoor_sol.add("걸림");
                                        break;
                                    default:
                                        frontDoor_sol.add("미확인");
                                        break;
                                }
                            } else {
                                frontDoor_sol.add("미확인");
                            }


                            if (!String.valueOf(map.get("inputdoor")).equals("") || !String.valueOf(map.get("inputdoor")).equals("na")) {
                                String inputdoor = (String) map.get("inputdoor");
                                switch (inputdoor) {
                                    case "close":
                                        inputDoor.add("닫힘");
                                        break;
                                    case "open":
                                        inputDoor.add("열림");
                                        break;
                                    case "jamming":
                                        inputDoor.add("걸림");
                                        break;
                                    default:
                                        inputDoor.add("미확인");
                                        break;
                                }
                            } else {
                                inputDoor.add("미확인");
                            }


                        } else if (subDevice.equals("ITAI")) {

                            if (map.get("status").equals("normal")) {
                                map.replace("status", "정상");
                            } else if (map.get("status").equals("caution")) {
                                map.replace("status", "주의");
                            } else if (map.get("status").equals("severe")) {
                                map.replace("status", "심각");
                            }

                            devices.add((String) map.get("deviceid")); // 장비값넣기
                            status.add((String) map.get("status")); // 상태값넣기
                            s_tmp2.add((String) map.get("s_tmp2")); // 온도
                            s_actuator_level.add((String) map.get("actuator_level")); // 배출량(%)
                            s_dis_info_level.add((String) map.get("dis_info_level")); // 배출무게(%)
                            s_dis_info_weight.add((String) map.get("dis_info_weight")); // 배출무게(g)
                            s_language.add((String) map.get("language")); // 사용언어
                            gps_laDatas.add((String) map.get("gps_la")); // 위도값넣기
                            gps_loDatas.add((String) map.get("gps_lo")); // 경도값넣기

                        }
                    }
                }
            } else {
                devices.add("0");
                status.add("0");
                temp_brd.add(null);
                level.add(null);
                batt_voltage.add("0");
                solar_current.add("0");
                solar_voltage.add("0");
                gps_laDatas.add("na");
                gps_loDatas.add("na");
                rsrp.add(null);
                frontDoor_sol.add("미확인");
                inputDoor.add("미확인");

//                s_tmp2.add("0");
//                s_actuator_level.add("0");
//                s_dis_info_level.add("0");
//                s_dis_info_weight.add("0");
//                s_language.add("na");

                y++;
            }
        }

        //장비 온오프라인구분
        List<Boolean> deviceOnOffstatus = new ArrayList<>(); // 온라인인지,오프라인인지 넣는 리스트
        List<Object> deviceOnOffTime = new ArrayList<>(); // 마지막 온오프라인 타임스탬프

        for (int i = 0; i < devices.size(); i++) {
            if(!devices.get(i).equals("0")) {
                HashMap<String, HashMap<String, Object>> onOfflineData = aciawsLambdaService.getDeviceonlineCheck(devices.get(i));
                deviceOnOffstatus.add(Boolean.parseBoolean(String.valueOf((onOfflineData.get("data").get("online")))));
                deviceOnOffTime.add(onOfflineData.get("data").get("timestamp"));
            }else{
                deviceOnOffstatus.add(false);
                deviceOnOffTime.add(null);
            }
        }
//
//        //지도상에 장비위치이동
        for (int i = 0; i < deviceIdListSize; i++) {
            String gps_laData = gps_laDatas.get(i);
            String gps_loData = gps_loDatas.get(i);
            if(gps_loData.equals("na") || gps_loData.equals("") || gps_laData.equals("na") || gps_laData.equals("")) {
                String gps_laSubStirng = "0.0";
                String gps_loSubStirng = "0.0";

                gps_laDatas2.add(gps_laSubStirng);
                gps_loDatas2.add(gps_loSubStirng);
            }else{
                if(gps_laData.startsWith("N")){
                    String gps_laSubStirng = gps_laData.replace("N","+");
                    gps_laDatas2.add(gps_laSubStirng);
                }else if(gps_laData.startsWith("S")){
                    String gps_laSubStirng = gps_laData.replace("S","-");
                    gps_laDatas2.add(gps_laSubStirng);
                }
                if(gps_loData.startsWith("E")){
                    String gps_loSubStirng = gps_loData.replace("E","+");
                    gps_loDatas2.add(gps_loSubStirng);
                }else if(gps_loData.startsWith("W")){
                    String gps_loSubStirng = gps_loData.replace("W","-");
                    gps_loDatas2.add(gps_loSubStirng);
                }
            }
        }

//        log.info("devices : "+devices);
//        log.info("status : "+status);
//        log.info("temp_brd : "+temp_brd);
//        log.info("level : "+level);
//        log.info("batt_level : "+batt_level);
//        log.info("solar_current : "+solar_current);
//        log.info("solar_voltage : "+solar_voltage);
//        log.info("gps_laDatas2 : "+gps_laDatas2);
//        log.info("gps_loDatas2 : "+gps_loDatas2);
//        log.info("deviceOnOffstatus : "+deviceOnOffstatus);
//        log.info("deviceOnOffTime : "+deviceOnOffTime);

        data.put("deviceOnOffstatus",deviceOnOffstatus);
        data.put("deviceOnOffTime",deviceOnOffTime);
        data.put("awss3url",AWSS3URL);
        data.put("devices",devices);
        data.put("status",status);
        data.put("temp_brd",temp_brd);
        data.put("level",level);
        data.put("batt_voltage",batt_voltage);
        data.put("solar_current",solar_current);
        data.put("solar_voltage",solar_voltage);
        data.put("gps_laDatas",gps_laDatas2);
        data.put("gps_loDatas",gps_loDatas2);
        data.put("rsrp",rsrp);
        data.put("frontDoor_sol",frontDoor_sol);
        data.put("inputDoor",inputDoor);

        data.put("s_tmp2",s_tmp2);
        data.put("s_actuator_level",s_actuator_level);
        data.put("s_dis_info_level",s_dis_info_level);
        data.put("s_dis_info_weight",s_dis_info_weight);
        data.put("s_language",s_language);

        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 상태값, 쓰레기양, 지도 차트
//    @Transactional
    @PostMapping("dataGraph")
    public ResponseEntity dataGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<String> stateNames = new ArrayList<>(); // AWS 장비 status값 이름
        List<Object> statusDatas = new ArrayList<>(); // AWS 장비 status값 리스트
        List<String> statusSize = new ArrayList<>(); // 상태값 사이즈
        List<List<String>> statusHardcording = new ArrayList<>(); // 정상or주의or심각 값이 0일경우 0값넣는 리스트
        statusHardcording.add(Arrays.asList("정상", "0"));
        statusHardcording.add(Arrays.asList("주의", "0"));
        statusHardcording.add(Arrays.asList("심각", "0"));
        List<String> barDataColumns = new ArrayList<>(); //쓰레기양
        List<List<String>> circleDataColumns = new ArrayList<>(); //상태값
        List<Integer> circleDataCount = new ArrayList<>(); // 상태값 개수

        List<List<Object>> mapDataColumns = new ArrayList<>(); // 맵데이터 담는 리스트
        List<String> deviceIdNames = new ArrayList<>(); // AWS 장비 device값 이름
        List<String> gps_laDatas = new ArrayList<>(); // AWS 장비 gps_la값 리스트
        List<String> gps_loDatas = new ArrayList<>(); // AWS 장비 gps_lo값 리스트
        List<String> gps_laDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        List<String> gps_loDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        //log.info("deviceids : " +deviceids);
        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트

//          log.info("AWS 장치 list : "+resData);
//          log.info("AWS 장치 data : "+resData.get("data"));
//        log.info("AWS 장치 size : "+resData.get("datacounts"));

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수
        //log.info("number : "+number);

        List<String> sortDevice = new ArrayList<>();

        //log.info("number : "+number);
        for (int i = 0; i < number; i++) {
            HashMap map = (HashMap) resData.get("data").get(i);
            sortDevice.add((String) map.get("deviceid"));
        }

        sortDevice.sort(Comparator.naturalOrder()); // 오름차순 정렬시키기
        //log.info("오름차순 : " + sortDevice);
        barDataColumns.add("쓰레기양");
        for (String deviceid : sortDevice) {
            for (int i = 0; i < number; i++) {
                HashMap map = (HashMap) resData.get("data").get(i);
                //log.info("데이터 : "+map);
                if (map.get("deviceid") == deviceid) {
                    //배열에다넣기
                    if (map.get("status").equals("normal")) {
                        map.replace("status", "정상");
                    } else if (map.get("status").equals("caution")) {
                        map.replace("status", "주의");
                    } else if (map.get("status").equals("severe")) {
                        map.replace("status", "심각");
                    }
                    barDataColumns.add((String) map.get("level")); //배출량 리스트
                    statusDatas.add(map.get("status")); //상태값차트

                    deviceIdNames.add((String) map.get("deviceid")); //맵 데이터 차트
                    gps_laDatas.add((String) map.get("gps_la")); //맵 데이터 차트
                    gps_loDatas.add((String) map.get("gps_lo")); //맵 데이터 차트
                }
            }
        }

        for (int i = 0; i < number; i++) {
            if (!statusSize.contains(statusDatas.get(i))) {
                statusSize.add((String) statusDatas.get(i));
            }
        }

//        log.info("statusDatas : " +statusDatas);
//        log.info("statusSize : " +statusSize);
//        log.info("statusSize.size() (최대3) : " +statusSize.size());

        //log.info("AWS 장치 deviceid : " +deviceIdNames);
        //log.info("장치id 0번째 : " +deviceIdNames.get(0));

        // html파일에 타임스탬프 보내기
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd");
        SimpleDateFormat timestampformatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Date time = new Date();
        String today = null;

        cal.setTime(time);
        cal.add(Calendar.DATE, -1);
        today = timestampformatter.format(cal.getTime());
        Timestamp nowtimestamp = Timestamp.valueOf(today);
        String yyyymmdd1 = format.format(cal.getTime());

        cal.setTime(time);
        cal.add(Calendar.DATE, -31);
        today = timestampformatter.format(cal.getTime());
        Timestamp beforetimestamp = Timestamp.valueOf(today);
        String yyyymmdd2 = format.format(cal.getTime());

//        log.info("어제날짜 타임스탬프 : " +nowtimestamp);
//        log.info("어제날짜 yyyymmdd1 : " +yyyymmdd1);
//        log.info("30일전날짜 타임스탬프 : " +beforetimestamp);
//        log.info("30일전날짜 yyyymmdd2 : " +yyyymmdd2);

        List<Double> averages = new ArrayList<>();
        List<Timestamp> timestamps = new ArrayList<>();

        // 쿼리dsl활용
        List<DevicestatsDto> devicestatsDto = devicestatusService.queryDslDevicestatsAvgQuerydsl(deviceIdNames,yyyymmdd1,yyyymmdd2);
        int d=0;
        for(int j=0; j<31; j++) {
            cal.setTime(time);
            cal.add(Calendar.DATE, -31+j);
            today = timestampformatter.format(cal.getTime());
            Timestamp timestamp = Timestamp.valueOf(today);
            timestamps.add(timestamp);

            String today2 = format.format(cal.getTime());
            if(d<devicestatsDto.size()) {
                if (today2.equals(devicestatsDto.get(d).getYyyymmdd())) {
                    averages.add((double) Math.round(devicestatsDto.get(d).getFullLevel() * 10 / 10.0));
                    d++;
                }else {
                    averages.add(0.0);
                }
            }else {
                averages.add(0.0);
            }
        }

//        log.info("바차트 들어갈 리스트 값 : " +barDataColumns);
        List<String> statusMaster = new ArrayList<>(); //정상,주의,심각 리스트
        statusMaster.add("정상");
        statusMaster.add("주의");
        statusMaster.add("심각");

//        log.info("statusMaster : "+statusSize); // 정상,주의,심각 데이터가 있는지 확인할수있는 리스트
//        log.info("stateNames : "+stateNames);
//        log.info("statusDatas : "+statusDatas);

        // 상태값 차트구하기
        int count = 0;
        for (int i = 0; i < statusMaster.size(); i++) {
            stateNames.clear();
            for (int j = 0; j < statusMaster.size(); j++) {
                if (!stateNames.contains(statusMaster.get(i))) {
                    stateNames.add(statusMaster.get(i));
                }
            }
            for (int j = 0; j < statusDatas.size(); j++) {
                if (stateNames.contains(statusDatas.get(j))) {
                    count++;
                }
            }
            stateNames.add(Integer.toString(count));

//            log.info("stateNames 데이터 : "+stateNames);
//            log.info("건수 : "+count);

            int cnt = 0;
            int cnt2 = 1;

            if (!circleDataColumns.contains(stateNames)) {
                circleDataColumns.add(Arrays.asList(stateNames.get(cnt), stateNames.get(cnt2)));
            }
            count = 0;
        }
        //log.info("원형차트 들어갈 리스트 값 : "+circleDataColumns);

        // 각 상태값의 대한 장치 개수
        int circleCount = Integer.parseInt(circleDataColumns.get(0).get(1))
                + Integer.parseInt(circleDataColumns.get(1).get(1))
                + Integer.parseInt(circleDataColumns.get(2).get(1));
        circleDataCount.add(circleCount);
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(0).get(1)));
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(1).get(1)));
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(2).get(1)));
        //log.info("상태값 개수 리스트 : "+circleDataCount);

//        log.info("위도 : " + gps_laDatas);
//        log.info("경도 : " + gps_loDatas);
        List<HashMap<String,String>> gps_lanlon = new ArrayList<>();
        List<String> gps_laStreet = new ArrayList<>();
        List<String> gps_loStreet = new ArrayList<>();

        HashMap<String,String> lanlondeviceid;

        lanlondeviceid = new HashMap<>();
        lanlondeviceid.put("devicename","BonBu");
        lanlondeviceid.put("lan","37.547611");
        lanlondeviceid.put("lon","127.048871");
        gps_lanlon.add(lanlondeviceid);

        for (int i = 0; i < deviceIdNames.size(); i++) {
            lanlondeviceid = new HashMap<>();

            String gps_laData = gps_laDatas.get(i);
            String gps_loData = gps_loDatas.get(i);
            if(gps_loData.equals("na") || gps_loData == null || gps_loData.equals("") || gps_laData.equals("na") || gps_laData == null || gps_laData.equals("")) {
                String gps_laSubStirng = "0.0";
                String gps_loSubStirng = "0.0";

                gps_laDatas2.add(gps_laSubStirng);
                gps_loDatas2.add(gps_loSubStirng);
                mapDataColumns.add(Arrays.asList(deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)),statusDatas.get(i)));
            }else{
                if(gps_laData.startsWith("N")){
                    String gps_laSubStirng = gps_laData.replace("N","+");
                    gps_laDatas2.add(gps_laSubStirng);
                    gps_laStreet.add(gps_laSubStirng);
                }else if(gps_laData.startsWith("S")){
                    String gps_laSubStirng = gps_laData.replace("S","-");
                    gps_laDatas2.add(gps_laSubStirng);
                    gps_laStreet.add(gps_laSubStirng);
                }
                if(gps_loData.startsWith("E")){
                    String gps_loSubStirng = gps_loData.replace("E","+");
                    gps_loDatas2.add(gps_loSubStirng);
                    gps_loStreet.add(gps_loSubStirng);
                }else if(gps_loData.startsWith("W")){
                    String gps_loSubStirng = gps_loData.replace("W","-");
                    gps_loDatas2.add(gps_loSubStirng);
                    gps_loStreet.add(gps_loSubStirng);
                }
                mapDataColumns.add(Arrays.asList(deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)),statusDatas.get(i)));

                lanlondeviceid.put("devicename",deviceIdNames.get(i));
                lanlondeviceid.put("lan",gps_laDatas2.get(i));
                lanlondeviceid.put("lon",gps_loDatas2.get(i));
                gps_lanlon.add(lanlondeviceid);
            }
        }

        data.put("deviceIdNames", deviceIdNames);
        data.put("statusDatas", statusDatas);
        data.put("map_data_columns", mapDataColumns);
        data.put("circle_data_columns", circleDataColumns);
        data.put("circle_data_count", circleDataCount);
        data.put("bar_data_columns",barDataColumns);
        data.put("averages", averages);
        data.put("nowtimestamp", nowtimestamp);
        data.put("beforetimestamp", beforetimestamp);
        data.put("timestamps", timestamps);

        res.addResponse("data", data);

        return ResponseEntity.ok(res.success());

    }

    // 원차트 새로고침
    @PostMapping("dataCircleGraph")
    public ResponseEntity dataCircleGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<String> stateNames = new ArrayList<>(); // AWS 장비 status값 이름
        List<Object> statusDatas = new ArrayList<>(); // AWS 장비 status값 리스트
        List<String> statusSize = new ArrayList<>(); // 상태값 사이즈
        List<List<String>> statusHardcording = new ArrayList<>(); // 정상or주의or심각 값이 0일경우 0값넣는 리스트
        statusHardcording.add(Arrays.asList("정상","0"));
        statusHardcording.add(Arrays.asList("주의","0"));
        statusHardcording.add(Arrays.asList("심각","0"));

        List<List<String>> circleDataColumns = new ArrayList<>(); //상태값
        List<Integer> circleDataCount = new ArrayList<>(); // 상태값 개수

        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트
        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수
        List<String> sortDevice = new ArrayList<>();
        for(int i = 0; i<number; i++) {
            HashMap map = (HashMap)resData.get("data").get(i);
            sortDevice.add((String) map.get("deviceid"));
        }
        sortDevice.sort(Comparator.naturalOrder()); // 오름차순 정렬시키기

        for(String deviceid:sortDevice){
            for(int i=0; i<number; i++){
                HashMap map = (HashMap)resData.get("data").get(i);
                if (map.get("deviceid") == deviceid) {
                    if (map.get("status").equals("normal")) {
                        map.replace("status", "정상");
                    } else if (map.get("status").equals("caution")) {
                        map.replace("status", "주의");
                    } else if (map.get("status").equals("severe")) {
                        map.replace("status", "심각");
                    }
                    statusDatas.add(map.get("status")); //상태값차트
                }
            }
        }

        for(int i = 0; i<number; i++) {
            if (!statusSize.contains(statusDatas.get(i))) {
                statusSize.add((String)statusDatas.get(i));
            }
        }

        List<String> statusMaster = new ArrayList<>(); //정상,주의,심각 리스트
        statusMaster.add("정상");
        statusMaster.add("주의");
        statusMaster.add("심각");

        // 상태값 차트구하기
        int count = 0;
        for(int i=0; i<statusMaster.size(); i++){
            stateNames.clear();
            for (int j = 0; j < statusMaster.size(); j++) {
                if (!stateNames.contains(statusMaster.get(i))) {
                    stateNames.add((String)statusMaster.get(i));
                }
            }
            for (int j = 0; j < statusDatas.size(); j++) {
                if (stateNames.contains(statusDatas.get(j))) {
                    count++;
                }
            }
            stateNames.add(Integer.toString(count));

            int cnt = 0;
            int cnt2 = 1;

            if(!circleDataColumns.contains(stateNames)){
                circleDataColumns.add(Arrays.asList(stateNames.get(cnt),stateNames.get(cnt2)));
            }
            count = 0;
        }

        // 각 상태값의 대한 장치 개수
        int circleCount = Integer.parseInt(circleDataColumns.get(0).get(1))
                +Integer.parseInt(circleDataColumns.get(1).get(1))
                +Integer.parseInt(circleDataColumns.get(2).get(1));
        circleDataCount.add(circleCount);
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(0).get(1)));
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(1).get(1)));
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(2).get(1)));
        // log.info("상태값 개수 리스트 : "+circleDataCount);

        data.put("statusDatas",statusDatas);
        data.put("circle_data_columns",circleDataColumns);
        data.put("circle_data_count",circleDataCount);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 바차트 새로고침
    @PostMapping("dataBarGraph")
    public ResponseEntity dataBarGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<String> deviceIdNames = new ArrayList<>(); // AWS 장비 status값 이름

        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트
        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수
        List<String> sortDevice = new ArrayList<>();
        for(int i = 0; i<number; i++) {
            HashMap map = (HashMap)resData.get("data").get(i);
            sortDevice.add((String) map.get("deviceid"));
        }
        sortDevice.sort(Comparator.naturalOrder()); // 오름차순 정렬시키기

        for(String deviceid:sortDevice){
            for(int i=0; i<number; i++){
                HashMap map = (HashMap)resData.get("data").get(i);
                if (map.get("deviceid") == deviceid) {
                    deviceIdNames.add((String) map.get("deviceid")); //맵 데이터 차트
                }
            }
        }

        // html파일에 타임스탬프 보내기
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd");
        SimpleDateFormat timestampformatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Date time = new Date();
        String today = null;

        cal.setTime(time);
        cal.add(Calendar.DATE, -1);
        today = timestampformatter.format(cal.getTime());
        Timestamp nowtimestamp = Timestamp.valueOf(today);
        String yyyymmdd1 = format.format(cal.getTime());

        cal.setTime(time);
        cal.add(Calendar.DATE, -31);
        today = timestampformatter.format(cal.getTime());
        Timestamp beforetimestamp = Timestamp.valueOf(today);
        String yyyymmdd2 = format.format(cal.getTime());

        List<Double> averages = new ArrayList<>();
        List<Timestamp> timestamps = new ArrayList<>();

        // 쿼리dsl활용하기 프로젝트
        List<DevicestatsDto> devicestatsDto = devicestatusService.queryDslDevicestatsAvgQuerydsl(deviceIdNames,yyyymmdd1,yyyymmdd2);
        int d=0;
        for(int j=0; j<31; j++) {
            cal.setTime(time);
            cal.add(Calendar.DATE, -31+j);
            today = timestampformatter.format(cal.getTime());
            Timestamp timestamp = Timestamp.valueOf(today);
            timestamps.add(timestamp);

            String today2 = format.format(cal.getTime());
            if(d<devicestatsDto.size()) {
                if (today2.equals(devicestatsDto.get(d).getYyyymmdd())) {
                    averages.add((double) Math.round(devicestatsDto.get(d).getFullLevel() * 10 / 10.0));
                    d++;
                }else {
                    averages.add(0.0);
                }
            }else {
                averages.add(0.0);
            }
        }

        data.put("averages", averages);
        data.put("nowtimestamp", nowtimestamp);
        data.put("beforetimestamp", beforetimestamp);
        data.put("timestamps", timestamps);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 맵 새로고침
    @PostMapping("dataMapGraph")
    public ResponseEntity dataMapGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<Object> statusDatas = new ArrayList<>(); // AWS 장비 status값 리스트
        List<String> barDataColumns = new ArrayList<>(); //쓰레기양
        List<List<Object>> mapDataColumns = new ArrayList<>(); // 맵데이터 담는 리스트
        List<String> deviceIdNames = new ArrayList<>(); // AWS 장비 device값 이름
        List<String> gps_laDatas = new ArrayList<>(); // AWS 장비 gps_la값 리스트
        List<String> gps_loDatas = new ArrayList<>(); // AWS 장비 gps_lo값 리스트
        List<String> gps_laDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        List<String> gps_loDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환

        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수
        List<String> sortDevice = new ArrayList<>();
        for(int i = 0; i<number; i++) {
            HashMap map = (HashMap)resData.get("data").get(i);
            sortDevice.add((String) map.get("deviceid"));
        }
        sortDevice.sort(Comparator.naturalOrder()); // 오름차순 정렬시키기
        barDataColumns.add("쓰레기양");
        for(int i=0; i<number; i++){
            HashMap map = (HashMap)resData.get("data").get(i);
            if (map.get("status").equals("normal")) {
                map.replace("status", "정상");
            } else if (map.get("status").equals("caution")) {
                map.replace("status", "주의");
            } else if (map.get("status").equals("severe")) {
                map.replace("status", "심각");
            }
            statusDatas.add((String) map.get("status")); //상태 리스트
            barDataColumns.add((String) map.get("level")); //배출량 리스트
            deviceIdNames.add((String) map.get("deviceid")); //장리 리스트
            gps_laDatas.add((String) map.get("gps_la")); // gps1 리스트
            gps_loDatas.add((String) map.get("gps_lo")); //gps2 리스트
        }

//        log.info("위도 : " + gps_laDatas);
//        log.info("경도 : " + gps_loDatas);
        for (int i = 0; i < deviceIdNames.size(); i++) {
            String gps_laData = gps_laDatas.get(i);
            String gps_loData = gps_loDatas.get(i);
            if(gps_loData.equals("na") || gps_loData.equals(null) || gps_loData.equals("") || gps_laData.equals("na") || gps_laData.equals(null) || gps_laData.equals("")) {
                String gps_laSubStirng = "0.0";
                String gps_loSubStirng = "0.0";

                gps_laDatas2.add(gps_laSubStirng);
                gps_loDatas2.add(gps_loSubStirng);
                mapDataColumns.add(Arrays.asList(deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)),statusDatas.get(i)));
            }else{
                if(gps_laData.startsWith("N")){
                    String gps_laSubStirng = gps_laData.replace("N","+");
                    gps_laDatas2.add(gps_laSubStirng);
                }else if(gps_laData.startsWith("S")){
                    String gps_laSubStirng = gps_laData.replace("S","-");
                    gps_laDatas2.add(gps_laSubStirng);
                }
                if(gps_loData.startsWith("E")){
                    String gps_loSubStirng = gps_loData.replace("E","+");
                    gps_loDatas2.add(gps_loSubStirng);
                }else if(gps_loData.startsWith("W")){
                    String gps_loSubStirng = gps_loData.replace("W","-");
                    gps_loDatas2.add(gps_loSubStirng);
                }
                mapDataColumns.add(Arrays.asList(deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)),statusDatas.get(i)));
            }
        }

        data.put("statusDatas",statusDatas);
        data.put("map_data_columns",mapDataColumns);
        data.put("bar_data_columns",barDataColumns);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 위치기반 선택장비 리스트 상세데이터 보내기
    @PostMapping("detailMapDataGraph")
    public ResponseEntity<Map<String,Object>> detailMapDataGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        //log.info("deviceids : "+deviceids);

        List<String> statusDatas = new ArrayList<>(); // AWS 장비 status값 리스트
        List<String> barDataColumns = new ArrayList<>(); //쓰레기양
        List<List<Object>> mapDataColumns = new ArrayList<>(); // 맵데이터 담는 리스트
        List<String> deviceIdNames = new ArrayList<>(); // AWS 장비 device값 이름
        List<String> gps_laDatas = new ArrayList<>(); // AWS 장비 gps_la값 리스트
        List<String> gps_loDatas = new ArrayList<>(); // AWS 장비 gps_lo값 리스트
        List<String> gps_laDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        List<String> gps_loDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        List<String> temp_brd = new ArrayList<>(); //온도리스트
        List<String> batt_voltage = new ArrayList<>(); //배터리잔량리스트
        List<String> solar_current = new ArrayList<>(); //전류 리스트
        List<String> solar_voltage = new ArrayList<>(); //전압리스트
        List<Integer> rsrp = new ArrayList<>(); // 안테나
        List<String> frontDoor_sol = new ArrayList<>(); // 전면도어
        List<String> inputDoor = new ArrayList<>();  // 투입구

        // ITAI
        List<String> s_tmp2 = new ArrayList<>(); // ITAI 온도
        List<String> s_actuator_level = new ArrayList<>(); // ITAI 배출량(%)
        List<String> s_dis_info_level = new ArrayList<>(); // ITAI 배출무게(%)
        List<String> s_dis_info_weight = new ArrayList<>(); // ITAI 배출무게(g)
        List<String> s_language = new ArrayList<>();// ITAI 사용언어

        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트
        log.info("AWS 장치 data : "+resData.get("data"));

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수

        barDataColumns.add("쓰레기양"); // 배출량 막대그래프 첫번째값 y축이름 -> 쓰레기양
        for(int i=0; i<number; i++){
            HashMap map = (HashMap)resData.get("data").get(i);

            String emType = ((String)map.get("deviceid")).substring(0,4);

            if (map.get("status").equals("normal")) {
                map.replace("status", "정상");
            } else if (map.get("status").equals("caution")) {
                map.replace("status", "주의");
            } else if (map.get("status").equals("severe")) {
                map.replace("status", "심각");
            }
            statusDatas.add((String) map.get("status")); //상태 리스트
            deviceIdNames.add((String) map.get("deviceid")); //장리 리스트
            gps_laDatas.add((String) map.get("gps_la")); // gps1 리스트
            gps_loDatas.add((String) map.get("gps_lo")); //gps2 리스트

            if(emType.equals("ISOL")) {
                temp_brd.add((String) map.get("temp_brd")); //온도 리스트
                barDataColumns.add((String) map.get("level")); //배출량 리스트
                batt_voltage.add((String) map.get("batt_voltage")); //배터리잔량 리스트
                solar_current.add((String) map.get("solar_current")); //전류 리스트
                solar_voltage.add((String) map.get("solar_voltage")); //전압 리스트

                if(!String.valueOf(map.get("rsrp")).equals("")) {
                    rsrp.add(Integer.parseInt(String.valueOf(map.get("rsrp"))));
                }else{
                    rsrp.add(null);
                }

                if(!String.valueOf(map.get("frontdoor_sol")).equals("")||!String.valueOf(map.get("frontdoor_sol")).equals("na")) {
                    String front = (String) map.get("frontdoor_sol");
                    switch (front) {
                        case "close":
                            frontDoor_sol.add("닫힘");
                            break;
                        case "open":
                            frontDoor_sol.add("열림");
                            break;
                        case "jamming":
                            frontDoor_sol.add("걸림");
                            break;
                        default:
                            frontDoor_sol.add("미확인");
                            break;
                    }
                }else{
                    frontDoor_sol.add("미확인");
                }

                if(!String.valueOf(map.get("inputdoor")).equals("")||!String.valueOf(map.get("inputdoor")).equals("na")) {
                    String inputdoor = (String) map.get("inputdoor");
                    switch (inputdoor) {
                        case "close":
                            inputDoor.add("닫힘");
                            break;
                        case "open":
                            inputDoor.add("열림");
                            break;
                        case "jamming":
                            inputDoor.add("걸림");
                            break;
                        default:
                            inputDoor.add("미확인");
                            break;
                    }
                }else{
                    inputDoor.add("미확인");
                }

            }else if(emType.equals("ITAI")){
                temp_brd.add((String) map.get("s_tmp2")); //온도 리스트
                barDataColumns.add((String) map.get("actuator_level")); //배출량 리스트
                batt_voltage.add((String) map.get("dis_info_level")); //배터리잔량 리스트
                solar_current.add((String) map.get("dis_info_weight")); //전류 리스트
                solar_voltage.add((String) map.get("language")); //전압 리스트
            }
        }

//        log.info("위도 : " + gps_laDatas);
//        log.info("경도 : " + gps_loDatas);
        for (int i = 0; i < deviceIdNames.size(); i++) {
            String gps_laData = gps_laDatas.get(i);
            String gps_loData = gps_loDatas.get(i);
            if(gps_loData.equals("na") || gps_loData.equals(null) || gps_loData.equals("") || gps_laData.equals("na") || gps_laData.equals(null) || gps_laData.equals("")) {
                String gps_laSubStirng = "0.0";
                String gps_loSubStirng = "0.0";

                gps_laDatas2.add(gps_laSubStirng);
                gps_loDatas2.add(gps_loSubStirng);
                mapDataColumns.add(Arrays.asList(deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)),statusDatas.get(i)));
            }else{
                if(gps_laData.startsWith("N")){
                    String gps_laSubStirng = gps_laData.replace("N","+");
                    gps_laDatas2.add(gps_laSubStirng);
                }else if(gps_laData.startsWith("S")){
                    String gps_laSubStirng = gps_laData.replace("S","-");
                    gps_laDatas2.add(gps_laSubStirng);
                }
                if(gps_loData.startsWith("E")){
                    String gps_loSubStirng = gps_loData.replace("E","+");
                    gps_loDatas2.add(gps_loSubStirng);
                }else if(gps_loData.startsWith("W")){
                    String gps_loSubStirng = gps_loData.replace("W","-");
                    gps_loDatas2.add(gps_loSubStirng);
                }
                mapDataColumns.add(Arrays.asList(deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)),statusDatas.get(i)));
            }
        }

        data.put("rsrp",rsrp);
        data.put("temp_brd",temp_brd);
        data.put("batt_voltage",batt_voltage);
        data.put("solar_current",solar_current);
        data.put("solar_voltage",solar_voltage);
        data.put("deviceIdNames",deviceIdNames);
        data.put("statusDatas",statusDatas);
        data.put("map_data_columns",mapDataColumns);
        data.put("bar_data_columns",barDataColumns);
        data.put("frontDoor_sol",frontDoor_sol);
        data.put("inputDoor",inputDoor);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 위치기반 상세데이터 보내기
    @PostMapping("deviceDetail")
    public ResponseEntity<Map<String,Object>> deviceDetail(@RequestParam(value="pushValue", defaultValue="") String pushValue) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        EquipmentDto deviceDetailList = dashboardService.findByEmNumber(pushValue);
        //log.info("deviceDetailList : "+deviceDetailList);

        HashMap<String,HashMap<String,Object>> onOfflineData = aciawsLambdaService.getDeviceonlineCheck(pushValue);
        Boolean deviceOnOffstatus = Boolean.parseBoolean(String.valueOf((onOfflineData.get("data").get("online"))));
        Object deviceOnOffTime = onOfflineData.get("data").get("timestamp");
//        log.info("onOfflineData : "+onOfflineData);
//        log.info("deviceOnOffTime : "+deviceOnOffTime);

        data.put("deviceOnOffstatus",deviceOnOffstatus);
        data.put("deviceOnOffTime",deviceOnOffTime);
        data.put("awss3url",AWSS3URL);
        data.put("deviceDetailList",deviceDetailList);
        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 새로고침체크여부의기 따라 유저정보 저장하기
    @PostMapping("refleshcheck")
    public ResponseEntity<Map<String,Object>> refleshcheck(@ModelAttribute AccountMapperDto accountMapperDto,
                                                           @RequestParam(value="userid", defaultValue="") String userid,
                                                           @RequestParam(value="checknum", defaultValue="") Integer checknum,
                                                           @RequestParam(value="timenum", defaultValue="") Integer timenum,
                                                           HttpServletRequest request) {
        AjaxResponse res = new AjaxResponse();
        HttpSession session = request.getSession();
        Account account = modelMapper.map(accountMapperDto, Account.class);
        Optional<Account> optionalAccount = accountService.findByUserid(userid);

        if(!optionalAccount.isPresent()){
            log.info("사용자 일반 관리자(일반정보) : 사용자아이디: '" + account.getUserid() + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
        }else{
            account.setId(optionalAccount.get().getId());
            account.setUserid(userid);
            account.setPassword(optionalAccount.get().getPassword());
            account.setUsername(optionalAccount.get().getUsername());
            account.setEmail(optionalAccount.get().getEmail());
            account.setCellphone(optionalAccount.get().getCellphone());
            account.setRole(optionalAccount.get().getRole());
            account.setApprovalType(optionalAccount.get().getApprovalType());
            account.setTeam(optionalAccount.get().getTeam());
            account.setCompany(optionalAccount.get().getCompany());
            account.setPosition(optionalAccount.get().getPosition());
            account.setUserRefleshCheck(checknum);
            account.setUserRefleshCount(timenum);
            account.setUserLayoutNumber(optionalAccount.get().getUserLayoutNumber());
            account.setUserPhoto(optionalAccount.get().getUserPhoto());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setModify_id(optionalAccount.get().getModify_id());
            account.setModifyDateTime(optionalAccount.get().getModifyDateTime());

            session.setAttribute("refleshCheck",checknum);
            session.setAttribute("refleshCheckNum",timenum);
        }

        accountService.modifyAccount(account);

        return ResponseEntity.ok(res.success());
    }

    //레이아웃 변경마다 로그인한 유저의 레이아웃모드 업데이트
    @PostMapping("layoutNumber")
    public ResponseEntity<Map<String,Object>> layoutNumber(@ModelAttribute AccountMapperDto accountMapperDto,
                                                           @RequestParam(value="userid", defaultValue="") String userid,
                                                           @RequestParam(value="layoutNum", defaultValue="") Integer layoutNum,
                                                           HttpServletRequest request) {
        AjaxResponse res = new AjaxResponse();
        HttpSession session = request.getSession();
        Account account = modelMapper.map(accountMapperDto, Account.class);
        Optional<Account> optionalAccount = accountService.findByUserid(userid);

        if(!optionalAccount.isPresent()){
            //log.info("사용자 일반 관리자(일반정보) : 사용자아이디: '" + account.getUserid() + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
        }else{
            account.setId(optionalAccount.get().getId());
            account.setUserid(userid);
            account.setPassword(optionalAccount.get().getPassword());
            account.setUsername(optionalAccount.get().getUsername());
            account.setEmail(optionalAccount.get().getEmail());
            account.setCellphone(optionalAccount.get().getCellphone());
            account.setRole(optionalAccount.get().getRole());
            account.setApprovalType(optionalAccount.get().getApprovalType());
            account.setTeam(optionalAccount.get().getTeam());
            account.setCompany(optionalAccount.get().getCompany());
            account.setPosition(optionalAccount.get().getPosition());
            account.setUserRefleshCheck(optionalAccount.get().getUserRefleshCheck());
            account.setUserRefleshCount(optionalAccount.get().getUserRefleshCount());
            account.setUserLayoutNumber(layoutNum);
            account.setUserPhoto(optionalAccount.get().getUserPhoto());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setModify_id(optionalAccount.get().getModify_id());
            account.setModifyDateTime(optionalAccount.get().getModifyDateTime());

            session.setAttribute("layoutNum",layoutNum);

        }

        accountService.modifyAccount(account);

        return ResponseEntity.ok(res.success());
    }

    // 지역 select바뀌기
    @PostMapping("location")
    public ResponseEntity<Map<String,Object>> location(@RequestParam(value="s_emCountry", defaultValue="")String emCountry){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        CodeType codeType = CodeType.valueOf("C0005");

        if (emCountry.equals("")) {
            List<MasterCodeDto> locationData = null;

            data.put("locationData",locationData);

            res.addResponse("data",data);
            return ResponseEntity.ok(res.success());
        }else{
            List<MasterCodeDto> locationData = masterCodeService.findAllByCodeTypeEqualsAndBcRef1(codeType,emCountry);

            data.put("locationData",locationData);

            res.addResponse("data",data);
            return ResponseEntity.ok(res.success());
        }
    }

    // 모뎀리셋
    @PostMapping("modemReset")
    public ResponseEntity<Map<String,Object>> modemReset(@RequestParam(value="emNumber", defaultValue="") String emNumber,
                                                          @RequestParam(value="timestamp", defaultValue="") String timestamp) throws Exception {
        AjaxResponse res = new AjaxResponse();

//        log.info("장비코드 : "+emNumber);
//        log.info("timestamp : "+timestamp);

        // 모뎀리셋버튼 실행 keyString -> modemreset
        aciawsIoTDeviceService.setModemReset(emNumber,timestamp);

        return ResponseEntity.ok(res.success());
    }
}