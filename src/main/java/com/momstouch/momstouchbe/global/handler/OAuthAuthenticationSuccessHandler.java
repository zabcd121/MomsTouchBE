package com.momstouch.momstouchbe.global.handler;

import com.momstouch.momstouchbe.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        response.setHeader(JwtTokenProvider.AUTHORIZATION_HEADER,accessToken);
    }
}
