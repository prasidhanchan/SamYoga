package com.sam.yoga.data

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale
import java.util.UUID

/**
 * YogaTTS - A wrapper for TextToSpeech functionality
 *
 * Features:
 * - Speaks yoga pose instructions and counts
 * - Manages TTS initialization and cleanup
 * - Provides state to observe speaking status
 */
class YogaTTS(private val context: Context) {

    var textToSpeech: TextToSpeech? = null
    var isSpeaking = MutableStateFlow(false)
    var isInitialized = false
        private set

    init {
        initializeTTS()
    }

    private fun initializeTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.ENGLISH)

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Toast.makeText(context, "Language not supported", Toast.LENGTH_SHORT).show()
                } else {
                    // Configure TTS parameters for better yoga instruction delivery
                    textToSpeech?.setPitch(1.0f) // Normal pitch
                    textToSpeech?.setSpeechRate(0.85f) // Slightly slower for clearer instructions

                    // Set up utterance progress listener
                    textToSpeech?.setOnUtteranceProgressListener(object :
                        UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            isSpeaking.value = true
                        }

                        override fun onDone(utteranceId: String?) {
                            isSpeaking.value = false
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            isSpeaking.value = false
                        }
                    })

                    isInitialized = true
                }
            }
        }
    }

    /**
     * Speak yoga instructions with appropriate pauses
     */
    fun speakYogaInstruction(text: String, queueMode: Int = TextToSpeech.QUEUE_ADD) {
        if (!isInitialized) return

        val utteranceId = UUID.randomUUID().toString()
        textToSpeech?.speak(text, queueMode, null, utteranceId)
    }

    /**
     * Stop all speaking
     */
    fun stopSpeaking() {
        textToSpeech?.stop()
        isSpeaking.value = false
    }

    /**
     * Release TTS resources when no longer needed
     */
    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false
    }
}