package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountMapperDto;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.devicestats.DevicestatsDto;
import kr.co.broadwave.aci.devicestats.DevicestatusService;
import kr.co.broadwave.aci.equipment.EquipmentDto;
import kr.co.broadwave.aci.equipment.EquipmentService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
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
    private final EquipmentService equipmentService;
    private final MasterCodeService masterCodeService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final DevicestatusService devicestatusService;

    @Autowired
    public DashboardRestController(DashboardService dashboardService,
                                   ModelMapper modelMapper,
                                   AccountService accountService,
                                   MasterCodeService masterCodeService,
                                   EquipmentService equipmentService,
                                   DevicestatusService devicestatusService) {
        this.modelMapper = modelMapper;
        this.dashboardService = dashboardService;
        this.accountService = accountService;
        this.equipmentService = equipmentService;
        this.masterCodeService = masterCodeService;
        this.devicestatusService = devicestatusService;
    }


    @PostMapping("monitering")
    public ResponseEntity monitering(@RequestParam(value="deviceids", defaultValue="") String deviceids){
        log.info("모니터링 조회 시작");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        //Sample of deviceids variable : {"deviceids":["ISOL-KR-SEOUL-0001","ISOL-KR-SEOUL-0002"]}

        HashMap<String, Object> resData = dashboardService.getDeviceLastestState(deviceids);
        log.info("resData : "+resData);
        data.clear();
        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        log.info("모니터링 조회 성공 ");
        return ResponseEntity.ok(res.success());

    }

    @PostMapping("devicelist")
    public ResponseEntity devicelist(){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        log.info("Device목록가져오기 시작");

        HashMap<String, Object> resData = dashboardService.getDeviceList("ISOL");
        data.clear();
        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        log.info("Device목록가져오기 성공 ");
        return ResponseEntity.ok(res.success());

    }

    @PostMapping("devicehitory")
    public ResponseEntity devicehistory(@RequestParam(value="deviceid", defaultValue="") String deviceid
                                        ,@RequestParam(value="intervaltime", defaultValue="") String intervaltime){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        //log.info("Device History 가져오기 시작");

        HashMap<String, Object> resData = dashboardService.getDeviceHistory(deviceid,intervaltime);
        data.clear();
        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        //log.info("Device History 가져오기 성공 ");
        return ResponseEntity.ok(res.success());

    }

    //장비 리스트 뿌리기
    @PostMapping ("deviceInfoList")
    public ResponseEntity deviceInfoList(@RequestParam (value="emNumber", defaultValue="") String emNumber,
//                                                            @RequestParam (value="emAgency", defaultValue="") String  emAgency,
                                                            @RequestParam (value="emType", defaultValue="")String emType,
                                                            @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                                            @RequestParam (value="emLocation", defaultValue="")String emLocation,
                                                            @PageableDefault Pageable pageable){

        Long emTypeId = null;
        Long emCountryId = null;
        Long emLocationId = null;

        log.info("emNumber : "+emNumber);
//        log.info("emAgency : "+emAgency);
        log.info("emType : "+emType);
        log.info("emCountry : "+emCountry);
       log.info("emLocation : "+emLocation);

        if(!emType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(emType);
            emTypeId = emTypes.get().getId();
        }
        if(!emCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(emCountry);
            emCountryId = emCountrys.get().getId();
        }
        if(!emLocation.equals("")){
            Optional<MasterCode> emLocations = masterCodeService.findByCode(emLocation);
            emLocationId = emLocations.get().getId();
        }

        Page<DashboardDeviceListViewDto> deviceInfoListDtos =
                dashboardService.findByDashboardListView(emNumber, emTypeId, emCountryId, emLocationId, pageable);
        return CommonUtils.ResponseEntityPage(deviceInfoListDtos);
    }

    // ASW 장치 데이터리스트 뿌리기
    @PostMapping("deviceAWSListView")
    public ResponseEntity deviceAWSListView(@RequestParam(value="deviceids", defaultValue="") String deviceids) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<String> devices = new ArrayList<>(); //장비값넣기
        List<String> status = new ArrayList<>(); // 상태값리스트
        List<String> temp_brd = new ArrayList<>(); // 온도리스트
        List<String> level = new ArrayList<>(); // 배출량리스트
        List<String> batt_level = new ArrayList<>(); // 배터리잔량리스트
        List<String> solar_current = new ArrayList<>(); // 전류리스트
        List<String> solar_voltage = new ArrayList<>(); // 전압리스트
        List<String> gps_laDatas = new ArrayList<>();// 위도리스트
        List<String> gps_loDatas = new ArrayList<>();  // 경도리스트
        List<String> gps_laDatas2 = new ArrayList<>();// 위도리스트
        List<String> gps_loDatas2 = new ArrayList<>();  // 경도리스트

        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids);
        List<String> sortDevice = new ArrayList<>();

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수

        for (int i = 0; i < number; i++) {
            HashMap map = (HashMap) resData.get("data").get(i);
            sortDevice.add((String) map.get("deviceid"));
        }
        sortDevice.sort(Comparator.naturalOrder());
        //log.info("오름차순 : " + sortDevice);

        // 아이디값의 따라 데이터넣기(오름차순)
        for (String deviceid : sortDevice) {
            for (int i = 0; i < number; i++) {
                HashMap map = (HashMap) resData.get("data").get(i);
                if (map.get("deviceid") == deviceid) {

                    if (map.get("status").equals("normal")) {
                        map.replace("status", "정상");
                    } else if (map.get("status").equals("caution")) {
                        map.replace("status", "주의");
                    } else if (map.get("status").equals("severe")) {
                        map.replace("status", "심각");
                    }

                    devices.add((String) map.get("deviceid")); //장비값넣기
                    status.add((String) map.get("status")); //상태값넣기
                    temp_brd.add((String) map.get("temp_brd")); // 온도값넣기
                    level.add((String) map.get("level")); //배출량값넣기
                    batt_level.add((String) map.get("batt_level")); //배터리잔량값넣기
                    solar_current.add((String) map.get("solar_current")); //전류값넣기
                    solar_voltage.add((String) map.get("solar_voltage")); //전압값넣기
                    gps_laDatas.add((String) map.get("gps_la")); //위도값넣기
                    gps_loDatas.add((String) map.get("gps_lo")); //경도값넣기
                }
            }
        }


        //log.info("위도 : " + gps_laDatas);
        //log.info("경도 : " + gps_loDatas);
        for (int i = 0; i < number; i++) {
            String gps_laData = gps_laDatas.get(i);
            String gps_loData = gps_loDatas.get(i);
            if(gps_loData.equals("na") || gps_loData.equals(null) || gps_loData.equals("") || gps_laData.equals("na") || gps_laData.equals(null) || gps_laData.equals("")) {
                String gps_laSubStirng = "0.0";
                String gps_loSubStirng = "0.0";

                gps_laDatas2.add(gps_laSubStirng);
                gps_loDatas2.add(gps_loSubStirng);
            }else{
                if(gps_laData.substring(0,1).equals("N")){
                    String gps_laSubStirng = gps_laData.replace("N","+");
                    gps_laDatas2.add(gps_laSubStirng);
                }else if(gps_laData.substring(0,1).equals("S")){
                    String gps_laSubStirng = gps_laData.replace("S","-");
                    gps_laDatas2.add(gps_laSubStirng);
                }
                if(gps_loData.substring(0,1).equals("E")){
                    String gps_loSubStirng = gps_loData.replace("E","+");
                    gps_loDatas2.add(gps_loSubStirng);
                }else if(gps_loData.substring(0,1).equals("W")){
                    String gps_loSubStirng = gps_loData.replace("W","-");
                    gps_loDatas2.add(gps_loSubStirng);
                }
            }
        }

        data.clear();
        data.put("awss3url",AWSS3URL);
        data.put("devices",devices);
        data.put("status",status);
        data.put("temp_brd",temp_brd);
        data.put("level",level);
        data.put("batt_level",batt_level);
        data.put("solar_current",solar_current);
        data.put("solar_voltage",solar_voltage);
        data.put("gps_laDatas",gps_laDatas2);
        data.put("gps_loDatas",gps_loDatas2);

