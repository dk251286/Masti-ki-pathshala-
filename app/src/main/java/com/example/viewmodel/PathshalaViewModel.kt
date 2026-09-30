package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AiTeacherAndAudioManager
import com.example.data.content.CurriculumRepository
import com.example.data.local.AppDatabase
import com.example.data.local.ChildProfileEntity
import com.example.data.local.LessonProgressEntity
import com.example.data.local.PathshalaRepository
import com.example.data.local.QuizScoreEntity
import com.example.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AppScreen {
    data object Home : AppScreen()
    data object HindiLearning : AppScreen()
    data object EnglishLearning : AppScreen()
    data object Counting : AppScreen()
    data object Colors : AppScreen()
    data object Animals : AppScreen()
    data object Fruits : AppScreen()
    data object Rhymes : AppScreen()
    data object GamesHub : AppScreen()
    data class ActiveGame(val gameType: GameType) : AppScreen()
    data object Rewards : AppScreen()
    data object ParentDashboard : AppScreen()
}

data class StarRewardEvent(
    val titleHi: String,
    val titleEn: String,
    val starsEarned: Int
)

class PathshalaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = PathshalaRepository(database.pathshalaDao())
    val audioManager = AiTeacherAndAudioManager(application)

    private val _screenStack = MutableStateFlow<List<AppScreen>>(listOf(AppScreen.Home))
    val currentScreen: StateFlow<AppScreen> = _screenStack
        .map { it.lastOrNull() ?: AppScreen.Home }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppScreen.Home)

    val profile: StateFlow<ChildProfileEntity> = repository.profileFlow
        .map { it ?: ChildProfileEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChildProfileEntity())

    val completedLessons: StateFlow<List<LessonProgressEntity>> = repository.lessonProgressFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizScores: StateFlow<List<QuizScoreEntity>> = repository.quizScoresFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _starRewardEvent = MutableStateFlow<StarRewardEvent?>(null)
    val starRewardEvent: StateFlow<StarRewardEvent?> = _starRewardEvent.asStateFlow()

    val currentLanguage: StateFlow<AppLanguage> = profile
        .map { if (it.languageCode == "en") AppLanguage.ENGLISH else AppLanguage.HINDI }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppLanguage.HINDI)

    val currentAgeGroup: StateFlow<AgeGroup> = profile
        .map { AgeGroup.fromId(it.ageGroupId) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AgeGroup.AGE_3_4)

    init {
        viewModelScope.launch {
            repository.ensureDefaultProfile()
        }
        // Track active learning time every 60 seconds
        viewModelScope.launch {
            while (true) {
                delay(60_000L)
                repository.addLearningMinutes(1)
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        audioManager.playPopTone()
        _screenStack.update { stack ->
            if (screen == AppScreen.Home) listOf(AppScreen.Home) else stack + screen
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        return if (current.size > 1) {
            audioManager.playPopTone()
            _screenStack.update { it.dropLast(1) }
            true
        } else {
            false
        }
    }

    fun toggleLanguage() {
        val nextCode = if (currentLanguage.value == AppLanguage.HINDI) "en" else "hi"
        viewModelScope.launch {
            repository.updateLanguage(nextCode)
            val greeting = if (nextCode == "hi") {
                "नमस्ते! अब हम हिंदी में सीखेंगे!"
            } else {
                "Hello! Now we will learn in English!"
            }
            audioManager.speak(
                greeting,
                if (nextCode == "hi") AppLanguage.HINDI else AppLanguage.ENGLISH
            )
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            repository.updateLanguage(language.code)
        }
    }

    fun selectAgeGroup(ageGroup: AgeGroup) {
        viewModelScope.launch {
            repository.updateAgeGroup(ageGroup.id)
            val msg = if (currentLanguage.value == AppLanguage.HINDI) {
                "आयु वर्ग ${ageGroup.labelHi} चुना गया! ${ageGroup.tipHi}"
            } else {
                "Selected ${ageGroup.labelEn}! ${ageGroup.tipEn}"
            }
            audioManager.speak(msg, currentLanguage.value)
        }
    }

    fun updateChildProfile(name: String, avatarEmoji: String, ageGroup: AgeGroup) {
        viewModelScope.launch {
            repository.updateChildProfile(name, avatarEmoji, ageGroup.id)
        }
    }

    fun completeLessonItem(
        itemId: String,
        subject: SubjectType,
        itemTitleHi: String,
        itemTitleEn: String,
        showPopup: Boolean = false
    ) {
        val alreadyDone = completedLessons.value.any { it.itemId == itemId }
        viewModelScope.launch {
            val stars = if (alreadyDone) 1 else 2
            repository.markLessonCompleted(itemId, subject.id, bonusStars = stars)
            audioManager.encourageSuccess(
                topic = if (currentLanguage.value == AppLanguage.HINDI) itemTitleHi else itemTitleEn,
                childName = profile.value.childName,
                language = currentLanguage.value
            )
            if (showPopup) {
                _starRewardEvent.value = StarRewardEvent(
                    titleHi = "शाबाश! $itemTitleHi सीख लिया!",
                    titleEn = "Great job learning $itemTitleEn!",
                    starsEarned = stars
                )
            }
        }
    }

    fun finishGameAndReward(gameType: GameType, score: Int, totalQuestions: Int) {
        val starsEarned = (score * 2).coerceAtLeast(2)
        viewModelScope.launch {
            repository.recordQuizScore(
                gameTypeId = gameType.id,
                gameTitle = if (currentLanguage.value == AppLanguage.HINDI) gameType.titleHi else gameType.titleEn,
                score = score,
                totalQuestions = totalQuestions,
                starsEarned = starsEarned
            )
            repository.addLearningMinutes(1)
            audioManager.encourageSuccess(
                topic = gameType.titleEn,
                childName = profile.value.childName,
                language = currentLanguage.value
            )
            _starRewardEvent.value = StarRewardEvent(
                titleHi = "${gameType.titleHi} पूरा हुआ! स्कोर: $score/$totalQuestions",
                titleEn = "${gameType.titleEn} Complete! Score: $score/$totalQuestions",
                starsEarned = starsEarned
            )
        }
    }

    fun dismissStarReward() {
        _starRewardEvent.value = null
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }

    fun generateQuestionsForGame(gameType: GameType): List<QuizQuestion> {
        return when (gameType) {
            GameType.FIND_LETTER -> generateFindLetterQuestions()
            GameType.MATCH_LETTER_PICTURE -> generateMatchLetterPictureQuestions()
            GameType.MATCH_HINDI_WORD_PIC -> generateMatchHindiWordQuestions()
            GameType.MATCH_ENGLISH_WORD_PIC -> generateMatchEnglishWordQuestions()
            GameType.COUNT_OBJECTS -> generateCountObjectsQuestions()
            GameType.IDENTIFY_COLORS -> generateIdentifyColorsQuestions()
            GameType.IDENTIFY_ANIMALS -> generateIdentifyAnimalsQuestions()
            GameType.IDENTIFY_FRUITS -> generateIdentifyFruitsQuestions()
            GameType.MEMORY_MATCH -> emptyList() // Memory match uses its own interactive card board
            GameType.SIMPLE_QUIZ -> generateMixedQuizQuestions()
        }
    }

    private fun generateFindLetterQuestions(): List<QuizQuestion> {
        val pool = CurriculumRepository.hindiSwar.take(8)
        val enPool = CurriculumRepository.englishAlphabet.take(8)
        val questions = mutableListOf<QuizQuestion>()

        pool.shuffled().take(3).forEach { target ->
            val distractors = (pool - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            questions.add(
                QuizQuestion(
                    promptHi = "अक्षर '${target.letter}' (${target.wordHi}) को खोजो और दबाओ!",
                    promptEn = "Find and tap the Hindi letter '${target.letter}' (${target.wordTranslit})!",
                    speechTextHi = "${target.letter} से ${target.wordHi}! अक्षर ${target.letter} कहाँ है?",
                    speechTextEn = "Find the letter ${target.letter} for ${target.wordTranslit}!",
                    centerEmoji = target.emoji,
                    centerLabel = "${target.letter} - ${target.wordHi}",
                    options = opts.map { QuizOption(it.letter, it.wordHi, it.emoji) },
                    correctIndex = opts.indexOf(target)
                )
            )
        }

        enPool.shuffled().take(2).forEach { target ->
            val distractors = (enPool - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            questions.add(
                QuizQuestion(
                    promptHi = "अंग्रेज़ी अक्षर '${target.letter}' (${target.wordEn}) को पहचानो!",
                    promptEn = "Find and tap the letter '${target.letter}' for ${target.wordEn}!",
                    speechTextHi = "${target.letter} फॉर ${target.wordEn} कहाँ है?",
                    speechTextEn = "Where is the letter ${target.letter} for ${target.wordEn}?",
                    centerEmoji = target.emoji,
                    centerLabel = "${target.letter} for ${target.wordEn}",
                    options = opts.map { QuizOption(it.letter, it.wordEn, it.emoji) },
                    correctIndex = opts.indexOf(target)
                )
            )
        }
        return questions
    }

    private fun generateMatchLetterPictureQuestions(): List<QuizQuestion> {
        val hiItems = CurriculumRepository.allHindiLetters.shuffled().take(3)
        val enItems = CurriculumRepository.englishAlphabet.shuffled().take(2)
        val result = mutableListOf<QuizQuestion>()

        hiItems.forEach { target ->
            val distractors = (CurriculumRepository.allHindiLetters - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            result.add(
                QuizQuestion(
                    promptHi = "'${target.letter}' अक्षर से कौन-सा चित्र शुरू होता है?",
                    promptEn = "Which picture starts with '${target.letter}'?",
                    speechTextHi = "${target.letter} अक्षर से कौन-सा चित्र शुरू होता है?",
                    speechTextEn = "Tap the picture that starts with ${target.letter}!",
                    centerEmoji = "🔤",
                    centerLabel = target.letter,
                    options = opts.map { QuizOption(it.wordHi, it.meaningEn, it.emoji) },
                    correctIndex = opts.indexOf(target)
                )
            )
        }

        enItems.forEach { target ->
            val distractors = (CurriculumRepository.englishAlphabet - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            result.add(
                QuizQuestion(
                    promptHi = "'${target.letter}' से शुरू होने वाला चित्र चुनो!",
                    promptEn = "Which picture starts with letter '${target.letter}'?",
                    speechTextHi = "${target.letter} से कौन सा चित्र बनता है?",
                    speechTextEn = "Which picture starts with the letter ${target.letter}?",
                    centerEmoji = "🔠",
                    centerLabel = target.letter,
                    options = opts.map { QuizOption(it.wordEn, it.wordHi, it.emoji) },
                    correctIndex = opts.indexOf(target)
                )
            )
        }
        return result
    }

    private fun generateMatchHindiWordQuestions(): List<QuizQuestion> {
        val pool = CurriculumRepository.allHindiLetters
        return pool.shuffled().take(5).map { target ->
            val distractors = (pool - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            QuizQuestion(
                promptHi = "शब्द '${target.wordHi}' का सही चित्र चुनो!",
                promptEn = "Match the Hindi word '${target.wordHi}' (${target.wordTranslit}) with its picture!",
                speechTextHi = "${target.wordHi} का चित्र कहाँ है? उसे छुओ!",
                speechTextEn = "Find the picture for the Hindi word ${target.wordTranslit}!",
                centerEmoji = "🪔",
                centerLabel = target.wordHi,
                options = opts.map { QuizOption(it.wordHi, "${it.letter} से ${it.wordHi}", it.emoji) },
                correctIndex = opts.indexOf(target)
            )
        }
    }

    private fun generateMatchEnglishWordQuestions(): List<QuizQuestion> {
        val pool = CurriculumRepository.englishAlphabet
        return pool.shuffled().take(5).map { target ->
            val distractors = (pool - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            QuizQuestion(
                promptHi = "अंग्रेज़ी शब्द '${target.wordEn}' का सही चित्र चुनो!",
                promptEn = "Tap the picture for the English word '${target.wordEn}'!",
                speechTextHi = "${target.wordEn} यानी ${target.wordHi} का चित्र चुनो!",
                speechTextEn = "Tap the picture for ${target.wordEn}!",
                centerEmoji = "📖",
                centerLabel = target.wordEn,
                options = opts.map { QuizOption(it.wordEn, it.wordHi, it.emoji) },
                correctIndex = opts.indexOf(target)
            )
        }
    }

    private fun generateCountObjectsQuestions(): List<QuizQuestion> {
        val pool = CurriculumRepository.countingLessons
        return pool.shuffled().take(5).map { target ->
            val distractors = (pool - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            val repeatedEmojis = List(target.number) { target.emoji }.joinToString(" ")
            QuizQuestion(
                promptHi = "गिनो यहाँ कितने ${target.objectNameHi} हैं?",
                promptEn = "Count how many ${target.objectNameEn} are here!",
                speechTextHi = "ध्यान से गिनो, यहाँ कुल कितने ${target.objectNameHi} हैं?",
                speechTextEn = "Count carefully! How many ${target.objectNameEn} do you see?",
                centerEmoji = repeatedEmojis,
                centerLabel = "${target.objectNameHi} (${target.objectNameEn})",
                options = opts.map { QuizOption("${it.digitHi} (${it.digitEn})", "${it.wordHi} / ${it.wordEn}", it.emoji) },
                correctIndex = opts.indexOf(target)
            )
        }
    }

    private fun generateIdentifyColorsQuestions(): List<QuizQuestion> {
        val pool = CurriculumRepository.colorsLessons
        return pool.shuffled().take(5).map { target ->
            val distractors = (pool - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            QuizQuestion(
                promptHi = "'${target.nameHi} (${target.nameEn})' रंग कौन-सा है?",
                promptEn = "Find and tap the color '${target.nameEn} (${target.nameHi})'!",
                speechTextHi = "${target.nameHi} रंग कहाँ है? ${target.objectHi}!",
                speechTextEn = "Tap the ${target.nameEn} color! Like ${target.objectEn}!",
                centerEmoji = target.emoji,
                centerLabel = "${target.nameHi} / ${target.nameEn}",
                options = opts.map {
                    QuizOption(
                        mainText = it.nameHi,
                        subText = it.nameEn,
                        emoji = it.emoji
                    )
                },
                correctIndex = opts.indexOf(target)
            )
        }
    }

    private fun generateIdentifyAnimalsQuestions(): List<QuizQuestion> {
        val pool = CurriculumRepository.animalsLessons
        return pool.shuffled().take(5).map { target ->
            val distractors = (pool - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            QuizQuestion(
                promptHi = "कौन बोलता है '${target.soundWordHi}'? ${target.nameHi} को पहचानो!",
                promptEn = "Who says '${target.soundWordEn}'? Tap the ${target.nameEn}!",
                speechTextHi = "${target.nameHi} कहाँ है जो ${target.soundWordHi} बोलता है?",
                speechTextEn = "Where is the ${target.nameEn} that says ${target.soundWordEn}?",
                centerEmoji = target.emoji,
                centerLabel = "${target.nameHi} (${target.nameEn})",
                options = opts.map { QuizOption(it.nameHi, it.nameEn, it.emoji) },
                correctIndex = opts.indexOf(target)
            )
        }
    }

    private fun generateIdentifyFruitsQuestions(): List<QuizQuestion> {
        val pool = CurriculumRepository.fruitsLessons
        return pool.shuffled().take(5).map { target ->
            val distractors = (pool - target).shuffled().take(3)
            val opts = (distractors + target).shuffled()
            QuizQuestion(
                promptHi = "मीठा फल '${target.nameHi} (${target.nameEn})' कौन-सा है?",
                promptEn = "Find the delicious fruit '${target.nameEn} (${target.nameHi})'!",
                speechTextHi = "${target.nameHi} फल को पहचानो और दबाओ!",
                speechTextEn = "Tap the fruit ${target.nameEn}!",
                centerEmoji = target.emoji,
                centerLabel = "${target.nameHi} / ${target.nameEn}",
                options = opts.map { QuizOption(it.nameHi, it.nameEn, it.emoji) },
                correctIndex = opts.indexOf(target)
            )
        }
    }

    private fun generateMixedQuizQuestions(): List<QuizQuestion> {
        return (
            generateFindLetterQuestions().take(1) +
                generateCountObjectsQuestions().take(1) +
                generateIdentifyColorsQuestions().take(1) +
                generateIdentifyAnimalsQuestions().take(1) +
                generateIdentifyFruitsQuestions().take(1)
            ).shuffled()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.shutdown()
    }
}
