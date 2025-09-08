package org.oiakushev.hospital.repository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.oiakushev.hospital.model.Personal;
import org.oiakushev.hospital.model.PersonalRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenRepository {
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    PersonalRepository personalRepository;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(HttpServletRequest httpServletRequest) {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now().plusMinutes(30)
                .atZone(ZoneId.systemDefault()).toInstant());

        String username = (String) httpServletRequest.getAttribute("user");

        String token = Jwts.builder()
                    .setId(id)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setNotBefore(now)
                    .setExpiration(exp)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();

        Personal personal = personalRepository.findByUsername(username);
        personal.setToken(token);
        personalRepository.save(personal);

        return token;
    }

    public void saveToken(String token, HttpServletRequest request, HttpServletResponse response) {
        if (token != null) {
            if (response.getHeaderNames().contains(HttpHeaders.AUTHORIZATION)) {
                response.setHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token);
            } else {
                response.addHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token);
            }
        }
    }

    public Personal loadAccount(HttpServletRequest request, PersonalRole personalRole) {
        String tokenFromHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (tokenFromHeader != null) {
            String token = tokenFromHeader.substring(BEARER_PREFIX.length());
            String username = getUsernameFromToken(token);
            Personal personal = personalRepository.findByUsername(username);

            if (personal != null && personal.getToken().equals(token) &&
                    personalRole.getIndex() <= personal.getRole()) {
                return personal;
            }
        }

        return null;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public Personal auth(HttpServletRequest request, PersonalRole personalRole) {
        Personal personal = loadAccount(request, personalRole);

        if (personal == null || StringUtils.isEmpty(personal.getToken())) {
            throw new JwtAuthException("Invalid or empty JWT token.");
        }

        Date expirationDate = getExpirationDateFromToken(personal.getToken());
        long diff = expirationDate.getTime() - (new Date()).getTime();

        if (diff <= 0) {
            throw new JwtAuthException("JWT token is expired.");
        }

        return personal;
    }
}
