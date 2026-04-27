package it.neaga.bank.sim.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val accountId: UUID,

    val username: String,

    @Column(unique = true, length = 100, nullable = false)
    val email: String,

    val passwordHash: String
): UserDetails {
    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return listOf()
    }

    override fun getPassword(): String? {
        return passwordHash
    }

    override fun getUsername(): String {
        return username
    }

}
