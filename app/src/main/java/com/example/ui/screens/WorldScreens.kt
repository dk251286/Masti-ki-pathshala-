package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.data.model.AnimalLessonItem
import com.example.data.model.AppLanguage
import com.example.ui.components.AiTeacherBanner
import com.example.ui.components.AnimatedSparkleBox
import com.example.ui.components.LessonNavigationRow
import com.example.ui.components.LessonTopBar
import com.example.ui.theme.*

@Composable
fun ColorsScreen(
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

    val colorsList = CurriculumRepository.colorsLessons
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentColor = colorsList[selectedIndex]
    val isCompleted = completedLessons.any { it.itemId == currentColor.id }

    LaunchedEffect(currentColor.id) {
        val text = if (language == AppLanguage.HINDI) {
            "${currentColor.nameHi} (${currentColor.nameEn})! ${currentColor.speechHi}"
        } else {
            "${currentColor.nameEn} (${currentColor.nameHi})! ${currentColor.speechEn}"
        }
        onSpeakText(text, language)
    }

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "रंगों की दुनिया (Colors)",
                titleEn = "Learn Bright Colors",
                language = language,
                stars = stars,
                accentColor = CardColorsBg,
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
            // Color Swatch Strip
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(colorsList) { index, item ->
                    val isSelected = index == selectedIndex
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(if (isSelected) 62.dp else 54.dp)
                            .clip(CircleShape)
                            .background(item.color)
                            .border(
                                width = if (isSelected) 4.dp else 2.dp,
                                color = if (isSelected) DeepInk else item.borderColor,
                                shape = CircleShape
                            )
                            .clickable { selectedIndex = index }
                            .testTag("color_swatch_${item.nameEn.lowercase()}")
                    ) {
                        if (isSelected) {
                            Text(text = item.emoji, fontSize = 24.sp)
                        }
                    }
                }
            }

            // Main Color Splash Stage Card
            Card(
                onClick = {
                    val text = if (language == AppLanguage.HINDI) currentColor.speechHi else currentColor.speechEn
                    onSpeakText(text, language)
                },
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(4.dp, currentColor.borderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth().testTag("color_main_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Giant Color Splash Circle + Object Emoji
                    AnimatedSparkleBox(
                        accentColor = currentColor.borderColor,
                        triggerKey = currentColor.id,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(165.dp)
                                .clip(CircleShape)
                                .background(currentColor.color)
                                .border(5.dp, currentColor.borderColor, CircleShape)
                        ) {
                            Text(text = currentColor.emoji, fontSize = 84.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = currentColor.borderColor,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = "${currentColor.nameHi} • ${currentColor.nameEn}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${currentColor.objectHi} (${currentColor.objectEn})",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepInk
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.HINDI) currentColor.speechHi else currentColor.speechEn,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MutedSlate,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LessonNavigationRow(
                language = language,
                accentColor = CardColorsBg,
                isCompleted = isCompleted,
                onPrevious = {
                    selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else colorsList.lastIndex
                },
                onRepeat = {
                    val text = if (language == AppLanguage.HINDI) currentColor.speechHi else currentColor.speechEn
                    onSpeakText(text, language)
                },
                onNext = {
                    selectedIndex = if (selectedIndex < colorsList.lastIndex) selectedIndex + 1 else 0
                },
                onMarkDone = {
                    onCompleteItem(currentColor.id, currentColor.nameHi, currentColor.nameEn)
                }
            )

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = {
                    val text = if (language == AppLanguage.HINDI) currentColor.speechHi else currentColor.speechEn
                    onSpeakText(text, language)
                }
            )
        }
    }
}

