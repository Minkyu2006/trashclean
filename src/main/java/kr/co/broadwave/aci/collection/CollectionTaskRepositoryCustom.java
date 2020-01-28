package kr.co.broadwave.aci.collection;

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
}
