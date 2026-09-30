package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AiTeacherApiClient
import com.example.data.content.CurriculumRepository
import com.example.data.local.ChildProfileEntity
import com.example.data.local.LessonProgressEntity
import com.example.data.local.QuizScoreEntity
import com.example.data.model.AgeGroup
import com.example.data.model.AppLanguage
import com.example.data.model.SubjectType
import com.example.ui.components.LessonTopBar
import com.example.ui.theme.*

@Composable
fun RewardsScreen(
    profile: ChildProfileEntity,
    language: AppLanguage,
    completedLessons: List<LessonProgressEntity>,
    onCelebrate: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val badges = CurriculumRepository.badges

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "मेरे सितारे और इनाम",
                titleEn = "My Stars & Trophy Room",
                language = language,
                stars = profile.totalStars,
                accentColor = MangoOrange,
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    onClick = onCelebrate,
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(4.dp, SunshineYellow),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("rewards_hero_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_reward_trophy),
                            contentDescription = "Golden Star Trophy",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .border(4.dp, SunshineYellow, CircleShape)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "${profile.avatarEmoji} ${profile.childName}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = DeepInk
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = SunshineYellowLight,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(2.dp, SunshineYellow)
                        ) {
                            Text(
                                text = "⭐ ${profile.totalStars} Stars Earned!",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MangoOrange,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) {
                                "पूरे किए गए पाठ: ${completedLessons.size} • ताली और संगीत सुनने के लिए ट्रॉफी छुएं!"
                            } else {
                                "Lessons Completed: ${completedLessons.size} • Tap Trophy to Celebrate!"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MutedSlate,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                Text(
                    text = if (language == AppLanguage.HINDI) "🏅 मेरे स्टिकर और मेडल (Stickers & Badges)" else "🏅 My Stickers & Badges",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepInk
                )
            }

            items(badges) { badge ->
                val isUnlocked = profile.totalStars >= badge.starsRequired
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) Color.White else Color(0xFFF1F5F9)
                    ),
                    border = BorderStroke(
                        width = 2.5.dp,
                        color = if (isUnlocked) MeadowGreen else Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
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
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isUnlocked) SunshineYellowLight else Color.LightGray.copy(alpha = 0.35f)
                                )
                        ) {
                            Text(
                                text = if (isUnlocked) badge.emoji else "🔒",
                                fontSize = 34.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.HINDI) badge.titleHi else badge.titleEn,
                                style = MaterialTheme.typography.titleLarge,
                                color = if (isUnlocked) DeepInk else MutedSlate,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) badge.descHi else badge.descEn,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedSlate
                            )
                        }

                        Surface(
                            color = if (isUnlocked) MeadowGreen else SunshineYellow,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = if (isUnlocked) "Unlocked ✓" else "${badge.starsRequired} ⭐",
                                color = if (isUnlocked) Color.White else DeepInk,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParentDashboardScreen(
    profile: ChildProfileEntity,
    language: AppLanguage,
    ageGroup: AgeGroup,
    completedLessons: List<LessonProgressEntity>,
    quizScores: List<QuizScoreEntity>,
    onSetLanguage: (AppLanguage) -> Unit,
    onSelectAgeGroup: (AgeGroup) -> Unit,
    onResetProgress: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmCreamBg,
        topBar = {
            LessonTopBar(
                titleHi = "माता-पिता डैशबोर्ड (Parent Section)",
                titleEn = "Parent Dashboard & Safety",
                language = language,
                stars = profile.totalStars,
                accentColor = SkyBlueDeep,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Key Metrics Overview (Age, Lessons, Quiz Scores, Stars, Learning Time)
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, SkyBlueDeep.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth().testTag("parent_stats_overview")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = profile.avatarEmoji, fontSize = 36.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${profile.childName} • ${ageGroup.labelHi} (${ageGroup.labelEn})",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DeepInk
                                )
                                Text(
                                    text = if (language == AppLanguage.HINDI) {
                                        "बच्चे की प्रगति और सीखने की रिपोर्ट"
                                    } else {
                                        "Child's Learning Progress & Activity Report"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedSlate
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ParentStatTile(
                                emoji = "📚",
                                value = "${completedLessons.size}",
                                label = if (language == AppLanguage.HINDI) "पाठ पूरे (Lessons)" else "Lessons Done",
                                color = CardHindiBg,
                                modifier = Modifier.weight(1f)
                            )
                            ParentStatTile(
                                emoji = "⭐",
                                value = "${profile.totalStars}",
                                label = if (language == AppLanguage.HINDI) "कुल सितारे (Stars)" else "Stars Earned",
                                color = MangoOrange,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ParentStatTile(
                                emoji = "⏱️",
                                value = "${profile.learningMinutes}m",
                                label = if (language == AppLanguage.HINDI) "सीखने का समय" else "Learning Time",
                                color = MeadowGreen,
                                modifier = Modifier.weight(1f)
                            )
                            ParentStatTile(
                                emoji = "🎯",
                                value = "${quizScores.size}",
                                label = if (language == AppLanguage.HINDI) "क्विज़ खेले (Quizzes)" else "Quizzes Played",
                                color = BerryPurple,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 2. Language & Age Selection Controls for Parents
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, SunshineYellow),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "🌐 भाषा और आयु सेटिंग्स (Language & Age)" else "🌐 Language & Age Settings",
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepInk
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AppLanguage.entries.forEach { lang ->
                                val selected = lang == language
                                Button(
                                    onClick = { onSetLanguage(lang) },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selected) SkyBlueDeep else WarmCreamBg,
                                        contentColor = if (selected) Color.White else DeepInk
                                    ),
                                    border = BorderStroke(1.5.dp, SkyBlueDeep),
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.HINDI) "हिंदी (Default)" else "English",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (language == AppLanguage.HINDI) "बच्चे का आयु वर्ग चुनें (1 से 6 वर्ष):" else "Select Child's Age Category (1 to 6 Years):",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepInk
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AgeGroup.entries.forEach { group ->
                                val selected = group == ageGroup
                                FilterChip(
                                    selected = selected,
                                    onClick = { onSelectAgeGroup(group) },
                                    label = {
                                        Text("${group.labelHi} / ${group.labelEn}")
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MangoOrange,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. Progress by Subject Breakdown
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, MeadowGreen.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "📊 विषयवार प्रगति (Progress by Subject)" else "📊 Progress by Subject",
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepInk
                        )

                        SubjectType.entries.filter { it != SubjectType.GAMES }.forEach { subject ->
                            val doneCount = completedLessons.count { it.subjectId == subject.id }
                            val progress = (doneCount.toFloat() / subject.totalItems.toFloat()).coerceIn(0f, 1f)
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${subject.emoji} ${subject.titleHi} (${subject.titleEn})",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DeepInk
                                    )
                                    Text(
                                        text = "$doneCount / ${subject.totalItems}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MeadowGreenDark
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    color = MeadowGreen,
                                    trackColor = Color(0xFFE2E8F0),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(50))
                                )
                            }
                        }
                    }
                }
            }

            // 4. Quiz Scores History
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, BerryPurple.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "🏆 हाल के खेल और क्विज़ स्कोर (Quiz Scores)" else "🏆 Recent Game & Quiz Scores",
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepInk
                        )

                        if (quizScores.isEmpty()) {
                            Text(
                                text = if (language == AppLanguage.HINDI) {
                                    "अभी तक कोई क्विज़ पूरा नहीं हुआ है। 'Learning Games' में जाकर खेल शुरू करें!"
                                } else {
                                    "No quizzes completed yet. Play a game in 'Learning Games' to see scores here!"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedSlate
                            )
                        } else {
                            quizScores.take(6).forEach { scoreItem ->
                                Surface(
                                    color = WarmCreamBg,
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(1.dp, Color.LightGray)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = scoreItem.gameTitle,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = DeepInk,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "${scoreItem.score}/${scoreItem.totalQuestions} (+${scoreItem.starsEarned}⭐)",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = BerryPurple
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. AI Voice Teacher Configuration & Child Safety / Privacy Policy Card
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = BorderStroke(2.dp, MeadowGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = MeadowGreenDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) {
                                    "🛡️ बाल सुरक्षा और गोपनीयता नीति (Child Safety & Privacy)"
                                } else {
                                    "🛡️ Child Safety & Privacy Policy"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                color = MeadowGreenDark
                            )
                        }

                        val safetyPoints = listOf(
                            "✓ 100% बाल-सुरक्षित: किसी अनजान व्यक्ति से चैट या सोशल मीडिया नहीं (No chat or social media).",
                            "✓ कोई लोकेशन ट्रैकिंग या व्यक्तिगत डेटा संग्रह नहीं (Zero location tracking or personal data collection).",
                            "✓ ऑफ़लाइन समर्थन: सभी वर्णमाला, गिनती, रंग, जानवर, फल, कविताएं और खेल बिना इंटरनेट के चलते हैं (Works offline).",
                            "✓ विज्ञापन-मुक्त और माता-पिता सुरक्षा गेट से सुरक्षित (Ad-free & protected by Parental Gate).",
                            if (AiTeacherApiClient.isApiKeyConfigured) {
                                "✓ AI Voice Teacher: Gemini API Key सक्रिय है + Android Native Hindi/English TTS."
                            } else {
                                "✓ AI Voice Teacher: ऑफ़लाइन हिंदी/अंग्रेज़ी TTS सक्रिय है (Optional: AI Studio Secrets में GEMINI_API_KEY जोड़ें)."
                            }
                        )

                        safetyPoints.forEach { bullet ->
                            Text(
                                text = bullet,
                                style = MaterialTheme.typography.bodyMedium,
                                color = DeepInk
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showPrivacyPolicyDialog = true },
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.weight(1f).testTag("privacy_policy_button")
                            ) {
                                Text(if (language == AppLanguage.HINDI) "पूरी गोपनीयता नीति" else "Full Privacy Policy")
                            }

                            OutlinedButton(
                                onClick = onResetProgress,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (language == AppLanguage.HINDI) "प्रगति रीसेट करें" else "Reset Progress")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            title = {
                Text(
                    text = "Masti Ki Pathshala — Child Safety & Privacy Policy",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. No Personal Data Collection: Masti Ki Pathshala stores child nickname, avatar, stars, and lesson progress strictly locally on your device using Android Room database.")
                    Text("2. Zero Social & Zero Chat: There are no public profiles, no external links, and no chat features with unknown people.")
                    Text("3. Zero Location or Sensor Tracking: The app never requests camera, microphone, contacts, or GPS location permissions.")
                    Text("4. Offline-First Design: Core lessons, pictures, sounds, and games operate completely offline.")
                    Text("5. Kid-Safe Audio: Voice guidance uses child-friendly Hindi & English phrases.")
                }
            },
            confirmButton = {
                Button(onClick = { showPrivacyPolicyDialog = false }) {
                    Text("OK / समझ गए")
                }
            }
        )
    }
}

@Composable
private fun ParentStatTile(
    emoji: String,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, color.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = color,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepInk
                )
            }
        }
    }
}
