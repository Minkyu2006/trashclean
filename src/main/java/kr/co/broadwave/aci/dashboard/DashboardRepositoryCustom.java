package kr.co.broadwave.aci.dashboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2019-11-22
 * Remark :
 */
public interface DashboardRepositoryCustom {
    Page<DashboardDeviceListViewDto> findByDashboardListView(Pageable pageable);
}
