package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.company.CompanyListDto;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.equipment.EquipmentDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    @Transactional
    @PostMapping("dataGraph")
    public ResponseEntity dataGraph() {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("그래프 적용성공");

        List<List<String>> circleDataColumns = new ArrayList<>(); //상태값
        List<String> barDataColumns = new ArrayList<>(); //쓰레기양
        List<String> deviceListColumns = new ArrayList<>(); //장치리스트
        barDataColumns.add("쓰레기양");

        List<String> exBarData = new ArrayList<>(); // 쓰레기양데이터 예시값
        exBarData.add("80");
        exBarData.add("50");
        exBarData.add("60");
        exBarData.add("90");
        exBarData.add("30");
        exBarData.add("50");
        exBarData.add("40");

        List<Equipment> equipments = equipmentService.findAll(); // 장치데이터 모두 불러오기
        for(int i=0; i<equipments.size();i++){
            deviceListColumns.add(equipments.get(i).getEmNumber());
            barDataColumns.add(exBarData.get(i));
        }

        List<String> stateNames = new ArrayList<>();
        stateNames.add("정상");
        stateNames.add("관심");
        stateNames.add("심각");
        stateNames.add("알수없음");
        List<String> statedatas = new ArrayList<>();
        statedatas.add(Integer.toString(6));
        statedatas.add(Integer.toString(4));
        statedatas.add(Integer.toString(2));
        statedatas.add(Integer.toString(3));

        log.info("상태값 리스트 : "+stateNames);
        log.info("상태값 데이터 : "+statedatas);
        log.info("장비데이터 모든값 : "+equipments);

        for (int i = 0; i<statedatas.size(); i++){
            int j=0;
            circleDataColumns.add(Arrays.asList(stateNames.get(i),statedatas.get(i)));
        }
        log.info("상태값 차트데이터 : "+circleDataColumns);
        log.info("장치리스트 : "+deviceListColumns);
        log.info("장치 차트데이터 : "+barDataColumns);

        data.put("circle_data_columns",circleDataColumns);
        data.put("bar_data_columns",barDataColumns);
        data.put("device_list_columns",deviceListColumns);

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
