package com.tinawu.springSecuritybase.config;

import com.google.gson.Gson;
import com.tinawu.springSecuritybase.dto.CreateUserInputDTO;
import com.tinawu.springSecuritybase.dto.LoginDTO;
import com.tinawu.springSecuritybase.dto.LoginResultDTO;
import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.filter.AuthorizationTokenFilter;
import com.tinawu.springSecuritybase.service.AuthorizationService;
import com.tinawu.springSecuritybase.service.CustomOAuth2UserService;
import com.tinawu.springSecuritybase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import sun.tools.jar.resources.jar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SelfAuthenticationProvider  selfAuthenticationProvider;

    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationSuccess authenticationSuccess;    //登录成功
    @Autowired
    LogoutHandler logoutHandler;
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private AuthorizationTokenFilter authenticationTokenFilter;

    @Value("${permitAll.url}")
    private String[] permitAllUrl;


    //用來配置全局的驗證資訊，也就是AuthenticationProvider 和UserDetailsService。
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(selfAuthenticationProvider);
    }


    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    //來修改/自訂Spring Security的預設安全配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http.cors().and();    // 關閉CSRF(跨站請求偽造)攻擊的防護，這樣才不會拒絕外部直接對API 發出的請求，例如Postman 與前端
        httpSecurity.authorizeRequests()
                // 設定放行名單
                .antMatchers("/login", "/oauth/**", "/auth/login/oauth2/code/google").permitAll()
                // 其餘路徑皆須進行驗證
//                .anyRequest().authenticated()
                .and()
                // 添加自定义 Token Filter
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().permitAll()
                .successHandler(authenticationSuccess) // 登录成功逻辑处理
                .and()
                .oauth2Login().permitAll()
                  .userInfoEndpoint()
                     .userService(customOAuth2UserService)
                  .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
//                        request.authenticate(response);
                        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
                        String email = oauthUser.getAttribute("email");
                        String username = oauthUser.getAttribute("name");
                        CreateUserInputDTO createUserInputDTO = new CreateUserInputDTO();
                        createUserInputDTO.setUserName(username);
                        createUserInputDTO.setEmail(email);
                        createUserInputDTO.setAccount(email);
                        createUserInputDTO.setAccountType("google");

                        userService.createUserBySSO(createUserInputDTO);

                        //儲存會員
                        System.out.println("儲存會員");
//                        System.out.println(oauthUser.getName());
                        System.out.println(email);


                        LoginDTO loginDTO = new LoginDTO();
                        loginDTO.setAccount(email);
                        //產生token
                        System.out.println("登入產生token");
                        LoginResultDTO loginResultDTO;

                        loginResultDTO = authorizationService.loginSSO(loginDTO);

//                        request.authenticate(response);
                        //返回token
//                        response.sendRedirect("/auth/googleOauth2/user/me");
                        System.out.println(loginResultDTO);
                        String json = new Gson().toJson(loginResultDTO);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(json);
                    }
                });





                httpSecurity.addFilterBefore(authenticationTokenFilter, LogoutFilter.class)
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()

                .csrf().disable();
    }

    }

