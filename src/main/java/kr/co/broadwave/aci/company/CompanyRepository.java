package kr.co.broadwave.aci.teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-03-26
 * Time : 10:23
 * Remark : Team Repository
 */
public interface TeamRepository extends JpaRepository<Team,Long>, QuerydslPredicateExecutor<Team> {

    Optional<Team> findByTeamname(String teamname);
    Optional<Team> findByTeamcode(String teamcode);


}
