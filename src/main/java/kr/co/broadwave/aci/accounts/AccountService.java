package kr.co.broadwave.aci.accounts;

import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.teams.Team;
import kr.co.broadwave.aci.teams.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author InSeok
 * Date : 2019-03-25
 * Time : 09:43
 * Remark : Account Service
 */
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountRepositoryCustom accountRepositoryCustom;

    @Autowired
    TeamService teamService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account){
        //password encoding
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account);
    }
    public Account updateAccount(Account account){
        return this.accountRepository.save(account);
    }

    @Transactional
    public Account modifyAccount(Account account){
        //password notencoding

        return this.accountRepository.save(account);
    }
    public Optional<Account> findByUserid(String userid ){
        return this.accountRepository.findByUserid(userid);
    }

    public Page<AccountDtoWithTeam> findAllBySearchStrings(String userid,String username,String email,Pageable pageable){
        return accountRepositoryCustom.findAllBySearchStrings(userid,username,email,pageable);
    }
    public Page<AccountDto> findAllByApproval(String username, String startDate, String endDate, Pageable pageable) {
        return accountRepositoryCustom.findAllByApproval(username,startDate,endDate,pageable);
    }

    public Long saveApproval(Account account, ApprovalType approvalType,String loginId){
        return accountRepositoryCustom.saveApproval(account,approvalType,loginId);
    }

    public void delete(Account account){
        accountRepository.delete(account);
    }

    public Long countByTeam(Team team){
        return accountRepository.countByTeam(team);
    }

    ////////아래는 테스트코드





    public List<Account> findAll() {
        return this.accountRepository.findAll();
    }



    public void list2() {
//
//        List<Account> accounts = this.accountRepository.findAllJoinFetch();
//        for (Account a : accounts) {
//            System.out.println(a.getUsername());
//            System.out.println(a.getTeam().getTeamname());
//        }
        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "userid");


        Page<Account> pageAccounts = this.accountRepository.findAll(
                this.accountRepository.findCustomByUsernameAndEmail("hgd","ddd"),pageable
        );

        pageAccounts.getContent().forEach(account -> {
            System.out.println(account.getUsername() + "/"+ account.getEmail() + "/" + account.getTeam().getTeamname());
        });


    }

    public Page<AccountDtoWithTeam> list3(String userid, String username,String teamname, Pageable pageable){
        //Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "userid");
        Page<AccountDtoWithTeam> allUserWithTeam = this.accountRepositoryCustom.findAllBySearchStrings(userid,username,teamname,pageable);
        return allUserWithTeam;
    }
    public void findbyUserid2(String userid){
        Optional<Account> optionalAccount = this.accountRepository.findByUserid(userid);

        Account ac = optionalAccount.get();
        System.out.println(ac.getUsername());

    }

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        //Account account = accountRepository.findByUserid(userid).get();
        Account account = accountRepository.findByUseridAndApprovalType(userid).get(); // 승인된사용자만 로그인가능


        return new User(account.getUserid(),account.getPassword(),true,true,true,true,getAuthorities(account));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(Account account) {
        return Arrays.asList(new SimpleGrantedAuthority(account.getRole().getCode()));
    }



}
