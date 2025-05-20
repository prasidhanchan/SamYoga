package com.sam.yoga.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.sam.yoga.R
import com.sam.yoga.SamYogaApplication.Companion.getAppContext
import com.sam.yoga.domain.models.Chat
import com.sam.yoga.domain.models.YogaPose

object Util {

    const val SIZE = 321

    fun Bitmap.centerCrop(desiredWidth: Int, desiredHeight: Int): Bitmap {
        val xStart = (width - desiredWidth) / 2
        val yStart = (height - desiredHeight) / 2

        if (xStart < 0 || yStart < 0 || desiredWidth > width || desiredHeight > height) {
            throw IllegalArgumentException("Invalid arguments for center cropping")
        }

        return Bitmap.createBitmap(this, xStart, yStart, desiredWidth, desiredHeight)
    }

    fun isPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    val systemPrompt = """
    You are a certified yoga and fitness expert. 
    Only answer yoga or exercise-related questions. 
    You can reply for greetings or any other greeting related interaction with the user. 
    If the user asks anything unrelated, politely decline. 
    If the question is unrelated to yoga or fitness, respond with: 'Sorry, I can only help with yoga or exercise-related topics.' 
    The name of this app is SamYoga. 
    This app currently has 8 yoga poses. 
    The poses are listed below:
    1. Ardha Chandrasana
    2. Adho Mukha Svanasana
    3. Baddha Konasana
    4. Natarajasana
    5. Trikonasana
    6. Utkata Konasana
    7. Veerabhadrasana
    8. Vrukshasana. 
    The maxOutputTokens are set to 1000 keep the response within or equal to it. 
    You can reply and remember the questions asked with you earlier. 
    Don't greet again if there exits previous questions. 
""".trimIndent()

    val boldRegex = Regex("\\*\\*(.*?)\\*\\*")

    val fakeChat = listOf<Chat>(
        Chat(
            message = "Hello how are you?",
            sender = "USER",
            timeStamp = 100089743L,
            isGenerating = false
        ),
        Chat(
            message = "I am good, how are you?",
            sender = "AI",
            timeStamp = 100089743L,
            isGenerating = false
        ),
        Chat(
            message = "I am good, what are you doing?",
            sender = "USER",
            timeStamp = 100089743L,
            isGenerating = false
        ),
        Chat(
            message = "I am good, thank you",
            sender = "AI",
            timeStamp = 100089743L,
            isGenerating = false
        ),
        Chat(
            message = "What is Yoga?",
            sender = "USER",
            timeStamp = 100089743L,
            isGenerating = false
        ),
        Chat(
            message = "Yoga is a practice of exercises",
            sender = "AI",
            timeStamp = 100089743L,
            isGenerating = false
        ),
        Chat(
            message = "What is meditation?",
            sender = "USER",
            timeStamp = 100089743L,
            isGenerating = false
        ),
        Chat(
            message = "Meditation is a practice of focusing on the present moment",
            sender = "AI",
            timeStamp = 100089743L,
            isGenerating = false
        ),
    )

    val poses = listOf(
        YogaPose(
            name = "Ardha Chandrasana",
            description = "",
            level = "Intermediate",
            time = 2L,
            image = "ardha_chandrasana"
        ),
        YogaPose(
            name = "Adho Mukha Svanasana",
            description = "",
            level = "Beginner",
            time = 3L,
            image = "adho_mukha_svanasana"
        ),
        YogaPose(
            name = "Baddha Konasana",
            description = "",
            level = "Beginner",
            time = 5L,
            image = "baddha_konasana"
        ),
        YogaPose(
            name = "Natarajasana",
            description = "",
            level = "Advanced",
            time = 2L,
            image = "natarajasana"
        ),
        YogaPose(
            name = "Trikonasana",
            description = "",
            level = "Beginner",
            time = 5L,
            image = "trikonasana"
        ),
        YogaPose(
            name = "Utkata Konasana",
            description = "",
            level = "Intermediate",
            time = 5L,
            image = "utkata_konasana"
        ),
        YogaPose(
            name = "Veerabhadrasana",
            description = "",
            level = "Intermediate",
            time = 3L,
            image = "veerabhadrasana"
        ),
        YogaPose(
            name = "Vrukshasana",
            description = "",
            level = "Advanced",
            time = 5L,
            image = "vrukshasana"
        )
    )

    fun getCorrectionTips(poseName: String): List<String> {
        val context = getAppContext()
        return when (poseName) {
            context.getString(R.string.ardha_chandrasana) -> listOf(
                "Keep the chest open and lifted.",
                "Engage the quadriceps and straighten the standing leg.",
                "Focus on a fixed point to maintain balance.",
                "Rotate the top hip outward, stacking it over the bottom hip."
            )

            context.getString(R.string.adho_mukha_svanasana) -> listOf(
                "Keep the back straight, lengthening the spine.",
                "Relax shoulders away from ears.",
                "Press heels toward the ground (knees can be slightly bent if needed).",
                "Keep a slight micro-bend to prevent hyperextension."
            )

            context.getString(R.string.baddha_konasana) -> listOf(
                "Sit upright with a straight spine.",
                "Use props like blocks under the knees for support.",
                "Bring heels closer to the groin without strain."
            )

            context.getString(R.string.natarajasana) -> listOf(
                "Keep hips squared forward.",
                "Lift the chest while extending the raised leg.",
                "Keep the raised leg’s knee pointing down.",
                "Engage the core to maintain stability."
            )

            context.getString(R.string.trikonasana) -> listOf(
                "Keep shoulders aligned and open sideways.",
                "Keep a slight micro-bend to avoid strain.",
                "Keep the neck neutral or look up only if comfortable.",
                "Distribute weight evenly between both feet."
            )

            context.getString(R.string.utkata_konasana) -> listOf(
                "Keep knees aligned with toes.",
                "Engage the core to keep a neutral spine.",
                "Widen the stance for better stability.",
                "Keep shoulders relaxed and away from ears."
            )

            context.getString(R.string.veerabhadrasana) -> listOf(
                "Align knee over ankle.",
                "Keep the back foot firmly pressed into the mat.",
                "Keep the torso upright and strong.",
                "Keep arms actively reaching in opposite directions."
            )

            else -> listOf(
                "Keep foot on thigh or calf, not the knee.",
                "Keep both hips squared and aligned.",
                "Fix your gaze on a steady focal point for better balance.",
                "Keep shoulders relaxed and arms extended gracefully."
            )
        }
    }
}