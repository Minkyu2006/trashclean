package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountRepository;
import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.teams.Team;
import kr.co.broadwave.aci.teams.TeamRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author InSeok
 * Date : 2019-08-14
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MasterCodeRestcontrollerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MasterCodeService masterCodeService;

    @Test
    public void masterCode_API_list() throws Exception {

    }

    @Test
    @WithMockUser(value = "testuser",roles = {"ADMIN"})
    public void masterCode_API_reg() throws Exception{

        //given
        Team t1 = Team.builder()
                .teamcode("A001")
                .teamname("TestTeam1")
                .remark("비고").build();
        teamRepository.save(t1);
        Account a1 = Account.builder()
                .userid("testuser")
                .username("테스트유저")
                .password("1234")
                .email("test@naver.com")
                .role(AccountRole.ROLE_ADMIN)
                .team(t1)
                .build();
        accountRepository.save(a1);

        //when

        mockMvc.perform(post("/api/mastercode/reg")
                .with(csrf())
                .param("codeType","C0001")
                .param("code","M0001")
                .param("name","부장")
                .param("remark","비고사항")
        )
                .andDo(print())
                .andExpect(status().isOk());

        //then
        Optional<MasterCode> optionalMasterCode = masterCodeService.findByCoAndCodeTypeAndCode(CodeType.C0001, "M0001");
        assertThat(optionalMasterCode.isPresent()).as("저장된값이 존재하는지 체크 [Expect true]").isEqualTo(true);
        assertThat(optionalMasterCode.get().getName()).as("저장된값의 이름 체크 [Expect 부장]").isEqualTo("부장");


    }
}