@Composable
fun AnimalsScreen(
    language: AppLanguage,
    stars: Int,
    completedLessons: List<LessonProgressEntity>,
    teacherMessage: String,
    isTeacherSpeaking: Boolean,
    onPlayAnimalSoundAndVoice: (AnimalLessonItem) -> Unit,
    onCompleteItem: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val animals = CurriculumRepository.animalsLessons
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentAnimal = animals[selectedIndex]
    val isCompleted = completedLessons.any { it.itemId == currentAnimal.id }

    LaunchedEffect(currentAnimal.id) {
        onPlayAnimalSoundAndVoice(currentAnimal)
    }

    val infiniteFloat = rememberInfiniteTransition(label = "animal_bounce")
    val animalScale by infiniteFloat.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.09f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animal_scale"
    )

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "प्यारे जानवर (Animals)",
                titleEn = "Meet the Animals",
                language = language,
                stars = stars,
                accentColor = CardAnimalsBg,
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
            // Animal Strip
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(animals) { index, item ->
                    val isSelected = index == selectedIndex
                    Surface(
                        onClick = { selectedIndex = index },
                        color = if (isSelected) item.cardColor else Color.White,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(2.dp, item.cardColor),
                        shadowElevation = if (isSelected) 6.dp else 2.dp,
                        modifier = Modifier
                            .size(62.dp)
                            .testTag("animal_chip_${item.nameEn.lowercase()}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = item.emoji, fontSize = 32.sp)
                        }
                    }
                }
            }

            // Main Animal Card with Sound Button
            Card(
                onClick = { onPlayAnimalSoundAndVoice(currentAnimal) },
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(4.dp, currentAnimal.cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth().testTag("animal_main_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedSparkleBox(
                        accentColor = currentAnimal.cardColor,
                        triggerKey = currentAnimal.id,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(150.dp)
                                .scale(animalScale)
                                .clip(CircleShape)
                                .background(currentAnimal.cardColor.copy(alpha = 0.16f))
                        ) {
                            Text(text = currentAnimal.emoji, fontSize = 86.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = currentAnimal.cardColor,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = "${currentAnimal.nameHi} • ${currentAnimal.nameEn}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Big Interactive Animal Sound Button
                    Button(
                        onClick = { onPlayAnimalSoundAndVoice(currentAnimal) },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SunshineYellow,
                            contentColor = DeepInk
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(52.dp)
                            .testTag("play_animal_sound_button")
                    ) {
                        Icon(Icons.Filled.Pets, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) {
                                "🔊 आवाज़: ${currentAnimal.soundWordHi}"
                            } else {
                                "🔊 Sound: ${currentAnimal.soundWordEn}"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.HINDI) currentAnimal.factHi else currentAnimal.factEn,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DeepInk,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LessonNavigationRow(
                language = language,
                accentColor = currentAnimal.cardColor,
                isCompleted = isCompleted,
                onPrevious = {
                    selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else animals.lastIndex
                },
                onRepeat = { onPlayAnimalSoundAndVoice(currentAnimal) },
                onNext = {
                    selectedIndex = if (selectedIndex < animals.lastIndex) selectedIndex + 1 else 0
                },
                onMarkDone = {
                    onCompleteItem(currentAnimal.id, currentAnimal.nameHi, currentAnimal.nameEn)
                }
            )

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = { onPlayAnimalSoundAndVoice(currentAnimal) }
            )
        }
    }
}

@Composable
fun FruitsScreen(
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

    val fruits = CurriculumRepository.fruitsLessons
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentFruit = fruits[selectedIndex]
    val isCompleted = completedLessons.any { it.itemId == currentFruit.id }

    LaunchedEffect(currentFruit.id) {
        val speech = if (language == AppLanguage.HINDI) {
            "${currentFruit.nameHi} (${currentFruit.nameEn})! ${currentFruit.tasteHi}"
        } else {
            "${currentFruit.nameEn} (${currentFruit.nameHi})! ${currentFruit.tasteEn}"
        }
        onSpeakText(speech, language)
    }

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "ताज़े फल (Fruits)",
                titleEn = "Healthy & Sweet Fruits",
                language = language,
                stars = stars,
                accentColor = CardFruitsBg,
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
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(fruits) { index, item ->
                    val isSelected = index == selectedIndex
                    Surface(
                        onClick = { selectedIndex = index },
                        color = if (isSelected) item.cardColor else Color.White,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(2.dp, item.cardColor),
                        shadowElevation = if (isSelected) 6.dp else 2.dp,
                        modifier = Modifier
                            .size(62.dp)
                            .testTag("fruit_chip_${item.nameEn.lowercase()}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = item.emoji, fontSize = 32.sp)
                        }
                    }
                }
            }

            Card(
                onClick = {
                    val speech = if (language == AppLanguage.HINDI) {
                        "${currentFruit.nameHi}! ${currentFruit.tasteHi}"
                    } else {
                        "${currentFruit.nameEn}! ${currentFruit.tasteEn}"
                    }
                    onSpeakText(speech, language)
                },
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(4.dp, currentFruit.cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth().testTag("fruit_main_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedSparkleBox(
                        accentColor = currentFruit.cardColor,
                        triggerKey = currentFruit.id,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(currentFruit.cardColor.copy(alpha = 0.15f))
                        ) {
                            Text(text = currentFruit.emoji, fontSize = 86.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = currentFruit.cardColor,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = "${currentFruit.nameHi} • ${currentFruit.nameEn}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.HINDI) currentFruit.tasteHi else currentFruit.tasteEn,
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepInk,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LessonNavigationRow(
                language = language,
                accentColor = currentFruit.cardColor,
                isCompleted = isCompleted,
                onPrevious = {
                    selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else fruits.lastIndex
                },
                onRepeat = {
                    val speech = if (language == AppLanguage.HINDI) {
                        "${currentFruit.nameHi}! ${currentFruit.tasteHi}"
                    } else {
                        "${currentFruit.nameEn}! ${currentFruit.tasteEn}"
                    }
                    onSpeakText(speech, language)
                },
                onNext = {
                    selectedIndex = if (selectedIndex < fruits.lastIndex) selectedIndex + 1 else 0
                },
                onMarkDone = {
                    onCompleteItem(currentFruit.id, currentFruit.nameHi, currentFruit.nameEn)
                }
            )

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = {
                    val speech = if (language == AppLanguage.HINDI) {
                        "${currentFruit.nameHi}! ${currentFruit.tasteHi}"
                    } else {
                        "${currentFruit.nameEn}! ${currentFruit.tasteEn}"
                    }
                    onSpeakText(speech, language)
                }
            )
        }
    }
}

@Composable
fun RhymesScreen(
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

    val rhymes = CurriculumRepository.rhymesLessons
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentRhyme = rhymes[selectedIndex]
    var activeLineIndex by remember(selectedIndex) { mutableIntStateOf(0) }
    val isCompleted = completedLessons.any { it.itemId == currentRhyme.id }
    val rhymeLang = if (currentRhyme.isHindiRhyme) AppLanguage.HINDI else AppLanguage.ENGLISH

    LaunchedEffect(currentRhyme.id) {
        onSpeakText(
            "${currentRhyme.titleHi}! ${currentRhyme.lines.joinToString(" ")}",
            rhymeLang
        )
    }

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "बाल कविताएं (Rhymes)",
                titleEn = "Sing-Along Rhymes",
                language = language,
                stars = stars,
                accentColor = CardRhymesBg,
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
            // Rhyme Selector Strip
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(rhymes) { index, item ->
                    val isSelected = index == selectedIndex
                    Surface(
                        onClick = { selectedIndex = index },
                        color = if (isSelected) item.cardColor else Color.White,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, item.cardColor),
                        modifier = Modifier.height(52.dp).testTag("rhyme_chip_${item.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item.emoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (item.isHindiRhyme) item.titleHi else item.titleEn,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) Color.White else DeepInk
                            )
                        }
                    }
                }
            }

            // Sing-Along Stage Card
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(4.dp, currentRhyme.cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth().testTag("rhyme_main_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = currentRhyme.emoji, fontSize = 48.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = currentRhyme.titleHi,
                                style = MaterialTheme.typography.headlineMedium,
                                color = currentRhyme.cardColor
                            )
                            Text(
                                text = currentRhyme.titleEn,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedSlate
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Interactive Karaoke Lines (tap any line to sing it!)
                    currentRhyme.lines.forEachIndexed { lineIdx, line ->
                        val isLineActive = lineIdx == activeLineIndex
                        Surface(
                            onClick = {
                                activeLineIndex = lineIdx
                                onSpeakText(line, rhymeLang)
                            },
                            color = if (isLineActive) currentRhyme.cardColor.copy(alpha = 0.16f) else WarmCreamBg,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                width = if (isLineActive) 2.dp else 1.dp,
                                color = if (isLineActive) currentRhyme.cardColor else Color.LightGray
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "🎶 $line",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DeepInk,
                                    fontWeight = if (isLineActive) FontWeight.ExtraBold else FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Sing line",
                                    tint = currentRhyme.cardColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onSpeakText(currentRhyme.lines.joinToString(" "), rhymeLang)
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = currentRhyme.cardColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("sing_full_rhyme_button")
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "🎵 पूरी कविता साथ गाएं!" else "🎵 Sing Full Rhyme Together!",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }

            LessonNavigationRow(
                language = language,
                accentColor = currentRhyme.cardColor,
                isCompleted = isCompleted,
                onPrevious = {
                    selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else rhymes.lastIndex
                },
                onRepeat = {
                    onSpeakText(currentRhyme.lines.joinToString(" "), rhymeLang)
                },
                onNext = {
                    selectedIndex = if (selectedIndex < rhymes.lastIndex) selectedIndex + 1 else 0
                },
                onMarkDone = {
                    onCompleteItem(currentRhyme.id, currentRhyme.titleHi, currentRhyme.titleEn)
                }
            )

            AiTeacherBanner(
                message = teacherMessage,
                isSpeaking = isTeacherSpeaking,
                language = language,
                onSpeakClick = {
                    onSpeakText(currentRhyme.lines.joinToString(" "), rhymeLang)
                }
            )
        }
    }
}
