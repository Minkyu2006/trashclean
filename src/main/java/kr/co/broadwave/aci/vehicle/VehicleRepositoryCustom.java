package kr.co.broadwave.aci.vehicle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Time :
 * Remark : VehicleRepositoryCustom
 */
public interface VehicleRepositoryCustom {

    Page<VehicleListDto> findByVehicleSearch(String vcNumber, String vcName, String vcShape, String vcUsage,Pageable pageable);
}
