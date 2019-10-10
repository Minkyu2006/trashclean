package kr.co.broadwave.aci.teams;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author InSeok
 * Date : 2019-04-02
 * Time : 14:39
 * Remark : Team queryDSL 용 구현
 */
@Repository
public class TeamRepositoryCustomImpl extends QuerydslRepositorySupport implements TeamRepositoryCustom {

    public TeamRepositoryCustomImpl() {
        super(Team.class);
    }



    @Override
    public Page<TeamDto> findAllBySearchStrings(String teamcode,String teamname, Pageable pageable) {

        JPQLQuery<TeamDto> query = findAllBySearchQuerydsl(teamcode, teamname);

        final List<TeamDto> teams = getQuerydsl().applyPagination(pageable,query).fetch();
        return new PageImpl<>(teams,pageable,query.fetchCount());

    }

    @Override
    public List<TeamDto> findAllBySearchStringsExcel(String teamcode,String teamname) {
        JPQLQuery<TeamDto> query = findAllBySearchQuerydsl(teamcode, teamname);

        return query.fetch();

    }

    private JPQLQuery<TeamDto> findAllBySearchQuerydsl(String teamcode,String teamname ){
        QTeam team = QTeam.team;
        JPQLQuery<TeamDto> query = from(team)
                .select(Projections.constructor(TeamDto.class,
                        team.id,
                        team.teamcode,
                        team.teamname,
                        team.remark
                ));
        if (teamname != null && !teamname.isEmpty()){
            query.where(team.teamname.containsIgnoreCase(teamname));
        }
        if (teamcode != null && !teamcode.isEmpty()){
            query.where(team.teamcode.likeIgnoreCase(teamcode.concat("%")));
        }
        return query;
    }


}
