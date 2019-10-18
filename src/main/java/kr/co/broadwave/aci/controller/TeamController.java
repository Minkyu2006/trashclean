package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.teams.TeamDto;
import kr.co.broadwave.aci.teams.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author InSeok
 * Date : 2019-04-01
 * Time : 10:15
 * Remark : 팀등록 컨트롤러
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class TeamController {

    @Autowired
    TeamService teamService;
    //팀등록화면
    @RequestMapping("teamreg")
    public String accoutrreg(){

        return "admin/teamreg";
    }

    @RequestMapping(value="/teamlist.xls" , params = {"teamcode", "teamname"})
    public String getExcelByExt(Model model, HttpServletRequest request,
                                @RequestParam(value = "teamcode") String teamcode,
                                @RequestParam(value = "teamname") String teamname

    ) {

        //엑셀헤더
        List<String> header = Arrays.asList("부서코드", "팀명", "비고");
        //엑셀전환할 자료
        List<TeamDto> downLoadData = teamService.findAllBySearchStringsExcel(teamcode, teamname);

        //엑셀전황
        CommonUtils.exceldataModel(model,header,downLoadData,"teamlist");

        log.info("부서 리스트 엑셀 다운로드 ( loginID : '" + CommonUtils.getCurrentuser(request) +"', IP : '" + CommonUtils.getIp(request) + "' )" );

        return "excelDownXls";

    }


}
