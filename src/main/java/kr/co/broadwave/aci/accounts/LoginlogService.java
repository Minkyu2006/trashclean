package kr.co.broadwave.aci.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author InSeok
 * Date : 2019-04-22
 * Time : 10:42
 * Remark :
 */@Service
public class LoginlogService {
    @Autowired
    LoginlogRepository loginlogRepository;

    public void save(Loginlog loginlog){

        //loginlog.setInsertDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        loginlog.setInsertDateTime(LocalDateTime.now());
        loginlogRepository.save(loginlog);
    }
    public long countByLoginAccount(Account account){
        return loginlogRepository.countByLoginAccount(account);
    }
}
