package kr.co.broadwave.aci.devicestats;

import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-03-12
 * Time :
 * Remark :  Firmware Repository
 */
public interface FirmwareRepository extends JpaRepository<Firmware,Long>, QuerydslPredicateExecutor<Firmware> {

}
