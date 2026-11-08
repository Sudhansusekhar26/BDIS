package com.bdis.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);

        if (savedRequest != null && !savedRequest.getRedirectUrl().contains("/login") && !savedRequest.getRedirectUrl().contains("/register")) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if ("ROLE_ADMIN".equals(role)) {
                getRedirectStrategy().sendRedirect(request, response, "/admin/dashboard");
                return;
            } else if ("ROLE_HOSPITAL".equals(role)) {
                getRedirectStrategy().sendRedirect(request, response, "/hospital/dashboard");
                return;
            } else if ("ROLE_USER".equals(role)) {
                getRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
                return;
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
