    package vnpt.project.Caller_management.Config;

    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import vnpt.project.Caller_management.Services.LoginServices;
    import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Bean
        public UserDetailsService userDetailsService() {
            return new LoginServices(); // Trả về lớp dịch vụ người dùng đã được triển khai
        }

        @Bean
        public AuthenticationProvider daoAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService());
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Tắt CSRF nếu không cần thiết
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/loginPage", "/register", "/logout", "/Dashboard").permitAll() // Cho phép các URL công khai
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Cho phép truy cập tài nguyên tĩnh
                        .requestMatchers("/accessDenied").permitAll()
                        .requestMatchers("/home").denyAll()/// Cho phép truy cập trang accessDenied
                        .requestMatchers("/ShowAll").hasRole("SYSADMIN") // Chỉ SYSADMIN mới có quyền truy cập
                        .anyRequest().authenticated() // Yêu cầu xác thực cho mọi yêu cầu khác
                )
                .formLogin(form -> form
                        .loginPage("/loginPage") // Trang đăng nhập tùy chỉnh
                        .loginProcessingUrl("/perform_login") // URL xử lý đăng nhập
                        .usernameParameter("email") // Sử dụng email làm tham số đăng nhập
                        .passwordParameter("password") // Sử dụng password làm tham số đăng nhập
                        .defaultSuccessUrl("/ShowAll", true) // Chuyển hướng sau khi đăng nhập thành công
                        .failureHandler(new CustomAuthenticationFailureHandler()) // Sử dụng CustomAuthenticationFailureHandler để xử lý lỗi
                        .permitAll() // Cho phép tất cả truy cập trang đăng nhập
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL xử lý đăng xuất
                        .logoutSuccessUrl("/loginPage?logout=true") // Chuyển hướng khi đăng xuất thành công
                        .invalidateHttpSession(true) // Hủy session khi đăng xuất
                        .clearAuthentication(true) // Xóa dữ liệu xác thực
                        .deleteCookies("JSESSIONID") // Xóa cookie phiên
                        .permitAll() // Cho phép truy cập URL logout
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/accessDenied") // Chuyển hướng đến trang khi người dùng không có quyền
                ).build();
    }


        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }