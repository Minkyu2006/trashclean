package kr.co.broadwave.aci.configs;

import kr.co.broadwave.aci.handler.LoginFailureHandler;
import kr.co.broadwave.aci.handler.LoginSuccessHandler;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/signup").hasRole("ADMIN")
                .antMatchers("/dashboard/**").hasAnyRole("ADMIN","USER","COLLECTOR","SUBADMIN")
                .antMatchers("/devicestats/**").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/collection/**").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/collections/mobileindex").hasRole("COLLECTOR")
                .antMatchers("/collections/collectionlist").hasRole("COLLECTOR")
                .antMatchers("/collections/collectionprocess").hasRole("COLLECTOR")
                .antMatchers("/testpage/**").hasAnyRole("ADMIN")
                .antMatchers("/","/assets/**","/login","/logout","/favicon.ico").permitAll()
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
                ;

    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler("/loginsuccess");
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new LoginFailureHandler();
    }


}
