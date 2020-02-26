package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ProcStatsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-01-28
 * Time :
 * Remark : CollectionRepositoryCystom
 */
public interface CollectionTaskRepositoryCustom {

    Page<CollectionListDto> findByCollectionList(String ctCode, String dateFrom, String dateTo, Long emTypeId, String userName, String vehicleNumber, Pageable pageable);

    List<CollectionInfoDto> findByCollectionInfoQueryDsl(String ctCode);

    Page<CollectionTaskListDateDto> findByCollectionsTaskDateList(String currentuserid, AccountRole role, ProcStatsType procStatsType, Pageable pageable);

    List<CollectionTaskListDeviceDto> findByCollectionsTaskDeviceList(String ctCode,String currentuserid, AccountRole role);

    CollectionTaskListDto findByCollectionsTaskInfoList(Long id);

    CollectionTaskListInfoDto findByCollectionListInfoQueryDsl(Long id);

    List<CollectionDto> findByCtCodeSeqQuerydsl(String ctCode);

}
