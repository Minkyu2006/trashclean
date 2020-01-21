package kr.co.broadwave.aci.vehicle;

import kr.co.broadwave.aci.company.CompanyListDto;
import kr.co.broadwave.aci.company.CompanyRepositoryCustom;
import kr.co.broadwave.aci.equipment.*;
import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Time :
 * Remark : Vehicle Service
 */
@Slf4j
@Service
public class VehicleService {
    private final ModelMapper modelMapper;
    private final VehicleRepository vehicleRepository;
    private final KeyGenerateService keyGenerateService;
    private final VehicleRepositoryCustom vehicleRepositoryCustom;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository,
                          KeyGenerateService keyGenerateService,
                          VehicleRepositoryCustom vehicleRepositoryCustom,
                          EquipmentRepositoryCustom equipmentRepositoryCustom,
                          ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.vehicleRepositoryCustom = vehicleRepositoryCustom;
        this.vehicleRepository = vehicleRepository;
        this.keyGenerateService = keyGenerateService;
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> findById2(Long Id) {
        return vehicleRepository.findById(Id);
    }

    public Page<VehicleListDto> findByVehicleSearch(String vcNumber, String vcName, String vcShape, String vcUsage, Pageable pageable) {
        return vehicleRepositoryCustom.findByVehicleSearch(vcNumber,vcName,vcShape,vcUsage,pageable);
    }

    public VehicleDto findById(Long id) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        if (vehicleOptional.isPresent()) {
            return modelMapper.map(vehicleOptional.get(), VehicleDto.class);
        } else {
            return null;
        }
    }

    public void delete(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

}
