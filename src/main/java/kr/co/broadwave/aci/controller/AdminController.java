package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.CodeType;
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

    @Autowired
    public AdminController( MasterCodeService masterCodeService, TeamService teamService) {

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



}
