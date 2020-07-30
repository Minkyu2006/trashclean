package kr.co.broadwave.aci.devicestats.errweight;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private final ErrweightRepositoryCustom errweightRepositoryCustom;
    private final ModelMapper modelMapper;

    @Autowired
    public ErrweightService(ErrweightRepository errweightRepository,
                            ErrweightRepositoryCustom errweightRepositoryCustom,
                            ModelMapper modelMapper) {
        this.errweightRepository = errweightRepository;
        this.errweightRepositoryCustom = errweightRepositoryCustom;
        this.modelMapper = modelMapper;
    }

    public Errweight save(Errweight errweight) {
        return errweightRepository.save(errweight);
    }

    public Optional<Errweight> findById(Long i) {
        return errweightRepository.findById(i);
    }


    public ErrweightMapperDto findById2(Long id) {
        Optional<Errweight> optionalSubInfo = errweightRepository.findById(id);
        return optionalSubInfo.map(subInfo -> modelMapper.map(subInfo, ErrweightMapperDto.class)).orElse(null);
    }

    public List<ErrweightDataDto> findByErrweighttDataListQuerydsl(ErrweightMapperDto errweightMapperDto,String fromVal, String toVal) {
        return errweightRepositoryCustom.findByErrweighttDataListQuerydsl(errweightMapperDto,fromVal,toVal);
    }
}
