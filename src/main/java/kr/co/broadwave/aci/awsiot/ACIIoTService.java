package kr.co.broadwave.aci.awsiot;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author InSeok
 * Date : 2019-10-29
 * Remark : AWS IoT 에 접근하는 함수
 */
@Service
@Slf4j
public class ACIIoTService {


    private String clientId = "iEcoProc-WEB-" + UUID.randomUUID(); // 웹어플리케이션 이름


    private final String aciIotAccessEndPoint;
    private final String aciIotAccessId;
    private final String aciIotAccessKey;

    private final AWSIotMqttClient client;



    public ACIIoTService(@Value("${aci.aws.iot.access.endpoint}") String aciIotAccessEndPoint
            , @Value("${aci.aws.iot.access.id}") String aciIotAccessId
            , @Value("${aci.aws.iot.access.key}") String aciIotAccessKey) {
        this.aciIotAccessEndPoint = aciIotAccessEndPoint;
        this.aciIotAccessId = aciIotAccessId;
        this.aciIotAccessKey = aciIotAccessKey;

        this.client = new AWSIotMqttClient(this.aciIotAccessEndPoint, clientId
                , this.aciIotAccessId
                , this.aciIotAccessKey);

        try {
            AWSIotDevice initdevice = new AWSIotDevice("ISOL-SEL-KR-0001");

            this.client.attach(initdevice);
            this.client.connect();


        }catch (Exception e){
            e.printStackTrace();
        }


    }


    //shadow 메세지 보내기
    public void shadowNonblockingSend(String thingName,String keyString, String valueString) {

        AWSIotDevice device = new AWSIotDevice(thingName);
        try {
            //shadow blocking send
            client.attach(device);

            String message = "{\"state\":{\"desired\":{\"" + keyString.toLowerCase() + "\":\"" + valueString + "\"}}}";
            device.update(message);
            client.detach(device);


        }catch (Exception e){
            e.printStackTrace();
            try{
                client.detach(device);


            }catch (Exception e1){
                e1.printStackTrace();


            }

        }

    }


    //shadow  메세지 보내기
    public void shadowNonblockingMessageParamSend(String thingName,String message) {

        //AWSIotMqttClient clientMulti = new AWSIotMqttClient(ACIIOTACCESSENDPOINT, clientId, ACIIOTACCESSID, ACIIOTACCESSKEY);
        //ACIIoTDevice deviceMulti = new ACIIoTDevice(thingName);;
        AWSIotDevice deviceMulti = new AWSIotDevice(thingName);;

        try {

            //shadow blocking send

            client.attach(deviceMulti);
            deviceMulti.update(message);
            client.detach(deviceMulti);



        }catch (Exception e){
            e.printStackTrace();
            try{
                client.detach(deviceMulti);

            }catch (Exception e1){
                e1.printStackTrace();
            }

        }


    }

    //shadow 정보 가져오기
    public String shadowDeviceGet(String thingName) {
        //AWSIotMqttClient client = new AWSIotMqttClient(this.aciIotAccessEndPoint, clientId, this.aciIotAccessId, this.aciIotAccessKey);
        ACIIoTDevice device = new ACIIoTDevice(thingName);
        try {

            //shadow

            client.attach(device);

            // Get shadow document.
            String resultStr = "";
            try {
                resultStr = device.get();
            }catch (Exception e){
                resultStr = "{\"state\":\"none\"}";
            }

            client.detach(device);



            return resultStr;

        }catch (Exception e){
            e.printStackTrace();
            try{
                client.detach(device);
            }catch (Exception e1){
                e1.printStackTrace();
                return null;
            }
            return null;
        }

    }

    public void setErrorReport(String deviceNumber) {
    }
}
