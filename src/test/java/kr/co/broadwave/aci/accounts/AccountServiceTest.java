package kr.co.broadwave.aci.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;



import static org.assertj.core.api.Java6Assertions.assertThat;


/**
 * @author InSeok
 * Date : 2019-04-24
 * Time : 12:34
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void saveAccount() {
    }

    @Test
    public void findByUserid() {

        assertThat(accountRepository.findByUserid("admin").isPresent()).isEqualTo(true);
        assertThat(accountRepository.findByUserid("XXXXX").isPresent()).isEqualTo(false);


    }

    @Test
    public void findAllBySearchStrings() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void countByTeam() {
    }
}