//        data.put("aswListDatas",resData.get("data"));
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 상태값, 쓰레기양, 지 차트
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

        //log.info("AWS 장치 list : "+resData);
        //log.info("AWS 장치 data : "+resData.get("data"));
//        log.info("AWS 장치 size : "+resData.get("datacounts"));

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수
        log.info("number : "+number);

        List<String> sortDevice = new ArrayList<>();

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
                    stateNames.add((String) statusMaster.get(i));
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
        for (int i = 0; i < deviceIdNames.size(); i++) {
            String gps_laData = gps_laDatas.get(i);
            String gps_loData = gps_loDatas.get(i);
            if(gps_loData.equals("na") || gps_loData.equals(null) || gps_loData.equals("") || gps_laData.equals("na") || gps_laData.equals(null) || gps_laData.equals("")) {
                String gps_laSubStirng = "0.0";
                String gps_loSubStirng = "0.0";

                gps_laDatas2.add(gps_laSubStirng);
                gps_loDatas2.add(gps_loSubStirng);
                mapDataColumns.add(Arrays.asList((String) deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)), (String) statusDatas.get(i)));
            }else{
                if(gps_laData.substring(0,1).equals("N")){
                    String gps_laSubStirng = gps_laData.replace("N","+");
                    gps_laDatas2.add(gps_laSubStirng);
                }else if(gps_laData.substring(0,1).equals("S")){
                    String gps_laSubStirng = gps_laData.replace("S","-");
                    gps_laDatas2.add(gps_laSubStirng);
                }
                if(gps_loData.substring(0,1).equals("E")){
                    String gps_loSubStirng = gps_loData.replace("E","+");
                    gps_loDatas2.add(gps_loSubStirng);
                }else if(gps_loData.substring(0,1).equals("W")){
                    String gps_loSubStirng = gps_loData.replace("W","-");
                    gps_loDatas2.add(gps_loSubStirng);
                }
                mapDataColumns.add(Arrays.asList((String) deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)), (String) statusDatas.get(i)));
            }
        }

