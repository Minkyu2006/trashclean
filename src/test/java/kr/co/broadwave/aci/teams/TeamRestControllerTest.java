package kr.co.broadwave.aci.teams;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author InSeok
 * Date : 2019-04-08
 * Time : 17:40
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeamRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TeamService teamService;

    @Test
    @WithMockUser(value = "testuser",roles = {"ADMIN"})
    public void team_API_team() throws Exception{

        mockMvc.perform(post("/api/team/team")
                .with(csrf())
                .param("teamcode","T00001")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.datarow").exists())
                .andExpect(jsonPath("data.datarow.teamcode").exists())
                .andExpect(jsonPath("data.datarow.teamcode").value("T00001"))

        ;
    }

    @Test
    @WithMockUser(value = "testuser",roles = {"ADMIN"})
    public void team_API_list() throws Exception{

        mockMvc.perform(post("/api/team/list")
                    .with(csrf())
                        .param("size","2")
                        .param("page","0")
                        .param("teamcode","T00001")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.datalist").exists())
                .andExpect(jsonPath("data.total_rows").exists())
                .andExpect(jsonPath("data.total_rows").value("1"))

        ;
    }
    @Test
    @WithMockUser(value = "testuser",roles = {"ADMIN"})
    public void team_API_reg_and_del() throws Exception{
        //=========   save     =============

        //given when then
        mockMvc.perform(post("/api/team/reg")
                        .with(csrf())
                        .param("teamcode","J0001")
                        .param("teamname","Test팀명")
                        .param("remark","테스트비고 ")
                        .param("mode","N")
                )
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Team> optionalTeam = teamService.findByTeamcode("J0001");
        Team team = optionalTeam.get();

        assertThat(optionalTeam.isPresent()).isEqualTo(true);
        assertThat(team.getTeamcode()).isEqualTo("J0001");

        //=========== del ============

        //when
        mockMvc.perform(post("/api/team/del")
                .with(csrf())
                .param("teamcode",team.getTeamcode())
        )
                .andDo(print())
                .andExpect(status().isOk());
        //then
        Optional<Team> optionalDeleteTeam = teamService.findByTeamcode(team.getTeamcode());
        assertThat(optionalDeleteTeam.isPresent()).isEqualTo(false);




    }


    @Test
    @WithMockUser(value = "testuser",roles = {"ADMIN"})
    public void team_API_del_error_notExists() throws Exception {

        //when then
        mockMvc.perform(post("/api/team/del")
                .with(csrf())
                .param("teamcode","X00001")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("err_code").exists())
                .andExpect(jsonPath("err_code").value("E003"))
                .andExpect(jsonPath("status").value("500"))
                ;


    }

    @Test
    @WithMockUser(value = "testuser",roles = {"ADMIN"})
    public void team_API_del_error_UseingData() throws Exception {

        //when then
        mockMvc.perform(post("/api/team/del")
                .with(csrf())
                .param("teamcode","T00001")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("err_code").exists())
                .andExpect(jsonPath("err_code").value("E002"))
                .andExpect(jsonPath("status").value("500"))
        ;


    }
}
