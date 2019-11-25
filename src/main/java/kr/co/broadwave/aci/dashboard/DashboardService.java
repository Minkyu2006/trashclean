package kr.co.broadwave.aci.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author InSeok
 * Date : 2019-10-21
 * Remark :
 */
@Service
@Slf4j
public class DashboardService {

    @Value("${aci.aws.api.key}")
    private String ACIAWSAPIKEY;
    @Value("${aci.aws.api.baseurl}")
    private String ACIAWSAPIBASEURL;

    private final ObjectMapper objectMapper;
    private final ACIAWSLambdaService aciawsLambdaService;
    private final DashboardRepositoryCustom dashboardRepositoryCustom;



    @Autowired
    public DashboardService(ObjectMapper objectMapper,
                            DashboardRepositoryCustom dashboardRepositoryCustom,
                            ACIAWSLambdaService aciawsLambdaService) {
        this.objectMapper = objectMapper;
        this.dashboardRepositoryCustom = dashboardRepositoryCustom;
        this.aciawsLambdaService = aciawsLambdaService;
    }


    //장비목록 가져오기(Dynamodb)
    public HashMap getDeviceList(String deviceType){
        return aciawsLambdaService.getDeviceList(deviceType);


    }


    //요청한장비의 마지막 상태 가져오기(Dynamodb)
    public HashMap getDeviceLastestState(String jsonDeviceList){
        return aciawsLambdaService.getDeviceLastestState(jsonDeviceList);
    }

    //특정장비의 history 가져오기(Dynamodb)
    public HashMap getDeviceHistory(String deviceid,String intervaltime){

        return aciawsLambdaService.getDeviceHistory(deviceid,intervaltime);

    }

    public Page<DashboardDeviceListViewDto> findByDashboardListView(Pageable pageable) {
        return dashboardRepositoryCustom.findByDashboardListView(pageable);
    }

}
