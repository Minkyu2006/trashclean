package kr.co.broadwave.aci.awsiot;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;

/**
 * @author InSeok
 * Date : 2019-10-29
 * Remark :
 */
public class ACIMessage extends AWSIotMessage {
    public ACIMessage(String topic, AWSIotQos qos, String payload) {
        super(topic, qos, payload);
    }

    @Override
    public void onSuccess() {
        // super.onSuccess();
        System.out.println("success nonBlocking Topic publish " );
    }
}
