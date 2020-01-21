package kr.co.broadwave.aci.vehicle;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.company.QCompany;
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
            (String vcNumber, String vcName, String vcShape,String vcUsage, Pageable pageable){

        QVehicle vehicle = QVehicle.vehicle;
        QCompany company = QCompany.company;

        JPQLQuery<VehicleListDto> query = from(vehicle)
                .innerJoin(vehicle.company,company)
                .select(Projections.constructor(VehicleListDto.class,
                        vehicle.id,
                        vehicle.vcNumber,
                        vehicle.vcName,
                        vehicle.vcShape,
                        vehicle.vcUsage,
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
        if (vcShape != null && !vcShape.isEmpty()){
            query.where(vehicle.vcShape.likeIgnoreCase(vcShape.concat("%")));
        }
        if (vcUsage != null && !vcUsage.isEmpty()){
            query.where(vehicle.vcUsage.containsIgnoreCase(vcUsage));
        }

        query.orderBy(vehicle.id.desc());

        final List<VehicleListDto> equipments = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(equipments, pageable, query.fetchCount());
    }

}
