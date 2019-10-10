package kr.co.broadwave.aci.teams;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * @author InSeok
 * Date : 2019-04-01
 * Time : 14:07
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TeamServiceTest {

    @Autowired
    TeamService teamService;

    @Test
    public void tesmSave(){
        //given
        Team team = Team.builder()
                .teamname("테스트용")
                .remark("테스트용")
                .insert_id("system")
                .insertDateTime(LocalDateTime.now())
                .build();

        //when
        Team saveteam = teamService.tesmSave(team);

        //then

        assertThat(team.getTeamname()).isEqualTo(saveteam.getTeamname());

    }


}