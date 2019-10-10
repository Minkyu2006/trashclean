package kr.co.broadwave.aci.teams;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author InSeok
 * Date : 2019-04-04
 * Time : 10:07
 * Remark :
 */
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class TeamRestControllerSliceTest {



    //@Autowired
    private MockMvc mockMvc;

    @InjectMocks
    TeamRestController controller;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s,local) -> new MappingJackson2JsonView())
                .build();
    }

    @Mock
    TeamService teamService;


    @Mock
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void teamlist() throws Exception{
        //given
        TeamDto t1 = TeamDto.builder()
                .id(1L)
                .teamcode("C0001")
                .teamname("팀1")
                .remark("비고1")
                .build();
        TeamDto t2 = TeamDto.builder()
                .id(2L)
                .teamcode("C0002")
                .teamname("팀2")
                .remark("비고2")
                .build();
        TeamDto t3 = TeamDto.builder()
                .id(3L)
                .teamcode("C0003")
                .teamname("팀3")
                .remark("비고3")
                .build();

        List<TeamDto> teamslist = Arrays.asList(t1,t2,t3);


        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "teamcode");
        Page<TeamDto> teams = new PageImpl<>(teamslist, pageable, teamslist.size());
        System.out.println(teams.getContent());

        given(teamService.findAllBySearchStrings(any(String.class),any(String.class),any(Pageable.class))).willReturn(teams);

        //when then
        mockMvc.perform(post("/api/team/list?size=2&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.datalist").exists())
                .andExpect(jsonPath("data.total_rows").exists())
        ;
//
//        assertThat(teams.getTotalElements()).isEqualTo(3);

    }



    @Test
    public void findAll() throws Exception{
        //given
        Team t1 = Team.builder()
                .id(1L)
                .teamcode("C0001")
                .teamname("팀1")
                .remark("비고1")
                .build();
        Team t2 = Team.builder()
                .id(2L)
                .teamcode("C0002")
                .teamname("팀2")
                .remark("비고2")
                .build();
        Team t3 = Team.builder()
                .id(3L)
                .teamcode("C0003")
                .teamname("팀3")
                .remark("비고3")
                .build();

        List<Team> teams = Arrays.asList(t1,t2,t3);

        given(teamService.findAll()).willReturn(teams);

        //when then
        mockMvc.perform(get("/api/team/listold")
                .param("teamname","")
        )
                .andDo(print())
                .andExpect(status().isOk());
//        assertThat(teams.getTotalElements()).isEqualTo(3);

    }

}