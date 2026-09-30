package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
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
import com.example.data.model.Number100Item
import com.example.ui.components.AiTeacherBanner
import com.example.ui.components.LessonNavigationRow
import com.example.ui.components.LessonTopBar
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CountingScreen(
    language: AppLanguage,
    stars: Int,
    completedLessons: List<LessonProgressEntity>,
    teacherMessage: String,
    isTeacherSpeaking: Boolean,
    onSpeakText: (String, AppLanguage) -> Unit,
    onCompleteItem: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var show1To100Board by remember { mutableStateOf(false) }
    val lessons = CurriculumRepository.countingLessons
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentLesson = lessons[selectedIndex]
    val itemId = "count_${currentLesson.number}"
    val isCompleted = completedLessons.any { it.itemId == itemId }

    // Track which objects the child has tapped so far (interactive counting!)
    val tappedIndices = remember(selectedIndex) { mutableStateListOf<Int>() }

    LaunchedEffect(selectedIndex, show1To100Board) {
        if (!show1To100Board) {
            val intro = if (language == AppLanguage.HINDI) {
                "${currentLesson.digitHi} (${currentLesson.wordHi})! ${currentLesson.wordHi} ${currentLesson.objectNameHi}! गिनने के लिए चित्रों को छुओ!"
            } else {
                "${currentLesson.digitEn} (${currentLesson.wordEn})! ${currentLesson.wordEn} ${currentLesson.objectNameEn}! Tap each picture to count!"
            }
            onSpeakText(intro, language)
        }
    }

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "गिनती सीखें (१–१००)",
                titleEn = "Learn Counting (1–100)",
                language = language,
                stars = stars,
                accentColor = CardCountingBg,
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
            // Mode Switcher: Interactive 1–10 Object Counting vs 1–100 Chart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { show1To100Board = false },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!show1To100Board) CardCountingBg else Color.White,
                        contentColor = if (!show1To100Board) Color.White else DeepInk
                    ),
                    border = BorderStroke(2.dp, CardCountingBg),
                    modifier = Modifier.weight(1f).height(50.dp).testTag("tab_count_1_10")
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "१–१० वस्तुएं गिनें" else "1–10 Tap & Count",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { show1To100Board = true },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (show1To100Board) CardCountingBg else Color.White,
                        contentColor = if (show1To100Board) Color.White else DeepInk
                    ),
                    border = BorderStroke(2.dp, CardCountingBg),
                    modifier = Modifier.weight(1f).height(50.dp).testTag("tab_count_1_100")
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "१–१०० गिनती चार्ट" else "1–100 Chart",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!show1To100Board) {
                // Number 1-10 Strip
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(lessons) { index, item ->
                        val isSelected = index == selectedIndex
                        Surface(
                            onClick = { selectedIndex = index },
                            color = if (isSelected) item.cardColor else Color.White,
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(2.dp, item.cardColor),
                            shadowElevation = if (isSelected) 6.dp else 2.dp,
                            modifier = Modifier
                                .size(62.dp)
                                .testTag("number_chip_${item.number}")
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = item.digitHi,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = if (isSelected) Color.White else DeepInk,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = item.digitEn,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else MutedSlate
                                )
                            }
                        }
                    }
                }

                // Main Interactive Object Counting Card
                Card(
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(4.dp, currentLesson.cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("counting_main_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Big Dual Number Display (Hindi & English)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = currentLesson.digitHi,
                                    fontSize = 74.sp,
                                    fontFamily = BalooFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = currentLesson.cardColor
                                )
                                Text(
                                    text = currentLesson.wordHi,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = DeepInk
                                )
                            }

                            Surface(
                                color = currentLesson.cardColor.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = "=",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = currentLesson.cardColor,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = currentLesson.digitEn,
                                    fontSize = 74.sp,
                                    fontFamily = BalooFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SkyBlueDeep
                                )
                                Text(
                                    text = currentLesson.wordEn,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = DeepInk
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Instruction bar
                        Surface(
                            color = SunshineYellowLight,
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.5.dp, SunshineYellow)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = Icons.Filled.TouchApp,
                                        contentDescription = null,
                                        tint = MangoOrange
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.HINDI) {
                                            "गिनने के लिए हर ${currentLesson.objectNameHi} को छुओ: (${tappedIndices.size}/${currentLesson.number})"
                                        } else {
                                            "Tap each ${currentLesson.objectNameEn} to count: (${tappedIndices.size}/${currentLesson.number})"
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepInk
                                    )
                                }

                                if (tappedIndices.isNotEmpty()) {
                                    IconButton(
                                        onClick = { tappedIndices.clear() },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Filled.Refresh, contentDescription = "Reset Count", tint = MangoOrange)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Tappable Objects Grid (FlowRow)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (objIndex in 0 until currentLesson.number) {
                                val isTapped = tappedIndices.contains(objIndex)
                                val countNumber = if (isTapped) tappedIndices.indexOf(objIndex) + 1 else 0
                                val scale by animateFloatAsState(
                                    targetValue = if (isTapped) 1.1f else 1f,
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                    label = "obj_scale_$objIndex"
                                )

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(78.dp)
                                        .scale(scale)
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(
                                            if (isTapped) MeadowGreen.copy(alpha = 0.2f)
                                            else currentLesson.cardColor.copy(alpha = 0.12f)
                                        )
                                        .border(
                                            width = if (isTapped) 3.dp else 1.5.dp,
                                            color = if (isTapped) MeadowGreen else currentLesson.cardColor,
                                            shape = RoundedCornerShape(22.dp)
                                        )
                                        .clickable {
                                            if (!tappedIndices.contains(objIndex)) {
                                                tappedIndices.add(objIndex)
                                                val newCount = tappedIndices.size
                                                val countLesson = lessons.getOrElse(newCount - 1) { currentLesson }
                                                val spoken = if (language == AppLanguage.HINDI) {
                                                    if (newCount == currentLesson.number) {
                                                        "${countLesson.wordHi}! शाबाश! कुल ${currentLesson.wordHi} ${currentLesson.objectNameHi}!"
                                                    } else {
                                                        "${countLesson.wordHi}! (${countLesson.digitHi})"
                                                    }
                                                } else {
                                                    if (newCount == currentLesson.number) {
                                                        "${countLesson.wordEn}! Awesome! Total ${currentLesson.wordEn} ${currentLesson.objectNameEn}!"
                                                    } else {
                                                        "${countLesson.wordEn}! (${countLesson.digitEn})"
                                                    }
                                                }
                                                onSpeakText(spoken, language)
                                            } else {
                                                val existingNum = tappedIndices.indexOf(objIndex) + 1
                                                val countLesson = lessons.getOrElse(existingNum - 1) { currentLesson }
                                                onSpeakText(
                                                    if (language == AppLanguage.HINDI) countLesson.wordHi else countLesson.wordEn,
                                                    language
                                                )
                                            }
                                        }
                                        .testTag("countable_object_$objIndex")
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = currentLesson.emoji, fontSize = 36.sp)
                                        if (isTapped) {
                                            Surface(
                                                color = MeadowGreen,
                                                shape = RoundedCornerShape(50)
                                            ) {
                                                Text(
                                                    text = "$countNumber",
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${currentLesson.digitHi} ${currentLesson.objectNameHi} • ${currentLesson.digitEn} ${currentLesson.objectNameEn}",
                            style = MaterialTheme.typography.titleLarge,
                            color = currentLesson.cardColor,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                LessonNavigationRow(
                    language = language,
                    accentColor = currentLesson.cardColor,
                    isCompleted = isCompleted,
                    onPrevious = {
                        selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else lessons.lastIndex
                    },
                    onRepeat = {
                        val text = if (language == AppLanguage.HINDI) {
                            "${currentLesson.wordHi}! ${currentLesson.digitHi} ${currentLesson.objectNameHi}!"
                        } else {
                            "${currentLesson.wordEn}! ${currentLesson.digitEn} ${currentLesson.objectNameEn}!"
                        }
                        onSpeakText(text, language)
                    },
                    onNext = {
                        selectedIndex = if (selectedIndex < lessons.lastIndex) selectedIndex + 1 else 0
                    },
                    onMarkDone = {
                        onCompleteItem(
                            itemId,
                            "${currentLesson.digitHi} (${currentLesson.wordHi})",
                            "${currentLesson.digitEn} (${currentLesson.wordEn})"
                        )
                    }
                )
            } else {
                // 1 to 100 Interactive Counting Chart
                Numbers1To100Board(
                    language = language,
                    onSpeakNumber = { item ->
                        val speech = if (language == AppLanguage.HINDI) {
                            "${item.digitHi} - ${item.wordHi} (${item.wordEn})"
                        } else {
                            "${item.number} - ${item.wordEn} (${item.wordHi})"
                        }
                        onSpeakText(speech, language)
                    }
                )
            }

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = {
                    val text = if (language == AppLanguage.HINDI) {
                        "${currentLesson.wordHi}! ${currentLesson.digitHi} ${currentLesson.objectNameHi}!"
                    } else {
                        "${currentLesson.wordEn}! ${currentLesson.digitEn} ${currentLesson.objectNameEn}!"
                    }
                    onSpeakText(text, language)
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Numbers1To100Board(
    language: AppLanguage,
    onSpeakNumber: (Number100Item) -> Unit
) {
    val all100 = CurriculumRepository.numbers1To100
    var selectedDecade by remember { mutableIntStateOf(0) } // 0 -> 1..10, 1 -> 11..20 ... 9 -> 91..100
    var highlightedItem by remember { mutableStateOf(all100.first()) }

    val currentSlice = remember(selectedDecade) {
        val start = selectedDecade * 10
        all100.subList(start, (start + 10).coerceAtMost(all100.size))
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Decade Range Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (d in 0 until 10) {
                val startNum = d * 10 + 1
                val endNum = (d + 1) * 10
                val isSelected = d == selectedDecade
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedDecade = d
                        highlightedItem = all100[startNum - 1]
                        onSpeakNumber(all100[startNum - 1])
                    },
                    label = {
                        Text(
                            text = "$startNum–$endNum",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CardCountingBg,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                )
            }
        }

        // Selected Number Spotlight Card
        Card(
            onClick = { onSpeakNumber(highlightedItem) },
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(3.dp, CardCountingBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = highlightedItem.digitHi,
                    fontSize = 54.sp,
                    fontFamily = BalooFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    color = MangoOrange
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = highlightedItem.wordHi,
                        style = MaterialTheme.typography.headlineMedium,
                        color = DeepInk
                    )
                    Text(
                        text = highlightedItem.wordEn,
                        style = MaterialTheme.typography.titleMedium,
                        color = SkyBlueDeep
                    )
                }
                Text(
                    text = "${highlightedItem.number}",
                    fontSize = 54.sp,
                    fontFamily = BalooFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    color = CardCountingBg
                )
            }
        }

        // Grid of 10 Numbers in the Selected Decade
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            currentSlice.forEach { item ->
                val isActive = item.number == highlightedItem.number
                Card(
                    onClick = {
                        highlightedItem = item
                        onSpeakNumber(item)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) CardCountingBg else Color.White
                    ),
                    border = BorderStroke(2.dp, CardCountingBg),
                    modifier = Modifier.weight(1f).height(76.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${item.digitHi} (${item.number})",
                                style = MaterialTheme.typography.titleLarge,
                                color = if (isActive) Color.White else MangoOrange,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "${item.wordHi} • ${item.wordEn}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isActive) Color.White.copy(alpha = 0.92f) else DeepInk
                            )
                        }
                        Text(text = "🔊", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
