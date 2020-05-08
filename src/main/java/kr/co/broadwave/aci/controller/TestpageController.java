package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.files.FileUploadDto;
import kr.co.broadwave.aci.files.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        HashMap<String,Object> resData = aciawsLambdaService.getDeviceBeacon(searchDate);
//        System.out.println("resData : "+resData);

        data.put("statusCode",resData.get("statusCode"));
        data.put("datarow1",resData.get("data"));
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

}
