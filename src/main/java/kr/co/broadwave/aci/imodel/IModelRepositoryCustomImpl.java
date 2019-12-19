package kr.co.broadwave.aci.imodel;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.equipment.EquipmentListDto;
import kr.co.broadwave.aci.equipment.EquipmentRepositoryCustom;
import kr.co.broadwave.aci.equipment.QEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Remark :
 */
@Repository
public class IModelRepositoryCustomImpl extends QuerydslRepositorySupport implements IModelRepositoryCustom{

    public IModelRepositoryCustomImpl() {
        super(Company.class);
    }

    @Override
    public Page<IModelListDto> findByIModelSearch(String mdName, Long mdTypeId, String mdRemark,Pageable pageable){

        QIModel iModel = QIModel.iModel;

        JPQLQuery<IModelListDto> query = from(iModel)
                .select(Projections.constructor(IModelListDto.class,
                        iModel.id,
                        iModel.mdNumber,
                        iModel.mdName,
                        iModel.mdType,
                        iModel.mdSubname,
                        iModel.mdRemark
                ));

        // 검색조건필터
        if (mdName != null && !mdName.isEmpty()){
            query.where(iModel.mdName.likeIgnoreCase(mdName.concat("%")));
        }

        if (mdTypeId != null ){
            query.where(iModel.mdType.id.eq(mdTypeId));
        }

        if (mdRemark != null && !mdRemark.isEmpty()){
            query.where(iModel.mdRemark.containsIgnoreCase(mdRemark));
        }

        query.orderBy(iModel.id.desc());

        final List<IModelListDto> iModels = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(iModels, pageable, query.fetchCount());
    }
}
