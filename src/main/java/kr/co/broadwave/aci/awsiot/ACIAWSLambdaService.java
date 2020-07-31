package kr.co.broadwave.aci.awsiot;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.equipment.EquipmentRepository;
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
import java.util.Optional;

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
    private final EquipmentRepository equipmentRepository;


    @Autowired
    public ACIAWSLambdaService(ObjectMapper objectMapper, EquipmentRepository equipmentRepository) {
        this.objectMapper = objectMapper;
        this.equipmentRepository = equipmentRepository;
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
    //장비온라인 상태 확인하기
    public HashMap getDeviceonlineCheck(String deviceid){

        final String url = ACIAWSAPIBASEURL + "/api/v1/devicesonline/{id}" ;

        Optional<Equipment> optionalEquipment = equipmentRepository.findByEmNumber(deviceid);
        if (!optionalEquipment.isPresent()){
            return null;
        }

        String emCertificationNumber = optionalEquipment.get().getEmCertificationNumber();
        if(emCertificationNumber==null || emCertificationNumber.equals("")){
            emCertificationNumber=deviceid;
        }

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        //params
        Map<String, String> params = new HashMap<>();
        params.put("id", emCertificationNumber);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(params)
                .toUri();
        //queryParams
        uri = UriComponentsBuilder
                .fromUri(uri)
                .queryParam("deviceid",deviceid)
                .build()
                .toUri();


        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getHashMap(res);
    }
    //DeviceInfo 값 가져오기(ex. 펌워어,통신사정보등)
    public HashMap getDeviceInfo(String deviceid){

        final String url = ACIAWSAPIBASEURL + "/api/v1/deviceinfo/{id}" ;

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
                .build()
                .toUri();


        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getHashMap(res);
    }

    //비콘센서 데이터가져오기
    public HashMap getDeviceBeacon(String yyyymmdd){

        final String url = ACIAWSAPIBASEURL + "/api/v1/beacon/{id}" ;

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        //params
        Map<String, String> params = new HashMap<>();
        params.put("id", yyyymmdd);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(params)
                .toUri();
        //queryParams
        uri = UriComponentsBuilder
                .fromUri(uri)
                .build()
                .toUri();

        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getHashMap(res);
    }

    //ncd센서 데이터가져오기(창고)
    public HashMap getDeviceNcd(String timeVal){
//        System.out.println("timeVal : "+timeVal);

        final String url = ACIAWSAPIBASEURL + "/api/v1/beacon/lora/{id}" ;

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        //params
        Map<String, String> params = new HashMap<>();
        params.put("id", timeVal);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(params)
                .toUri();
        //queryParams
        uri = UriComponentsBuilder
                .fromUri(uri)
                .build()
                .toUri();

        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getHashMap(res);
    }

    //beacon센서 데이터가져오기
    public HashMap getBeacon(String timeVal){
//        System.out.println("timeVal : "+timeVal);

        final String url = ACIAWSAPIBASEURL + "/api/v1/beacon/office/{id}" ;

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        //params
        Map<String, String> params = new HashMap<>();
        params.put("id", timeVal);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(params)
                .toUri();
        //queryParams
        uri = UriComponentsBuilder
                .fromUri(uri)
                .build()
                .toUri();

        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getHashMap(res);
    }

    //ml추론 센서데이터가져오기
    public HashMap getDeviceMl(String timeVal,Integer mode){
//        System.out.println("timeVal : "+timeVal);
        final String url;

        if(mode ==2) {
            url = ACIAWSAPIBASEURL + "/api/v1/beacon/mltest/{id}";
        }else{
            url = ACIAWSAPIBASEURL + "/api/v1/beacon/mltestall/{id}";
        }

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        //params
        Map<String, String> params = new HashMap<>();
        params.put("id", timeVal);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(params)
                .toUri();
        //queryParams
        uri = UriComponentsBuilder
                .fromUri(uri)
                .build()
                .toUri();

        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getHashMap(res);
    }

    //ncd센서 데이터가져오기(제주)
    public HashMap getDeviceNcdJeju(String timeVal){
//        System.out.println("timeVal : "+timeVal);

        final String url = ACIAWSAPIBASEURL + "/api/v1/beacon/ncdjeju/{id}" ;

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        //params
        Map<String, String> params = new HashMap<>();
        params.put("id", timeVal);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(params)
                .toUri();
        //queryParams
        uri = UriComponentsBuilder
                .fromUri(uri)
                .build()
                .toUri();

        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        return getHashMap(res);
    }

    //장비온라인 상태 확인하기
    public HashMap getDeviceErrorReport(String params){

        final String url = ACIAWSAPIBASEURL + "/api/v1/itainer/errreport";

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-key",ACIAWSAPIKEY);

        HttpEntity<String> entity = new HttpEntity<>(params,headers);

        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return getHashMap(res);

    }

}
