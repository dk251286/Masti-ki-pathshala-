package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppLanguage
import com.example.data.model.SubjectType
import com.example.ui.components.StarRewardDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.PathshalaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MastiKiPathshalaApp()
            }
        }
    }
}

@Composable
fun MastiKiPathshalaApp(viewModel: PathshalaViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val ageGroup by viewModel.currentAgeGroup.collectAsStateWithLifecycle()
    val completedLessons by viewModel.completedLessons.collectAsStateWithLifecycle()
    val quizScores by viewModel.quizScores.collectAsStateWithLifecycle()
    val starRewardEvent by viewModel.starRewardEvent.collectAsStateWithLifecycle()
    val teacherMessage by viewModel.audioManager.teacherMessage.collectAsStateWithLifecycle()
    val isTeacherSpeaking by viewModel.audioManager.isSpeaking.collectAsStateWithLifecycle()

    Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
        when (screen) {
            is AppScreen.Home -> {
                HomeScreen(
                    profile = profile,
                    language = language,
                    ageGroup = ageGroup,
                    completedLessons = completedLessons,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onNavigate = { viewModel.navigateTo(it) },
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onSelectAgeGroup = { viewModel.selectAgeGroup(it) },
                    onUpdateProfile = { name, emoji, age ->
                        viewModel.updateChildProfile(name, emoji, age)
                    },
                    onSpeakGreeting = {
                        viewModel.audioManager.speakBilingual(
                            textHi = "नमस्ते ${profile.childName}! मस्ती की पाठशाला में आपका स्वागत है! चलो खेलो, मस्ती करो और सीखो!",
                            textEn = "Hello ${profile.childName}! Welcome to Masti Ki Pathshala! Let's play, have fun and learn!",
                            preferredLanguage = language
                        )
                    }
                )
            }

            is AppScreen.HindiLearning -> {
                HindiLearningScreen(
                    language = language,
                    stars = profile.totalStars,
                    completedLessons = completedLessons,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onSpeakHindi = { text ->
                        viewModel.audioManager.speak(text, AppLanguage.HINDI)
                    },
                    onCompleteItem = { itemId, titleHi, titleEn ->
                        viewModel.completeLessonItem(
                            itemId = itemId,
                            subject = SubjectType.HINDI,
                            itemTitleHi = titleHi,
                            itemTitleEn = titleEn,
                            showPopup = true
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.EnglishLearning -> {
                EnglishLearningScreen(
                    language = language,
                    stars = profile.totalStars,
                    completedLessons = completedLessons,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onSpeakEnglish = { text ->
                        viewModel.audioManager.speak(text, AppLanguage.ENGLISH)
                    },
                    onCompleteItem = { itemId, titleHi, titleEn ->
                        viewModel.completeLessonItem(
                            itemId = itemId,
                            subject = SubjectType.ENGLISH,
                            itemTitleHi = titleHi,
                            itemTitleEn = titleEn,
                            showPopup = true
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.Counting -> {
                CountingScreen(
                    language = language,
                    stars = profile.totalStars,
                    completedLessons = completedLessons,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onSpeakText = { text, lang ->
                        viewModel.audioManager.speak(text, lang)
                    },
                    onCompleteItem = { itemId, titleHi, titleEn ->
                        viewModel.completeLessonItem(
                            itemId = itemId,
                            subject = SubjectType.COUNTING,
                            itemTitleHi = titleHi,
                            itemTitleEn = titleEn,
                            showPopup = true
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.Colors -> {
                ColorsScreen(
                    language = language,
                    stars = profile.totalStars,
                    completedLessons = completedLessons,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onSpeakText = { text, lang ->
                        viewModel.audioManager.speak(text, lang)
                    },
                    onCompleteItem = { itemId, titleHi, titleEn ->
                        viewModel.completeLessonItem(
                            itemId = itemId,
                            subject = SubjectType.COLORS,
                            itemTitleHi = titleHi,
                            itemTitleEn = titleEn,
                            showPopup = true
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.Animals -> {
                AnimalsScreen(
                    language = language,
                    stars = profile.totalStars,
                    completedLessons = completedLessons,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onPlayAnimalSoundAndVoice = { animal ->
                        viewModel.audioManager.playAnimalSoundAndVoice(animal, language)
                    },
                    onCompleteItem = { itemId, titleHi, titleEn ->
                        viewModel.completeLessonItem(
                            itemId = itemId,
                            subject = SubjectType.ANIMALS,
                            itemTitleHi = titleHi,
                            itemTitleEn = titleEn,
                            showPopup = true
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.Fruits -> {
                FruitsScreen(
                    language = language,
                    stars = profile.totalStars,
                    completedLessons = completedLessons,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onSpeakText = { text, lang ->
                        viewModel.audioManager.speak(text, lang)
                    },
                    onCompleteItem = { itemId, titleHi, titleEn ->
                        viewModel.completeLessonItem(
                            itemId = itemId,
                            subject = SubjectType.FRUITS,
                            itemTitleHi = titleHi,
                            itemTitleEn = titleEn,
                            showPopup = true
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.Rhymes -> {
                RhymesScreen(
                    language = language,
                    stars = profile.totalStars,
                    completedLessons = completedLessons,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onSpeakText = { text, lang ->
                        viewModel.audioManager.speak(text, lang)
                    },
                    onCompleteItem = { itemId, titleHi, titleEn ->
                        viewModel.completeLessonItem(
                            itemId = itemId,
                            subject = SubjectType.RHYMES,
                            itemTitleHi = titleHi,
                            itemTitleEn = titleEn,
                            showPopup = true
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.GamesHub -> {
                GamesHubScreen(
                    language = language,
                    stars = profile.totalStars,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    onSelectGame = { gameType ->
                        viewModel.navigateTo(AppScreen.ActiveGame(gameType))
                    },
                    onSpeakGreeting = {
                        viewModel.audioManager.speakBilingual(
                            textHi = "चलो मज़ेदार खेल खेलें और ढेर सारे सितारे जीतें!",
                            textEn = "Let's play fun learning games and win shiny stars!",
                            preferredLanguage = language
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.ActiveGame -> {
                ActiveGameScreen(
                    gameType = screen.gameType,
                    language = language,
                    stars = profile.totalStars,
                    teacherMessage = teacherMessage,
                    isTeacherSpeaking = isTeacherSpeaking,
                    questionsProvider = { viewModel.generateQuestionsForGame(screen.gameType) },
                    onSpeakText = { text, lang ->
                        viewModel.audioManager.speak(text, lang)
                    },
                    onCorrectAnswer = { topic ->
                        viewModel.audioManager.encourageSuccess(topic, profile.childName, language)
                    },
                    onWrongAnswer = { topic ->
                        viewModel.audioManager.encourageRetry(topic, profile.childName, language)
                    },
                    onGameFinished = { score, total ->
                        viewModel.finishGameAndReward(screen.gameType, score, total)
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.Rewards -> {
                RewardsScreen(
                    profile = profile,
                    language = language,
                    completedLessons = completedLessons,
                    onCelebrate = {
                        viewModel.audioManager.encourageSuccess(
                            topic = "Masti Ki Pathshala",
                            childName = profile.childName,
                            language = language
                        )
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.ParentDashboard -> {
                ParentDashboardScreen(
                    profile = profile,
                    language = language,
                    ageGroup = ageGroup,
                    completedLessons = completedLessons,
                    quizScores = quizScores,
                    onSetLanguage = { viewModel.setLanguage(it) },
                    onSelectAgeGroup = { viewModel.selectAgeGroup(it) },
                    onResetProgress = { viewModel.resetProgress() },
                    onBack = { viewModel.navigateBack() }
                )
            }
        }
    }

    starRewardEvent?.let { reward ->
        StarRewardDialog(
            event = reward,
            language = language,
            onDismiss = { viewModel.dismissStarReward() }
        )
    }
}
