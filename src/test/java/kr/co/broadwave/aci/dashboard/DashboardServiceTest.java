package kr.co.broadwave.aci.dashboard;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author InSeok
 * Date : 2019-10-21
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DashboardServiceTest {

    @Autowired
    DashboardService dashboardService;


    @Test
    //@Ignore
    public void getDeviceList() {
        dashboardService.getDeviceList("ISOL");
    }

    @Test
    //@Ignore
    public void getDeviceLastestState(){
                String jsonParam = "{\n" +
                "  \"deviceids\": [\n" +
                "    \"ISOL-KR-SEOUL-0001\",\n" +
                "    \"ISOL-KR-SEOUL-0002\"\n" +
                "  ]\n" +
                "}";

        dashboardService.getDeviceLastestState(jsonParam);
    }
}
