package kr.co.broadwave.aci.devicestats.frimware;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author Minkyu
 * Date : 2020-03-12
 * Time :
 * Remark :  Firmware Repository
 */
public interface FirmwareRepository extends JpaRepository<kr.co.broadwave.aci.devicestats.frimware.Firmware,Long>, QuerydslPredicateExecutor<Firmware> {

}
