package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.accounts.*;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.company.CompanyAccountDto;
import kr.co.broadwave.aci.company.CompanyService;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import kr.co.broadwave.aci.teams.TeamDto;
import kr.co.broadwave.aci.teams.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-03-25
 * Time : 09:17
 * Remark : 메인 컨트롤러
 */
@Slf4j
@Controller
public class Maincontroller {

    private final AccountService accountService;
    private final LoginlogService loginlogService;
    private final MasterCodeService masterCodeService;
    private final TeamService teamService;
    private final CompanyService companyService;

    @Autowired
    public Maincontroller(AccountService accountService,LoginlogService loginlogService, MasterCodeService masterCodeService, TeamService teamService, CompanyService companyService) {
        this.accountService = accountService;
        this.loginlogService = loginlogService;
        this.masterCodeService = masterCodeService;
        this.teamService = teamService;
        this.companyService = companyService;
    }

    //메인화면
    @RequestMapping("/")
    public String main(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent").toUpperCase();
        String IS_MOBILE = "MOBILE";
        if(userAgent.contains(IS_MOBILE)) {
            return "redirect:/collection/mobileindex";
        }
        return "index";
    }

    @RequestMapping("/mypage")
    public String mypage(Model model, HttpServletRequest request){
        String userid = CommonUtils.getCurrentuser(request);
        Account account = accountService.findByUserid(userid).get();

        model.addAttribute("account",account);
        return "mypage";
    }

    @RequestMapping("/loginsuccess")
    public String loginsuccess(HttpServletRequest request){

        //Security 로그인정보가져와서 세션에 저장하자
        HttpSession session = request.getSession();
        String login_ip = CommonUtils.getIp(request);
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Account> optionalAccount = accountService.findByUserid(request.getUserPrincipal().getName());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            //userid 세션저장
            session.setAttribute("userid", account.getUserid());
            session.setAttribute("username", account.getUsername());
            session.setAttribute("teamname", account.getTeam().getTeamname());
            session.setAttribute("role", account.getRole().getCode());

            Loginlog loginlog = Loginlog.builder()
                    .loginAccount(account)
                    .userid(account.getUserid())
                    .successYN("Y")
                    .loginIp(login_ip)
                    .build();
            loginlogService.save(loginlog);

            log.info("============Login Success============");
            log.info(" session userid " + session.getAttribute("userid"));
            log.info(" session username " + session.getAttribute("username"));
            log.info(" session teamname " + session.getAttribute("teamname"));
            log.info(" session role " + session.getAttribute("role"));
            log.info("=====================================");
        }

        String userAgent = request.getHeader("User-Agent").toUpperCase();
        String IS_MOBILE = "MOBILE";
        if(userAgent.contains(IS_MOBILE)) {
            return "redirect:/collection/mobileindex";
        }
        return "redirect:/dashboard/dashboardall";

    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request){

        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);

        return "login";
    }

    @RequestMapping("/signup")
    public String signup(Model model){
        List<MasterCodeDto> positions = masterCodeService.findCodeList(CodeType.C0001); // 직급코드가져오기
        List<TeamDto> teams = teamService.findTeamList();
        List<CompanyAccountDto> companys = companyService.findCompanyList();
        System.out.println("companys : "+companys);

        model.addAttribute("companys", companys);
        model.addAttribute("roles", AccountRole.values());
        model.addAttribute("positions", positions);
        model.addAttribute("teams", teams);
        return "signup";
    }

}
