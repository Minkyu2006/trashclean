package kr.co.broadwave.aci.position;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.QAccount;
import kr.co.broadwave.aci.teams.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark :
 */
public interface PositionRepository extends JpaRepository<Position,Long>,QuerydslPredicateExecutor<Account> {
    Optional<Position> findByPsZoneCode(String psZoneCode);

    @Query("select a from Position a join fetch a.psCountry join fetch  a.psLocation")
    Optional<Position> findByPositionInfo(Long id);

}
