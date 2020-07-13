package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.files.FileUploadDto;
import kr.co.broadwave.aci.files.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author InSeok
 * Date : 2019-10-17
 * Remark :
 */
@Controller
@RequestMapping("/testpage")
public class TestpageController {

    private final FileUploadService fileUploadService;
    private final ACIAWSLambdaService aciawsLambdaService;



    @Autowired
    public TestpageController(FileUploadService fileUploadService, ACIAWSLambdaService aciawsLambdaService) {
        this.fileUploadService = fileUploadService;
        this.aciawsLambdaService = aciawsLambdaService;
    }

    @RequestMapping("dashboardfront")
    public String dashboardfront(){
        return "testpage/dashboardfront";
    }

    @RequestMapping("monitering")
    public String monitering(){
        return "testpage/monitering";
    }

    @RequestMapping("layout")
    public String layout(){
        return "testpage/layout";
    }

    @RequestMapping("fileupload")
    public String fileupload(){
        return "testpage/fileupload";
    }

    @RequestMapping("listtest")
    public String listtest(){
        return "testpage/listtest";
    }

    @RequestMapping("listtest2")
    public String listtest2(){
        return "testpage/listtest2";
    }

    @RequestMapping("listtest3")
    public String listtest3(){
        return "testpage/listtest3";
    }

    @RequestMapping("daysdevicetest")
    public String daysdevicetest(){
        return "testpage/daysdevicetest";
    }

    @RequestMapping("filedownload/{fileid}")
    @ResponseBody
    public byte[] downProcess(HttpServletResponse response,
                              @PathVariable Long fileid) throws IOException {

        FileUploadDto fileUploadDto = fileUploadService.findById(fileid);

        byte[] bytes = fileUploadService.fileDownload(fileUploadDto.getFilePath(),fileUploadDto.getSaveFileName());


        response.setHeader("Content-Disposition",
                "attachment;filename=\"" + URLEncoder.encode(fileUploadDto.getFileName(), "UTF-8") + "\"");
        response.setContentLength(bytes.length);
        return bytes;
    }

    @RequestMapping("mobileindex")
    public String mobileindex(){
        return "testpage/mobileindex";
    }

    @RequestMapping("collectionprocesstest")
    public String collectionprocesstest(){
        return "testpage/collectionprocesstest";
    }

    @RequestMapping("collectionlisttest")
    public String collectionlisttest(){
        return "testpage/collectionlisttest";
    }

    @RequestMapping("devicebasevaluetest")
    public String devicebasevaluetest(){
        return "testpage/devicebasevaluetest";
    }

    @RequestMapping("routingtest1")
    public String routingtest1(){
        return "testpage/routingtest1";
    }

    @RequestMapping("routingtest2")
    public String routingtest2(){
        return "testpage/routingtest2";
    }

    @RequestMapping("firmwaretest")
    public String firmwaretest(){
        return "testpage/firmwaretest";
    }

    @RequestMapping("logintest")
    public String logintest(){
        return "testpage/logintest";
    }

    @RequestMapping("collectionroutingtest1")
    public String collectionroutingtest1(){
        return "testpage/collectionroutingtest1";
    }

    @RequestMapping("collectionroutingtest2")
    public String collectionroutingtest2(){
        return "testpage/collectionroutingtest2";
    }

    @RequestMapping("strongpoint")
    public String strongpoint(){
        return "testpage/strongpoint";
    }

    @RequestMapping("beacongraph")
    public String beacongraph(){
        return "testpage/beacongraph";
    }

    // 비콘센서데이터가져오기
    @PostMapping("beaconData")
    public ResponseEntity<Map<String,Object>> beaconData(@RequestParam(value="yyyymmdd", defaultValue="") String yyyymmdd) throws Exception {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String searchDate = null;
//        System.out.println("yyyymmdd : "+yyyymmdd);
        if (!yyyymmdd.equals("")) {
            searchDate = yyyymmdd.substring(0, 10).replace("-", "");
        }
//        System.out.println("searchDate : "+searchDate);

        HashMap<String, Object> resData = aciawsLambdaService.getDeviceBeacon(searchDate);
//        System.out.println("resData : "+resData);

        data.put("statusCode", resData.get("statusCode"));
        data.put("datarow1", resData.get("data"));
        res.addResponse("data", data);

        return ResponseEntity.ok(res.success());
    }

