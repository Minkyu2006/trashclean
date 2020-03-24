package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.mastercode.MasterCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time :
 * Remark : IModelRepositoryCustom
 */
public interface IModelRepositoryCustom {
    Page<IModelListDto> findByIModelSearch(String mdName,Long emTypeId,Long mdTypeId, String mdRemark,Pageable pageable);

    List<IModelChangeDto> findByEmTypeQuerydsl(MasterCode masterCode);
}
