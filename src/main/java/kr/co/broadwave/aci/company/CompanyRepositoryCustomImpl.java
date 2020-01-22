package kr.co.broadwave.aci.company;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Remark :
 */
@Repository
public class CompanyRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyRepositoryCustom{

    public CompanyRepositoryCustomImpl() {
        super(Company.class);
    }

    @Override
    public Page<CompanyListDto> findByCompanySearch(String csNumber, String csOperator, Long csDivisionType, Long csRegionalType,
                                                    Pageable pageable){

        QCompany company = QCompany.company;

        JPQLQuery<CompanyListDto> query = from(company)
                .select(Projections.constructor(CompanyListDto.class,
                        company.id,
                        company.csNumber,
                        company.csOperator,
                        company.csDivision,
                        company.csRegional,
                        company.csRepresentative,
                        company.csBuisnessNumber,
                        company.csManager,
                        company.csTelephone,
                        company.csFax
                ));


        // 검색조건필터
        if (csNumber != null && !csNumber.isEmpty()){
            query.where(company.csNumber.likeIgnoreCase(csNumber.concat("%")));
        }
        if (csOperator != null && !csOperator.isEmpty()){
            query.where(company.csOperator.containsIgnoreCase(csOperator));
        }
        if (csDivisionType != null ){
            query.where(company.csDivision.id.eq(csDivisionType));
        }
        if (csRegionalType != null ){
            query.where(company.csRegional.id.eq(csRegionalType));
        }

        query.orderBy(company.id.desc());

        final List<CompanyListDto> companys = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(companys, pageable, query.fetchCount());
    }

    @Override
    public Page<CompanyListDto>  findByAgencySearch(String csNumber, String csOperator, Pageable pageable){

        QCompany company = QCompany.company;

        JPQLQuery<CompanyListDto> query = from(company)
                .select(Projections.constructor(CompanyListDto.class,
                        company.id,
                        company.csNumber,
                        company.csOperator,
                        company.csDivision,
                        company.csRegional,
                        company.csRepresentative,
                        company.csBuisnessNumber,
                        company.csManager,
                        company.csTelephone,
                        company.csFax
                ));


        // 검색조건필터
        if (csNumber != null && !csNumber.isEmpty()){
            query.where(company.csNumber.likeIgnoreCase(csNumber.concat("%")));
        }
        if (csOperator != null && !csOperator.isEmpty()){
            query.where(company.csOperator.containsIgnoreCase(csOperator));
        }
//        if (csDivisionType != null){
//            query.where(company.csDivision.eq(csDivisionType));
//        }

        query.orderBy(company.id.desc());

        final List<CompanyListDto> companys = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(companys, pageable, query.fetchCount());
    }

}
