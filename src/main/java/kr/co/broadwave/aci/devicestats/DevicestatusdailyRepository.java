package kr.co.broadwave.aci.devicestats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-10
 * Time :
 * Remark : Devicestatusdaily Repository
 */
public interface DevicestatusdailyRepository extends JpaRepository<Devicestatusdaily,Long>, QuerydslPredicateExecutor<Devicestatusdaily> {

}
