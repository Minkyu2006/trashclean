package kr.co.broadwave.aci.devicestats.errweight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author Minkyu
 * Date : 2020-07-30
 * Time :
 * Remark : Errweight Repository
 */
public interface ErrweightRepository extends JpaRepository<kr.co.broadwave.aci.devicestats.errweight.Errweight,Long>, QuerydslPredicateExecutor<Errweight> {

}

