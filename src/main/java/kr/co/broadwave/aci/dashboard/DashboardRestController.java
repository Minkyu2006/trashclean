package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.common.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private AjaxResponse res = new AjaxResponse();
    private HashMap<String, Object> data = new HashMap<>();

    private final DashboardService dashboardService;

    @Autowired
    public DashboardRestController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }


    @PostMapping("monitering")
    public ResponseEntity monitering(){
        log.info("모니터링 조회 시작");


        String jsonParam = "{\n" +
                "  \"deviceids\": [\n" +
                "    \"ISOL-KR-SEOUL-0001\",\n" +
                "    \"ISOL-KR-SEOUL-0002\"\n" +
                "  ]\n" +
                "}";

        HashMap<String, Object> resData = dashboardService.getDeviceLastestState(jsonParam);
        data.clear();
        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        log.info("모니터링 조회 성공 ");
        return ResponseEntity.ok(res.success());

    }
    @PostMapping("devicelist")
    public ResponseEntity devicelist(){
        log.info("Device목록가져오기 시작");


        HashMap<String, Object> resData = dashboardService.getDeviceList("ISOL");
        data.clear();
        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        log.info("Device목록가져오기 성공 ");
        return ResponseEntity.ok(res.success());

    }

}
