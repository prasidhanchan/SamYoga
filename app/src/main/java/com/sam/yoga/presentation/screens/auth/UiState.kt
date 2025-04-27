package com.sam.yoga.presentation.screens.auth

import com.sam.yoga.domain.models.User

data class UiState(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var rePassword: String = "",
    var loading: Boolean = false,
    var user: User? = null
)