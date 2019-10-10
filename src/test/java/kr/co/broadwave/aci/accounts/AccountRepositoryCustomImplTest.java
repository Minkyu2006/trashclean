package kr.co.broadwave.aci.accounts;

import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeRepository;
import kr.co.broadwave.aci.teams.Team;
import kr.co.broadwave.aci.teams.TeamRepository;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author InSeok
 * Date : 2019-04-23
 * Time : 09:32
 * Remark : 사용자 조회 QueryDsl 테스트
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountRepositoryCustomImplTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountRepositoryCustom accountRepositoryCustom;
    @Autowired
    MasterCodeRepository masterCodeRepository;

    @Test
    public void findAllBySearchStrings() {
        //given


        Team t1 = Team.builder()
                .teamcode("QA001")
                .teamname("TestTeam1")
                .remark("비고").build();
        teamRepository.save(t1);
        MasterCode p1 = MasterCode.builder()
                .codeType(CodeType.C0001)
                .code("ADMIN")
                .name("관리자")
                .remark("최초시스템자동생성")
                .insert_id("system")
                .insertDateTime(LocalDateTime.now())
                .modify_id("system")
                .modifyDateTime(LocalDateTime.now())
                .build();
        masterCodeRepository.save(p1);

        Account a1 = Account.builder()
                .userid("S0001")
                .username("테스트유저")
                .password("1234")
                .email("test@naver.com")
                .role(AccountRole.ROLE_ADMIN)
                .team(t1)
                .position(p1)
                .build();
        Account a2 = Account.builder()
                .userid("S0002")
                .username("테스트유저2")
                .password("1234")
                .email("test2@naver.com")
                .role(AccountRole.ROLE_ADMIN)
                .team(t1)
                .position(p1)
                .build();
        Account a3 = Account.builder()
                .userid("S0003")
                .username("신규유저")
                .password("1234")
                .email("test3@naver.com")
                .role(AccountRole.ROLE_ADMIN)
                .team(t1)
                .position(p1)
                .build();
        accountRepository.save(a1);
        accountRepository.save(a2);
        accountRepository.save(a3);

        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "userid");

        //when
        Page<AccountDtoWithTeam> accounts1 = accountRepositoryCustom.findAllBySearchStrings("S000", "", "", pageable);
        Page<AccountDtoWithTeam> accounts2 = accountRepositoryCustom.findAllBySearchStrings("", "테스트", "", pageable);


        //then
        //System.out.println(accounts1.getContent());
        assertThat(accounts1.getTotalPages()).isEqualTo(2);
        assertThat(accounts1.getContent().size()).isEqualTo(2);
        assertThat(accounts1.getTotalElements()).isEqualTo(3);


        assertThat(accounts2.getTotalPages()).isEqualTo(1);
        assertThat(accounts2.getContent().size()).isEqualTo(2);



        accountRepository.delete(a1);
        accountRepository.delete(a2);
        accountRepository.delete(a3);
        teamRepository.delete(t1);
        masterCodeRepository.delete(p1);



    }

    @Test
    public void findAllByApproval(){

        //given
        MasterCode p1 = MasterCode.builder()
                .codeType(CodeType.C0001)
                .code("ADMIN")
                .name("관리자")
                .remark("최초시스템자동생성")
                .insert_id("system")
                .insertDateTime(LocalDateTime.now())
                .modify_id("system")
                .modifyDateTime(LocalDateTime.now())
                .build();
        masterCodeRepository.save(p1);
        Team t1 = Team.builder()
                .teamcode("QQA001")
                .teamname("TestTeam1")
                .remark("비고").build();
        teamRepository.save(t1);
        Account a1 = Account.builder()
                .userid("S0001")
                .username("테스트유저")
                .password("1234")
                .email("test@naver.com")
                .role(AccountRole.ROLE_ADMIN)
                .approvalType(ApprovalType.AT01)
                .insertDateTime(LocalDateTime.now())
                .team(t1)
                .position(p1)
                .build();
        Account a2 = Account.builder()
                .userid("S0002")
                .username("테스트유저2")
                .password("1234")
                .email("test2@naver.com")
                .approvalType(ApprovalType.AT02)
                .role(AccountRole.ROLE_ADMIN)
                .insertDateTime(LocalDateTime.now())
                .team(t1)
                .position(p1)
                .build();
        Account a3 = Account.builder()
                .userid("S0003")
                .username("신규유저")
                .password("1234")
                .email("test3@naver.com")
                .role(AccountRole.ROLE_ADMIN)
                .approvalType(ApprovalType.AT01)
                .insertDateTime(LocalDateTime.now())
                .team(t1)
                .position(p1)
                .build();
        accountRepository.save(a1);
        accountRepository.save(a2);
        accountRepository.save(a3);

        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "userid");

        //when
        Page<AccountDto> accounts1 = accountRepositoryCustom.findAllByApproval("", "", "", pageable);
        Page<AccountDto> accounts2 = accountRepositoryCustom.findAllByApproval("신규유저", "", "", pageable);



        //then
        System.out.println(accounts1.getContent());
        assertThat(accounts1.getTotalPages()).as("totalPages [expect 1]").isEqualTo(1);    // 미승인유저
        assertThat(accounts1.getContent().size()).as("CurrentPage Size [expect 2]").isEqualTo(2); //현재페이지에나오는수
        assertThat(accounts1.getTotalElements()).as("Total row count [expect 2]").isEqualTo(2);  // 총 라인수


        assertThat(accounts2.getTotalPages()).as("totalPages [expect 1]").isEqualTo(1);
        assertThat(accounts2.getContent().size()).as("CurrentPage Size [expect 1]").isEqualTo(1);


        //정리
        accountRepository.delete(a1);
        accountRepository.delete(a2);
        accountRepository.delete(a3);
        teamRepository.delete(t1);
        masterCodeRepository.delete(p1);

    }

    @Test
    public void saveApproval(){
        //given
        MasterCode p1 = MasterCode.builder()
                .codeType(CodeType.C0001)
                .code("ADMIN")
                .name("관리자")
                .remark("최초시스템자동생성")
                .insert_id("system")
                .insertDateTime(LocalDateTime.now())
                .modify_id("system")
                .modifyDateTime(LocalDateTime.now())
                .build();
        masterCodeRepository.save(p1);
        Team t1 = Team.builder()
                .teamcode("QQQA001")
                .teamname("TestTeam1")
                .remark("비고").build();
        teamRepository.save(t1);
        Account a1 = Account.builder()
                .userid("S0001")
                .username("테스트유저")
                .password("1234")
                .email("test@naver.com")
                .role(AccountRole.ROLE_ADMIN)
                .approvalType(ApprovalType.AT01)
                .team(t1)
                .position(p1)
                .build();
        Account a2 = Account.builder()
                .userid("S0002")
                .username("테스트유저2")
                .password("1234")
                .email("test2@naver.com")
                .approvalType(ApprovalType.AT01)
                .role(AccountRole.ROLE_ADMIN)
                .team(t1)
                .position(p1)
                .build();
        Account a3 = Account.builder()
                .userid("S0003")
                .username("신규유저")
                .password("1234")
                .email("test3@naver.com")
                .approvalType(ApprovalType.AT01)
                .role(AccountRole.ROLE_ADMIN)
                .team(t1)
                .position(p1)
                .build();
        accountRepository.save(a1);
        accountRepository.save(a2);
        accountRepository.save(a3);

        //when
        Long aLong = accountRepositoryCustom.saveApproval(a1, ApprovalType.AT02, "testApproval");

        Optional<Account> optionalAccount = accountRepository.findByUserid(a1.getUserid());


        //then
        assertThat(aLong).as("업데이트 처리된 행 [expect 1]").isEqualTo(1); //수정된 행이 1개
        assertThat(optionalAccount.get().getApprovalType()).as("승인필드 값 확인 [expect AT02]").isEqualTo(ApprovalType.AT02);
        assertThat(optionalAccount.get().getApproval_id()).as("승인 한 사람 아이디 확인 [expect 'testApproval']").isEqualTo("testApproval");


        //delete
        accountRepository.delete(a1);
        accountRepository.delete(a2);
        accountRepository.delete(a3);
        teamRepository.delete(t1);
        masterCodeRepository.delete(p1);

    }
}