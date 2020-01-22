package kr.co.broadwave.aci.vehicle;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private final VehicleRepositoryCustom vehicleRepositoryCustom;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository,
                          VehicleRepositoryCustom vehicleRepositoryCustom,
                          ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.vehicleRepositoryCustom = vehicleRepositoryCustom;
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> findById2(Long Id) {
        return vehicleRepository.findById(Id);
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

    public Page<VehicleListDto> findByVehicleSearch(String vcNumber, String vcName, Long vcShapeId, Long vcUsageId, Long vcStateId, Pageable pageable) {
        return vehicleRepositoryCustom.findByVehicleSearch(vcNumber,vcName,vcShapeId,vcUsageId,vcStateId,pageable);
    }

    public Optional<Vehicle> findByVcNumber(String vcNumber) {
        return vehicleRepository.findByVcNumber(vcNumber);
    }
}
