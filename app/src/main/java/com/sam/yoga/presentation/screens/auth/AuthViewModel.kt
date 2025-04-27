package com.sam.yoga.presentation.screens.auth

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sam.yoga.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom

class AuthViewModel : ViewModel() {

    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
        private set

    val firebaseAuth = FirebaseAuth.getInstance()
    val fireStore = FirebaseFirestore.getInstance()

    fun signUp(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (error: String?) -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val userId = firebaseAuth.currentUser?.uid!!
                        val hashedPassword = hashPassword(password)
                        val newUser = User(
                            userId = userId,
                            name = name,
                            email = email,
                            password = hashedPassword
                        ).toMap()

                        // Save user data to Firestore
                        fireStore.collection("Users")
                            .document(userId)
                            .set(newUser)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { error ->
                                onError(error.localizedMessage)
                            }
                        uiState.update { it.copy(loading = false) }
                    }
                    .await()
            } catch (e: Exception) {
                onError(e.localizedMessage)
            }

            withContext(Dispatchers.Main) {
                uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (error: String?) -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        onSuccess()
                    }
                    .await()
            } catch (e: Exception) {
                onError(e.localizedMessage)
            }

            withContext(Dispatchers.Main) {
                uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun hashPassword(password: String): String {
        val salt = ByteArray(12)
        SecureRandom().nextBytes(salt)

        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(salt)
        val hashBytes = messageDigest.digest(password.toByteArray(Charsets.UTF_8))

        return Base64.encodeToString(hashBytes, Base64.DEFAULT)
    }

    fun setEmail(email: String) {
        uiState.update { it.copy(email = email) }
    }

    fun setPassword(password: String) {
        uiState.update { it.copy(password = password) }
    }

    fun setRePassword(password: String) {
        uiState.update { it.copy(rePassword = password) }
    }

    fun setName(name: String) {
        uiState.update { it.copy(name = name) }
    }
}