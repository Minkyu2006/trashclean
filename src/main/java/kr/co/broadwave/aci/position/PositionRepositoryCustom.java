package kr.co.broadwave.aci.position;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark :
 */
public interface PositionRepositoryCustom {

    Page<PositionListDto> findByPositionSearch(String psBaseCode, String psBaseName, Long psLocationId, Long psCountryId, Pageable pageable);

    Page<PositionPopListDto> findByPositionPopSearch(Long psCountryId, Long psLocationId, String deviceid,String division, Pageable pageable);
}
