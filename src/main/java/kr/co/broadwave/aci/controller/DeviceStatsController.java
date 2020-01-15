package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2019-01-06
 * Remark :
 */

@Controller
@RequestMapping("/devicestats")
public class DeviceStatsController {

    private final MasterCodeService masterCodeService;
    private final AccountService accountService;

    @Autowired
    public DeviceStatsController(MasterCodeService masterCodeService,
                               AccountService accountService) {
        this.accountService = accountService;
        this.masterCodeService = masterCodeService;
    }

    @RequestMapping("daysdevice")
    public String daysdevice(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);

        return "devicestats/daysdevice";
    }

}
