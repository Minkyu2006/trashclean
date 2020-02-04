package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.files.FileUploadDto;
import kr.co.broadwave.aci.files.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author InSeok
 * Date : 2019-10-17
 * Remark :
 */
@Controller
@RequestMapping("/testpage")
public class TestpageController {
    private final FileUploadService fileUploadService;

    @Autowired
    public TestpageController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
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
}
