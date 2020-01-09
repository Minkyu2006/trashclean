package kr.co.broadwave.aci.accounts;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import kr.co.broadwave.aci.teams.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-03-25
 * Time : 09:36
 * Remark : 사용자정보 레파지토리
 */
public interface AccountRepository extends JpaRepository<Account,Long>,QuerydslPredicateExecutor<Account> {

    //@EntityGraph(attributePaths = {"team","position"})
    @Query("select a from Account a join fetch a.team join fetch a.position where a.userid = :userid")
    Optional<Account> findByUserid(@Param("userid") String userid);

    //승인된 사용자에대해서만 조회
    @Query("select a from Account a join fetch a.team where a.userid = :userid and a.approvalType = 'AT02'")
    Optional<Account> findByUseridAndApprovalType(@Param("userid") String userid);

    long countByTeam(Team team);



    //@Query("select a from Account a join fetch a.team")
    //List<Account> findAllJoinFetch();

    @EntityGraph(attributePaths = "team")
    @Query("select a from Account a")
    List<Account> findAllJoinFetch();

    // querydsl
    default Predicate findCustomByUsernameAndEmail(String username,String email){
        BooleanBuilder builder = new BooleanBuilder();
        QAccount qAccount = QAccount.account;

        builder.and(qAccount.userid.containsIgnoreCase("hgd"));

        return builder;
    }

    /*
    public default Predicate makePredicate(String email, String name) {
        BooleanBuilder builder = new BooleanBuilder();
        QUserVo userVo = QUserVo.userVo;

        builder.and(userVo.idx.gt(111));
        return builder;

     */


}
