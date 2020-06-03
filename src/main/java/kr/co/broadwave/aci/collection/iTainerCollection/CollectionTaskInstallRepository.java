package kr.co.broadwave.aci.collection.iTainerCollection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-06-02
 * Time :
 * Remark : CollectionTaskInstallRepository
 */
public interface CollectionTaskInstallRepository extends JpaRepository<CollectionTaskInstall,Long>, QuerydslPredicateExecutor<CollectionTaskInstall>{

    @Query("select a from CollectionTaskInstall a join fetch a.accountId join fetch a.vehicleId join fetch a.ciPriority where a.id = :id")
    Optional<CollectionTaskInstall> findByCollectionTaskInstallInfo(Long id);

    Optional<CollectionTaskInstall> findByCiCode(String ciCode);

//    @Query("select a from Position a join fetch a.psCountry join fetch a.psLocation where a.id = :id")
//    Optional<Position> findByPositionInfo(Long id);

}
