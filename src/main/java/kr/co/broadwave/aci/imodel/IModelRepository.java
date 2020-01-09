package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.mastercode.MasterCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time : 11:53
 * Remark : IModel Repository
 */
public interface IModelRepository extends JpaRepository<IModel,Long>, QuerydslPredicateExecutor<IModel> {

    Optional<IModel> findByMdNumber(String mdNumber);

    List<IModel> findByEmType(MasterCode id);

}
