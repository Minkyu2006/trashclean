package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.equipment.EquipmentDto;
import kr.co.broadwave.aci.equipment.EquipmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-10-21
 * Remark :
 */
@Service
@Slf4j
public class DashboardService {

    private final ModelMapper modelMapper;
    private final ACIAWSLambdaService aciawsLambdaService;
    private final DashboardRepositoryCustom dashboardRepositoryCustom;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public DashboardService(ModelMapper modelMapper,
                            EquipmentRepository equipmentRepository,
                            DashboardRepositoryCustom dashboardRepositoryCustom,
                            ACIAWSLambdaService aciawsLambdaService) {
        this.modelMapper = modelMapper;
        this.dashboardRepositoryCustom = dashboardRepositoryCustom;
        this.aciawsLambdaService = aciawsLambdaService;
        this.equipmentRepository = equipmentRepository;
    }

    //장비목록 가져오기(Dynamodb)
    public HashMap getDeviceList(String deviceType){
        return aciawsLambdaService.getDeviceList(deviceType);
    }

    //요청한장비의 마지막 상태 가져오기(Dynamodb)
    public HashMap getDeviceLastestState(String jsonDeviceList){
        return aciawsLambdaService.getDeviceLastestState(jsonDeviceList);
    }

    //특정장비의 history 가져오기(Dynamodb)
    public HashMap getDeviceHistory(String deviceid,String intervaltime){
        return aciawsLambdaService.getDeviceHistory(deviceid,intervaltime);
    }

    public List<DashboardDeviceListViewDto> findByDashboardListView
            (String emNumber, Long emTypeId,Long emCountryId,Long emLocationId, Pageable pageable) {
        return dashboardRepositoryCustom.findByDashboardListView(emNumber,emTypeId,emCountryId,emLocationId,pageable);
    }

    public List<Equipment> findAll() {
        return equipmentRepository.findAll();
    }

    public DashboardDeviceListViewDto findByDashboardListViewDeviceInfo(String emNumber) {
        return dashboardRepositoryCustom.findByDashboardListViewDeviceInfo(emNumber);
    }

    public EquipmentDto findByEmNumber(String pushValue) {
        Optional<Equipment> optionalEquipment = equipmentRepository.findByEmNumber(pushValue);
        return optionalEquipment.map(equipment -> modelMapper.map(equipment, EquipmentDto.class)).orElse(null);
//        if (optionalEquipment.isPresent()) {
//            return modelMapper.map(optionalEquipment.get(), EquipmentDto.class);
//        } else {
//            return null;
//        }
    }
}
