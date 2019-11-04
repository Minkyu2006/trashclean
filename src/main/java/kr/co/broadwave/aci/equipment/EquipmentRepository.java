package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:56
 * Remark : Equipment Repository
 */
public interface EquipmentRepository extends JpaRepository<Equipment,Long>, QuerydslPredicateExecutor<Equipment> {
    Optional<Equipment> findByEmNumber(String emNumber);

//    @Query("select a from Equipment a join fetch a.emType where a.equipment = :equipment")
//    Equipment save(Equipment equipment);
}
