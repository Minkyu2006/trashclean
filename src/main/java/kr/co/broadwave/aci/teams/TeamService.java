package kr.co.broadwave.aci.teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-03-26
 * Time : 10:23
 * Remark :
 */
@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamRepositoryCustom teamRepositoryCustom;

    public Team tesmSave(Team team){
        return this.teamRepository.save(team);
    }

    public Optional<Team> findById(Long id){
        return teamRepository.findById(id);
    }

    public Optional<Team> findByTeamcode(String teamcode){
        return teamRepository.findByTeamcode(teamcode);
    }

    public List<Team> findAll() {
        return this.teamRepository.findAll();
    }

    public Page<TeamDto> findAllBySearchStrings(String teamcode,String teamname, Pageable pageable){
        return teamRepositoryCustom.findAllBySearchStrings(teamcode,teamname,pageable);
    }
    public void delete(Team team){
        teamRepository.delete(team);
    }

    public List<TeamDto> findAllBySearchStringsExcel(String teamcode,String teamname) {
        return teamRepositoryCustom.findAllBySearchStringsExcel(teamcode,teamname);
    }
    public List<TeamDto> findTeamList(){
        return teamRepositoryCustom.findAllBySearchStringsExcel("","");
    }

}
