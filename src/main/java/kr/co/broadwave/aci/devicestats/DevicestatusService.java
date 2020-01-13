package kr.co.broadwave.aci.devicestats;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.company.CompanyDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-10
 * Time :
 * Remark : DevicestatusService
 */
@Slf4j
@Service
public class DevicestatusService {
    private final ModelMapper modelMapper;
    private final DevicestatusRepository devicestatusRepository;
    @Autowired
    public DevicestatusService(ModelMapper modelMapper,
                               DevicestatusRepository devicestatusRepository) {
        this.modelMapper = modelMapper;
        this.devicestatusRepository = devicestatusRepository;
    }

    public DevicestatsDto findById(String yyyymmddDeviceid) {
        Optional<Devicestatus> optionalDevicestatus = devicestatusRepository.findById(yyyymmddDeviceid);
        if (optionalDevicestatus.isPresent()) {
            return modelMapper.map(optionalDevicestatus.get(), DevicestatsDto.class);
        } else {
            return null;
        }
    }

}
