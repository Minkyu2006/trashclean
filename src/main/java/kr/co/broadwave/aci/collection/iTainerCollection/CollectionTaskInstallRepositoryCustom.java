package kr.co.broadwave.aci.collection.iTainerCollection;

import kr.co.broadwave.aci.bscodes.AccordiType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-06-02
 * Time :
 * Remark : CollectionTaskInstallRepositoryCustom
 */
public interface CollectionTaskInstallRepositoryCustom {

    Page<CollectionTaskInstallListDto> findByCollectionTaskInstallSearch(AccordiType ciTypes, Long ciPriorityId, String ciCode, String psZoneCode, String deviceid, Pageable pageable);

    List<CollectionTaskInstallDto> findByCollectionTaskInstallInfo(String ciCode);
}
