package kr.co.broadwave.aci.awsiot;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author InSeok
 * Date : 2019-11-25
 * Remark : AWS IoT Device 장비를 제어 또는 상태값을 가져오기위한 서비스
 */
@Service
@Slf4j
public class ACIAWSIoTDeviceService {


    private final ObjectMapper objectMapper;
    private final ACIIoTService aciIoTService;

    @Autowired
    public ACIAWSIoTDeviceService(ObjectMapper objectMapper, ACIIoTService aciIoTService) {
        this.objectMapper = objectMapper;
        this.aciIoTService = aciIoTService;
    }


    //Shadow 상태가져오기(IoT)
    public HashMap getDeviceList(String deviceid) throws Exception {
        log.info("장비 Shadown 값 가져오기 :'" + deviceid +"'");
        //device 상태가져오기
        String resultStr = aciIoTService.shadowDeviceGet(deviceid);
        //System.out.println(resultStr);

        try{
            HashMap resultMap = objectMapper.readValue(resultStr, HashMap.class);
            return resultMap;


        }catch (Exception e){
            //e.printStackTrace();
            log.error("장비 Shadown 값 실패 : '" + e.toString() +"'");
            return null;
        }


    }


}
