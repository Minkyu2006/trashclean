package kr.co.broadwave.aci.teams;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;


/**
 * @author InSeok
 * Date : 2019-04-02
 * Time : 14:49
 * Remark : TeamRepositoryCustomImplTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TeamRepositoryCustomImplTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamRepositoryCustom teamRepositoryCustom;

    @Test
    public void findAllBySearchStrings() {
        //given
        Team t1 = Team.builder()
                .teamcode("AAA001")
                .teamname("TestTeam1")
                .remark("비고").build();

        Team t2 = Team.builder()
                .teamcode("AAA002")
                .teamname("TestTeam2")
                .remark("비고2").build();
        Team t3 = Team.builder()
                .teamcode("AAA003")
                .teamname("TestTeam3")
                .remark("비고3").build();
        List<Team> tall =Arrays.asList(t1,t2,t3);
        teamRepository.saveAll(tall);



        Pageable pageable = PageRequest.of(1, 2, Sort.Direction.ASC, "teamname");

        //when
        Page<TeamDto> teams = teamRepositoryCustom.findAllBySearchStrings("AAA00","", pageable);


        System.out.println("=============================");
        System.out.println("getTotalPages : " + teams.getTotalPages());
        System.out.println("getNumber : " + teams.getNumber());
        System.out.println("getTotalElements : " + teams.getTotalElements());
        System.out.println("getNumberOfElements : " + teams.getNumberOfElements());
        System.out.println("getContent : " + teams.getContent());

        System.out.println("=============================");


        //then
        assertThat(teams.getTotalPages()).isEqualTo(2);
        assertThat(teams.getContent().size()).isEqualTo(1);
        teamRepository.delete(t1);
        teamRepository.delete(t2);
        teamRepository.delete(t3);

    }
}