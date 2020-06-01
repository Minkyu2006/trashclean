package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.bscodes.AccordiType;
import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private final MasterCodeService masterCodeService;

    @Autowired
    public CollectionController(MasterCodeService masterCodeService){
        this.masterCodeService=masterCodeService;
    }

    // 수거업무조회
    @RequestMapping("collectionsearch")
    public String collectionsearch(Model model){

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

        return "collection/collectionsearch";
    }

    // 수거업무모니터링
    @RequestMapping("collectionmonitering")
    public String collectionmonitering(){
        return "collection/collectionmonitering";
    }

    // 수거업무등록
    @RequestMapping("collectionreg")
    public String collectionreg(){
        return "collection/collectionreg";
    }

    // iSolarbin 수거업무등록
    @RequestMapping("collectionisolarbin")
    public String collectionisolarbin(Model model){

        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);
        List<MasterCodeDto> vcShapes = masterCodeService.findCodeList(CodeType.C0010);
        List<MasterCodeDto> vcUsages = masterCodeService.findCodeList(CodeType.C0011);
        List<MasterCodeDto> vcStates = masterCodeService.findCodeList(CodeType.C0012);

        model.addAttribute("equipdCountrys", equipdCountrys);
        model.addAttribute("vcShapes", vcShapes);
        model.addAttribute("vcUsages", vcUsages);
        model.addAttribute("vcStates", vcStates);

        return "collection/collectionisolarbin";
    }

    // iTainer 수거업무등록
    @RequestMapping("collectionitainer")
    public String collectionitainer(Model model){

        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);
        List<MasterCodeDto> vcShapes = masterCodeService.findCodeList(CodeType.C0010);
        List<MasterCodeDto> vcUsages = masterCodeService.findCodeList(CodeType.C0011);
        List<MasterCodeDto> vcStates = masterCodeService.findCodeList(CodeType.C0012);

        model.addAttribute("equipdCountrys", equipdCountrys);
        model.addAttribute("vcShapes", vcShapes);
        model.addAttribute("vcUsages", vcUsages);
        model.addAttribute("vcStates", vcStates);

        return "collection/collectionitainer";
    }

    @RequestMapping("accorditainer")
    public String accorditainer(Model model){
        List<MasterCodeDto> ciTypes = masterCodeService.findCodeList(CodeType.C0016);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);
        List<MasterCodeDto> vcShapes = masterCodeService.findCodeList(CodeType.C0010);
        List<MasterCodeDto> vcUsages = masterCodeService.findCodeList(CodeType.C0011);
        List<MasterCodeDto> vcStates = masterCodeService.findCodeList(CodeType.C0012);

        model.addAttribute("ciTypes", ciTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);
        model.addAttribute("vcShapes", vcShapes);
        model.addAttribute("vcUsages", vcUsages);
        model.addAttribute("vcStates", vcStates);
        model.addAttribute("accordiTypes", AccordiType.values());

        return "collection/accorditainer";
    }

}
