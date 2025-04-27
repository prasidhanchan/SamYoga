package com.sam.yoga.domain.models

import androidx.compose.runtime.Stable

@Stable
data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val gender: String = "Unknown"
) {
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            "userId" to this.userId,
            "name" to this.name,
            "email" to this.email,
            "password" to this.password,
            "gender" to this.gender
        )
    }
}