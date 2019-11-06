package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.teams.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:56
 * Remark : Company Repository
 */
public interface CompanyRepository extends JpaRepository<Company,Long>, QuerydslPredicateExecutor<Company> {
    Optional<Company> findByCsNumber(String csNumber);

    Optional<Company> findByCsOperator(String company);
}
