package kr.co.broadwave.aci.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author Minkyu
 * Date : 2020-01-23
 * Time :
 * Remark : CollectionTaskRepository
 */
public interface CollectionTaskRepository extends JpaRepository<CollectionTask,Long>, QuerydslPredicateExecutor<CollectionTask>{
}
