package com.miva.billmanager

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig

class AIModelManager {

    companion object {
        private const val MODEL_NAME = "gemini-3.7-flash"
        private const val TEMPERATURE = 0.0f
        private const val TOP_P = 0.95f
        private const val TOP_K = 32
        private const val MAX_OUTPUT_TOKENS = 2048
        private const val RESPONSE_MIME_TYPE = "application/json"
        
        private const val SYSTEM_INSTRUCTION = "You are an expert financial receipt and invoice parser. " +
                "Extract fields with 100% literal accuracy " +
                "without guessing or assuming values."

        private val strictBillConfig = generationConfig {
            temperature = TEMPERATURE
            topP = TOP_P
            topK = TOP_K
            maxOutputTokens = MAX_OUTPUT_TOKENS
            responseMimeType = RESPONSE_MIME_TYPE
        }

        val generativeModel = Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            generationConfig = strictBillConfig,
            systemInstruction = content {
                text(SYSTEM_INSTRUCTION)
            })
    }
}
