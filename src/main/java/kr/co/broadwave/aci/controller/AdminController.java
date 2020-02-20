package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.company.CompanyAccountDto;
import kr.co.broadwave.aci.company.CompanyService;
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

    private final MasterCodeService masterCodeService;
    private final TeamService teamService;
    private final CompanyService companyService;

    @Autowired
    public AdminController(MasterCodeService masterCodeService, TeamService teamService, CompanyService companyService) {
        this.masterCodeService = masterCodeService;
        this.teamService = teamService;
        this.companyService = companyService;
    }

    //사용자관리화면
    @RequestMapping("accountreg")
    public String accoutrreg(Model model){

        List<MasterCodeDto> positions = masterCodeService.findCodeList(CodeType.C0001); // 직급코드가져오기
        List<TeamDto> teams = teamService.findTeamList();
        List<CompanyAccountDto> companys = companyService.findCompanyList();

        model.addAttribute("roles", AccountRole.values());
        model.addAttribute("positions", positions);
        model.addAttribute("teams", teams);
        model.addAttribute("companys", companys);
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

    //차량등록
    @RequestMapping("vehiclereg")
    public String vehiclereg(Model model){
        List<MasterCodeDto> vcShapes = masterCodeService.findCodeList(CodeType.C0010);
        List<MasterCodeDto> vcUsages = masterCodeService.findCodeList(CodeType.C0011);
        List<MasterCodeDto> vcStates = masterCodeService.findCodeList(CodeType.C0012);

        model.addAttribute("vcShapes", vcShapes);
        model.addAttribute("vcUsages", vcUsages);
        model.addAttribute("vcStates", vcStates);

        return "admin/vehiclereg";
    }

}
