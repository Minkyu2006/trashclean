package kr.co.broadwave.aci.awsiot;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author InSeok
 * Date : 2019-10-29
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ACIIoTServiceTest {

    @Autowired
    ACIIoTService aciIoTService;

    @Test
    @Ignore
    public void shadowNonblockingSend(){
        aciIoTService.shadowNonblockingSend("ISOL-KR-SEL-0004","doorsol","open");

    }
    @Test
    @Ignore
    public void shadowDeviceGet(){
        aciIoTService.shadowDeviceGet("ISOL-KR-SEL-0004");
    }

}
