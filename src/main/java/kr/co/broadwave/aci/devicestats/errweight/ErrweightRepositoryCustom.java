package kr.co.broadwave.aci.devicestats.errweight;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2020-03-13
 * Remark :
 */
public interface ErrweightRepositoryCustom {
    List<ErrweightDataDto> findByErrweighttDataListQuerydsl(ErrweightMapperDto errweightMapperDto,String fromVal, String toVal);
}