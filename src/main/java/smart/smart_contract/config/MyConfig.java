package smart.smart_contract.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import smart.smart_contract.Repository.UserRepository;

@Configuration
@EnableWebSecurity
public class MyConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserDetailsService getUserDetailService() {
        return new UserDetailsServiceImpl(); // ✅ आपकी custom class
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(getUserDetailService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ✅ Spring Security 6 के लिए AuthenticationManager ऐसे बनता है
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ✅ Modern SecurityFilterChain configuration
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/user/**").hasAuthority("ROLE_USER")
            .requestMatchers("/**").permitAll()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/user/index")
            .loginProcessingUrl("/dologin")
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/")
        )
        .authenticationProvider(authenticationProvider())
       .oauth2Login(oauth2 -> oauth2
    .loginPage("/login")
    .defaultSuccessUrl("/user/index", true)
    .userInfoEndpoint(userInfo -> userInfo
        .userService(customOAuth2UserService())
    )
);


    return http.build();
}



@Bean
public CustomOAuth2UserService customOAuth2UserService() {
    return new CustomOAuth2UserService(userRepository);
}

}