package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.awsiot.ACIIoTService;
import kr.co.broadwave.aci.bscodes.*;
import kr.co.broadwave.aci.imodel.IModelDto;
import kr.co.broadwave.aci.imodel.IModelService;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import kr.co.broadwave.aci.teams.TeamDto;
import kr.co.broadwave.aci.teams.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author InSeok
 * Date : 2019-07-25
 * Remark :
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    private final IModelService iModelService;
    private final MasterCodeService masterCodeService;
    private final TeamService teamService;


    @Autowired
    public AdminController(MasterCodeService masterCodeService, TeamService teamService,IModelService iModelService) {
        this.iModelService = iModelService;
        this.masterCodeService = masterCodeService;
        this.teamService = teamService;
    }

    //사용자등록화면
    @RequestMapping("accountreg")
    public String accoutrreg(Model model){

        List<MasterCodeDto> positions = masterCodeService.findCodeList(CodeType.C0001); // 직급코드가져오기
        List<TeamDto> teams = teamService.findTeamList();
        model.addAttribute("roles", AccountRole.values());
        model.addAttribute("positions", positions);
        model.addAttribute("teams", teams);
        model.addAttribute("approvalTypes", ApprovalType.values());

        return "admin/accountreg";
    }

    //사용자승인
    @RequestMapping("accountapproval")
    public String accountApproval(){
        return "admin/accountapproval";
    }


    //관리코드등록
    @RequestMapping("mastercodereg")
    public String masterCodeReg(Model model){
        model.addAttribute("codetypes", CodeType.values());

        return "admin/mastercodereg";
    }

    //업체등록
    @RequestMapping("compreg")
    public String compReg(Model model){
        List<MasterCodeDto> compDivisions = masterCodeService.findCodeList(CodeType.C0006);
        List<MasterCodeDto> compcsRegionals = masterCodeService.findCodeList(CodeType.C0007);

        model.addAttribute("compDivisions", compDivisions);
        model.addAttribute("compcsRegionals", compcsRegionals);

        return "admin/compreg";
    }

    //장비등록
    @RequestMapping("equipreg")
    public String equipreg(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);
        List<MasterCodeDto> modelTypes = masterCodeService.findCodeList(CodeType.C0009);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);
        model.addAttribute("modelTypes", modelTypes);

        return "admin/equipmentreg";
    }

    //모델등록
    @RequestMapping("modelreg")
    public String modelreg(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> modelTypes = masterCodeService.findCodeList(CodeType.C0009);
        List<MasterCodeDto> equipdUnits = masterCodeService.findCodeList(CodeType.C0008);

        model.addAttribute("equipdUnits", equipdUnits);
        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("modelTypes", modelTypes);

        return "admin/modelreg";
    }

}
