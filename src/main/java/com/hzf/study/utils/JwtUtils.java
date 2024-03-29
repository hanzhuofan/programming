package com.hzf.study.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {
    public static final long DEFAULT_TOKEN_TIME_MS = 30 * 60 * 1000;

   /*
    iss: 该JWT的签发者
    sub: 该JWT所面向的用户
    aud: 接收该JWT的一方
    exp(expires): 什么时候过期，这里是一个Unix时间戳
    iat(issued at): 在什么时候签发的
   */

    /**
     * 签名秘钥
     */
    public static final String SECRET = "token";

    /**
     * 生成token
     *
     * @param id 一般传入userName
     * @return
     */
    public static String createJwtToken(String id) {
        String issuer = "GYB";
        String subject = "";
        return createJwtToken(id, issuer, subject, DEFAULT_TOKEN_TIME_MS);
    }

    public static String createJwtToken(String id, long ttlMillis) {
        String issuer = "GYB";
        String subject = "";
        return createJwtToken(id, issuer, subject, ttlMillis);
    }


    /**
     * 生成Token
     *
     * @param id        编号
     * @param issuer    该JWT的签发者，是否使用是可选的
     * @param subject   该JWT所面向的用户，是否使用是可选的；
     * @param ttlMillis 签发时间
     * @return token String
     */
    public static String createJwtToken(String id, String issuer, String subject, long ttlMillis) {

        // 签名算法 ，将对token进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成签发时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 通过秘钥签名JWT
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        claims.put("userDetails", id);

        // Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .setClaims(claims)
                .signWith(signatureAlgorithm, signingKey);

        // if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();

    }

    // Sample method to validate and read the JWT
    public static Claims parseJWT(String jwt) {
        // This line will throw an exception if it is not a signed JWS (as expected)
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(jwt).getBody();
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * 验证jwt的有效期
     *
     * @param claims
     * @return
     */
    public static Boolean isTokenExpired(Claims claims) {
        return claims == null || claims.getExpiration().before(new Date());
    }
}
