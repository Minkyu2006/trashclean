package kr.co.broadwave.aci.handler;

import kr.co.broadwave.aci.accounts.Loginlog;
import kr.co.broadwave.aci.accounts.LoginlogService;
import kr.co.broadwave.aci.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author InSeok
 * Date : 2019-04-22
 * Time : 15:11
 * Remark :
 */
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    LoginlogService loginlogService;



    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {

        String login_ip = CommonUtils.getIp(req);

        log.info("로그인 실패 : '" + req.getParameter("username") +"' IP:'" + login_ip + "'");


        Loginlog loginlog = Loginlog.builder()
                .userid(req.getParameter("username"))
                .successYN("N")
                .loginIp(login_ip)
                .build();
        loginlogService.save(loginlog);

        req.getRequestDispatcher("/login?error=true").forward(req, res);


    }


}
