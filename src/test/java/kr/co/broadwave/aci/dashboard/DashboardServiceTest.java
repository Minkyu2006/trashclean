package kr.co.broadwave.aci.dashboard;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.assertj.core.api.Java6Assertions.assertThat;

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
    public void getDeviceList() {
        HashMap res = dashboardService.getDeviceList("ISOL");
        assertThat(res.get("statusCode")).as("Dashboard Get DeviceList [expect 200]").isEqualTo(200);



    }

    @Test
    public void getDeviceLastestState(){
                String jsonParam = "{\n" +
                "  \"deviceids\": [\n" +
                "    \"ISOL-KR-SEOUL-0001\",\n" +
                "    \"ISOL-KR-SEOUL-0002\"\n" +
                "  ]\n" +
                "}";

        HashMap res = dashboardService.getDeviceLastestState(jsonParam);
        assertThat(res.get("statusCode")).as("Dashboard Get DeviceLastestState [expect 200]").isEqualTo(200);
    }

    @Test
    public void getDeviceHistory(){
        HashMap res = dashboardService.getDeviceHistory("ISOL-KR-SEOUL-0002", "72");
        assertThat(res.get("statusCode")).as("Dashboard Get DeviceHistory [expect 200]").isEqualTo(200);
    }
}
