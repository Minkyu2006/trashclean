package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.bscodes.DivisionType;
import kr.co.broadwave.aci.bscodes.EmType;
import kr.co.broadwave.aci.bscodes.NowStateType;
import kr.co.broadwave.aci.bscodes.RegionalType;
import kr.co.broadwave.aci.company.CompanyListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 09:17
 * Remark : EquipmentRepositoryCystom
 */
public interface EquipmentRepositoryCystom {
    Page<EquipmentListDto> findByEquipmentSearch(String emNumber, String emDesignation, EmType emTypes, NowStateType nowStateType, Pageable pageable);
}
