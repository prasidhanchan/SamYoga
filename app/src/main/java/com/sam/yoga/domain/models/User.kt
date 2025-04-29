package com.sam.yoga.domain.models

data class User(
    var userId: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var gender: String = "Unknown"
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