//        log.info("deviceIdNames : "+deviceIdNames);
//        log.info("gps_laDatas2 : "+gps_laDatas2);
//        log.info("gps_loDatas2 : "+gps_loDatas2);
//        log.info("mapDataColumns : "+mapDataColumns);

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
                mapDataColumns.add(Arrays.asList((String) deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)), (String) statusDatas.get(i)));
            }else{
                if(gps_laData.substring(0,1).equals("N")){
                    String gps_laSubStirng = gps_laData.replace("N","+");
                    gps_laDatas2.add(gps_laSubStirng);
                }else if(gps_laData.substring(0,1).equals("S")){
                    String gps_laSubStirng = gps_laData.replace("S","-");
                    gps_laDatas2.add(gps_laSubStirng);
                }
                if(gps_loData.substring(0,1).equals("E")){
                    String gps_loSubStirng = gps_loData.replace("E","+");
                    gps_loDatas2.add(gps_loSubStirng);
                }else if(gps_loData.substring(0,1).equals("W")){
                    String gps_loSubStirng = gps_loData.replace("W","-");
                    gps_loDatas2.add(gps_loSubStirng);
                }
                mapDataColumns.add(Arrays.asList((String) deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)), (String) statusDatas.get(i)));
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
    public ResponseEntity detailMapDataGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();


        List<String> statusDatas = new ArrayList<>(); // AWS 장비 status값 리스트
        List<String> deviceidDatas = new ArrayList<>(); // AWS 장비 ID값 리스트
        List<String> barDataColumns = new ArrayList<>(); //쓰레기양
        List<List<Object>> mapDataColumns = new ArrayList<>(); // 맵데이터 담는 리스트
        List<String> deviceIdNames = new ArrayList<>(); // AWS 장비 device값 이름
        List<String> gps_laDatas = new ArrayList<>(); // AWS 장비 gps_la값 리스트
        List<String> gps_loDatas = new ArrayList<>(); // AWS 장비 gps_lo값 리스트
        List<String> gps_laDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        List<String> gps_loDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        List<String> temp_brd = new ArrayList<>(); //온도리스트
        List<String> batt_level = new ArrayList<>(); //배터리잔량리스트
        List<String> solar_current = new ArrayList<>(); //전류 리스트
        List<String> solar_voltage = new ArrayList<>(); //전압리스트

        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트

