package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import com.example.BuildConfig
import com.example.data.model.AnimalLessonItem
import com.example.data.model.AnimalSoundType
import com.example.data.model.AppLanguage
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.PI
import kotlin.math.sin

// =========================================================================================
// CLEAN AI VOICE TEACHER API INTEGRATION LAYER (Gemini API via BuildConfig.GEMINI_API_KEY)
// =========================================================================================
// Configure GEMINI_API_KEY in the AI Studio Secrets panel (mapped via .env / .env.example).
// When configured and online, AiTeacherApiClient can generate dynamic child-safe Hindi/English
// encouragement phrases using gemini-3.5-flash. When offline or unconfigured, the app
// automatically uses its built-in offline bilingual voice library and Android TextToSpeech.
// =========================================================================================

@JsonClass(generateAdapter = true)
data class GeminiPart(val text: String)

@JsonClass(generateAdapter = true)
data class GeminiContent(val parts: List<GeminiPart>)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(val content: GeminiContent?)

@JsonClass(generateAdapter = true)
data class GeminiResponse(val candidates: List<GeminiCandidate>?)

interface GeminiTeacherApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateTeacherEncouragement(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object AiTeacherApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    val configuredApiKey: String
        get() = runCatching {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String
        }.getOrNull().orEmpty()

    val isApiKeyConfigured: Boolean
        get() {
            val key = configuredApiKey
            return key.isNotBlank() && key != "MY_GEMINI_API_KEY" && !key.startsWith("YOUR_")
        }

    private val apiService: GeminiTeacherApiService by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiTeacherApiService::class.java)
    }

    suspend fun fetchEncouragementOrFallback(
        topic: String,
        childName: String,
        language: AppLanguage,
        isPositive: Boolean
    ): String = withContext(Dispatchers.IO) {
        if (!isApiKeyConfigured) {
            return@withContext getOfflinePhrase(language, isPositive, childName)
        }
        try {
            val langPrompt = if (language == AppLanguage.HINDI) {
                "Respond in simple, joyful Hindi (Devanagari script) in 1 short sentence (max 8 words) for a 3-year-old child named $childName."
            } else {
                "Respond in simple, cheerful English in 1 short sentence (max 8 words) for a 3-year-old child named $childName."
            }
            val actionDesc = if (isPositive) {
                "The child just learned '$topic' and got a star! Praise them warmly."
            } else {
                "The child tapped the wrong answer on '$topic'. Gently encourage them to try again."
            }
            val response = apiService.generateTeacherEncouragement(
                apiKey = configuredApiKey,
                request = GeminiRequest(
                    contents = listOf(GeminiContent(listOf(GeminiPart("$actionDesc $langPrompt")))),
                    systemInstruction = GeminiContent(
                        listOf(
                            GeminiPart(
                                "You are Didi, a warm, loving preschool teacher in 'Masti Ki Pathshala'. " +
                                    "Use short, simple, encouraging sentences only. Never use complex words."
                            )
                        )
                    )
                )
            )
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
            if (!text.isNullOrBlank()) text else getOfflinePhrase(language, isPositive, childName)
        } catch (_: Exception) {
            getOfflinePhrase(language, isPositive, childName)
        }
    }

    fun getOfflinePhrase(language: AppLanguage, isPositive: Boolean, childName: String = ""): String {
        val cleanName = childName.substringBefore("(").trim()
        return if (language == AppLanguage.HINDI) {
            if (isPositive) {
                listOf(
                    "बहुत बढ़िया $cleanName! शाबाश!",
                    "शाबाश! तुमने बिल्कुल सही बताया!",
                    "वाह! तुम तो सुपर स्टार हो!",
                    "बहुत अच्छे! ऐसे ही सीखते रहो!",
                    "कमाल कर दिया! एक चमकता तारा तुम्हारे लिए!"
                ).random()
            } else {
                listOf(
                    "कोई बात नहीं, एक बार फिर कोशिश करो!",
                    "चलो फिर से ध्यान से देखते हैं!",
                    "एक बार फिर कोशिश करो, तुम कर सकते हो!"
                ).random()
            }
        } else {
            if (isPositive) {
                listOf(
                    "Very good! Great job $cleanName!",
                    "Superstar! You got it right!",
                    "Wonderful job! Keep shining!",
                    "Yay! That was amazing!",
                    "Awesome work! Here is a star for you!"
                ).random()
            } else {
                listOf(
                    "Nice try! Let's try one more time!",
                    "Almost there! Try again!",
                    "You can do it! Give it another tap!"
                ).random()
            }
        }
    }
}

