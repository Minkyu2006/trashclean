package kr.co.broadwave.aci.dashboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.equipment.EquipmentService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.rmi.CORBA.ValueHandler;
import java.io.IOException;
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


    private final DashboardService dashboardService;
    private final EquipmentService equipmentService;
    private final MasterCodeService masterCodeService;

    @Autowired
    public DashboardRestController(DashboardService dashboardService,
                                                    MasterCodeService masterCodeService,
                                                    EquipmentService equipmentService) {
        this.dashboardService = dashboardService;
        this.equipmentService = equipmentService;
        this.masterCodeService = masterCodeService;
    }


    @PostMapping("monitering")
    public ResponseEntity monitering(@RequestParam(value="deviceids", defaultValue="") String deviceids
                            ){
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

    // aws 데이터값 뿌리기 (차트,리스트 View) 안쓰는함수
    @PostMapping ("awsDataListView")
    public ResponseEntity awsDataListView(){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<Equipment> equipment = dashboardService.findAll();

        data.clear();
        data.put("equipment",equipment);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    //장비 리스트 뿌리기
    @PostMapping ("deviceInfoList")
    public ResponseEntity deviceInfoList(@RequestParam (value="emNumber", defaultValue="") String emNumber,
                                                            @RequestParam (value="emAgency", defaultValue="") String  emAgency,
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

        Page<DashboardDeviceListViewDto> deviceInfoListDtos =
                dashboardService.findByDashboardListView(emNumber, emTypeId, emAgency, emCountryId, pageable);

        return CommonUtils.ResponseEntityPage(deviceInfoListDtos);
    }

    // ASW 장치 데이터리스트 뿌리기
    @PostMapping("deviceAWSListView")
    public ResponseEntity deviceAWSListView(@RequestParam(value="deviceids", defaultValue="") String deviceids) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        HashMap<String, Object> resData = dashboardService.getDeviceLastestState(deviceids);

        data.clear();
        data.put("aswListDatas",resData.get("data"));
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 쓰레기통 상태값, 쓰레기양 차트
//    @Transactional
    @PostMapping("dataGraph")
    public ResponseEntity dataGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) throws IOException {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<String> stateNames = new ArrayList<>(); // AWS 장비 status값 이름
        List<Object> statusDatas = new ArrayList<>(); // AWS 장비 status값 리스트
        List<String> deviceidDatas = new ArrayList<>(); // AWS 장비 ID값 리스트
        List<String> statusSize = new ArrayList<>(); // 상태값 사이즈
        List<List<String>> statusHardcording = new ArrayList<>(); // 정상or주의or심각 값이 0일경우 0값넣는 리스트
        statusHardcording.add(Arrays.asList("정상","0"));
        statusHardcording.add(Arrays.asList("주의","0"));
        statusHardcording.add(Arrays.asList("심각","0"));

        List<List<String>> circleDataColumns = new ArrayList<>(); //상태값
        List<Integer> circleDataCount = new ArrayList<>(); // 상태값 개수

        List<String> barDataColumns = new ArrayList<>(); //쓰레기양
        List<String> mapBarDataColumns = new ArrayList<>(); //맵에 보여줄 데이터 쓰레기양

        List<List<Object>> mapDataColumns = new ArrayList<>(); // 맵데이터 담는 리스트
        List<String> deviceIdNames = new ArrayList<>(); // AWS 장비 device값 이름
        List<String> gps_laDatas = new ArrayList<>(); // AWS 장비 gps_la값 리스트
        List<String> gps_loDatas = new ArrayList<>(); // AWS 장비 gps_lo값 리스트
        List<String> gps_laDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        List<String> gps_loDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트



        Map<String, String> deviceMap = mapper.readValue(deviceids, Map.class);
        List<String> keySetList = new ArrayList<>(deviceMap.keySet());
        // 오름차순 //
        Collections.sort(keySetList, (o1, o2) -> (deviceMap.get(o1).compareTo(deviceMap.get(o2))));
        List<String> a = new ArrayList<>();
        for(String key : keySetList) {
            System.out.println(String.format("Key : %s, Value : %s", key, a.add(deviceMap.get(key))));
        }
        System.out.println(a);


//        log.info("AWS 장치 list : "+resData);
//        log.info("AWS 장치 data : "+resData.get("data"));
//        log.info("AWS 장치 size : "+resData.get("datacounts"));

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString()); //반복수
//        log.info("number : "+number);

        barDataColumns.add("쓰레기양"); // 배출량 막대그래프 첫번째값 y축이름 -> 쓰레기양
        for(int i = 0; i<number; i++){
            Object dataObject = resData.get("data").get(i);
            HashMap map = (HashMap) dataObject;
            if (map.get("status").equals("caution")) {
                map.replace("status", "주의");
            } else if (map.get("status").equals("normal")) {
                map.replace("status", "정상");
            } else if (map.get("status").equals("severe")) {
                map.replace("status", "심각");
            }

            statusDatas.add(map.get("status")); //상태값차트

            deviceidDatas.add((String) map.get("deviceid")); //배출량차트
            barDataColumns.add((String) map.get("level")); //배출량차트
            mapBarDataColumns.add((String) map.get("level")); //맵배출량차트

            deviceIdNames.add((String) map.get("deviceid")); //맵 데이터 차트
            gps_laDatas.add((String) map.get("gps_la")); //맵 데이터 차트
            gps_loDatas.add((String) map.get("gps_lo")); //맵 데이터 차트
        }

        for(int i = 0; i<number; i++) {
            if (!statusSize.contains(statusDatas.get(i))) {
                statusSize.add((String)statusDatas.get(i));
            }
        }

//        log.info("statusDatas : " +statusDatas);
//        log.info("statusSize : " +statusSize);
//        log.info("statusSize.size() (최대3) : " +statusSize.size());

 //        log.info("AWS 장치 deviceid : " +deviceidDatas);
//        log.info("바차트 들어갈 리스트 값 : " +barDataColumns);

        // 상태값 차트구하기
        int count = 0;
        for(int i=0; i<statusSize.size(); i++){
            stateNames.clear();
            for (int j = 0; j < statusSize.size(); j++) {
                if (!stateNames.contains(statusSize.get(i))) {
                    stateNames.add((String)statusSize.get(i));
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

            if(!circleDataColumns.contains(stateNames)){
                circleDataColumns.add(Arrays.asList(stateNames.get(cnt),stateNames.get(cnt2)));
            }
            count = 0;
        }

        // 값이 0일경우 넣기
        if(!statusSize.contains("정상")) {
            circleDataColumns.add(statusHardcording.get(0));
        }
        if(!statusSize.contains("주의")) {
            circleDataColumns.add(statusHardcording.get(1));
        }
        if(!statusSize.contains("심각")) {
            circleDataColumns.add(statusHardcording.get(2));
        }
        // log.info("원형차트 들어갈 리스트 값 : "+circleDataColumns);

        // 각 상태값의 대한 장치 개수
        int circleCount = Integer.parseInt(circleDataColumns.get(0).get(1))
                                +Integer.parseInt(circleDataColumns.get(1).get(1))
                                +Integer.parseInt(circleDataColumns.get(2).get(1));
        circleDataCount.add(circleCount);
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(0).get(1)));
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(1).get(1)));
        circleDataCount.add(Integer.parseInt(circleDataColumns.get(2).get(1)));
        // log.info("상태값 개수 리스트 : "+circleDataCount);

        for(int i =0; i<deviceIdNames.size(); i++){
            String gps_laData = gps_laDatas.get(i);
            String gps_loData = gps_loDatas.get(i);

            String gps_laSubStirng = gps_laData.substring(1);
            String gps_loSubStirng = gps_loData.substring(1);

            gps_laDatas2.add(gps_laSubStirng);
            gps_loDatas2.add(gps_loSubStirng);

            mapDataColumns.add(Arrays.asList((String)deviceIdNames.get(i),Double.parseDouble(gps_laDatas2.get(i)),Double.parseDouble(gps_loDatas2.get(i))));
        }

//        log.info("deviceIdNames : "+deviceIdNames);
//        log.info("gps_laDatas2 : "+gps_laDatas2);
//        log.info("gps_loDatas2 : "+gps_loDatas2);
//        log.info("mapDataColumns : "+mapDataColumns);

        data.put("statusDatas",statusDatas);
        data.put("mapBarDataColumns",mapBarDataColumns);
        data.put("map_data_columns",mapDataColumns);
        data.put("circle_data_columns",circleDataColumns);
        data.put("circle_data_count",circleDataCount);
        data.put("bar_data_columns",barDataColumns);
        data.put("device_list_columns",deviceidDatas);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

}