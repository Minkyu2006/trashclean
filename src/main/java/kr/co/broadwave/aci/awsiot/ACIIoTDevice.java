package kr.co.broadwave.aci.awsiot;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;

/**
 * @author InSeok
 * Date : 2019-10-29
 * Remark :
 */
public class ACIIoTDevice extends AWSIotDevice {
    public ACIIoTDevice(String thingName) {
        super(thingName);
    }

    @AWSIotDeviceProperty
    private String tmpsensor;


    @Override
    public void onShadowUpdate(String jsonState) {
        //super.onShadowUpdate(jsonState);
        //To-do code
        System.out.println("onShadowUpdate Event : " + jsonState.toString() );

    }

    public String getTmpsensor() {
        return tmpsensor;
    }

    public void setTmpsensor(String tmpsensor) {
        this.tmpsensor = tmpsensor;
    }
}
