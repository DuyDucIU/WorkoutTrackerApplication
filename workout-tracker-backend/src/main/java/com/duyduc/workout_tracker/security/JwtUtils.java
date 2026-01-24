package com.duyduc.workout_tracker.security;

import com.duyduc.workout_tracker.exception.JwtTokenException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.expiration}")
    private Long jwtExpirationMilliseconds;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(Authentication authentication) {

        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationMilliseconds);

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(authentication.getName())
                .issueTime(currentDate)
                .expirationTime(expireDate)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(jwtSecret.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new JwtTokenException("Failed to generate JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return signedJWT.verify(verifier) && expiryTime.after(new Date());
        } catch (JOSEException | ParseException e) {
            throw new JwtTokenException("Failed to validate JWT token", e);
        }
    }

    public String getUsername(String token) {
        try {
            if (!validateToken(token)) {
                throw new JwtTokenException("Invalid or expired JWT token");
            }
            SignedJWT jwt = SignedJWT.parse(token);
            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new JwtTokenException("Failed to parse JWT token", e);
        }
    }

    public String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        } else
            return null;
    }
}
