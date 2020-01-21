package kr.co.broadwave.aci.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Time :
 * Remark : Vehicle Repository
 */
public interface VehicleRepository extends JpaRepository<Vehicle,Long>, QuerydslPredicateExecutor<Vehicle> {

    Optional<Vehicle> findByVcNumber(String vcNumber);

}
