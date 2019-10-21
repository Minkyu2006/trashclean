package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.common.HeaderRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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


    public String getMoniteringAllData(){

        final String uri = ACIAWSAPIBASEURL + "/api/v1/isolarbins";


        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.APPLICATION_JSON_VALUE));
        interceptors.add(new HeaderRequestInterceptor("x-api-key", ACIAWSAPIKEY));

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("x-api-key",aciAWSApiKey);

//
//        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(headers);
//        Map<String, String> params = new HashMap<>();
//        params.put("name", "jaeyeon");

        //ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        //String result = restTemplate.postForObject(uri, entity, String.class);
        //restTemplate.getForObject(uri, entity, String.class);
        ResponseEntity<String> res = restTemplate.getForEntity(uri,  String.class);


        //System.out.println(res.getBody());
        if (res.getStatusCode() == HttpStatus.OK) {
            return res.getBody();
        }
        return null;
    }

}
