package com.momstouch.momstouchbe.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    private final MemberDetailsService memberDetailsService;

    private final String secretKey;
    private final long tokenValidityInMs;
    private final long refreshTokenValidityInMs;

    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.token-validity-in-sec}") long tokenValidity,
                            @Value("${jwt.refresh-token-validity-in-sec}") long refreshTokenValidity,
                            MemberDetailsService memberDetailsService){
        this.secretKey = secretKey;
        this.tokenValidityInMs = tokenValidity * 200;
        this.refreshTokenValidityInMs = refreshTokenValidity * 1000;
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {  // init()
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMs);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now) // 발행시간
                .signWith(key, SignatureAlgorithm.HS512) // 암호화
                .setExpiration(validity) // 만료
                .compact();
    }

    /**
     * 토큰으로 부터 Authentication 객체를 얻어온다.
     * Authentication 안에 user의 정보가 담겨있음.
     * UsernamePasswordAuthenticationToken 객체로 Authentication을 쉽게 만들수 있으며,
     * 매게변수로 UserDetails, pw, authorities 까지 넣어주면
     * setAuthenticated(true)로 인스턴스를 생성해주고
     * Spring-Security는 그것을 체크해서 로그인을 처리함
     */
    public Authentication getAuthentication(String token) throws IllegalArgumentException {
        Claims claims = getClaims(token);
        UserDetails userDetails = memberDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
//        } catch (SignatureException e) {
//            throw new TokenException(ErrorCode.INCORRECT_SIGNATURE);
//        } catch (ExpiredJwtException e) {
//            throw new TokenException(ErrorCode.EXPIRED_TOKEN);
//        } catch (MalformedJwtException | UnsupportedJwtException e) {
//            throw new TokenException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public String createRefreshToken(Authentication authentication){
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMs);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();

        return (expiration.getTime() - now);
    }

    public String resolveToken(String bearerToken) {
        if(bearerToken == null || bearerToken.equals("")) {
            log.info("bearer token not passed");
            //throw new TokenException(ErrorCode.HEADER_NULL_TOKEN);
            throw new IllegalArgumentException("bearer token not passed");
        }

        if(!bearerToken.startsWith(BEARER)) {
            //throw new TokenException(ErrorCode.INVALID_TYPE_TOKEN);
            throw new IllegalArgumentException("invalid type token");
        }
        return bearerToken.substring(7);
    }
}
