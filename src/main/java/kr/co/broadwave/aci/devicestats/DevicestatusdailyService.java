package kr.co.broadwave.aci.devicestats;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-10
 * Time :
 * Remark : Devicestatusdaily Service
 */
@Slf4j
@Service
public class DevicestatusdailyService {
    private final ModelMapper modelMapper;
    private final DevicestatusdailyRepository devicestatusdailyRepository;
    @Autowired
    public DevicestatusdailyService(ModelMapper modelMapper,
    DevicestatusdailyRepository devicestatusdailyRepository) {
        this.modelMapper = modelMapper;
        this.devicestatusdailyRepository = devicestatusdailyRepository;
    }

}
