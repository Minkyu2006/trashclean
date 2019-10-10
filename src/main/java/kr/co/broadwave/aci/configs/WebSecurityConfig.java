package kr.co.broadwave.aci.configs;


import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.handler.LoginFailureHandler;
import kr.co.broadwave.aci.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * @author InSeok
 * Date : 2019-04-01
 * Time : 13:30
 * Remark : Security 관련 config
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/record/list").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/record/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/mypage").hasAnyRole("ADMIN","USER")
                .antMatchers("/","/assets/**","/login","/logout","/testwebpage","/favicon.ico").permitAll()
                .anyRequest().permitAll()

                .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(successHandler())
                    .failureHandler(failureHandler())
                    .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                //.and()
                //.csrf().disable()
                ;



    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //super.configure(web);
//        web.ignoring().antMatchers(HttpMethod.POST, "/api/**");
//    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler("/loginsuccess");
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new LoginFailureHandler();
    }


}
