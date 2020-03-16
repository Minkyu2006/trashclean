package kr.co.broadwave.aci.awsiot;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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
    public HashMap getDeviceStatus(String deviceid) throws Exception {
        log.info("장비 Shadow 값 가져오기 :'" + deviceid +"'");
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

    //Shadow Isolarbin 문열기(IoT)
    public void setDeviceDoor(String deviceid,String door) throws Exception {
        log.info("장비 Shadow 문열기 닫기 요청 :'" + deviceid +"'");
        //device 상태가져오기
        aciIoTService.shadowNonblockingSend(deviceid,"doorsol",door);

    }

    //Shadow Isolarbin DataReq(IoT) -> param : 디바이스 아이디, 타임스탬프
    public void setDataRequest(String deviceid,String ts) throws Exception {
        log.info("장비 Shadow 데이터 요청 :'" + deviceid +"'");
        //device 상태가져오기
        aciIoTService.shadowNonblockingSend(deviceid,"datareq",ts);

    }
    //Shadow Isolarbin 액츄에이터리셋(IoT) -> param : 디바이스 아이디, 타임스탬프
    public void setActuatorReset(String deviceid,String ts) throws Exception {
        log.info("장비 Shadow 액추에이터리셋 요청 :'" + deviceid +"'");
        //device 상태가져오기
        aciIoTService.shadowNonblockingSend(deviceid,"v_actuatorreset",ts);
    }

    //Shadow Isolarbin LED 점멸 (IoT) -> param :  디바이스 아이디, 타임스탬프
    public void setLightFlicker(String deviceid,String ts) throws Exception {
        log.info("장비 Shadow LED점멸 요청 :'" + deviceid +"'");
        //device 상태가져오기
        aciIoTService.shadowNonblockingSend(deviceid,"light_flicker",ts);

    }
    //Shadow Isolarbin 수거관리시작 (IoT) -> param :  디바이스 아이디, 수거관리번호+seq
    public void setCollectStart(String deviceid,String ctCode) throws Exception {
        log.info("장비 Shadow 수거관리시작 요청 :'" + deviceid +"'");
        //device 상태가져오기
        aciIoTService.shadowNonblockingSend(deviceid,"collect_start",ctCode);

    }
    //Shadow Isolarbin 수거관리종료 (IoT) -> param :  디바이스 아이디, 수거관리번호+seq
    public void setCollectEnd(String deviceid,String ctCode) throws Exception {
        log.info("장비 Shadow 수거관리종료 요청 :'" + deviceid +"'");
        //device 상태가져오기
        aciIoTService.shadowNonblockingSend(deviceid,"collect_end",ctCode);

    }
    //Shadow Isolarbin 기본값셋팅 (IoT) -> param :  디바이스 아이디, 기본값(value)
    public void setDeviceBaseSetting(String deviceid,String v_interval,String v_presstime, String v_inputtime
            ,String v_solenoidtime,String v_yellowstart, String v_redstart ) throws Exception {
        log.info("장비 Shadow 기본값셋팅 요청 :'" + deviceid +"'");

        String message = "{\"state\":{\"desired\":";
        message = message + " {\"v_interval\":\"" + v_interval.toLowerCase() + "\"";
        message = message + ",\"v_presstime\":\"" + v_presstime.toLowerCase() + "\"";
        message = message + ",\"v_inputtime\":\"" + v_inputtime.toLowerCase() + "\"";
        message = message + ",\"v_solenoidtime\":\"" + v_solenoidtime.toLowerCase() + "\"";
        message = message + ",\"v_yellowstart\":\"" + v_yellowstart.toLowerCase() + "\"";
        message = message + ",\"v_redstart\":\"" + v_redstart.toLowerCase() + "\"";
        message = message +"}}}";
        aciIoTService.shadowNonblockingMessageParamSend(deviceid,message);

    }
    //Shadow Isolarbin DataReq(IoT) -> param : 디바이스 아이디, 타임스탬프
    public void setDeviceInfoRequest(String deviceid,String ts) throws Exception {
        log.info("장비 Shadow DeviceInfo 요청 :'" + deviceid +"'");
        //device 상태가져오기
        aciIoTService.shadowNonblockingSend(deviceid,"deviceinfo",ts);

    }
    //Shadow firmwareupdate(IoT) -> param : 디바이스 아이디, 펌웨어버전 및 경로 ($@로 문자열구분 버전 및 파일경로
    // ex. v1.0.1$@https://s3.ap-northeast-2.amazonaws.com/iecoprocuploadfiles/uploadfiles/20191225/3b48ccb2aa744327b7106ce2b06a33c9.bin
    public void setFirmwareUpdate(String deviceid,String str) throws Exception {
        log.info("장비 Shadow firmwareupdate 요청 :'" + deviceid +"'");
        //device 상태가져오기
        aciIoTService.shadowNonblockingSend(deviceid,"firmwareupdate",str);

    }


}
