package com.demo.campingnavi.config;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Role;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import com.demo.campingnavi.service.MyAccessDeniedHandler;
import com.demo.campingnavi.service.MyAuthenticationEntryPoint;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final HttpSession session;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**", "/static/**", "/assets/**", "/templates/**","/css/**", "/js/**", "/images/**", "/memberImages/**", "/qnaImages/**", "/replyImages/**")
                .requestMatchers("/summernote/**")
                .requestMatchers("/camp/**", "/qna/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/resources/**", "/static/**", "/assets/**", "/templates/**","/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/member/join", "/", "/admin/login", "/admin/loginProc", "/member/joinProc", "/member/membershipAgree", "/member/membership", "/member/validateUser", "/member/validateNickname").permitAll()
                        .requestMatchers("/member/search/**", "/member/login").permitAll()
                        .requestMatchers("/mailSend", "mailCheck", "/camp/search", "/camp/detail/go", "/oauth-login/member/loginProc", "/oauth-login/member/login", "/oauth-login/member/logoutProc").permitAll()
                        .requestMatchers( "/admin/supervisor/list", "/admin/supervisor/list/page", "/admin/adminAddProc").hasRole("SUPERVISOR")
                        .requestMatchers("/admin/login").rememberMe()
                        .requestMatchers("/admin/**").hasAnyRole("SUPERVISOR", "ADMIN")
                        .requestMatchers("/member/**", "/main").hasRole("USER")
                        .anyRequest().authenticated()
                );
        http
                .exceptionHandling()
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .accessDeniedHandler(new MyAccessDeniedHandler());
        http
                .formLogin((auth) -> auth
                        .loginPage("/oauth-login/member/login")
                        .defaultSuccessUrl("/main")
                        .usernameParameter("username")
                        .passwordParameter("pw")
                        .loginProcessingUrl("/oauth-login/member/loginProc")
                        .successHandler(
                                (request, response, authentication) -> {
                                    Member member = memberRepository.findByUsername(authentication.getName());
                                    session.setAttribute("loginUser", member);
                                    if (member.getRole().equals("ROLE_USER")) {
                                        response.sendRedirect("/main");
                                    } else if (member.getRole().equals("ROLE_ADMIN") || member.getRole().equals("ROLE_SUPERVISOR")) {
                                        response.sendRedirect("/admin/");
                                    }

                                }
                        )
                        .failureHandler(
                                (request, response, exception) -> {
                                    System.out.println("exception : " + exception.getMessage());
                                    request.setAttribute("msg", "로그인에 실패했습니다.");
                                    request.getRequestDispatcher("/").forward(request, response);
                                }
                        )
                        .permitAll()
                );

        http
                .csrf((auth) -> auth.disable()
                ); // 테스트하기 위해 잠시 disable
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        http
                .headers((headerConfig) -> headerConfig.frameOptions(
                        frameOptionsConfig -> frameOptionsConfig.disable()
                ))
                .logout(
                        (logoutConfig) -> logoutConfig
                                .logoutSuccessUrl("/")
                                .logoutUrl("/oauth-login/member/logoutProc")
                                .logoutSuccessHandler(
                                        (request, response, authentication) -> {response.sendRedirect("/");}
                                )
                )
                .oauth2Login((oauth) -> oauth.loginPage("/oauth-login/member/login")
                        .defaultSuccessUrl("/main")
                        .failureUrl("/oauth-login/member/login")
                        .successHandler(
                                (request, response, authentication) -> {
                                    Member member = memberRepository.findByUsername(authentication.getName());
                                    session.setAttribute("loginUser", member);
                                    if(member.getSex().equals(" ") || member.getBirth().equals(" ") || member.getPhone().equals(" ") || member.getAddr1().equals(" ")) {
                                        request.setAttribute("msg", "정보를 입력해주세요.");
                                        request.getRequestDispatcher("/member/mypage/oauth").forward(request, response);
                                    } else {
                                        response.sendRedirect("/main");
                                    }
                                }
                        )
                        .failureHandler(
                                (request, response, exception) -> {
                                    System.out.println("exception : " + exception.getMessage());
                                    response.sendRedirect("/oauth-login/member/login");
                                }
                        )
                        .permitAll());
        return http.build();
    }


}
