package ma.microtech.smartShop.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.microtech.smartShop.exceptions.ForbiddenException;
import ma.microtech.smartShop.exceptions.UnauthorizedException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String path = request.getRequestURI();
            String method = request.getMethod();

            // Public
            if (path.equals("/api/auth/login") || path.equals("/api/auth/logout")
                    || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
                filterChain.doFilter(request, response);
                return;
            }

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                throw new UnauthorizedException("Please login");
            }

            String role = (String) session.getAttribute("role");

            if ("CLIENT".equals(role)) {
                if (path.equals("/api/products") && method.equals("GET")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                if (path.equals("/api/auth/me") && method.equals("GET")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                if (path.equals("/api/clients/me") && method.equals("GET")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                if (path.equals("/api/orders/me") && method.equals("GET")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                throw new ForbiddenException("Access denied: Clients can only view products and their own data");
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
