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
        try{
            for(int i = 0; i<100;i++) {
                if (i % 2 == 0 ) {
                    aciIoTService.shadowNonblockingSend("ISOL-KR-SEL-0001", "doorsol", "close" + i);
                }else{
                    aciIoTService.shadowNonblockingSend("ISOL-KR-SEL-0005", "doorsol", "close" + i);
                }
                //Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Test
    @Ignore
    public void shadowNonblockingMessageParamSend(){
        try{
            for(int i = 0; i<20;i++) {
                String message = "{\"state\":{\"desired\":{\"check\":\"" + i + "\"}}}";
                if (i % 2 == 0 ) {

                    aciIoTService.shadowNonblockingMessageParamSend("ISOL-KR-SEL-0001", message);
                }else{
                    aciIoTService.shadowNonblockingMessageParamSend("ISOL-KR-SEL-0005", message);
                }
                //Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    @Ignore
    public void shadowDeviceGet(){
        aciIoTService.shadowDeviceGet("ISOL-KR-SEL-0004");
    }

}
