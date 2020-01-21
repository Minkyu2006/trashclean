package kr.co.broadwave.aci.vehicle;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.company.QCompany;
import kr.co.broadwave.aci.mastercode.QMasterCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Remark :
 */
@Repository
public class VehicleRepositoryCustomImpl extends QuerydslRepositorySupport implements VehicleRepositoryCustom{

    public VehicleRepositoryCustomImpl() {
        super(Vehicle.class);
    }

    @Override
    public Page<VehicleListDto> findByVehicleSearch
            (String vcNumber, String vcName, Long vcShapeId, Long vcUsageId,Long vcStateId, Pageable pageable){

        QVehicle vehicle = QVehicle.vehicle;
        QCompany company = QCompany.company;
        QMasterCode masterCode = QMasterCode.masterCode;

        JPQLQuery<VehicleListDto> query = from(vehicle)
                .innerJoin(vehicle.vcShape,masterCode)
                .innerJoin(vehicle.vcUsage,masterCode)
                .innerJoin(vehicle.vcState,masterCode)
                .innerJoin(vehicle.company,company)
                .select(Projections.constructor(VehicleListDto.class,
                        vehicle.id,
                        vehicle.vcNumber,
                        vehicle.vcName,
                        vehicle.vcShape.name,
                        vehicle.vcUsage.name,
                        vehicle.vcState.name,
                        vehicle.vcStartDate,
                        vehicle.vcEndDate,
                        vehicle.vcManagement,
                        company.csOperator
                ));


        // 검색조건필터
        if (vcNumber != null && !vcNumber.isEmpty()){
            query.where(vehicle.vcNumber.likeIgnoreCase(vcNumber.concat("%")));
        }
        if (vcName != null && !vcName.isEmpty()){
            query.where(vehicle.vcName.containsIgnoreCase(vcName));
        }
        if (vcShapeId != null ){
            query.where(vehicle.vcShape.id.eq(vcShapeId));
        }
        if (vcUsageId != null ){
            query.where(vehicle.vcUsage.id.eq(vcUsageId));
        }
        if (vcStateId != null ){
            query.where(vehicle.vcState.id.eq(vcStateId));
        }

        query.orderBy(vehicle.id.desc());

        final List<VehicleListDto> equipments = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(equipments, pageable, query.fetchCount());
    }

}
