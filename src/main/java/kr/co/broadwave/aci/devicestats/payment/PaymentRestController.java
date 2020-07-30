package kr.co.broadwave.aci.devicestats.payment;

import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.devicestats.DevicestatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2020-07-28
 * Time :
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentRestController {

    private final DevicestatusService devicestatusService;

    @Autowired
    public PaymentRestController(DevicestatusService devicestatusService) {
        this.devicestatusService = devicestatusService;
    }

    // 배출정보 리스트
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> list(@RequestParam(value="fromVal", defaultValue="") String fromVal,
                                                   @RequestParam(value="toVal", defaultValue="") String toVal,
                                                    @RequestParam (value="deviceid", defaultValue="")String deviceid,
                                                    @RequestParam (value="basename", defaultValue="")String basename){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("fromVal : "+fromVal);
//        log.info("toVal : "+toVal);

        List<PaymentListDto> paymentListDtos = devicestatusService.findByPaymentListQuerydsl(fromVal,toVal,deviceid,basename);
//        log.info("paymentListDtos : "+paymentListDtos);
        data.put("paymentListDtos",paymentListDtos);

        res.addResponse("data", data);
        return ResponseEntity.ok(res.success());

    }

}