class AiTeacherAndAudioManager(context: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _teacherMessage = MutableStateFlow("नमस्ते बच्चों! चलो खेल-खेल में सीखें! 🌟")
    val teacherMessage: StateFlow<String> = _teacherMessage.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isTtsReady = true
                    tts?.setPitch(1.18f) // Friendly, cheerful child-teacher pitch
                    tts?.setSpeechRate(0.88f) // Clear, unhurried pronunciation for young kids
                }
            }
        } catch (_: Exception) {
            isTtsReady = false
        }
    }

    fun speak(text: String, language: AppLanguage, updateBanner: Boolean = true) {
        if (updateBanner) {
            _teacherMessage.value = text
        }
        scope.launch {
            _isSpeaking.value = true
            playPopTone()
            speakInternal(text, language)
            _isSpeaking.value = false
        }
    }

    fun speakBilingual(textHi: String, textEn: String, preferredLanguage: AppLanguage) {
        val chosen = if (preferredLanguage == AppLanguage.HINDI) textHi else textEn
        speak(chosen, preferredLanguage, updateBanner = true)
    }

    fun encourageSuccess(topic: String, childName: String, language: AppLanguage) {
        val immediate = AiTeacherApiClient.getOfflinePhrase(language, isPositive = true, childName = childName)
        _teacherMessage.value = immediate
        scope.launch {
            playSuccessFanfare()
            speakInternal(immediate, language)
            if (AiTeacherApiClient.isApiKeyConfigured) {
                val aiPhrase = AiTeacherApiClient.fetchEncouragementOrFallback(topic, childName, language, true)
                _teacherMessage.value = aiPhrase
            }
        }
    }

    fun encourageRetry(topic: String, childName: String, language: AppLanguage) {
        val phrase = AiTeacherApiClient.getOfflinePhrase(language, isPositive = false, childName = childName)
        _teacherMessage.value = phrase
        scope.launch {
            playGentleRetryTone()
            speakInternal(phrase, language)
        }
    }

    fun playAnimalSoundAndVoice(animal: AnimalLessonItem, language: AppLanguage) {
        val speech = if (language == AppLanguage.HINDI) {
            "${animal.nameHi}! ${animal.nameHi} बोलता है ${animal.soundWordHi} ${animal.factHi}"
        } else {
            "${animal.nameEn}! The ${animal.nameEn} says ${animal.soundWordEn} ${animal.factEn}"
        }
        _teacherMessage.value = speech
        scope.launch {
            _isSpeaking.value = true
            synthesizeAnimalSound(animal.soundType)
            speakInternal(speech, language)
            _isSpeaking.value = false
        }
    }

    private fun speakInternal(text: String, language: AppLanguage) {
        val engine = tts ?: return
        if (!isTtsReady) return
        try {
            val locale = if (language == AppLanguage.HINDI) {
                Locale.forLanguageTag("hi-IN")
            } else {
                Locale.forLanguageTag("en-IN")
            }
            val result = engine.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                engine.language = Locale.US
            }
            val cleanSpeech = text.replace(Regex("[\\p{So}\\p{Cn}]"), "")
            engine.speak(cleanSpeech, TextToSpeech.QUEUE_FLUSH, null, "pathshala_utt_${System.currentTimeMillis()}")
        } catch (_: Exception) {
            // Ignore TTS hardware exceptions in headless test environments
        }
    }

    fun playPopTone() {
        scope.launch(Dispatchers.Default) {
            playFrequencySequence(listOf(520.0 to 45, 680.0 to 55))
        }
    }

    fun playSuccessFanfare() {
        scope.launch(Dispatchers.Default) {
            // Cheerful C-E-G-C major arpeggio chime
            playFrequencySequence(
                listOf(
                    523.25 to 80,
                    659.25 to 80,
                    783.99 to 90,
                    1046.50 to 180
                )
            )
        }
    }

    fun playGentleRetryTone() {
        scope.launch(Dispatchers.Default) {
            playFrequencySequence(listOf(360.0 to 100, 300.0 to 140))
        }
    }

    private suspend fun synthesizeAnimalSound(soundType: AnimalSoundType) = withContext(Dispatchers.Default) {
        val pattern: List<Pair<Double, Int>> = when (soundType) {
            AnimalSoundType.DOG -> listOf(220.0 to 110, 160.0 to 90, 230.0 to 110, 150.0 to 100)
            AnimalSoundType.CAT -> listOf(580.0 to 120, 660.0 to 140, 520.0 to 160)
            AnimalSoundType.COW -> listOf(150.0 to 180, 140.0 to 260, 130.0 to 180)
            AnimalSoundType.LION -> listOf(130.0 to 160, 110.0 to 220, 95.0 to 260)
            AnimalSoundType.ELEPHANT -> listOf(440.0 to 120, 620.0 to 180, 740.0 to 220)
            AnimalSoundType.MONKEY -> listOf(500.0 to 80, 650.0 to 80, 520.0 to 80, 700.0 to 90)
            AnimalSoundType.HORSE -> listOf(600.0 to 90, 520.0 to 90, 440.0 to 110, 380.0 to 120)
            AnimalSoundType.GOAT -> listOf(420.0 to 90, 400.0 to 90, 420.0 to 120)
            AnimalSoundType.TIGER -> listOf(140.0 to 150, 115.0 to 200, 100.0 to 220)
            AnimalSoundType.RABBIT -> listOf(780.0 to 60, 880.0 to 60, 960.0 to 70)
            AnimalSoundType.PEACOCK -> listOf(680.0 to 140, 860.0 to 160, 640.0 to 150)
            AnimalSoundType.BEAR -> listOf(135.0 to 180, 120.0 to 220)
        }
        playFrequencySequence(pattern)
    }

    private fun playFrequencySequence(notes: List<Pair<Double, Int>>) {
        try {
            val sampleRate = 22050
            val totalDurationMs = notes.sumOf { it.second }
            val numSamples = (sampleRate * totalDurationMs) / 1000
            if (numSamples <= 0) return
            val samples = ShortArray(numSamples)
            var currentIndex = 0

            for ((freq, durationMs) in notes) {
                val noteSamples = (sampleRate * durationMs) / 1000
                for (i in 0 until noteSamples) {
                    if (currentIndex >= numSamples) break
                    val envelope = if (i < noteSamples / 6) {
                        i.toDouble() / (noteSamples / 6.0)
                    } else if (i > (noteSamples * 4) / 5) {
                        (noteSamples - i).toDouble() / (noteSamples / 5.0)
                    } else {
                        1.0
                    }
                    val angle = 2.0 * PI * i * freq / sampleRate
                    samples[currentIndex++] = (sin(angle) * Short.MAX_VALUE * 0.32 * envelope).toInt().toShort()
                }
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(samples.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(samples, 0, samples.size)
            audioTrack.play()
        } catch (_: Exception) {
            // Safe fallback if audio device is unavailable
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {
        }
    }
}
