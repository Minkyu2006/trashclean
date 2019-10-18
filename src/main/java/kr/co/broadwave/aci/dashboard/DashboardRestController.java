package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.common.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Random;

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

    @PostMapping("monitering")
    public ResponseEntity team(){
        log.info("모니터링 조회 시작");
        Double result = Math.random() * 100;

        data.clear();
        data.put("datarow1",result.toString());
        res.addResponse("data",data);

        log.info("모니터링 조회 성공 ");
        return ResponseEntity.ok(res.success());

    }

}
