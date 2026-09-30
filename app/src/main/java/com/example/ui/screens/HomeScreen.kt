package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.local.ChildProfileEntity
import com.example.data.local.LessonProgressEntity
import com.example.data.model.AgeGroup
import com.example.data.model.AppLanguage
import com.example.data.model.SubjectType
import com.example.ui.components.AiTeacherBanner
import com.example.ui.components.ParentalGateDialog
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen

private data class HomeMenuButton(
    val titleMain: String,
    val titleSub: String,
    val emoji: String,
    val badgeSymbol: String,
    val colorTop: Color,
    val colorBottom: Color,
    val screen: AppScreen,
    val subject: SubjectType,
    val testTag: String
)

@Composable
fun HomeScreen(
    profile: ChildProfileEntity,
    language: AppLanguage,
    ageGroup: AgeGroup,
    completedLessons: List<LessonProgressEntity>,
    teacherMessage: String,
    isTeacherSpeaking: Boolean,
    onNavigate: (AppScreen) -> Unit,
    onToggleLanguage: () -> Unit,
    onSelectAgeGroup: (AgeGroup) -> Unit,
    onUpdateProfile: (String, String, AgeGroup) -> Unit,
    onSpeakGreeting: () -> Unit
) {
    var showParentalGate by remember { mutableStateOf(false) }
    var showAvatarEditor by remember { mutableStateOf(false) }

    val menuButtons = remember {
        listOf(
            HomeMenuButton(
                titleMain = "Hindi सीखें",
                titleSub = "स्वर अ–अः और व्यंजन क–ज्ञ",
                emoji = "🪔",
                badgeSymbol = "अ आ",
                colorTop = Color(0xFFFF7043),
                colorBottom = Color(0xFFD84315),
                screen = AppScreen.HindiLearning,
                subject = SubjectType.HINDI,
                testTag = "btn_hindi_learn"
            ),
            HomeMenuButton(
                titleMain = "English Learn",
                titleSub = "A for Apple to Z for Zebra",
                emoji = "🔤",
                badgeSymbol = "A B C",
                colorTop = Color(0xFF42A5F5),
                colorBottom = Color(0xFF1565C0),
                screen = AppScreen.EnglishLearning,
                subject = SubjectType.ENGLISH,
                testTag = "btn_english_learn"
            ),
            HomeMenuButton(
                titleMain = "गिनती सीखें",
                titleSub = "१–१० और १–१०० Counting",
                emoji = "🔢",
                badgeSymbol = "१ २ ३",
                colorTop = Color(0xFF26A69A),
                colorBottom = Color(0xFF00695C),
                screen = AppScreen.Counting,
                subject = SubjectType.COUNTING,
                testTag = "btn_counting_learn"
            ),
            HomeMenuButton(
                titleMain = "Colors",
                titleSub = "रंगों की जादुई दुनिया",
                emoji = "🎨",
                badgeSymbol = "🌈",
                colorTop = Color(0xFFAB47BC),
                colorBottom = Color(0xFF6A1B9A),
                screen = AppScreen.Colors,
                subject = SubjectType.COLORS,
                testTag = "btn_colors_learn"
            ),
            HomeMenuButton(
                titleMain = "Animals",
                titleSub = "जानवर और उनकी आवाज़ें",
                emoji = "🦁",
                badgeSymbol = "🐘",
                colorTop = Color(0xFFFFA726),
                colorBottom = Color(0xFFEF6C00),
                screen = AppScreen.Animals,
                subject = SubjectType.ANIMALS,
                testTag = "btn_animals_learn"
            ),
            HomeMenuButton(
                titleMain = "Fruits",
                titleSub = "ताज़े और मीठे फल",
                emoji = "🍎",
                badgeSymbol = "🥭",
                colorTop = Color(0xFFEC407A),
                colorBottom = Color(0xFFC2185B),
                screen = AppScreen.Fruits,
                subject = SubjectType.FRUITS,
                testTag = "btn_fruits_learn"
            ),
            HomeMenuButton(
                titleMain = "Rhymes",
                titleSub = "मज़ेदार बाल कविताएं",
                emoji = "🎵",
                badgeSymbol = "🎶",
                colorTop = Color(0xFF26C6DA),
                colorBottom = Color(0xFF00838F),
                screen = AppScreen.Rhymes,
                subject = SubjectType.RHYMES,
                testTag = "btn_rhymes_learn"
            ),
            HomeMenuButton(
                titleMain = "Learning Games",
                titleSub = "10 मज़ेदार खेल और क्विज़",
                emoji = "🎮",
                badgeSymbol = "🏆",
                colorTop = Color(0xFF7E57C2),
                colorBottom = Color(0xFF4527A0),
                screen = AppScreen.GamesHub,
                subject = SubjectType.GAMES,
                testTag = "btn_games_learn"
            )
        )
    }

    Scaffold(
        containerColor = WarmCreamBg,
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 12.dp,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onNavigate(AppScreen.Rewards) },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SunshineYellow,
                            contentColor = DeepInk
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("home_rewards_button")
                    ) {
                        Text("🏆", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "सितारे और इनाम (${profile.totalStars}⭐)" else "Stars & Rewards (${profile.totalStars}⭐)",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    OutlinedButton(
                        onClick = { showParentalGate = true },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(2.dp, SkyBlueDeep),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("home_parent_section_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SupervisorAccount,
                            contentDescription = "Parent Section",
                            tint = SkyBlueDeep
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "माता-पिता (Parents)" else "Parent Section",
                            style = MaterialTheme.typography.labelLarge,
                            color = SkyBlueDeep,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Hero Header with Logo, Tagline, Child Avatar, Stars, and Language Switcher
            item {
                HomeHeroHeader(
                    profile = profile,
                    language = language,
                    onAvatarClick = { showAvatarEditor = true },
                    onStarsClick = { onNavigate(AppScreen.Rewards) },
                    onToggleLanguage = onToggleLanguage
                )
            }

            // Age Category Selector (Age 1-2, 2-3, 3-4, 4-5, 5-6)
            item {
                AgeCategoryBar(
                    selectedAge = ageGroup,
                    language = language,
                    onSelectAge = onSelectAgeGroup,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Friendly AI Voice Teacher Banner
            item {
                AiTeacherBanner(
                    message = teacherMessage,
                    isSpeaking = isTeacherSpeaking,
                    language = language,
                    onSpeakClick = onSpeakGreeting,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Section Header for the 8 Big Colorful Learning Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "🌈 चलो आज क्या सीखें?" else "🌈 What Shall We Learn Today?",
                        style = MaterialTheme.typography.headlineMedium,
                        color = DeepInk
                    )
                    Surface(
                        color = MeadowGreen.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) ageGroup.labelHi else ageGroup.labelEn,
                            style = MaterialTheme.typography.labelLarge,
                            color = MeadowGreenDark,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 8 Big Colorful Category Cards arranged in 2-column rows
            val chunkedButtons = menuButtons.chunked(2)
            items(chunkedButtons.size) { rowIndex ->
                val pair = chunkedButtons[rowIndex]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    pair.forEach { btn ->
                        val completedCount = completedLessons.count { it.subjectId == btn.subject.id }
                        BigCategoryCard(
                            button = btn,
                            completedCount = completedCount,
                            modifier = Modifier.weight(1f),
                            onClick = { onNavigate(btn.screen) }
                        )
                    }
                }
            }
        }
    }

    if (showParentalGate) {
        ParentalGateDialog(
            language = language,
            onSuccess = {
                showParentalGate = false
                onNavigate(AppScreen.ParentDashboard)
            },
            onDismiss = { showParentalGate = false }
        )
    }

    if (showAvatarEditor) {
        ChildAvatarPickerDialog(
            currentProfile = profile,
            currentAgeGroup = ageGroup,
            language = language,
            onSave = { name, emoji, age ->
                onUpdateProfile(name, emoji, age)
                showAvatarEditor = false
            },
            onDismiss = { showAvatarEditor = false }
        )
    }
}

@Composable
private fun HomeHeroHeader(
    profile: ChildProfileEntity,
    language: AppLanguage,
    onAvatarClick: () -> Unit,
    onStarsClick: () -> Unit,
    onToggleLanguage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFF8F00), Color(0xFFFFB300), Color(0xFFFFCA28))
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_hero_banner),
            contentDescription = "Masti Ki Pathshala Playground Banner",
            contentScale = ContentScale.Crop,
            alpha = 0.26f,
            modifier = Modifier
                .fillMaxWidth()
                .height(225.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top bar: Child Profile pill, Language switch, and Star counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    onClick = onAvatarClick,
                    color = Color.White,
                    shape = RoundedCornerShape(50),
                    shadowElevation = 4.dp,
                    modifier = Modifier.testTag("child_profile_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SunshineYellowLight)
                                .border(1.5.dp, MangoOrange, CircleShape)
                        ) {
                            Text(text = profile.avatarEmoji, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = profile.childName,
                                style = MaterialTheme.typography.labelLarge,
                                color = DeepInk,
                                maxLines = 1
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "प्रोफ़ाइल बदलें ✏️" else "Edit Profile ✏️",
                                fontSize = 10.sp,
                                color = MutedSlate
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        onClick = onToggleLanguage,
                        color = SkyBlueDeep,
                        shape = RoundedCornerShape(50),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .height(42.dp)
                            .testTag("language_toggle_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Language,
                                contentDescription = "Switch Language",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "हिंदी / EN" else "EN / हिंदी",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                    }

                    Surface(
                        onClick = onStarsClick,
                        color = Color.White,
                        shape = RoundedCornerShape(50),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .height(42.dp)
                            .testTag("home_stars_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⭐", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.totalStars}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MangoOrange,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // App Logo + Title & Tagline Card
            Surface(
                color = Color.White.copy(alpha = 0.94f),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 6.dp,
                border = BorderStroke(2.dp, Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "Masti Ki Pathshala Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(2.dp, SunshineYellow, RoundedCornerShape(18.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Masti Ki Pathshala",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MangoOrange,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Khelo, Masti Karo Aur Seekho!",
                            style = MaterialTheme.typography.titleMedium,
                            color = SkyBlueDeep
                        )
                        Text(
                            text = "खेलो, मस्ती करो और सीखो! ✨",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepInk
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AgeCategoryBar(
    selectedAge: AgeGroup,
    language: AppLanguage,
    onSelectAge: (AgeGroup) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = if (language == AppLanguage.HINDI) {
                "👶 बच्चे की उम्र चुनें (Age Group): ${selectedAge.tipHi}"
            } else {
                "👶 Select Child Age: ${selectedAge.tipEn}"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MutedSlate,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AgeGroup.entries.forEach { group ->
                val isSelected = group == selectedAge
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectAge(group) },
                    label = {
                        Text(
                            text = if (language == AppLanguage.HINDI) group.labelHi else group.labelEn,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MangoOrange,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
                        containerColor = Color.White,
                        labelColor = DeepInk
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = SunshineYellow,
                        borderWidth = 1.5.dp
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("age_chip_${group.id}")
                )
            }
        }
    }
}

@Composable
private fun BigCategoryCard(
    button: HomeMenuButton,
    completedCount: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(2.5.dp, Color.White),
        modifier = modifier
            .height(152.dp)
            .testTag(button.testTag)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(button.colorTop, button.colorBottom)
                    )
                )
                .padding(14.dp)
        ) {
            // Top-right badge pill
            Surface(
                color = Color.White.copy(alpha = 0.25f),
                shape = RoundedCornerShape(50),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = if (completedCount > 0) "⭐ $completedCount" else button.badgeSymbol,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.92f),
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(58.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = button.emoji, fontSize = 32.sp)
                    }
                }

                Column {
                    Text(
                        text = button.titleMain,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = button.titleSub,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.92f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ChildAvatarPickerDialog(
    currentProfile: ChildProfileEntity,
    currentAgeGroup: AgeGroup,
    language: AppLanguage,
    onSave: (String, String, AgeGroup) -> Unit,
    onDismiss: () -> Unit
) {
    val avatars = listOf("🐘", "🐵", "🦁", "🐰", "🦚", "🦋", "🐼", "🐯")
    var name by remember { mutableStateOf(currentProfile.childName) }
    var selectedAvatar by remember { mutableStateOf(currentProfile.avatarEmoji) }
    var selectedAge by remember { mutableStateOf(currentAgeGroup) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(3.dp, SunshineYellow),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) "बच्चे की प्रोफ़ाइल" else "Child Profile & Avatar",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepInk
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(if (language == AppLanguage.HINDI) "बच्चे का प्यारा नाम" else "Child's Name")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "अपना प्यारा दोस्त (अवतार) चुनो:" else "Pick Your Cute Avatar:",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepInk
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    avatars.forEach { emoji ->
                        val isChosen = emoji == selectedAvatar
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (isChosen) SunshineYellow else SunshineYellowLight)
                                .border(2.dp, if (isChosen) MangoOrange else Color.Transparent, CircleShape)
                                .clickable { selectedAvatar = emoji }
                        ) {
                            Text(text = emoji, fontSize = 28.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text(if (language == AppLanguage.HINDI) "पीछे" else "Cancel")
                    }
                    Button(
                        onClick = { onSave(name, selectedAvatar, selectedAge) },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MeadowGreen),
                        modifier = Modifier.weight(1f).height(48.dp).testTag("save_profile_button")
                    ) {
                        Text(if (language == AppLanguage.HINDI) "सेव करें ✨" else "Save ✨")
                    }
                }
            }
        }
    }
}
