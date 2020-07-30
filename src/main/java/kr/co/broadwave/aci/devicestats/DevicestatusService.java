package kr.co.broadwave.aci.devicestats;

import kr.co.broadwave.aci.devicestats.frimware.Firmware;
import kr.co.broadwave.aci.devicestats.frimware.FirmwareFileListDto;
import kr.co.broadwave.aci.devicestats.frimware.FirmwareRepository;
import kr.co.broadwave.aci.devicestats.frimware.FirmwareRepositoryCustom;
import kr.co.broadwave.aci.devicestats.payment.PaymentListDto;
import kr.co.broadwave.aci.devicestats.payment.PaymentRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-10
 * Time :
 * Remark : Devicestatus Service
 */
@Slf4j
@Service
public class DevicestatusService {

    private final DevicestatsRepositoryCustom devicestatsRepositoryCustom;
    private final FirmwareRepository firmwareRepository;
    private final FirmwareRepositoryCustom firmwareRepositoryCustom;
    private final PaymentRepositoryCustom paymentRepositoryCustom;

    @Autowired
    public DevicestatusService(DevicestatsRepositoryCustom devicestatsRepositoryCustom,
                               FirmwareRepository firmwareRepository,
                               FirmwareRepositoryCustom firmwareRepositoryCustom,
                               PaymentRepositoryCustom paymentRepositoryCustom) {
        this.devicestatsRepositoryCustom = devicestatsRepositoryCustom;
        this.firmwareRepositoryCustom = firmwareRepositoryCustom;
        this.paymentRepositoryCustom = paymentRepositoryCustom;
        this.firmwareRepository = firmwareRepository;
    }

    public List<DevicestatsDto> queryDslDevicestatsAvgQuerydsl(List<String> deviceid,String yyyymmdd1,String yyyymmdd2) {
        return devicestatsRepositoryCustom.queryDslDevicestatsAvgQuerydsl(deviceid,yyyymmdd1,yyyymmdd2);
    }

    public List<DevicestatsDailyDto> queryDslDeviceDaily(List<String> deviceid,String sMonth) {
        return devicestatsRepositoryCustom.queryDslDeviceDaily(deviceid,sMonth);
    }

    public List<DevicestatsDailyHourLevelDto> queryDslDeviceDailyHourLevel(List<String> deviceid, String sendDate) {
        return devicestatsRepositoryCustom.queryDslDeviceDailyHourLevel(deviceid,sendDate);
    }

    public List<DevicestatsDailyMonthDto> queryDslDeviceDailyMonth(String deviceid,String deviceMonth){
        return devicestatsRepositoryCustom.queryDslDeviceDailyMonth(deviceid,deviceMonth);
    }

    public Firmware save(Firmware firmware) {
        return firmwareRepository.save(firmware);
    }

    public Page<FirmwareFileListDto> findByFirmwareListQuerydsl(Pageable pageable) {
        return firmwareRepositoryCustom.findByFirmwareListQuerydsl(pageable);
    }

    public Optional<Firmware> findById(Long id) {
        return firmwareRepository.findById(id);
    }

    public void firmwareDelete(Firmware firmware) {
        firmwareRepository.delete(firmware);
    }

    public List<PaymentListDto> findByPaymentListQuerydsl(String fromVal, String toVal, String deviceid, String basename) {
        return paymentRepositoryCustom.findByPaymentListQuerydsl(fromVal,toVal,deviceid,basename);
    }
}
