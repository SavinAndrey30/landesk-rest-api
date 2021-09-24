package ru.t1.asavin.techSupportAutomation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("api").tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/incidents").hasAnyRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/incidents").hasAnyRole("USER")

                .antMatchers("/api/incidents/**").hasAnyRole("ANALYST")

                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/**").hasRole("ADMIN")

                .and()
                .csrf().disable()
                .formLogin().disable();

        http.headers().frameOptions().sameOrigin();

    }
}