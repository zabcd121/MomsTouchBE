package com.momstouch.momstouchbe.global.jwt;

import com.momstouch.momstouchbe.global.vo.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.momstouch.momstouchbe.global.vo.ErrorCode.*;
import static com.momstouch.momstouchbe.global.vo.ErrorCode.UNKNOWN_PROBLEM_TOKEN;


@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        Exception exception = (Exception) request.getAttribute("exception");

        if(exception == null) {
            setResponse(response, UNAUTHORIZED_MEMBER);
        } else {
            setResponse(response, UNKNOWN_PROBLEM_TOKEN);
        }

        log.error("exception: {}", exception.getMessage());

    }

    private void setResponse(HttpServletResponse response, ErrorCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("code", exceptionCode.getCode());
        responseJson.put("message", exceptionCode.getMessage());


        response.getWriter().print(responseJson);
    }
}