//        log.info("AWS 장치 data : "+resData.get("data"));

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수

        barDataColumns.add("쓰레기양"); // 배출량 막대그래프 첫번째값 y축이름 -> 쓰레기양
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
                temp_brd.add((String) map.get("temp_brd")); //온도 리스트
                batt_level.add((String) map.get("batt_level")); //배터리잔량 리스트
                solar_current.add((String) map.get("solar_current")); //전류 리스트
                solar_voltage.add((String) map.get("solar_voltage")); //전압 리스트
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
                mapDataColumns.add(Arrays.asList((String) deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)), (String) statusDatas.get(i)));
            }else{
                if(gps_laData.substring(0,1).equals("N")){
                    String gps_laSubStirng = gps_laData.replace("N","+");
                    gps_laDatas2.add(gps_laSubStirng);
                }else if(gps_laData.substring(0,1).equals("S")){
                    String gps_laSubStirng = gps_laData.replace("S","-");
                    gps_laDatas2.add(gps_laSubStirng);
                }
                if(gps_loData.substring(0,1).equals("E")){
                    String gps_loSubStirng = gps_loData.replace("E","+");
                    gps_loDatas2.add(gps_loSubStirng);
                }else if(gps_loData.substring(0,1).equals("W")){
                    String gps_loSubStirng = gps_loData.replace("W","-");
                    gps_loDatas2.add(gps_loSubStirng);
                }
                mapDataColumns.add(Arrays.asList((String) deviceIdNames.get(i), Double.parseDouble(gps_laDatas2.get(i)), Double.parseDouble(gps_loDatas2.get(i)), (String) statusDatas.get(i)));
            }
        }

        data.put("temp_brd",temp_brd);
        data.put("batt_level",batt_level);
        data.put("solar_current",solar_current);
        data.put("solar_voltage",solar_voltage);
        data.put("deviceIdNames",deviceIdNames);
        data.put("statusDatas",statusDatas);
        data.put("map_data_columns",mapDataColumns);
        data.put("bar_data_columns",barDataColumns);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 위치기반 상세데이터 보내기
    @PostMapping("deviceDetail")
    public ResponseEntity deviceDetail(@RequestParam(value="pushValue", defaultValue="") String pushValue) {
            AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        EquipmentDto deviceDetailList = dashboardService.findByEmNumber(pushValue);
        log.info("deviceDetailList : "+deviceDetailList);

        data.put("awss3url",AWSS3URL);
        data.put("deviceDetailList",deviceDetailList);
        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 새로고침체크여부의기 따라 유저정보 저장하기
    @PostMapping("refleshcheck")
    public ResponseEntity refleshcheck(@ModelAttribute AccountMapperDto accountMapperDto,
                                       @RequestParam(value="userid", defaultValue="") String userid,
                                       @RequestParam(value="checknum", defaultValue="") Integer checknum,
                                       @RequestParam(value="timenum", defaultValue="") Integer timenum) {
        AjaxResponse res = new AjaxResponse();

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
            account.setPosition(optionalAccount.get().getPosition());
            account.setUserRefleshCheck(checknum);
            account.setUserRefleshCount(timenum);
            account.setUserPhoto(optionalAccount.get().getUserPhoto());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setModify_id(optionalAccount.get().getModify_id());
            account.setModifyDateTime(optionalAccount.get().getModifyDateTime());
        }

        accountService.modifyAccount(account);

        return ResponseEntity.ok(res.success());
    }

    // 지역 select바뀌기
    @PostMapping("location")
    public ResponseEntity location(@RequestParam(value="s_emCountry", defaultValue="")String emCountry){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        CodeType codeType = CodeType.valueOf("C0005");

        if (emCountry.equals("")) {
            List<MasterCodeDto> locationData = null;

            data.clear();
            data.put("locationData",locationData);

            res.addResponse("data",data);
            return ResponseEntity.ok(res.success());
        }else{
            List<MasterCodeDto> locationData = masterCodeService.findAllByCodeTypeEqualsAndBcRef1(codeType,emCountry);

            data.clear();
            data.put("locationData",locationData);

            res.addResponse("data",data);
            return ResponseEntity.ok(res.success());
        }
    }

}
