package ru.dipech.listeneng.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import ru.dipech.listeneng.service.DevelopmentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import static ru.dipech.listeneng.util.Constants.MONTH;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final String securityKey;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private DevelopmentService developmentService;

    public WebSecurityConfig(
        DataSource dataSource,
        @Value("${app.security.key}") String securityKey
    ) {
        this.dataSource = dataSource;
        this.securityKey = securityKey;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            // Allow index page loading
            .antMatchers("/*").permitAll()
            // Allow dist resources loading
            .antMatchers("/dist/**").permitAll()
            // Allow session check endpoint
            .antMatchers("/api/session/check").permitAll()
            // Allow login/register/logout
            .antMatchers("/api/security/*").permitAll()
            // Every other request should be authenticated
            .anyRequest().authenticated()

            .and()
            .logout()
            .logoutUrl("/api/security/logout")
            .deleteCookies("JSESSIONID")
            .logoutSuccessHandler(getLogoutSuccessHandler())
            .logoutSuccessUrl("/")

            .and()
            .rememberMe()
            .alwaysRemember(true)
            .rememberMeServices(persistentTokenBasedRememberMeServices());
//        if (developmentService.isProd()) {
//            http.requiresChannel().anyRequest().requiresSecure()
//                .and().portMapper().http(8080).mapsTo(80);
//        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private LogoutSuccessHandler getLogoutSuccessHandler() {
        return (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK);
    }

    @Bean
    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
        var persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        var service = new PersistentTokenBasedRememberMeServices(securityKey, userDetailsService,
            persistentTokenRepository);
        service.setAlwaysRemember(true);
        service.setParameter("remember-me");
        service.setTokenValiditySeconds(MONTH);
        return service;
    }

}
