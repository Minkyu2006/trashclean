package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2020-01-28
 * Time :
 * Remark : CollectionRepositoryCystom
 */
public interface CollectionTaskRepositoryCustom {

    Page<CollectionListDto> findByCollectionList(String ctCode, String dateFrom, String dateTo, Long emTypeId, String userName, String vehicleNumber, Pageable pageable);

    CollectionInfoDto findByCollectionInfoQueryDsl(Long id);

    Page<CollectionTaskListDto> findByCollectionsTaskList(String currentuserid, AccountRole role, ProcStatsType procStatsType, Pageable pageable);

    CollectionTaskListInfoDto findByCollectionListInfoQueryDsl(Long id);
}
