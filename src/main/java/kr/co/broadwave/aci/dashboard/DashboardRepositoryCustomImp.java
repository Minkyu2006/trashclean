package kr.co.broadwave.aci.dashboard;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.company.QCompany;
import kr.co.broadwave.aci.equipment.QEquipment;
import kr.co.broadwave.aci.files.QFileUpload;
import kr.co.broadwave.aci.imodel.QIModel;
import kr.co.broadwave.aci.mastercode.QMasterCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2019-11-22
 * Remark :
 */
@Repository
public class DashboardRepositoryCustomImp  extends QuerydslRepositorySupport implements DashboardRepositoryCustom{

    public DashboardRepositoryCustomImp() {
        super(Company.class);
    }

    @Override
    public List<DashboardDeviceListViewDto> findByDashboardListView(String emNumber, Long emTypeId,Long emCountryId,Long emLocationId, Pageable pageable){

        QEquipment equipment = QEquipment.equipment;
        QIModel qiModel = QIModel.iModel;
        QFileUpload fileUpload = QFileUpload.fileUpload;
        QCompany company = QCompany.company;

        JPQLQuery<DashboardDeviceListViewDto> query = from(equipment)
                .innerJoin(equipment.mdId,qiModel)
                .innerJoin(equipment.company,company)
                .leftJoin(equipment.mdId.mdFileid,fileUpload)
                .select(Projections.constructor(DashboardDeviceListViewDto.class,
                        equipment.id,
                        equipment.emNumber,
                        equipment.emType,
                        qiModel.mdName,
                        company.csOperator,
                        equipment.emLocation,
                        equipment.emCountry,
                        equipment.emInstallDate,
                        equipment.emSubName,
                        fileUpload.filePath,
                        fileUpload.saveFileName
                ));

        // 검색조건필터
        if (emNumber != null && !emNumber.isEmpty()){
            query.where(equipment.emNumber.likeIgnoreCase(emNumber.concat("%")));
        }
        if (emTypeId != null ){
            query.where(equipment.emType.id.eq(emTypeId));
        }
        if (emCountryId != null ){
            query.where(equipment.emCountry.id.eq(emCountryId));
        }
        if (emLocationId != null ){
            query.where(equipment.emLocation.id.eq(emLocationId));
        }

        query.orderBy(equipment.emNumber.asc());

        return query.fetch();
    }

    @Override
    public DashboardDeviceListViewDto findByDashboardListViewDeviceInfo(String emNumber){

        QEquipment equipment = QEquipment.equipment;
        QIModel qiModel = QIModel.iModel;
        QFileUpload fileUpload = QFileUpload.fileUpload;
        QMasterCode masterCode = QMasterCode.masterCode;
        QCompany company = QCompany.company;

        JPQLQuery<DashboardDeviceListViewDto> query = from(equipment)
                .innerJoin(equipment.mdId,qiModel)
                .leftJoin(equipment.emState,masterCode)
                .innerJoin(equipment.company,company)
                .leftJoin(equipment.mdId.mdFileid,fileUpload)
                .select(Projections.constructor(DashboardDeviceListViewDto.class,
                        equipment.id,
                        equipment.emNumber,
                        equipment.emType,
                        qiModel.mdName,
                        company.csOperator,
                        equipment.emLocation,
                        equipment.emCountry,
                        equipment.emInstallDate,
                        equipment.emSubName,
                        fileUpload.filePath,
                        fileUpload.saveFileName
                ));

        // 검색조건필터
        if (emNumber != null){
            query.where(equipment.emNumber.eq(emNumber));
        }

        return query.fetchOne();
    }

}
