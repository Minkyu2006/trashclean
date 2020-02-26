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
 * Date : 2020-02-19
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ACIAWSLambdaServiceTest {
    @Autowired
    ACIAWSLambdaService aciawsLambdaService;

    //장비온라인상태 조회 API
    @Test
    public void getDeviceonlineType(){
        HashMap deviceonlineCheck = aciawsLambdaService.getDeviceonlineCheck("ISOL-KR-SEL-0001");
        //System.out.println(deviceonlineCheck);
        assertThat(deviceonlineCheck.containsKey("statusCode")).as("Device Online Check Test [expect true] :").isEqualTo(true);

    }
    @Test
    public void getDeviceInfo(){
        HashMap deviceInfo = aciawsLambdaService.getDeviceInfo("ISOL-KR-SEL-0001");
        //System.out.println(deviceInfo);
        assertThat(deviceInfo.containsKey("statusCode")).as("Device Online Check Test [expect true] :").isEqualTo(true);


    }

}