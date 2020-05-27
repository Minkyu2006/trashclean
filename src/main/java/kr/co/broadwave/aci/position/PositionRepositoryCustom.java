package kr.co.broadwave.aci.position;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark :
 */
public interface PositionRepositoryCustom {

    Page<PositionListDto> findByPositionSearch(String psZoneCode, String psZoneName, Long psLocationId, Long psCountryId, Pageable pageable);

}
