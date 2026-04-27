package it.neaga.bank.sim.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecureDigestAlgorithm
import io.jsonwebtoken.security.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.cglib.core.internal.Function
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Objects
import javax.crypto.SecretKey

@Service
class JwtService {

    @Value("\${security.jwt.secret-key}")
    lateinit var secretKey: String

    @Value("\${security.jwt.expiration-secs: 600}")
    var jwtExpirationTime: Long = 0


    fun <T> extractClaims(token: String, claimFunction: Function<Claims, T>): T{
        val claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
        return claimFunction.apply(claims)
    }

    fun getUsernameFrom(token: String): String = extractClaims(token, Claims::getSubject)
    fun getExpirationFrom(token: String): Boolean = extractClaims(token, Claims::getExpiration).before(Date())
    fun getSigningKey(): SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    fun isValid(token: String, user: UserDetails): Boolean {
        val username = getUsernameFrom(token)
        return username == user.username && !getExpirationFrom(token)
    }

    fun generateNewToken(user: UserDetails, extraClaims: Map<String, Object>): String {
        return Jwts
            .builder()
            .claims(extraClaims)
            .subject(user.username)
            .issuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            .expiration(Date.from(LocalDateTime.now().plusSeconds(jwtExpirationTime).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact()
    }


}