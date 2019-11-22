package kr.co.broadwave.aci.equipment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 09:17
 * Remark : EquipmentRepositoryCystom
 */
public interface EquipmentRepositoryCustom {
    Page<EquipmentListDto> findByEquipmentSearch
            (String emNumber, String emDesignation, Long emTypeId, Long emCountryId, Pageable pageable);
}
