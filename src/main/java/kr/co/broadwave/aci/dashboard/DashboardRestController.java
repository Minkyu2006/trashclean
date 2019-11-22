package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.equipment.EquipmentDto;
import kr.co.broadwave.aci.equipment.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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


    // 업체 정보 보기
    @PostMapping ("infoList")
    public ResponseEntity dashboardDeviceInfo(){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.clear();
//        data.put("dashboardDevice",dashboardDevice);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

}
