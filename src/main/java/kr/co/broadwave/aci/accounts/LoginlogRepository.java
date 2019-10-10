package kr.co.broadwave.aci.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author InSeok
 * Date : 2019-04-22
 * Time : 10:41
 * Remark : Loginlog 리파지토리
 */
public interface LoginlogRepository extends JpaRepository<Loginlog,Long> {

    long countByLoginAccount(Account account);


}
