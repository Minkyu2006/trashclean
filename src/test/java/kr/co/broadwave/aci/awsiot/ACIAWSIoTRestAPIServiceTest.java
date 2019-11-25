package kr.co.broadwave.aci.awsiot;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author InSeok
 * Date : 2019-11-25
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ACIAWSIoTRestAPIServiceTest {

    @Autowired
    ACIAWSIoTDeviceService aciawsIoTDeviceService;

    @Test
    public void getDeviceList() throws Exception{
        HashMap deviceList = aciawsIoTDeviceService.getDeviceList("ISOL-KR-SEL-0004");
        //System.out.println(deviceList);
        assertThat(deviceList.containsKey("timestamp")).as("DevicShadow Get Test [expect true] :").isEqualTo(true);
    }


}
