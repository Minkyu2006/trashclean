package kr.co.broadwave.aci.devicestats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-10
 * Time :
 * Remark : Devicestatus Repository
 */
public interface DevicestatusRepository extends JpaRepository<Devicestatus,String>, QuerydslPredicateExecutor<Devicestatus> {

}
