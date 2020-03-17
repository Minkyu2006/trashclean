package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.bscodes.ProcStatsType;
import kr.co.broadwave.aci.collection.CollectionTaskListInfoDto;
import kr.co.broadwave.aci.collection.CollectionTaskService;
import kr.co.broadwave.aci.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Minkyu
 * Date : 2020-03-17
 * Time :
 * Remark : Mobile 관련 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/mobile")
public class MobileController {

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

    private final CollectionTaskService collectionTaskService;

    @Autowired
    public MobileController(CollectionTaskService collectionTaskService){
        this.collectionTaskService=collectionTaskService;
    }

    //수거업무메인화면 - 모바일
    @RequestMapping("mobileindex")
    public String mobileindex(HttpServletRequest request){
        String currentuserid = CommonUtils.getCurrentuser(request);
        if(currentuserid.equals("system")){
            return "login";
        }else{
            return "mobile/mobileindex";
        }
    }

    //수거업무처리 - 모바일
    @RequestMapping("collectionprocess/{id}")
    public String collectionprocessid(HttpServletRequest request,Model model, @PathVariable Long id){
        String currentuserid = CommonUtils.getCurrentuser(request);
        CollectionTaskListInfoDto collectionTasks = collectionTaskService.findByCollectionListInfoQueryDsl(id);
        //log.info("collectionTasks : "+collectionTasks);
        ProcStatsType procStatsType = ProcStatsType.valueOf("CL02");
        //AccountRole accountRole = AccountRole.valueOf("ROLE_COLLECTOR");
        if(currentuserid.equals("system")) {
            return "login";
        }else if(!collectionTasks.getProcStatsType().equals(procStatsType)){
            return "mobile/collectionlist";
        }else{
            model.addAttribute("collectionTasks", collectionTasks);
            model.addAttribute("mdmaximum", Math.round(collectionTasks.getMdmaximum())+collectionTasks.getMdunit());
            model.addAttribute("emcountrylocation", collectionTasks.getEmCountry()+"/"+collectionTasks.getEmLoation());
            model.addAttribute("fileurl", AWSS3URL+collectionTasks.getFilePath()+collectionTasks.getSaveFileName());

            return "mobile/collectionprocess";
        }
    }

    //수거업무처리 - 모바일
    @RequestMapping("collectionprocess")
    public String collectionprocess(HttpServletRequest request){
        String currentuserid = CommonUtils.getCurrentuser(request);
        if(currentuserid.equals("system")){
            return "login";
        }else{
            return "error/404";
        }
    }

    //수거업무리스트 - 모바일
    @RequestMapping("collectionlist")
    public String collectionlist(HttpServletRequest request){
        String currentuserid = CommonUtils.getCurrentuser(request);
        if(currentuserid.equals("system")){
            return "login";
        }else{
            return "mobile/collectionlist";
        }
    }
}
