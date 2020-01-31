package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import kr.co.broadwave.aci.collection.CollectionTaskListInfoDto;
import kr.co.broadwave.aci.collection.CollectionTaskService;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Time :
 * Remark : Collection 관련 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/collection")
public class CollectionController {

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

    private final MasterCodeService masterCodeService;
    private final CollectionTaskService collectionTaskService;

    @Autowired
    public CollectionController(MasterCodeService masterCodeService,
                                CollectionTaskService collectionTaskService){
        this.masterCodeService=masterCodeService;
        this.collectionTaskService=collectionTaskService;
    }

    @RequestMapping("collectiontask")
    public String collectiontask(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);
        List<MasterCodeDto> vcShapes = masterCodeService.findCodeList(CodeType.C0010);
        List<MasterCodeDto> vcUsages = masterCodeService.findCodeList(CodeType.C0011);
        List<MasterCodeDto> vcStates = masterCodeService.findCodeList(CodeType.C0012);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);
        model.addAttribute("vcShapes", vcShapes);
        model.addAttribute("vcUsages", vcUsages);
        model.addAttribute("vcStates", vcStates);

        return "collection/collectiontask";
    }

    @RequestMapping("mobileindex")
    public String mobileindex(){

        return "collection/mobileindex";
    }

    @RequestMapping("collectionprocess/{id}")
    public String collectionprocess(Model model, @PathVariable Long id){

        CollectionTaskListInfoDto collectionTasks = collectionTaskService.findByCollectionListInfoQueryDsl(id);
        //log.info("collectionTasks : "+collectionTasks);
        ProcStatsType procStatsType = ProcStatsType.valueOf("CL02");
        if(!collectionTasks.getProcStatsType().equals(procStatsType)){
            return "collection/collectionlist";
        }else{
            model.addAttribute("collectionTasks", collectionTasks);
            model.addAttribute("mdmaximum", Math.round(collectionTasks.getMdmaximum())+collectionTasks.getMdunit());
            model.addAttribute("emcountrylocation", collectionTasks.getEmCountry()+"/"+collectionTasks.getEmLoation());
            model.addAttribute("fileurl", AWSS3URL+collectionTasks.getFilePath()+collectionTasks.getSaveFileName());

            return "collection/collectionprocess";
        }
    }

    @RequestMapping("collectionlist")
    public String collectionlist(){
        return "collection/collectionlist";
    }
}
