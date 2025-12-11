package pawkar.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import pawkar.backend.security.jwt.AuthEntryPointJwt;
import pawkar.backend.security.jwt.AuthTokenFilter;
import pawkar.backend.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Specify your frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        configuration.setAllowCredentials(true); // Allow credentials
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers(
                        "/auth/**",
                        "/test/**",
                        "/error"
                    ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/roles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/subcategorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/subcategoria-roles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/series/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/equipos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/jugadores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/plantillas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/generacion-encuentros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tabla-posicion/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/verificacion/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/estadios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/configuracion/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/roles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/subcategorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/subcategoria-roles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/series/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/equipos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/jugadores/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/plantillas/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tabla-posicion/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/estadios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/configuracion/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/encuentros/**").permitAll()
                    .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider());
            
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
