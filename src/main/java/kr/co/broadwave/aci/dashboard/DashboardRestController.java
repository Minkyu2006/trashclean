package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.equipment.EquipmentService;
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

import javax.transaction.Transactional;
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

    @Autowired
    public DashboardRestController(DashboardService dashboardService,
                                                    EquipmentService equipmentService) {
        this.dashboardService = dashboardService;
        this.equipmentService = equipmentService;
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
                                        ,@RequestParam(value="intervaltime", defaultValue="") String intervaltime
    ){
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

    // 쓰레기통 상태값, 쓰레기양 차트
//    @Transactional
    @PostMapping("dataGraph")
    public ResponseEntity dataGraph(@RequestParam(value="deviceids", defaultValue="") String deviceids) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("그래프 적용성공");

        List<List<String>> circleDataColumns = new ArrayList<>(); //상태값
        List<Object> barDataColumns = new ArrayList<>(); //쓰레기양

        List<String> exBarData = new ArrayList<>(); // 쓰레기양데이터 예시값
        exBarData.add("80");
        exBarData.add("50");

        HashMap<String, Object> resData = dashboardService.getDeviceLastestState(deviceids); //AWS상 데이터리스트

        List<String> stateNames = new ArrayList<>(); // AWS 장비 status값 이름
        List<Object> statusDatas = new ArrayList<>(); // AWS 장비 status값 리스트
        List<Object> deviceidDatas = new ArrayList<>(); // AWS 장비 ID값 리스트

        log.info("AWS 장치 list : "+resData);
        log.info("AWS 장치 data : "+resData.get("data"));
        log.info("AWS 장치 size : "+resData.get("datacounts"));

        Object datacounts = resData.get("datacounts");
        int number = Integer.parseInt(datacounts.toString());

        for(int i = 0; i<number; i++){
            Object a = ((ArrayList) resData.get("data")).get(i);
            HashMap b = (HashMap) a;
            statusDatas.add(b.get("status"));
        }
        log.info("AWS 장치 status : " +statusDatas);

        List<String> statusSize = new ArrayList<>();
        for(int i=0; i<number; i++){
            if (!statusSize.contains(statusDatas.get(i))) {
                statusSize.add((String)statusDatas.get(i));
            }
        }
        log.info("AWS 장치 몇바퀴돌껀지 statusSize (최대3) : " +statusSize.size());

        int count = 0;
        for(int j=0; j<statusSize.size(); j++) {
            String statuss = (String)statusDatas.get(j);
            stateNames.clear();
            for (int i = 0; i < number; i++) {
                if (!stateNames.contains(statuss)) {
                    stateNames.add(statuss);
                }
            }
            for (int i = 0; i < number; i++) {
                if (stateNames.contains((String)statusDatas.get(i))) {
                    count++;
                }
            }
            stateNames.add(Integer.toString(count));

//            log.info("stateNames 데이터 : "+stateNames);
//            log.info("건수 : "+count);

            int cnt = 0;
            int cnt2 = 1;
            circleDataColumns.add(Arrays.asList(stateNames.get(cnt),stateNames.get(cnt2)));
            count = 0;
        }
        //테스트용 주의,심각
        List<String> testHardcording = new ArrayList<>();
        testHardcording.add("caution");
        testHardcording.add("severe");
        circleDataColumns.add(Arrays.asList(testHardcording.get(0),"2"));
        circleDataColumns.add(Arrays.asList(testHardcording.get(1),"1"));

        log.info("원형차트 들어갈 리스트 값 : "+circleDataColumns);

        barDataColumns.add("쓰레기양");
        for(int i = 0; i<number; i++){
            Object a = ((ArrayList) resData.get("data")).get(i);
            HashMap b = (HashMap) a;
            deviceidDatas.add(b.get("deviceid"));
            barDataColumns.add(b.get("level"));
        }

        //테스트용
        deviceidDatas.add("ISOL-KR-INC-0001");
        deviceidDatas.add("ITAI-US-TXS-0001");
        deviceidDatas.add("INET-ES-MDR-0001");
        barDataColumns.add("35");
        barDataColumns.add("50");
        barDataColumns.add("75");

        log.info("AWS 장치 deviceid : " +deviceidDatas);
        log.info("바차트 들어갈 리스트 값 : " +barDataColumns);

        data.put("circle_data_columns",circleDataColumns);
        data.put("bar_data_columns",barDataColumns);
        data.put("device_list_columns",deviceidDatas);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    //장비 리스트 뿌리기
    @PostMapping ("deviceInfoList")
    public ResponseEntity deviceInfoList(@PageableDefault Pageable pageable){
        Page<DashboardDeviceListViewDto> deviceInfoListDtos = dashboardService.findByDashboardListView(pageable);



        return CommonUtils.ResponseEntityPage(deviceInfoListDtos);
    }

}
