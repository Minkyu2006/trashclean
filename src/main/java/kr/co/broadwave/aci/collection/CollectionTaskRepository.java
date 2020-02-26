package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-23
 * Time :
 * Remark : CollectionTaskRepository
 */
public interface CollectionTaskRepository extends JpaRepository<CollectionTask,Long>, QuerydslPredicateExecutor<CollectionTask> {
    Optional<CollectionTask> findByCtCode(String ctCode);
}
