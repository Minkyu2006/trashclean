package kr.co.broadwave.aci.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author InSeok
 * Date : 2019-04-03
 * Time : 11:09
 * Remark : 로그인성공시 처리하는 페이지
 * 20190404 최인석 -> 사용하지않는 클래스임
 */

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {



    public LoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session != null) {
            String redirectUrl = (String) session.getAttribute("prevPage");
            if (redirectUrl != null) {
                super.onAuthenticationSuccess(request, response, authentication);
//                session.removeAttribute("prevPage");
//                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            }
            else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } else {

            super.onAuthenticationSuccess(request, response, authentication);
        }


    }
}
