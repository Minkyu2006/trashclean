package kr.co.broadwave.aci.awsiot;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author InSeok
 * Date : 2019-11-22
 * Remark :
 */
@Service
@Slf4j
public class ACIAWSLambdaService {
    @Value("${aci.aws.api.key}")
    private String ACIAWSAPIKEY;
    @Value("${aci.aws.api.baseurl}")
    private String ACIAWSAPIBASEURL;

    private final ObjectMapper objectMapper;

    @Autowired
    public ACIAWSLambdaService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //장비목록 가져오기(Dynamodb)
    public HashMap getDeviceList(String deviceType){

        final String url = ACIAWSAPIBASEURL + "/api/v1/isolarbins";

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);


        HttpEntity<Map<String, String>> entity = new HttpEntity<>(headers);

        //queryParams
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("devicetype", deviceType)
                .build()
                .toUri();



        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);


        return getHashMap(res);
    }

    //restTemplate 호출후 받은결과값을 반환해주는 함수
    private HashMap getHashMap(ResponseEntity<String> res) {
        if (res.getStatusCode() == HttpStatus.OK) {

            String bodystr = res.getBody();
            try{
                HashMap resultMap = objectMapper.readValue(bodystr, HashMap.class);
                return resultMap;


            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }

    //요청한장비의 마지막 상태 가져오기(Dynamodb)
    public HashMap getDeviceLastestState(String jsonDeviceList){

        final String url = ACIAWSAPIBASEURL + "/api/v1/isolarbins/";

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        //request body Example
//        String jsonParam = "{\n" +
//                "  \"deviceids\": [\n" +
//                "    \"ISOL-KR-SEOUL-0001\",\n" +
//                "    \"ISOL-KR-SEOUL-0003\"\n" +
//                "  ]\n" +
//                "}";


        HttpEntity<String> entity = new HttpEntity<>(jsonDeviceList,headers);


        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


        return getHashMap(res);
    }

    //특정장비의 history 가져오기(Dynamodb)
    public HashMap getDeviceHistory(String deviceid,String intervaltime){

        final String url = ACIAWSAPIBASEURL + "/api/v1/isolarbins/{id}" ;

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        //params
        Map<String, String> params = new HashMap<>();
        params.put("id", deviceid);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(params)
                .toUri();
        //queryParams
        uri = UriComponentsBuilder
                .fromUri(uri)
                .queryParam("intervalhour",intervaltime)
                .build()
                .toUri();


        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getHashMap(res);
    }
}
