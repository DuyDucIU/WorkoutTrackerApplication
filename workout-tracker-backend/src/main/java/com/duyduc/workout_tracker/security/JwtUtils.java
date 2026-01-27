package com.duyduc.workout_tracker.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
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

    public String generateToken(Authentication authentication, Integer userId) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationMilliseconds);

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("userId", userId)
                .issueTime(currentDate)
                .expirationTime(expireDate)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(jwtSecret.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new BadCredentialsException("Failed to generate JWT token", e);
        }
    }

    private JWTClaimsSet parseAndValidate(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(verifier)) {
                throw new BadCredentialsException("Invalid JWT signature");
            }

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiryTime == null || expiryTime.before(new Date())) {
                throw new BadCredentialsException("JWT token expired");
            }

            return signedJWT.getJWTClaimsSet();

        } catch (JOSEException | ParseException e) {
            throw new AuthenticationServiceException("Invalid or expired JWT", e);
        }
    }


    public boolean validateToken(String token) {
        parseAndValidate(token);
        return true;
    }

    public String getUsernameFromToken(String token) {
        return parseAndValidate(token).getSubject();
    }

    public Integer getUserIdFromToken(String token) {
        try {
            return parseAndValidate(token).getIntegerClaim("userId");
        } catch (ParseException e) {
            throw new BadCredentialsException("Invalid or expired JWT", e);
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        } else
            return null;
    }
}
