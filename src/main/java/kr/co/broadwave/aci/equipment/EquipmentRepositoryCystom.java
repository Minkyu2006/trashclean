package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.bscodes.*;
import kr.co.broadwave.aci.company.CompanyListDto;
import kr.co.broadwave.aci.mastercode.MasterCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 09:17
 * Remark : EquipmentRepositoryCystom
 */
public interface EquipmentRepositoryCystom {
    Page<EquipmentListDto> findByEquipmentSearch(String emNumber, String emDesignation, Pageable pageable);
}
