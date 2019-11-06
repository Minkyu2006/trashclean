package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.bscodes.DivisionType;
import kr.co.broadwave.aci.bscodes.RegionalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 09:17
 * Remark : CompanyRepositoryCystom
 */
public interface CompanyRepositoryCystom{

    Page<CompanyListDto> findByCompanySearch(String csNumber, String csOperator, Long csDivisionType, Long csRegionalType, Pageable pageable);

    Page<CompanyListDto> findByAgencySearch(String csNumber, String csOperator, Pageable pageable);
}
