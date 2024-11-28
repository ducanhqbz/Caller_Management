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
        // Log the failure

        System.out.println("DAy la onauthentication failure");
        System.out.println("Login failed: " + exception.getMessage());

        // Redirect only if the response has not been committed

            String errorMessage = "Invalid email or password. Please try again.";
            request.getSession().setAttribute("error", errorMessage);

            // Set the default failure URL
            super.setDefaultFailureUrl("/loginPage?error=true");

            // Perform the redirect
            super.onAuthenticationFailure(request, response, exception);

    }
}
