package kr.co.broadwave.aci.awsiot;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;

/**
 * @author InSeok
 * Date : 2019-10-29
 * Remark :
 */
public class ACIShadowMessage extends AWSIotMessage {
    public ACIShadowMessage(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
    }
}
