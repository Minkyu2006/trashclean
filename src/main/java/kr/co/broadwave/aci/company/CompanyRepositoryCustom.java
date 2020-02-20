package kr.co.broadwave.aci.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 09:17
 * Remark : CompanyRepositoryCystom
 */
public interface CompanyRepositoryCustom {

    Page<CompanyListDto> findByCompanySearch(String csNumber, String csOperator, Long csDivisionType, Long csRegionalType, Pageable pageable);

    Page<CompanyListDto> findByAgencySearch(String csNumber, String csOperator, Pageable pageable);

    List<CompanyAccountDto> findCompanyList();
}
