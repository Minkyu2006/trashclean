package kr.co.broadwave.aci.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:56
 * Remark : Equipment Repository
 */
public interface EquipmentRepository extends JpaRepository<Equipment,Long>, QuerydslPredicateExecutor<Equipment> {

    Optional<Equipment> findByEmNumber(String emNumber);

}
