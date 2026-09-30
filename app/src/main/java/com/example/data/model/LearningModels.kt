package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class AppLanguage(val code: String, val displayNameHi: String, val displayNameEn: String) {
    HINDI("hi", "हिंदी (Hindi)", "Hindi (हिंदी)"),
    ENGLISH("en", "अंग्रेज़ी (English)", "English (अंग्रेज़ी)")
}

enum class AgeGroup(
    val id: String,
    val labelHi: String,
    val labelEn: String,
    val tipHi: String,
    val tipEn: String
) {
    AGE_1_2("1-2", "1–2 वर्ष", "Age 1–2", "आवाज़ें, रंग और बड़े चित्र देखें", "Sounds, colors & big pictures"),
    AGE_2_3("2-3", "2–3 वर्ष", "Age 2–3", "स्वर, फल, जानवर और कविताएं", "Swar, fruits, animals & rhymes"),
    AGE_3_4("3-4", "3–4 वर्ष", "Age 3–4", "अक्षर A-Z, स्वर और १–१० गिनती", "Letters A-Z, Swar & 1–10 counting"),
    AGE_4_5("4-5", "4–5 वर्ष", "Age 4–5", "व्यंजन, शब्द मिलान और पहेलियां", "Vyanjan, word matching & puzzles"),
    AGE_5_6("5-6", "5–6 वर्ष", "Age 5–6", "१–१०० गिनती, मेमोरी गेम और क्विज़", "1–100 counting, memory game & quiz");

    companion object {
        fun fromId(id: String): AgeGroup = entries.find { it.id == id } ?: AGE_3_4
    }
}

enum class SubjectType(
    val id: String,
    val titleHi: String,
    val titleEn: String,
    val emoji: String,
    val totalItems: Int
) {
    HINDI("hindi", "Hindi सीखें", "Learn Hindi", "अ", 25),
    ENGLISH("english", "English Learn", "English Learn", "A", 26),
    COUNTING("counting", "गिनती सीखें", "Counting 1–100", "१२३", 20),
    COLORS("colors", "रंग पहचानो", "Colors", "🎨", 10),
    ANIMALS("animals", "जानवर (Animals)", "Animals", "🐘", 12),
    FRUITS("fruits", "फल (Fruits)", "Fruits", "🍎", 10),
    RHYMES("rhymes", "कविताएं (Rhymes)", "Rhymes", "🎵", 6),
    GAMES("games", "खेल-खेल में सीखें", "Learning Games", "🎮", 10)
}

enum class GameType(
    val id: String,
    val number: Int,
    val titleHi: String,
    val titleEn: String,
    val subtitleHi: String,
    val subtitleEn: String,
    val emoji: String,
    val accentHex: Long
) {
    FIND_LETTER(
        "find_letter", 1,
        "सही अक्षर खोजो", "Find the Correct Letter",
        "आवाज़ सुनकर सही अक्षर दबाओ", "Listen and tap the matching letter",
        "🔤", 0xFFFF6E40
    ),
    MATCH_LETTER_PICTURE(
        "match_letter_pic", 2,
        "अक्षर से चित्र मिलाओ", "Match Letter with Picture",
        "अक्षर देखकर सही चित्र चुनो", "Pick the picture that starts with the letter",
        "🖼️", 0xFF2979FF
    ),
    MATCH_HINDI_WORD_PIC(
        "match_hindi_word", 3,
        "हिंदी शब्द और चित्र", "Match Hindi Word with Picture",
        "हिंदी शब्द पढ़ो/सुनो और चित्र चुनो", "Match the Hindi word to its picture",
        "🪔", 0xFFFFAB00
    ),
    MATCH_ENGLISH_WORD_PIC(
        "match_english_word", 4,
        "English Word & Picture", "Match English Word with Picture",
        "अंग्रेज़ी शब्द से सही चित्र मिलाओ", "Tap the picture for the English word",
        "📖", 0xFF00B8D4
    ),
    COUNT_OBJECTS(
        "count_objects", 5,
        "गिनो और बताओ", "Count Objects",
        "चित्रों को गिनकर सही संख्या चुनो", "Count the objects and tap the number",
        "🔢", 0xFF00C853
    ),
    IDENTIFY_COLORS(
        "identify_colors", 6,
        "रंग पहचानो", "Identify Colors",
        "पूछे गए रंग के गुब्बारे को छुओ", "Tap the balloon with the right color",
        "🎈", 0xFFAA00FF
    ),
    IDENTIFY_ANIMALS(
        "identify_animals", 7,
        "जानवर पहचानो", "Identify Animals",
        "आवाज़ और नाम से जानवर पहचानो", "Find the animal from its name & sound",
        "🦁", 0xFFFF6D00
    ),
    IDENTIFY_FRUITS(
        "identify_fruits", 8,
        "फल पहचानो", "Identify Fruits",
        "मीठे-मीठे फलों को पहचानो", "Spot the delicious fruit",
        "🥭", 0xFFFF1744
    ),
    MEMORY_MATCH(
        "memory_match", 9,
        "याददाश्त का खेल", "Memory Matching Game",
        "कार्ड पलटो और एक जैसे जोड़े मिलाओ", "Flip cards and match identical pairs",
        "🧩", 0xFF651FFF
    ),
    SIMPLE_QUIZ(
        "simple_quiz", 10,
        "सुपर मस्ती क्विज़", "Simple Super Quiz",
        "सभी विषयों का मज़ेदार महा-क्विज़", "Fun mixed quiz across all topics",
        "🏆", 0xFF00C853
    )
}

