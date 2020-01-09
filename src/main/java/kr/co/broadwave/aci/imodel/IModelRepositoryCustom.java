package kr.co.broadwave.aci.imodel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time :
 * Remark : IModelRepositoryCustom
 */
public interface IModelRepositoryCustom {
    Page<IModelListDto> findByIModelSearch(String mdName,Long emTypeId,Long mdTypeId, String mdRemark,Pageable pageable);

}
