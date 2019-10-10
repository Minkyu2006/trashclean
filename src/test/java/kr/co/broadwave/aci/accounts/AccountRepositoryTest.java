package kr.co.broadwave.aci.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author InSeok
 * Date : 2019-03-25
 * Time : 09:56
 * Remark : AccountService Test
 */
@RunWith(SpringRunner.class)
//@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;
  //  AccountService accountService;

    @Test
    public void saveTest() {

        //given
        Account newAccount = Account.builder()
                .userid("admin")
                .password("1234")
                .insertDateTime(LocalDateTime.now())
                .modifyDateTime(null)
                .build();

        //when
        Account saveAccount = accountRepository.save(newAccount);

        //then
        assertThat(saveAccount.getUserid()).isEqualTo(newAccount.getUserid());
        //assertEquals( , newAccount.getUserid());
        accountRepository.delete(newAccount);

    }
}