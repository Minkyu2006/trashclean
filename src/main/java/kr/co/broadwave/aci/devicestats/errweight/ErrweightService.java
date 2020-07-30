package kr.co.broadwave.aci.devicestats.errweight;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-07-30
 * Time :
 * Remark : ErrweightService Service
 */
@Slf4j
@Service
public class ErrweightService {

    private final ErrweightRepository errweightRepository;

    @Autowired
    public ErrweightService(ErrweightRepository errweightRepository) {
        this.errweightRepository = errweightRepository;
    }

    public Errweight save(Errweight errweight) {
        return errweightRepository.save(errweight);
    }

    public Optional<Errweight> findById(Long i) {
        return errweightRepository.findById(i);
    }
}
