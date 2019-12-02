package kr.co.broadwave.aci.dashboard;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("Device History 가져오기 시작");

        HashMap<String, Object> resData = dashboardService.getDeviceHistory(deviceid,intervaltime);
        data.clear();
        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        log.info("Device History 가져오기 성공 ");
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
    public ResponseEntity dataGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("그래프 적용성공");

        List<List<String>> circleDataColumns = new ArrayList<>(); //상태값
        List<Object> barDataColumns = new ArrayList<>(); //쓰레기양

        HashMap<String, Object> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트

        List<String> stateNames = new ArrayList<>(); // AWS 장비 status값 이름
        List<Object> statusDatas = new ArrayList<>(); // AWS 장비 status값 리스트
        List<Object> deviceidDatas = new ArrayList<>(); // AWS 장비 ID값 리스트

//        log.info("AWS 장치 list : "+resData);
//        log.info("AWS 장치 data : "+resData.get("data"));
//        log.info("AWS 장치 size : "+resData.get("datacounts"));

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString());
//        log.info("number : "+number);

        for(int i = 0; i<number; i++){
            Object dataObject = ((ArrayList) resData.get("data")).get(i);
            HashMap map = (HashMap) dataObject;
            if(map.get("status").equals("caution")){
                map.replace("status","주의");
            }else if(map.get("status").equals("normal")){
                map.replace("status","정상");
            }else if(map.get("status").equals("severe")){
                map.replace("status","심각");
            }
            statusDatas.add(map.get("status"));
        }
//        log.info("statusDatas : " +statusDatas);

        List<String> statusSize = new ArrayList<>();
        for(int i=0; i<number; i++){
            if (!statusSize.contains(statusDatas.get(i))) {
                statusSize.add((String)statusDatas.get(i));
            }
        }
//        log.info("statusSize : " +statusSize);
//        log.info("statusSize.size() (최대3) : " +statusSize.size());

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

        //테스트용 주의,심각
        List<List<String>> statusHardcording = new ArrayList<>();
        statusHardcording.add(Arrays.asList("정상","0"));
        statusHardcording.add(Arrays.asList("주의","0"));
        statusHardcording.add(Arrays.asList("심각","0"));

        if(!statusSize.contains("정상")) {
            circleDataColumns.add(statusHardcording.get(0));
        }
        if(!statusSize.contains("주의")) {
            circleDataColumns.add(statusHardcording.get(1));
        }
        if(!statusSize.contains("심각")) {
            circleDataColumns.add(statusHardcording.get(2));
        }
//        log.info("원형차트 들어갈 리스트 값 : "+circleDataColumns);



        barDataColumns.add("쓰레기양");
        for(int i = 0; i<number; i++){
            Object a = ((ArrayList) resData.get("data")).get(i);
            HashMap b = (HashMap) a;
            deviceidDatas.add(b.get("deviceid"));
            barDataColumns.add(b.get("level"));
        }

        List<List<Object>> mapDataColumns = new ArrayList<>();

//        log.info("AWS 장치 list : "+resData);
//        log.info("AWS 장치 data : "+resData.get("data"));
//        log.info("AWS 장치 size : "+resData.get("datacounts"));
//        log.info("number : "+number);

        List<String> deviceIdNames = new ArrayList<>(); // AWS 장비 device값 이름
        List<String> gps_laDatas = new ArrayList<>(); // AWS 장비 gps_la값 리스트
        List<String> gps_loDatas = new ArrayList<>(); // AWS 장비 gps_lo값 리스트

        List<String> gps_laDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환
        List<String> gps_loDatas2 = new ArrayList<>(); // AWS 장비 gps_la값 리스트 변환

        for(int i = 0; i<number; i++){
            Object dataObject = ((ArrayList) resData.get("data")).get(i);
            HashMap map = (HashMap) dataObject;
            deviceIdNames.add((String)map.get("deviceid"));
            gps_laDatas.add((String)map.get("gps_la"));
            gps_loDatas.add((String)map.get("gps_lo"));
        }

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

        //테스트용
//        deviceidDatas.add("ISOL-KR-INC-0001");
//        deviceidDatas.add("ITAI-US-TXS-0001");
//        deviceidDatas.add("INET-ES-MDR-0001");
//        barDataColumns.add("35");
//        barDataColumns.add("50");
//        barDataColumns.add("75");

//        log.info("AWS 장치 deviceid : " +deviceidDatas);
//        log.info("바차트 들어갈 리스트 값 : " +barDataColumns);

        data.put("map_data_columns",mapDataColumns);
        data.put("circle_data_columns",circleDataColumns);
        data.put("bar_data_columns",barDataColumns);
        data.put("device_list_columns",deviceidDatas);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

}
