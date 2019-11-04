package kr.co.broadwave.aci.keygenerate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

/**
 * @author Minkyu
 * Date : 2019-09-16
 * Remark : 키값을 생성해주는 서비스
 */
@Service
@Slf4j
public class KeyGenerateService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public String keyGenerate(String autokey,String userid){
        StoredProcedureQuery proc = em.createStoredProcedureQuery("proc_autonum");


        proc.registerStoredProcedureParameter("autokey", String.class, ParameterMode.IN);
        proc.registerStoredProcedureParameter("userid", String.class, ParameterMode.IN);
        proc.registerStoredProcedureParameter("keycode", String.class, ParameterMode.OUT);

        proc.setParameter("autokey",autokey);
        proc.setParameter("userid",userid);
        proc.execute();

        String keycode = (String) proc.getOutputParameterValue("keycode");
        log.info("키생성호출 autokey = '" + autokey + "' userid ='" + userid + " 생성된키 : +" + keycode + "'");
        return keycode;
    }
}
