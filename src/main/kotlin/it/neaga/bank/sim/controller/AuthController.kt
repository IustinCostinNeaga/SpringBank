package it.neaga.bank.sim.controller

import it.neaga.bank.sim.service.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) {
    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): LoginResponse {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
        val user = userDetailsService.loadUserByUsername(req.email)
        return LoginResponse(jwtService.generateNewToken(user, HashMap()))
    }
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)