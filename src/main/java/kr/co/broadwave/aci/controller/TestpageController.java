package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.files.FileUploadDto;
import kr.co.broadwave.aci.files.FileUploadService;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
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
    private final MasterCodeService masterCodeService;
    private final FileUploadService fileUploadService;

    @Autowired
    public TestpageController(MasterCodeService masterCodeService, FileUploadService fileUploadService) {
        this.masterCodeService = masterCodeService;
        this.fileUploadService = fileUploadService;
    }

    @RequestMapping("monitering")
    public String monitering(){
        return "testpage/monitering";
    }

    @RequestMapping("maptest")
    public String maptest(){
        return "testpage/maptest";
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


}
