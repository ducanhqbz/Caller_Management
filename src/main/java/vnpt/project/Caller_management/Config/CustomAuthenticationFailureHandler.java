package vnpt.project.Caller_management.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // Kiểm tra xem ngoại lệ có phải do mật khẩu không khớp
        if (exception.getMessage().contains("Bad credentials")) {
            // Thực hiện xử lý khi mật khẩu không đúng
            System.out.println("Mật khẩu không đúng!");

            // Bạn có thể lưu thêm thông tin vào request để hiển thị cho người dùng

            request.setAttribute("errorMessage", "Mật khẩu không chính xác. Vui lòng thử lại.");
            super.setDefaultFailureUrl("/loginPage?error=true");
            super.onAuthenticationFailure(request, response, exception);
        }

        // Chuyển hướng về trang login với thông báo lỗi
        super.setDefaultFailureUrl("/loginPage?error=true");
        super.onAuthenticationFailure(request, response, exception);
    }
}
