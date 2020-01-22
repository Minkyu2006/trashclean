package kr.co.broadwave.aci.teams;

import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-04-02
 * Time : 14:09
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/team")
public class TeamRestController {

    private AjaxResponse res = new AjaxResponse();
    private HashMap<String, Object> data = new HashMap<>();

    private final TeamService teamService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Autowired
    public TeamRestController(TeamService teamService, AccountService accountService, ModelMapper modelMapper) {
        this.teamService = teamService;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @PostMapping ("team")
    public ResponseEntity<Map<String,Object>> team(@RequestParam (value="teamcode", defaultValue="") String teamcode
                            ){
        //log.info("단일부서조회  / teamcode: '" + teamcode +"'");
        Optional<Team> optionalTeam = teamService.findByTeamcode(teamcode);

        if (!optionalTeam.isPresent()){
            //log.info("단일부서조회실패 : 조회할 데이터가 존재하지않음 , 조회대상 teamcode: '" + teamcode +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(),ResponseErrorCode.E004.getDesc()));
        }
        Team team = optionalTeam.get();

        data.clear();
        data.put("datarow",team);
        res.addResponse("data",data);

        //log.info("단일부서 조회 성공 : " + team.toString() );
        return ResponseEntity.ok(res.success());

    }


    @PostMapping ("reg")
    public ResponseEntity<Map<String,Object>> teamreg(@ModelAttribute TeamMapperDto teamMapperDto,HttpServletRequest request){

        Team team = modelMapper.map(teamMapperDto, Team.class);

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Team> optionalTeam = teamService.findByTeamcode(team.getTeamcode());
        //신규일때
        if ( teamMapperDto.getMode().equals("N")) {

            //부서코드가 중복되면 에러메세지 반환
            if (optionalTeam.isPresent()) {
                //log.info("부서저장실패(부서코드중복) 부서코드: '" + team.getTeamcode() + "'");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E001.getCode(), ResponseErrorCode.E001.getDesc()));
            }
            team.setInsertDateTime(LocalDateTime.now());
            team.setInsert_id(currentuserid);
        }else{//수정일때
            if (!optionalTeam.isPresent()) {
                //log.info("부서정보수정실패 : 부서코드: '" + team.getTeamcode() + "'");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc()));
            }else{
                team.setId(optionalTeam.get().getId()); // 수정하고자하는 팀에 ID부여
                team.setInsertDateTime(optionalTeam.get().getInsertDateTime());
                team.setInsert_id(optionalTeam.get().getInsert_id());
                team.setModifyDateTime(LocalDateTime.now());
                team.setModify_id(currentuserid);

            }

        }

        Team teamSave = teamService.tesmSave(team);
        data.put("datarow",teamSave);
        res.addResponse("data",data);

        //log.info("부서 저장 성공 : " + teamSave.toString() );
        return ResponseEntity.ok(res.success());
    }

    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> teamdel(@RequestParam (value="teamcode", defaultValue="") String teamcode
                                   ){
        //log.info("부서 삭제 / teamcode: " + teamcode );
        Optional<Team> optionalTeam = teamService.findByTeamcode(teamcode);
        //정보가있는지 체크
        if (!optionalTeam.isPresent()){
            //log.info("부서삭제실패 : 삭제할 데이터가 존재하지않음 , 삭제대상 teamcode : " + teamcode);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(),ResponseErrorCode.E003.getDesc()));
        }
        Team team = optionalTeam.get();
        //사용중인지 체크
        if (accountService.countByTeam(team) > 0){
            //log.info("부서삭제실패 : Account에서 사용중인데이터 , 삭제대상 teamcode : " + teamcode);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E002.getCode(),ResponseErrorCode.E002.getDesc()));
        }

        teamService.delete(team);
        return ResponseEntity.ok(res.success());
    }

    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> teamlist(@RequestParam (value="teamcode", defaultValue="") String teamcode,
                                   @RequestParam (value="teamname", defaultValue="") String teamname ,
                                   @PageableDefault Pageable pageable){

        //log.info("부서 리스트 조회 / 조회조건 : teamcode / '" + teamcode + "', teamname / '" + teamname + "'");
        Page<TeamDto> teams = teamService.findAllBySearchStrings(teamcode,teamname, pageable);

        return CommonUtils.ResponseEntityPage(teams);
    }

    @RequestMapping("listold")
    public ResponseEntity<Map<String,Object>> teamlistold(@RequestParam(value="teamname", defaultValue="") String teamname){

        //log.info("부서 리스트(List)조회 / 조회조건 : " + teamname );

        List<Team> teams = teamService.findAll();

        if(teams.size()> 0 ){
            res.addResponse("datalist",teams);
            res.addResponse("total_rows",teams.size());

        }else{
            res.addResponse("total_rows",teams.size());
        }
        return ResponseEntity.ok(res.success());
    }

}