    @RequestMapping("accorditainerlisttest")
    public String accorditainerlisttest(){
        return "testpage/accorditainerlisttest";
    }

    @RequestMapping("accorditainerregtest")
    public String accorditainerregtest(){
        return "testpage/accorditainerregtest";
    }

    @RequestMapping("sensorncd")
    public String sensorncd(){
        return "testpage/sensorncd";
    }

    // ncd센서데이터가져오기(창고)
    @PostMapping("ncdData")
    public ResponseEntity<Map<String,Object>> ncdData(@RequestParam(value="fromVal", defaultValue="") String fromVal,
                                                      @RequestParam(value="toVal", defaultValue="") String toVal) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        String timeVal = fromVal+"-"+toVal;
        System.out.println("timeVal : "+timeVal);
        HashMap<String, Object> resData = aciawsLambdaService.getDeviceNcd(timeVal);
        System.out.println("resData : "+resData);

        data.put("statusCode", resData.get("statusCode"));
        data.put("datarow", resData.get("data"));
        res.addResponse("data", data);

        return ResponseEntity.ok(res.success());
    }

    @RequestMapping("sensorbeacon")
    public String sensorbeacon(){
        return "testpage/sensorbeacon";
    }

    // beacon센서데이터가져오기
    @PostMapping("beacon")
    public ResponseEntity<Map<String,Object>> beacon(@RequestParam(value="fromVal", defaultValue="") String fromVal,
                                                     @RequestParam(value="toVal", defaultValue="") String toVal) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        String timeVal = fromVal+"-"+toVal;
//        System.out.println("timeVal : "+timeVal);
        HashMap<String, Object> resData = aciawsLambdaService.getBeacon(timeVal);
//        System.out.println("resData : "+resData);

        data.put("statusCode", resData.get("statusCode"));
        data.put("datarow1", resData.get("data"));
        res.addResponse("data", data);

        return ResponseEntity.ok(res.success());
    }

    @RequestMapping("sensorncdjeju")
    public String sensorncdjeju(){
        return "testpage/sensorncdjeju";
    }

    // ncd센서데이터가져오기(제주)
    @PostMapping("ncdDataJeju")
    public ResponseEntity<Map<String,Object>> ncdDataJeju(@RequestParam(value="fromVal", defaultValue="") String fromVal,
                                                          @RequestParam(value="toVal", defaultValue="") String toVal) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        String timeVal = fromVal+"-"+toVal;
//        System.out.println("timeVal : "+timeVal);
        HashMap<String, Object> resData = aciawsLambdaService.getDeviceNcdJeju(timeVal);
        System.out.println("resData : "+resData);

        data.put("statusCode", resData.get("statusCode"));
        data.put("datarow", resData.get("data"));
        res.addResponse("data", data);

        return ResponseEntity.ok(res.success());
    }

    @RequestMapping("mlsensor")
    public String mlsensor(){
        return "testpage/mlsensor";
    }

    // ml추론 센서데이터가져오기
    @PostMapping("mlData")
    public ResponseEntity<Map<String,Object>> mlData(@RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                     @RequestParam(value="fromVal", defaultValue="") String fromVal,
                                                     @RequestParam(value="toVal", defaultValue="") String toVal) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        String timeVal = deviceid+"="+fromVal+"="+toVal;
//        System.out.println("timeVal : "+timeVal);
        HashMap<String, Object> resData = aciawsLambdaService.getDeviceMl(timeVal);
        System.out.println("resData : "+resData);

        data.put("statusCode", resData.get("statusCode"));
        data.put("datarow", resData.get("data"));
        res.addResponse("data", data);

        return ResponseEntity.ok(res.success());
    }

    @RequestMapping("oauth2test")
    public String oauth2test(){
        return "testpage/oauth2test";
    }

    @RequestMapping("oauth2test2")
    public String oauth2test2(){
        return "testpage/oauth2test2";
    }

    // Oauth2 API실행
    @PostMapping("oauth")
    public ResponseEntity<Map<String,Object>> oauth() {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        RestOperations restOperations;
//        InputStream inputStream = new ByteArrayInputStream(restOperations.getForObject(URI.create())})

        data.put("data", "");
        res.addResponse("data", data);
        return ResponseEntity.ok(res.success());
    }

}
