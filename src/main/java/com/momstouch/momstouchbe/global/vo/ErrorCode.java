package com.momstouch.momstouchbe.global.vo;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    HEADER_NULL_TOKEN(BAD_REQUEST, "0206", "인증 토큰이 전달되지 않았습니다."),
    INVALID_TYPE_TOKEN(BAD_REQUEST, "0207", "Bearer 토큰이 전달되지 않았습니다."),
    UNKNOWN_PROBLEM_TOKEN(BAD_REQUEST, "0208", "알 수 없는 토큰 에러입니다."),
    INCORRECT_SIGNATURE(BAD_REQUEST, "0209", "잘못된 서명이 전달되었습니다."),
    EXPIRED_TOKEN(BAD_REQUEST, "0210", "만료된 토큰이 전달되었습니다."),
    UNSUPPORTED_TOKEN(BAD_REQUEST, "0211", "지원되지 않는 토큰이 전달되었습니다."),
    UNAUTHORIZED_MEMBER(FORBIDDEN, "0212", "권한 부족 접근 거부"),
    NOT_FOUND_REFRESH_TOKEN_IN_STORE(NOT_FOUND, "0213", "존재하지 않는 refresh token 입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "0217", "refresh token이 만료되었습니다."),
    NOT_FOUND_ACCOUNT_QR(UNAUTHORIZED, "0218", "유효하지 않은 QR 코드입니다."),
    NOT_FOUND_MEMBER(NOT_FOUND, "0219", "존재하지 않는 회원입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
