package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-10-17
 * Remark :
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final MasterCodeService masterCodeService;
    private final AccountService accountService;

    @Autowired
    public DashboardController(MasterCodeService masterCodeService,
                               AccountService accountService) {
        this.accountService = accountService;
        this.masterCodeService = masterCodeService;
    }

    @RequestMapping("dashboardall")
    public String dashboard(Model model, HttpServletRequest request){

        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> account = accountService.findByUserid(currentuserid);

        String userId = null;
        Integer refleshCheck = null;
        Integer refleshCount = null;
        Integer layoutNumber = null;

        if(account.isPresent()){
            userId = account.get().getUserid();
            refleshCheck = account.get().getUserRefleshCheck();
            refleshCount = account.get().getUserRefleshCount();
            layoutNumber = account.get().getUserLayoutNumber();
        }

        model.addAttribute("userId", userId);
        model.addAttribute("refleshCheck", refleshCheck);
        model.addAttribute("refleshCount", refleshCount);
        model.addAttribute("layoutNumber", layoutNumber);

        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);

        return "dashboard/dashboardall";
    }

    @RequestMapping("locationbase")
    public String locationbased(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);
        return "dashboard/locationbase";
    }

}
