package kr.co.broadwave.aci.imodel;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.equipment.EquipmentCollectionRegDto;
import kr.co.broadwave.aci.equipment.QEquipment;
import kr.co.broadwave.aci.files.QFileUpload;
import kr.co.broadwave.aci.mastercode.QMasterCode;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Repository
public class IModelRepositoryCustomImpl extends QuerydslRepositorySupport implements IModelRepositoryCustom{

    public IModelRepositoryCustomImpl() {
        super(Company.class);
    }

    @Override
    public Page<IModelListDto> findByIModelSearch(String mdName,Long emTypeId,Long mdTypeId,String mdRemark,Pageable pageable){

        QIModel iModel = QIModel.iModel;
        QFileUpload fileUpload = QFileUpload.fileUpload;

        JPQLQuery<IModelListDto> query = from(iModel)
                .leftJoin(iModel.mdFileid,fileUpload)
                .select(Projections.constructor(IModelListDto.class,
                        iModel.id,
                        fileUpload.filePath,
                        fileUpload.saveFileName,
                        iModel.mdNumber,
                        iModel.mdName,
                        iModel.emType,
                        iModel.mdType,
                        iModel.mdSubname,
                        iModel.mdRemark,
                        iModel.mdMaximumPayload,
                        iModel.mdUnit
                ));

        // 검색조건필터
        if (mdName != null && !mdName.isEmpty()){
            query.where(iModel.mdName.likeIgnoreCase(mdName.concat("%")));
        }

        if (emTypeId != null ){
            query.where(iModel.emType.id.eq(emTypeId));
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
