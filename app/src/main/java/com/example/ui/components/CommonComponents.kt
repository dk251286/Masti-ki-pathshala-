package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
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
import com.example.data.model.AppLanguage
import com.example.ui.theme.*
import com.example.viewmodel.StarRewardEvent
import kotlin.random.Random

@Composable
fun LessonTopBar(
    titleHi: String,
    titleEn: String,
    language: AppLanguage,
    stars: Int,
    accentColor: Color,
    onBack: () -> Unit,
    onStarsClick: () -> Unit = {}
) {
    Surface(
        color = accentColor,
        shadowElevation = 6.dp,
        shape = RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
                    .testTag("back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) titleHi else titleEn,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (language == AppLanguage.HINDI) titleEn else titleHi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1
                )
            }

            Surface(
                onClick = onStarsClick,
                color = Color.White,
                shape = RoundedCornerShape(50),
                shadowElevation = 3.dp,
                modifier = Modifier.height(42.dp).testTag("topbar_stars_badge")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⭐", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$stars",
                        style = MaterialTheme.typography.titleMedium,
                        color = MangoOrange,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun AiTeacherBanner(
    message: String,
    isSpeaking: Boolean,
    language: AppLanguage,
    onSpeakClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "teacher_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking) 1.12f else 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSpeaking) 420 else 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "avatar_scale"
    )

    Card(
        onClick = onSpeakClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        border = BorderStroke(2.dp, SunshineYellow),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("ai_teacher_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(62.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .border(3.dp, MangoOrange, CircleShape)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_ai_teacher),
                    contentDescription = "AI Voice Teacher",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MangoOrange,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "🎙️ AI दीदी टीचर" else "🎙️ AI Voice Teacher",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "(सुनने के लिए छुएं)" else "(Tap to listen)",
                        fontSize = 11.sp,
                        color = MutedSlate
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepInk,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            FilledIconButton(
                onClick = onSpeakClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MangoOrange,
                    contentColor = Color.White
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Speak",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun LessonNavigationRow(
    language: AppLanguage,
    accentColor: Color,
    isCompleted: Boolean,
    onPrevious: () -> Unit,
    onRepeat: () -> Unit,
    onNext: () -> Unit,
    onMarkDone: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onPrevious,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, accentColor),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("lesson_prev_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", tint = accentColor)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "पीछे" else "Prev",
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor
                )
            }

            Button(
                onClick = onRepeat,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow, contentColor = DeepInk),
                modifier = Modifier
                    .weight(1.3f)
                    .height(54.dp)
                    .testTag("lesson_repeat_button")
            ) {
                Icon(Icons.Filled.Replay, contentDescription = "Repeat")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "फिर से सुनें" else "Repeat",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onNext,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.White),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("lesson_next_button")
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) "आगे" else "Next",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
            }
        }

        Button(
            onClick = onMarkDone,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCompleted) MeadowGreen else BerryPurple,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("lesson_complete_button")
        ) {
            Icon(
                imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Filled.Star,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isCompleted) {
                    if (language == AppLanguage.HINDI) "सीख लिया! फिर से +1 ⭐ पाएं" else "Learned! Tap for +1 ⭐ Bonus"
                } else {
                    if (language == AppLanguage.HINDI) "मैंने सीख लिया! (+2 ⭐ पाएं)" else "I Learned This! (Get +2 ⭐)"
                },
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AnimatedSparkleBox(
    accentColor: Color,
    triggerKey: Any,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    var bounceTarget by remember(triggerKey) { mutableStateOf(false) }
    LaunchedEffect(triggerKey) {
        bounceTarget = true
    }
    val scale by animateFloatAsState(
        targetValue = if (bounceTarget) 1f else 0.84f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_spring"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .drawBehind {
                val radius = size.minDimension * 0.45f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = 0.22f), Color.Transparent),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = radius * 1.3f
                    )
                )
            },
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun StarRewardDialog(
    event: StarRewardEvent,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(4.dp, SunshineYellow),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("star_reward_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_reward_trophy),
                    contentDescription = "Star Trophy",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .border(4.dp, SunshineYellow, CircleShape)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "⭐ +${event.starsEarned} Stars! ⭐",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MangoOrange,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) event.titleHi else event.titleEn,
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepInk,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "शाबाश! बहुत बढ़िया काम किया!" else "Awesome! You are a superstar!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MutedSlate,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MeadowGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("reward_continue_button")
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "आगे खेलें! 🎉" else "Keep Playing! 🎉",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ParentalGateDialog(
    language: AppLanguage,
    onSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    val numA = remember { Random.nextInt(3, 7) }
    val numB = remember { Random.nextInt(2, 5) }
    val correctAnswer = numA + numB
    val options = remember(numA, numB) {
        listOf(correctAnswer, correctAnswer + 2, correctAnswer - 1, correctAnswer + 4).shuffled()
    }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(3.dp, SkyBlueDeep),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("parental_gate_dialog")
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Parental Gate",
                    tint = SkyBlueDeep,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "माता-पिता सुरक्षा गेट" else "Parents Only Gate",
                    style = MaterialTheme.typography.headlineMedium,
                    color = DeepInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) {
                        "आगे बढ़ने के लिए इस सरल सवाल का उत्तर चुनें:"
                    } else {
                        "Please solve this simple math question to enter Parent Zone:"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedSlate,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    color = SunshineYellowLight,
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(2.dp, SunshineYellow)
                ) {
                    Text(
                        text = "$numA + $numB = ?",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MangoOrange,
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 10.dp)
                    )
                }
                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "गलत उत्तर, कृपया फिर से चुनें।" else "Incorrect answer, please try again.",
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    options.forEach { ans ->
                        Button(
                            onClick = {
                                if (ans == correctAnswer) {
                                    onSuccess()
                                } else {
                                    showError = true
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SkyBlueDeep),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("gate_option_$ans")
                        ) {
                            Text(
                                text = "$ans",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onDismiss) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "रद्द करें (Cancel)" else "Cancel",
                        color = MutedSlate
                    )
                }
            }
        }
    }
}
