package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.GameType
import com.example.data.model.QuizQuestion
import com.example.ui.components.AiTeacherBanner
import com.example.ui.components.LessonTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GamesHubScreen(
    language: AppLanguage,
    stars: Int,
    teacherMessage: String,
    isTeacherSpeaking: Boolean,
    onSelectGame: (GameType) -> Unit,
    onSpeakGreeting: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "१० मज़ेदार खेल (Learning Games)",
                titleEn = "10 Fun Learning Games",
                language = language,
                stars = stars,
                accentColor = CardGamesBg,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AiTeacherBanner(
                    message = teacherMessage,
                    isSpeaking = isTeacherSpeaking,
                    language = language,
                    onSpeakClick = onSpeakGreeting
                )
            }

            items(GameType.entries) { game ->
                val cardAccent = Color(game.accentHex)
                Card(
                    onClick = { onSelectGame(game) },
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(3.dp, cardAccent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("game_card_${game.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(66.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(cardAccent.copy(alpha = 0.16f))
                        ) {
                            Text(text = game.emoji, fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = cardAccent,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "Game #${game.number}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) game.titleHi else game.titleEn,
                                style = MaterialTheme.typography.titleLarge,
                                color = DeepInk,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) game.subtitleHi else game.subtitleEn,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedSlate
                            )
                        }

                        FilledIconButton(
                            onClick = { onSelectGame(game) },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = cardAccent,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActiveGameScreen(
    gameType: GameType,
    language: AppLanguage,
    stars: Int,
    teacherMessage: String,
    isTeacherSpeaking: Boolean,
    questionsProvider: () -> List<QuizQuestion>,
    onSpeakText: (String, AppLanguage) -> Unit,
    onCorrectAnswer: (String) -> Unit,
    onWrongAnswer: (String) -> Unit,
    onGameFinished: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val accentColor = Color(gameType.accentHex)

    if (gameType == GameType.MEMORY_MATCH) {
        MemoryMatchingGameView(
            gameType = gameType,
            language = language,
            stars = stars,
            teacherMessage = teacherMessage,
            isTeacherSpeaking = isTeacherSpeaking,
            onSpeakText = onSpeakText,
            onCorrectAnswer = onCorrectAnswer,
            onGameFinished = onGameFinished,
            onBack = onBack
        )
        return
    }

    var questions by remember(gameType) { mutableStateOf(questionsProvider()) }
    var currentQuestionIdx by remember(questions) { mutableIntStateOf(0) }
    var score by remember(questions) { mutableIntStateOf(0) }
    var selectedOptionIdx by remember(currentQuestionIdx) { mutableStateOf<Int?>(null) }
    var isAnsweredCorrectly by remember(currentQuestionIdx) { mutableStateOf(false) }

    val currentQuestion = questions.getOrElse(currentQuestionIdx) { questions.first() }

    LaunchedEffect(currentQuestionIdx, questions) {
        val speech = if (language == AppLanguage.HINDI) currentQuestion.speechTextHi else currentQuestion.speechTextEn
        onSpeakText(speech, language)
    }

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = gameType.titleHi,
                titleEn = gameType.titleEn,
                language = language,
                stars = stars,
                accentColor = accentColor,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Question Progress Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = accentColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) {
                            "सवाल ${currentQuestionIdx + 1} / ${questions.size}"
                        } else {
                            "Question ${currentQuestionIdx + 1} / ${questions.size}"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    color = SunshineYellowLight,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.5.dp, SunshineYellow)
                ) {
                    Text(
                        text = "⭐ Score: $score",
                        style = MaterialTheme.typography.titleMedium,
                        color = MangoOrange,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            // Question Prompt Card
            Card(
                onClick = {
                    val speech = if (language == AppLanguage.HINDI) currentQuestion.speechTextHi else currentQuestion.speechTextEn
                    onSpeakText(speech, language)
                },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(3.dp, accentColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth().testTag("quiz_prompt_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQuestion.centerEmoji,
                        fontSize = if (currentQuestion.centerEmoji.length > 4) 40.sp else 68.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = accentColor.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = currentQuestion.centerLabel,
                            style = MaterialTheme.typography.headlineMedium,
                            color = accentColor,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) currentQuestion.promptHi else currentQuestion.promptEn,
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepInk,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                val speech = if (language == AppLanguage.HINDI) currentQuestion.speechTextHi else currentQuestion.speechTextEn
                                onSpeakText(speech, language)
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen Question", tint = accentColor)
                        }
                    }
                }
            }

            // 4 Large Child-Friendly Option Buttons (2x2 Grid)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentQuestion.options.forEachIndexed { index, opt ->
                    val isSelected = selectedOptionIdx == index
                    val isCorrectOption = index == currentQuestion.correctIndex

                    val bgColor = when {
                        isAnsweredCorrectly && isCorrectOption -> MeadowGreen
                        isSelected && !isCorrectOption -> Color(0xFFFFCDD2)
                        else -> Color.White
                    }
                    val borderColor = when {
                        isAnsweredCorrectly && isCorrectOption -> MeadowGreenDark
                        isSelected && !isCorrectOption -> Color(0xFFD32F2F)
                        else -> accentColor
                    }
                    val textColor = if (isAnsweredCorrectly && isCorrectOption) Color.White else DeepInk

                    Card(
                        onClick = {
                            if (!isAnsweredCorrectly) {
                                selectedOptionIdx = index
                                if (isCorrectOption) {
                                    isAnsweredCorrectly = true
                                    score += 1
                                    onCorrectAnswer(opt.mainText)
                                } else {
                                    onWrongAnswer(opt.mainText)
                                }
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        border = BorderStroke(3.dp, borderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(126.dp)
                            .testTag("quiz_option_$index")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = opt.emoji, fontSize = 38.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = opt.mainText,
                                style = MaterialTheme.typography.titleLarge,
                                color = textColor,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                            Text(
                                text = opt.subText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isAnsweredCorrectly && isCorrectOption) Color.White.copy(alpha = 0.9f) else MutedSlate,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Next Question / Finish Game Button when answered
            if (isAnsweredCorrectly) {
                Button(
                    onClick = {
                        if (currentQuestionIdx < questions.lastIndex) {
                            currentQuestionIdx += 1
                        } else {
                            onGameFinished(score, questions.size)
                            questions = questionsProvider()
                            currentQuestionIdx = 0
                            score = 0
                        }
                    },
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MeadowGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("next_question_button")
                ) {
                    Text(
                        text = if (currentQuestionIdx < questions.lastIndex) {
                            if (language == AppLanguage.HINDI) "शाबाश! अगला सवाल आगे बढ़ें" else "Awesome! Next Question"
                        } else {
                            if (language == AppLanguage.HINDI) "खेल पूरा करें और ⭐ पाएं!" else "Finish Game & Claim ⭐!"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = {
                    val speech = if (language == AppLanguage.HINDI) currentQuestion.speechTextHi else currentQuestion.speechTextEn
                    onSpeakText(speech, language)
                }
            )
        }
    }
}

private data class MemoryCardState(
    val id: Int,
    val emoji: String,
    val labelHi: String,
    val labelEn: String,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MemoryMatchingGameView(
    gameType: GameType,
    language: AppLanguage,
    stars: Int,
    teacherMessage: String,
    isTeacherSpeaking: Boolean,
    onSpeakText: (String, AppLanguage) -> Unit,
    onCorrectAnswer: (String) -> Unit,
    onGameFinished: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pairsPool = remember {
        listOf(
            Triple("🐘", "हाथी", "Elephant"),
            Triple("🥭", "आम", "Mango"),
            Triple("🦁", "शेर", "Lion"),
            Triple("🍎", "सेब", "Apple"),
            Triple("🦋", "तितली", "Butterfly"),
            Triple("🐰", "खरगोश", "Rabbit")
        )
    }

    fun createDeck(): List<MemoryCardState> {
        val chosen = pairsPool.shuffled().take(4)
        return (chosen + chosen).shuffled().mapIndexed { idx, triple ->
            MemoryCardState(
                id = idx,
                emoji = triple.first,
                labelHi = triple.second,
                labelEn = triple.third
            )
        }
    }

    var cards by remember { mutableStateOf(createDeck()) }
    var firstFlippedIndex by remember { mutableStateOf<Int?>(null) }
    var isBusy by remember { mutableStateOf(false) }
    val matchedPairsCount = cards.count { it.isMatched } / 2

    LaunchedEffect(Unit) {
        val msg = if (language == AppLanguage.HINDI) {
            "कार्ड पलटो और एक जैसे दो चित्रों की जोड़ी मिलाओ!"
        } else {
            "Flip two cards and match the identical pictures!"
        }
        onSpeakText(msg, language)
    }

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = gameType.titleHi,
                titleEn = gameType.titleEn,
                language = language,
                stars = stars,
                accentColor = Color(gameType.accentHex),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) {
                        "मिलाई गई जोड़ियाँ: $matchedPairsCount / 4"
                    } else {
                        "Matched Pairs: $matchedPairsCount / 4"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepInk
                )

                OutlinedButton(
                    onClick = {
                        cards = createDeck()
                        firstFlippedIndex = null
                        isBusy = false
                    },
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "New Board")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (language == AppLanguage.HINDI) "नया खेल" else "Reset")
                }
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                cards.forEachIndexed { index, card ->
                    val showFace = card.isFlipped || card.isMatched
                    Card(
                        onClick = {
                            if (!isBusy && !card.isFlipped && !card.isMatched) {
                                val updated = cards.toMutableList()
                                updated[index] = card.copy(isFlipped = true)
                                cards = updated
                                onSpeakText(
                                    if (language == AppLanguage.HINDI) card.labelHi else card.labelEn,
                                    language
                                )

                                val firstIdx = firstFlippedIndex
                                if (firstIdx == null) {
                                    firstFlippedIndex = index
                                } else {
                                    isBusy = true
                                    val firstCard = cards[firstIdx]
                                    if (firstCard.emoji == card.emoji) {
                                        val matchedList = cards.toMutableList()
                                        matchedList[firstIdx] = firstCard.copy(isMatched = true, isFlipped = true)
                                        matchedList[index] = card.copy(isMatched = true, isFlipped = true)
                                        cards = matchedList
                                        firstFlippedIndex = null
                                        isBusy = false
                                        onCorrectAnswer(card.labelHi)
                                        if (matchedList.all { it.isMatched }) {
                                            onGameFinished(4, 4)
                                        }
                                    } else {
                                        scope.launch {
                                            delay(750L)
                                            val resetList = cards.toMutableList()
                                            resetList[firstIdx] = firstCard.copy(isFlipped = false)
                                            resetList[index] = card.copy(isFlipped = false)
                                            cards = resetList
                                            firstFlippedIndex = null
                                            isBusy = false
                                        }
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                card.isMatched -> MeadowGreen.copy(alpha = 0.2f)
                                showFace -> Color.White
                                else -> Color(gameType.accentHex)
                            }
                        ),
                        border = BorderStroke(
                            width = 3.dp,
                            color = if (card.isMatched) MeadowGreen else Color(gameType.accentHex)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(112.dp)
                            .testTag("memory_card_$index")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (showFace) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = card.emoji, fontSize = 44.sp)
                                    Text(
                                        text = if (language == AppLanguage.HINDI) card.labelHi else card.labelEn,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DeepInk
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "🌟", fontSize = 36.sp)
                                    Text(
                                        text = if (language == AppLanguage.HINDI) "पलटें" else "Tap",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = {
                    val msg = if (language == AppLanguage.HINDI) {
                        "कार्ड पलटो और एक जैसे दो चित्रों की जोड़ी मिलाओ!"
                    } else {
                        "Flip two cards and match the identical pictures!"
                    }
                    onSpeakText(msg, language)
                }
            )
        }
    }
}