data class HindiLetterItem(
    val id: String,
    val letter: String,
    val isSwar: Boolean,
    val wordHi: String,
    val wordTranslit: String,
    val meaningEn: String,
    val phraseHi: String,
    val emoji: String,
    val cardColor: Color
)

data class EnglishLetterItem(
    val id: String,
    val letter: String,
    val wordEn: String,
    val wordHi: String,
    val phraseEn: String,
    val emoji: String,
    val cardColor: Color
)

data class NumberLessonItem(
    val number: Int,
    val digitHi: String,
    val digitEn: String,
    val wordHi: String,
    val wordEn: String,
    val objectNameHi: String,
    val objectNameEn: String,
    val emoji: String,
    val cardColor: Color
)

data class Number100Item(
    val number: Int,
    val digitHi: String,
    val wordHi: String,
    val wordEn: String
)

data class ColorLessonItem(
    val id: String,
    val nameHi: String,
    val nameEn: String,
    val color: Color,
    val borderColor: Color,
    val objectHi: String,
    val objectEn: String,
    val emoji: String,
    val speechHi: String,
    val speechEn: String
)

enum class AnimalSoundType {
    DOG, CAT, COW, LION, ELEPHANT, MONKEY, HORSE, GOAT, TIGER, RABBIT, PEACOCK, BEAR
}

data class AnimalLessonItem(
    val id: String,
    val nameHi: String,
    val nameEn: String,
    val soundWordHi: String,
    val soundWordEn: String,
    val factHi: String,
    val factEn: String,
    val emoji: String,
    val soundType: AnimalSoundType,
    val cardColor: Color
)

data class FruitLessonItem(
    val id: String,
    val nameHi: String,
    val nameEn: String,
    val tasteHi: String,
    val tasteEn: String,
    val emoji: String,
    val cardColor: Color
)

data class RhymeItem(
    val id: String,
    val titleHi: String,
    val titleEn: String,
    val isHindiRhyme: Boolean,
    val emoji: String,
    val lines: List<String>,
    val cardColor: Color
)

data class BadgeItem(
    val id: String,
    val titleHi: String,
    val titleEn: String,
    val descHi: String,
    val descEn: String,
    val emoji: String,
    val starsRequired: Int
)

data class QuizQuestion(
    val promptHi: String,
    val promptEn: String,
    val speechTextHi: String,
    val speechTextEn: String,
    val centerEmoji: String,
    val centerLabel: String,
    val options: List<QuizOption>,
    val correctIndex: Int
)

data class QuizOption(
    val mainText: String,
    val subText: String,
    val emoji: String,
    val colorHex: Long = 0xFFFFFFFF
)
