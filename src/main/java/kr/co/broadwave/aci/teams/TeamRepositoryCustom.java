package kr.co.broadwave.aci.teams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author InSeok
 * Date : 2019-04-02
 * Time : 14:38
 * Remark : QueryDSL용 인터페이스
 */
public interface TeamRepositoryCustom {
    Page<TeamDto> findAllBySearchStrings(String teamcode, String teamname, Pageable pageable);
    List<TeamDto> findAllBySearchStringsExcel(String teamcode, String teamname);
}
