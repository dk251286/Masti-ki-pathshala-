package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.content.CurriculumRepository
import com.example.data.local.LessonProgressEntity
import com.example.data.model.AppLanguage
import com.example.data.model.SubjectType
import com.example.ui.components.AiTeacherBanner
import com.example.ui.components.AnimatedSparkleBox
import com.example.ui.components.LessonNavigationRow
import com.example.ui.components.LessonTopBar
import com.example.ui.theme.*

@Composable
fun HindiLearningScreen(
    language: AppLanguage,
    stars: Int,
    completedLessons: List<LessonProgressEntity>,
    teacherMessage: String,
    isTeacherSpeaking: Boolean,
    onSpeakHindi: (String) -> Unit,
    onCompleteItem: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var showSwar by remember { mutableStateOf(true) }
    val activeList = if (showSwar) CurriculumRepository.hindiSwar else CurriculumRepository.hindiVyanjan
    var selectedIndex by remember(showSwar) { mutableIntStateOf(0) }
    val currentItem = activeList.getOrElse(selectedIndex) { activeList.first() }
    val isCompleted = completedLessons.any { it.itemId == currentItem.id }

    // Speak whenever the selected letter changes
    LaunchedEffect(currentItem.id) {
        onSpeakHindi("${currentItem.letter} से ${currentItem.wordHi}! ${currentItem.phraseHi}")
    }

    val infiniteFloat = rememberInfiniteTransition(label = "hindi_float")
    val emojiScale by infiniteFloat.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emoji_bounce"
    )

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "हिंदी वर्णमाला सीखें",
                titleEn = "Learn Hindi Varnamala",
                language = language,
                stars = stars,
                accentColor = CardHindiBg,
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
            // Swar vs Vyanjan Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showSwar = true },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showSwar) CardHindiBg else Color.White,
                        contentColor = if (showSwar) Color.White else DeepInk
                    ),
                    border = BorderStroke(2.dp, CardHindiBg),
                    modifier = Modifier.weight(1f).height(50.dp).testTag("tab_hindi_swar")
                ) {
                    Text(
                        text = "स्वर (अ – अः)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { showSwar = false },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!showSwar) CardHindiBg else Color.White,
                        contentColor = if (!showSwar) Color.White else DeepInk
                    ),
                    border = BorderStroke(2.dp, CardHindiBg),
                    modifier = Modifier.weight(1f).height(50.dp).testTag("tab_hindi_vyanjan")
                ) {
                    Text(
                        text = "व्यंजन (क – म)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Horizontal Letter Strip for Quick Tapping
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(activeList) { index, item ->
                    val isSelected = index == selectedIndex
                    val done = completedLessons.any { it.itemId == item.id }
                    Surface(
                        onClick = { selectedIndex = index },
                        color = if (isSelected) item.cardColor else Color.White,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            width = if (isSelected) 3.dp else 1.5.dp,
                            color = if (done) MeadowGreen else item.cardColor
                        ),
                        shadowElevation = if (isSelected) 6.dp else 2.dp,
                        modifier = Modifier
                            .size(60.dp)
                            .testTag("hindi_letter_chip_${item.letter}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = item.letter,
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (isSelected) Color.White else DeepInk,
                                fontWeight = FontWeight.ExtraBold
                            )
                            if (done) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(MeadowGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Main Large Interactive Letter & Picture Card
            Card(
                onClick = {
                    onSpeakHindi("${currentItem.letter} से ${currentItem.wordHi}! ${currentItem.phraseHi}")
                },
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(4.dp, currentItem.cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hindi_main_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = currentItem.cardColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "${selectedIndex + 1} / ${activeList.size}",
                                style = MaterialTheme.typography.labelLarge,
                                color = currentItem.cardColor,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        FilledIconButton(
                            onClick = {
                                onSpeakHindi("${currentItem.letter} से ${currentItem.wordHi}! ${currentItem.phraseHi}")
                            },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = currentItem.cardColor,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play Hindi Voice")
                        }
                    }

                    // Large Letter + Bouncing Picture side by side
                    AnimatedSparkleBox(
                        accentColor = currentItem.cardColor,
                        triggerKey = currentItem.id,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = currentItem.letter,
                                fontSize = 104.sp,
                                fontFamily = BalooFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                color = currentItem.cardColor
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(132.dp)
                                    .scale(emojiScale)
                                    .clip(CircleShape)
                                    .background(currentItem.cardColor.copy(alpha = 0.14f))
                            ) {
                                Text(text = currentItem.emoji, fontSize = 76.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = currentItem.cardColor,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = "${currentItem.letter} से ${currentItem.wordHi}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${currentItem.wordTranslit} • ${currentItem.meaningEn}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MutedSlate
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentItem.phraseHi,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DeepInk,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Repeat, Previous, Next & Complete Controls
            LessonNavigationRow(
                language = language,
                accentColor = currentItem.cardColor,
                isCompleted = isCompleted,
                onPrevious = {
                    selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else activeList.lastIndex
                },
                onRepeat = {
                    onSpeakHindi("${currentItem.letter} से ${currentItem.wordHi}! ${currentItem.phraseHi}")
                },
                onNext = {
                    selectedIndex = if (selectedIndex < activeList.lastIndex) selectedIndex + 1 else 0
                },
                onMarkDone = {
                    onCompleteItem(
                        currentItem.id,
                        "${currentItem.letter} से ${currentItem.wordHi}",
                        "${currentItem.letter} for ${currentItem.wordTranslit}"
                    )
                }
            )

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = {
                    onSpeakHindi("${currentItem.letter} से ${currentItem.wordHi}! ${currentItem.phraseHi}")
                }
            )
        }
    }
}

@Composable
fun EnglishLearningScreen(
    language: AppLanguage,
    stars: Int,
    completedLessons: List<LessonProgressEntity>,
    teacherMessage: String,
    isTeacherSpeaking: Boolean,
    onSpeakEnglish: (String) -> Unit,
    onCompleteItem: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val letters = CurriculumRepository.englishAlphabet
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentItem = letters[selectedIndex]
    val isCompleted = completedLessons.any { it.itemId == currentItem.id }

    LaunchedEffect(currentItem.id) {
        onSpeakEnglish("${currentItem.letter}! ${currentItem.letter} for ${currentItem.wordEn}! ${currentItem.phraseEn}")
    }

    val infiniteFloat = rememberInfiniteTransition(label = "english_float")
    val emojiScale by infiniteFloat.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "en_emoji_bounce"
    )

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "English A to Z सीखें",
                titleEn = "Learn A to Z Alphabet",
                language = language,
                stars = stars,
                accentColor = CardEnglishBg,
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
            // A-Z Horizontal Selector Strip
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(letters) { index, item ->
                    val isSelected = index == selectedIndex
                    val done = completedLessons.any { it.itemId == item.id }
                    Surface(
                        onClick = { selectedIndex = index },
                        color = if (isSelected) item.cardColor else Color.White,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            width = if (isSelected) 3.dp else 1.5.dp,
                            color = if (done) MeadowGreen else item.cardColor
                        ),
                        shadowElevation = if (isSelected) 6.dp else 2.dp,
                        modifier = Modifier
                            .size(58.dp)
                            .testTag("english_letter_chip_${item.letter}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = item.letter,
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (isSelected) Color.White else DeepInk,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            // Large Letter & Picture Card
            Card(
                onClick = {
                    onSpeakEnglish("${currentItem.letter}! ${currentItem.letter} for ${currentItem.wordEn}! ${currentItem.phraseEn}")
                },
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(4.dp, currentItem.cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("english_main_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = currentItem.cardColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Letter ${selectedIndex + 1} of 26",
                                style = MaterialTheme.typography.labelLarge,
                                color = currentItem.cardColor,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        FilledIconButton(
                            onClick = {
                                onSpeakEnglish("${currentItem.letter}! ${currentItem.letter} for ${currentItem.wordEn}! ${currentItem.phraseEn}")
                            },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = currentItem.cardColor,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play English Pronunciation")
                        }
                    }

                    AnimatedSparkleBox(
                        accentColor = currentItem.cardColor,
                        triggerKey = currentItem.id,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${currentItem.letter}${currentItem.letter.lowercase()}",
                                    fontSize = 86.sp,
                                    fontFamily = BalooFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = currentItem.cardColor
                                )
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(132.dp)
                                    .scale(emojiScale)
                                    .clip(CircleShape)
                                    .background(currentItem.cardColor.copy(alpha = 0.14f))
                            ) {
                                Text(text = currentItem.emoji, fontSize = 76.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = currentItem.cardColor,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = "${currentItem.letter} for ${currentItem.wordEn}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "हिंदी अर्थ: ${currentItem.wordHi}",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepInk,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentItem.phraseEn,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MutedSlate,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LessonNavigationRow(
                language = language,
                accentColor = currentItem.cardColor,
                isCompleted = isCompleted,
                onPrevious = {
                    selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else letters.lastIndex
                },
                onRepeat = {
                    onSpeakEnglish("${currentItem.letter}! ${currentItem.letter} for ${currentItem.wordEn}! ${currentItem.phraseEn}")
                },
                onNext = {
                    selectedIndex = if (selectedIndex < letters.lastIndex) selectedIndex + 1 else 0
                },
                onMarkDone = {
                    onCompleteItem(
                        currentItem.id,
                        "${currentItem.letter} फॉर ${currentItem.wordEn} (${currentItem.wordHi})",
                        "${currentItem.letter} for ${currentItem.wordEn}"
                    )
                }
            )

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = {
                    onSpeakEnglish("${currentItem.letter}! ${currentItem.letter} for ${currentItem.wordEn}!")
                }
            )
        }
    }
}
