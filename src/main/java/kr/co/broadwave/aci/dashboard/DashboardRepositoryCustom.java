package kr.co.broadwave.aci.dashboard;

import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2019-11-22
 * Remark :
 */
public interface DashboardRepositoryCustom {
    List<DashboardDeviceListViewDto> findByDashboardListView(String emNumber, Long emTypeId, Long emCountryId, Long emLocationId, Pageable pageable);

    DashboardDeviceListViewDto findByDashboardListViewDeviceInfo(String emNumber);
}
