package kr.co.broadwave.aci.awsiot;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author InSeok
 * Date : 2019-10-29
 * Remark : AWS IoT 에 접근하는 함수
 */
@Service
@Slf4j
public class ACIIoTService {

    @Value("${aci.aws.iot.access.endpoint}")
    private String ACIIOTACCESSENDPOINT;

    @Value("${aci.aws.iot.access.id}")
    private String ACIIOTACCESSID;

    @Value("${aci.aws.iot.access.key}")
    private String ACIIOTACCESSKEY;

    private String clientId = "iEcoProc-WEB"; // 웹어플리케이션 이름


    //shadow 메세지 보내기
    public void shadowNonblockingSend(String thingName,String keyString, String valueString) {

        try {


            AWSIotMqttClient client = new AWSIotMqttClient(ACIIOTACCESSENDPOINT, clientId, ACIIOTACCESSID, ACIIOTACCESSKEY);

            //String thingName = "ISOL-KR-SEOUL-0002";                    // replace with your AWS IoT Thing name


            ACIIoTDevice device = new ACIIoTDevice(thingName);;

            //shadow
            client.attach(device);
            long reportInterval = 0;            // milliseconds. Default interval is 3000.
            device.setReportInterval(reportInterval);
            client.connect();

            //System.out.println("Shadow Nonblocking connect");

            // Update shadow document
            String message = "{\"state\":{\"desired\":{\"" + keyString + "\":\"" + valueString + "\"}}}";
            device.update(message,3000);

            //System.out.println("shadow nonBlocking update");

            client.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //shadow 정보 가져오기
    public String shadowDeviceGet(String thingName) {

        try {


            AWSIotMqttClient client = new AWSIotMqttClient(ACIIOTACCESSENDPOINT, clientId, ACIIOTACCESSID, ACIIOTACCESSKEY);


            ACIIoTDevice device = new ACIIoTDevice(thingName);;

            //shadow
            client.attach(device);
            long reportInterval = 0;            // milliseconds. Default interval is 3000.
            device.setReportInterval(reportInterval);
            client.connect();


            // Get shadow document.
            String resultStr = device.get(3000);

            client.disconnect();
            return resultStr;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
