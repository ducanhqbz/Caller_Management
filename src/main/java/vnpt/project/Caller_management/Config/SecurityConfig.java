package vnpt.project.Caller_management.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
        return new LoginServices(); // Đảm bảo trả về lớp dịch vụ người dùng đã được triển khai
    }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);//tạo môi trường sử dụng userdetailservice
            authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);//mã hóa mật khẩu + so sánh vs mật khẩu lấy từ cơ sở dữ liệu
            return authenticationManagerBuilder.build();
        }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Bật hoặc tắt CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/loginPage", "/register", "/logout", "/home", "/Dashboard").permitAll() // Cho phép truy cập các đường dẫn này mà không cần đăng nhập
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Cho phép truy cập các tài nguyên tĩnh mà không cần đăng nhập
                        .requestMatchers("/accessDenied").permitAll() // Ai cũng có thể truy cập trang accessDenied
                        .requestMatchers("/ShowAll").hasRole("SYSADMIN") // Chỉ cho phép người dùng có role "SYSADMIN" truy cập
                        .anyRequest().authenticated() // Mọi yêu cầu khác đều yêu cầu xác thực (đăng nhập)
                )
                .formLogin(form -> form
                        .loginPage("/loginPage") // Trang đăng nhập tùy chỉnh
                        .loginProcessingUrl("/perform_login") // URL xử lý đăng nhập
                        .usernameParameter("email") // Trường email trong form đăng nhập
                        .passwordParameter("password") // Trường password trong form đăng nhập
                        .defaultSuccessUrl("/ShowAll", true) // Điều hướng tới /success sau khi đăng nhập thành công
                        .failureUrl("/loginPage?error=true") // Điều hướng về trang login khi đăng nhập thất bại
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL để xử lý đăng xuất
                        .logoutSuccessUrl("/loginPage?logout=true") // Điều hướng tới /loginPage sau khi đăng xuất thành công
                        .invalidateHttpSession(true) // Hủy bỏ session hiện tại khi đăng xuất
                        .clearAuthentication(true) // Xóa dữ liệu xác thực
                        .deleteCookies("JSESSIONID") // Xóa cookie JSESSIONID để đảm bảo người dùng đã đăng xuất hoàn toàn
                        .permitAll() // Cho phép mọi người truy cập URL logout
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/accessDenied") // Chuyển hướng đến trang /accessDenied khi người dùng không có quyền
                ).build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Sử dụng BCryptPasswordEncoder để mã hóa mật khẩu
    }


}
