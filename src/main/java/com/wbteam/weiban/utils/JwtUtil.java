package com.wbteam.weiban.utils;

import com.wbteam.weiban.entity.Carer;
import com.wbteam.weiban.entity.Child;
import com.wbteam.weiban.entity.Elder;
import com.wbteam.weiban.entity.enums.User;
import com.wbteam.weiban.exception.RedisTokenNullException;
import com.wbteam.weiban.exception.TokenErrorException;
import com.wbteam.weiban.service.CarerService;
import com.wbteam.weiban.service.ChildService;
import com.wbteam.weiban.service.ElderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    @Autowired
    private ElderService elderService;

    @Autowired
    private ChildService childService;

    @Autowired
    private CarerService carerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Long ACCESS_TOKEN_EXPIRATION = 3600L*1000;

    private static final Long REFRESH_TOKEN_EXPIRATION = 14*24*3600L*1000;

    @Value("${token.subject}")
    private String subject; //主题

    @Value("${token.issuer}")
    private String issuer;  //签发人

    @Value("${token.secret}")
    private String secret;  //盐

    public String getToken(String userId, User user) {
        Map<String,Object> header = new HashMap<>();
        header.put("alg","HS256");
        header.put("typ","JWT");
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user);
        claims.put("role",user.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setHeader(header)
                .setSubject(subject)
                .setIssuer(issuer)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String refreshToken(String userId, User user) {
        Map<String,Object> header = new HashMap<>();
        header.put("alg","HS256");
        header.put("typ","JWT");
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user);
        claims.put("role",user.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setHeader(header)
                .setSubject(subject)
                .setIssuer(issuer)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public Object getUser(String token, HttpServletRequest request) throws Exception {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String userId = (String)claims.get("userId");
        String role = (String) claims.get("role");
        String redisToken = redisTemplate.opsForValue().get(role + userId);
        if (redisToken == null ) throw new RedisTokenNullException();
        if(!redisToken.equals(token)) throw new TokenErrorException();
        switch (role) {
            case "elder":
                Elder elder = elderService.selectByID(userId);
                request.setAttribute("user", elder);
                request.setAttribute("role", role);
                return elder;
            case "child":
                Child child = childService.selectById(userId);
                request.setAttribute("user", child);
                request.setAttribute("role", role);
                return child;
            case "carer":
                Carer carer = carerService.selectById(userId);
                request.setAttribute("user", carer);
                request.setAttribute("role", role);
                return carer;
            default:
                return null;
        }
    }

    public Object getUser(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String userId = (String)claims.get("userId");
        String role = (String) claims.get("role");
        switch (role) {
            case "elder":
                return elderService.selectByID(userId);
            case "child":
                return childService.selectById(userId);
            case "carer":
                return carerService.selectById(userId);
            default:
                return null;
        }
    }

}
