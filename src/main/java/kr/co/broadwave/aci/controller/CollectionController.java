package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.files.FileUploadService;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Time :
 * Remark : Collectio 관련 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/collections")
public class CollectionController {

    @Autowired
    public CollectionController(){

    }

    @RequestMapping("collectionprocess")
    public String collectionprocess(){
        return "collections/collectionprocess";
    }
}
