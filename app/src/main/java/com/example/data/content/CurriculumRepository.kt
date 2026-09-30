package com.example.data.content

import androidx.compose.ui.graphics.Color
import com.example.data.model.*

object CurriculumRepository {

    private val palette = listOf(
        Color(0xFFFF6E40), Color(0xFF2979FF), Color(0xFF00C853),
        Color(0xFFAA00FF), Color(0xFFFFAB00), Color(0xFFFF1744),
        Color(0xFF00B8D4), Color(0xFF651FFF)
    )

    val hindiSwar: List<HindiLetterItem> = listOf(
        HindiLetterItem("hi_s1", "अ", true, "अनार", "Anaar", "Pomegranate", "अ से अनार, मीठे-मीठे लाल दाने!", "🍎", palette[0]),
        HindiLetterItem("hi_s2", "आ", true, "आम", "Aam", "Mango", "आ से आम, फलों का राजा आम!", "🥭", palette[4]),
        HindiLetterItem("hi_s3", "इ", true, "इमली", "Imli", "Tamarind", "इ से इमली, खट्टी-मीठी इमली!", "🫘", palette[2]),
        HindiLetterItem("hi_s4", "ई", true, "ईख", "Eekh", "Sugarcane", "ई से ईख, मीठा-मीठा गन्ना!", "🎋", palette[1]),
        HindiLetterItem("hi_s5", "उ", true, "उल्लू", "Ullu", "Owl", "उ से उल्लू, रात को जागे!", "🦉", palette[3]),
        HindiLetterItem("hi_s6", "ऊ", true, "ऊन", "Oon", "Wool", "ऊ से ऊन, गर्म स्वेटर बनाए!", "🧶", palette[5]),
        HindiLetterItem("hi_s7", "ऋ", true, "ऋषि", "Rishi", "Sage", "ऋ से ऋषि, ध्यान लगाएं!", "🧘", palette[6]),
        HindiLetterItem("hi_s8", "ए", true, "एड़ी", "Edee", "Heel", "ए से एड़ी, पैरों से चलें!", "🦶", palette[7]),
        HindiLetterItem("hi_s9", "ऐ", true, "ऐनक", "Ainak", "Spectacles", "ऐ से ऐनक, दादाजी का चश्मा!", "👓", palette[0]),
        HindiLetterItem("hi_s10", "ओ", true, "ओखली", "Okhli", "Mortar", "ओ से ओखली, मसाले कूटो!", "🥣", palette[4]),
        HindiLetterItem("hi_s11", "औ", true, "औरत", "Aurat", "Woman / Mother", "औ से औरत, प्यारी माँ!", "👩", palette[5]),
        HindiLetterItem("hi_s12", "अं", true, "अंगूर", "Angoor", "Grapes", "अं से अंगूर, हरे-हरे गुच्छे!", "🍇", palette[2]),
        HindiLetterItem("hi_s13", "अः", true, "अः (प्रातः)", "Aha", "Joyful Sound", "अः से हँसो, हा हा हा!", "😄", palette[1])
    )

    val hindiVyanjan: List<HindiLetterItem> = listOf(
        HindiLetterItem("hi_v1", "क", false, "कबूतर", "Kabootar", "Pigeon", "क से कबूतर, गुटर-गूँ बोले!", "🕊️", palette[1]),
        HindiLetterItem("hi_v2", "ख", false, "खरगोश", "Khargosh", "Rabbit", "ख से खरगोश, तेज़ दौड़े!", "🐰", palette[2]),
        HindiLetterItem("hi_v3", "ग", false, "गमला", "Gamla", "Flower Pot", "ग से गमला, सुंदर फूल खिलाए!", "🪴", palette[0]),
        HindiLetterItem("hi_v4", "घ", false, "घड़ी", "Ghadi", "Clock / Watch", "घ से घड़ी, टिक-टिक समय बताए!", "⏰", palette[3]),
        HindiLetterItem("hi_v5", "च", false, "चम्मच", "Chammach", "Spoon", "च से चम्मच, खीर खाओ!", "🥄", palette[4]),
        HindiLetterItem("hi_v6", "छ", false, "छतरी", "Chhatri", "Umbrella", "छ से छतरी, बारिश से बचाए!", "☂️", palette[5]),
        HindiLetterItem("hi_v7", "ज", false, "जहाज़", "Jahaaz", "Ship", "ज से जहाज़, पानी पर तैरे!", "🚢", palette[6]),
        HindiLetterItem("hi_v8", "झ", false, "झंडा", "Jhanda", "Flag", "झ से झंडा, ऊँचा रहे तिरंगा!", "🇮🇳", palette[7]),
        HindiLetterItem("hi_v9", "ट", false, "टमाटर", "Tamatar", "Tomato", "ट से टमाटर, लाल और गोल!", "🍅", palette[5]),
        HindiLetterItem("hi_v10", "पतंग", false, "तितली", "Titli", "Butterfly", "त से तितली, रंग-बिरंगी उड़े!", "🦋", palette[3]).copy(letter = "त"),
        HindiLetterItem("hi_v11", "प", false, "पतंग", "Patang", "Kite", "प से पतंग, आसमान में उड़े!", "🪁", palette[4]),
        HindiLetterItem("hi_v12", "म", false, "मछली", "Machhli", "Fish", "म से मछली, जल की रानी है!", "🐟", palette[6])
    )

    val allHindiLetters: List<HindiLetterItem> = hindiSwar + hindiVyanjan

    val englishAlphabet: List<EnglishLetterItem> = listOf(
        EnglishLetterItem("en_a", "A", "Apple", "सेब", "A for Apple, sweet red apple!", "🍎", palette[5]),
        EnglishLetterItem("en_b", "B", "Ball", "गेंद", "B for Ball, bounce the ball!", "⚽", palette[1]),
        EnglishLetterItem("en_c", "C", "Cat", "बिल्ली", "C for Cat, little cat says meow!", "🐱", palette[4]),
        EnglishLetterItem("en_d", "D", "Dog", "कुत्ता", "D for Dog, friendly puppy dog!", "🐶", palette[0]),
        EnglishLetterItem("en_e", "E", "Elephant", "हाथी", "E for Elephant, big gentle elephant!", "🐘", palette[6]),
        EnglishLetterItem("en_f", "F", "Fish", "मछली", "F for Fish, swimming in the water!", "🐠", palette[2]),
        EnglishLetterItem("en_g", "G", "Grapes", "अंगूर", "G for Grapes, juicy bunch of grapes!", "🍇", palette[3]),
        EnglishLetterItem("en_h", "H", "Horse", "घोड़ा", "H for Horse, galloping fast!", "🐴", palette[7]),
        EnglishLetterItem("en_i", "I", "Ice Cream", "आइसक्रीम", "I for Ice Cream, yummy cold treat!", "🍦", palette[5]),
        EnglishLetterItem("en_j", "J", "Jug", "जग", "J for Jug, full of fresh water!", "🏺", palette[1]),
        EnglishLetterItem("en_k", "K", "Kite", "पतंग", "K for Kite, flying high in the sky!", "🪁", palette[4]),
        EnglishLetterItem("en_l", "L", "Lion", "शेर", "L for Lion, king of the jungle!", "🦁", palette[0]),
        EnglishLetterItem("en_m", "M", "Mango", "आम", "M for Mango, sweet yellow mango!", "🥭", palette[4]),
        EnglishLetterItem("en_n", "N", "Nest", "घोंसला", "N for Nest, home for baby birds!", "🪹", palette[2]),
        EnglishLetterItem("en_o", "O", "Orange", "संतरा", "O for Orange, round and tangy!", "🍊", palette[0]),
        EnglishLetterItem("en_p", "P", "Parrot", "तोता", "P for Parrot, green talking bird!", "🦜", palette[2]),
        EnglishLetterItem("en_q", "Q", "Queen", "रानी", "Q for Queen, wearing a golden crown!", "👑", palette[3]),
        EnglishLetterItem("en_r", "R", "Rabbit", "खरगोश", "R for Rabbit, hopping in the garden!", "🐰", palette[5]),
        EnglishLetterItem("en_s", "S", "Sun", "सूरज", "S for Sun, shining bright all day!", "☀️", palette[4]),
        EnglishLetterItem("en_t", "T", "Tiger", "बाघ", "T for Tiger, strong striped tiger!", "🐯", palette[0]),
        EnglishLetterItem("en_u", "U", "Umbrella", "छतरी", "U for Umbrella, keeps us dry in rain!", "☂️", palette[6]),
        EnglishLetterItem("en_v", "V", "Van", "गाड़ी", "V for Van, driving on the road!", "🚐", palette[1]),
        EnglishLetterItem("en_w", "W", "Watch", "घड़ी", "W for Watch, tells us the time!", "⌚", palette[7]),
        EnglishLetterItem("en_x", "X", "Xylophone", "ज़ाइलोफ़ोन", "X for Xylophone, plays happy music!", "🎼", palette[3]),
        EnglishLetterItem("en_y", "Y", "Yacht", "नाव", "Y for Yacht, sailing on the sea!", "⛵", palette[6]),
        EnglishLetterItem("en_z", "Z", "Zebra", "ज़ेबरा", "Z for Zebra, black and white stripes!", "🦓", palette[2])
    )

    val countingLessons: List<NumberLessonItem> = listOf(
        NumberLessonItem(1, "१", "1", "एक", "One", "सेब", "Apple", "🍎", palette[5]),
        NumberLessonItem(2, "२", "2", "दो", "Two", "गेंदें", "Balls", "⚽", palette[1]),
        NumberLessonItem(3, "३", "3", "तीन", "Three", "तारे", "Stars", "⭐", palette[4]),
        NumberLessonItem(4, "४", "4", "चार", "Four", "तितलियाँ", "Butterflies", "🦋", palette[3]),
        NumberLessonItem(5, "५", "5", "पाँच", "Five", "आम", "Mangoes", "🥭", palette[0]),
        NumberLessonItem(6, "६", "6", "छह", "Six", "फूल", "Flowers", "🌻", palette[2]),
        NumberLessonItem(7, "७", "7", "सात", "Seven", "गुब्बारे", "Balloons", "🎈", palette[5]),
        NumberLessonItem(8, "८", "8", "आठ", "Eight", "मछलियाँ", "Fishes", "🐠", palette[6]),
        NumberLessonItem(9, "९", "9", "नौ", "Nine", "आइसक्रीम", "Ice Creams", "🍦", palette[7]),
        NumberLessonItem(10, "१०", "10", "दस", "Ten", "बतखें", "Ducks", "🐤", palette[4])
    )

    private val hindiNumberNames = listOf(
        "", "एक", "दो", "तीन", "चार", "पाँच", "छह", "सात", "आठ", "नौ", "दस",
        "ग्यारह", "बारह", "तेरह", "चौदह", "पंद्रह", "सोलह", "सत्रह", "अठारह", "उन्नीस", "बीस",
        "इक्कीस", "बाईस", "तेईस", "चौबीस", "पच्चीस", "छब्बीस", "सत्ताईस", "अट्ठाईस", "उनतीस", "तीस",
        "इकतीस", "बत्तीस", "तैंतीस", "चौंतीस", "पैंतीस", "छत्तीस", "सैंतीस", "अड़तीस", "उनतालीस", "चालीस",
        "इकतालीस", "बयालीस", "तैंतालीस", "चवालीस", "पैंतालीस", "छियालीस", "सैंतालीस", "अड़तालीस", "उनचास", "पचास",
        "इक्यावन", "बावन", "तिरेपन", "चौवन", "पचपन", "छप्पन", "सत्तावन", "अट्ठावन", "उनसठ", "साठ",
        "इकसठ", "बासठ", "तिरेसठ", "चौंसठ", "पैंसठ", "छियासठ", "सड़सठ", "अड़सठ", "उनहत्तर", "सत्तर",
        "इकहत्तर", "बहत्तर", "तिहत्तर", "चौहत्तर", "पचहत्तर", "छिहत्तर", "सतहत्तर", "अठहत्तर", "उनासी", "अस्सी",
        "इक्यासी", "बयासी", "तिरासी", "चौरासी", "पचासी", "छियासी", "सतासी", "अठासी", "नवासी", "नब्बे",
        "इक्यानवे", "बानवे", "तिरानवे", "चौरानवे", "पचानवे", "छियानवे", "सत्तानवे", "अट्ठानवे", "निन्यानवे", "सौ"
    )

    fun toDevanagariDigits(n: Int): String {
        val devDigits = charArrayOf('०', '१', '२', '३', '४', '५', '६', '७', '८', '९')
        return n.toString().map { devDigits[it - '0'] }.joinToString("")
    }

    private fun englishWordForNumber(n: Int): String {
        val ones = arrayOf("", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")
        val tens = arrayOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")
        return when {
            n == 100 -> "One Hundred"
            n < 20 -> ones[n]
            n % 10 == 0 -> tens[n / 10]
            else -> "${tens[n / 10]}-${ones[n % 10]}"
        }
    }

    val numbers1To100: List<Number100Item> by lazy {
        (1..100).map { n ->
            Number100Item(
                number = n,
                digitHi = toDevanagariDigits(n),
                wordHi = hindiNumberNames.getOrElse(n) { n.toString() },
                wordEn = englishWordForNumber(n)
            )
        }
    }

    val colorsLessons: List<ColorLessonItem> = listOf(
        ColorLessonItem("col_red", "लाल", "Red", Color(0xFFE53935), Color(0xFFB71C1C), "लाल सेब", "Red Apple", "🍎", "यह लाल रंग है, जैसे मीठा सेब!", "This is Red color, like a sweet apple!"),
        ColorLessonItem("col_blue", "नीला", "Blue", Color(0xFF1E88E5), Color(0xFF0D47A1), "नीला आसमान", "Blue Whale", "🐳", "यह नीला रंग है, जैसे खुला आसमान!", "This is Blue color, like the big ocean!"),
        ColorLessonItem("col_green", "हरा", "Green", Color(0xFF43A047), Color(0xFF1B5E20), "हरा पेड़", "Green Tree", "🌳", "यह हरा रंग है, जैसे हरा-भरा पेड़!", "This is Green color, like a leafy tree!"),
        ColorLessonItem("col_yellow", "पीला", "Yellow", Color(0xFFFDD835), Color(0xFFF57F17), "पीला सूरज", "Yellow Sun", "🌻", "यह पीला रंग है, जैसे चमकता सूरज!", "This is Yellow color, like a bright sunflower!"),
        ColorLessonItem("col_orange", "नारंगी", "Orange", Color(0xFFFB8C00), Color(0xFFE65100), "नारंगी संतरा", "Orange Fruit", "🍊", "यह नारंगी रंग है, जैसे रसीला संतरा!", "This is Orange color, like a juicy orange!"),
        ColorLessonItem("col_pink", "गुलाबी", "Pink", Color(0xFFEC407A), Color(0xFF880E4F), "गुलाबी कमल", "Pink Flamingo", "🦩", "यह गुलाबी रंग है, कितना प्यारा फूल!", "This is Pink color, soft and pretty!"),
        ColorLessonItem("col_black", "काला", "Black", Color(0xFF263238), Color(0xFF000000), "काला कौआ", "Black Hat", "🎩", "यह काला रंग है, जैसे रात का आसमान!", "This is Black color, like a magic hat!"),
        ColorLessonItem("col_white", "सफ़ेद", "White", Color(0xFFF5F5F5), Color(0xFF90A4AE), "सफ़ेद बादल", "White Snowman", "⛄", "यह सफ़ेद रंग है, जैसे रूई के बादल!", "This is White color, like fluffy clouds!"),
        ColorLessonItem("col_purple", "बैंगनी", "Purple", Color(0xFF8E24AA), Color(0xFF4A148C), "बैंगनी अंगूर", "Purple Grapes", "🍇", "यह बैंगनी रंग है, जैसे स्वादिष्ट जामुन!", "This is Purple color, like royal grapes!"),
        ColorLessonItem("col_brown", "भूरा", "Brown", Color(0xFF6D4C41), Color(0xFF3E2723), "भूरा भालू", "Brown Teddy Bear", "🧸", "यह भूरा रंग है, जैसे प्यारा टेडी बियर!", "This is Brown color, like a cuddly bear!")
    )

    val animalsLessons: List<AnimalLessonItem> = listOf(
        AnimalLessonItem("ani_dog", "कुत्ता", "Dog", "भौं-भौं!", "Woof Woof!", "कुत्ता हमारा वफ़ादार दोस्त है और घर की रखवाली करता है।", "Dog is a friendly pet that says Woof Woof!", "🐶", AnimalSoundType.DOG, palette[0]),
        AnimalLessonItem("ani_cat", "बिल्ली", "Cat", "म्याऊँ-म्याऊँ!", "Meow Meow!", "प्यारी बिल्ली को दूध बहुत पसंद है और वह म्याऊँ बोलती है।", "Little cat loves milk and softly says Meow!", "🐱", AnimalSoundType.CAT, palette[4]),
        AnimalLessonItem("ani_cow", "गाय", "Cow", "अंभाऽऽ (मूँ)!", "Moo Moo!", "गाय हमें पौष्टिक और मीठा दूध देती है।", "Cow is gentle and gives us healthy milk!", "🐮", AnimalSoundType.COW, palette[2]),
        AnimalLessonItem("ani_lion", "शेर", "Lion", "दहाड़ (गर्जन)!", "Roarrr!", "शेर जंगल का बहादुर राजा कहलाता है!", "Lion is the brave king of the jungle!", "🦁", AnimalSoundType.LION, palette[4]),
        AnimalLessonItem("ani_elephant", "हाथी", "Elephant", "चिंघाड़ (पों-पों)!", "Trumpet!", "हाथी की लंबी सूंड और बड़े-बड़े कान होते हैं!", "Elephant is the biggest land animal with a long trunk!", "🐘", AnimalSoundType.ELEPHANT, palette[1]),
        AnimalLessonItem("ani_monkey", "बंदर", "Monkey", "खो-खो, चटर-पटर!", "Ooh Ooh Aah Aah!", "नटखट बंदर पेड़ की डालियों पर उछल-कूद करता है!", "Playful monkey loves bananas and jumps on trees!", "🐵", AnimalSoundType.MONKEY, palette[0]),
        AnimalLessonItem("ani_horse", "घोड़ा", "Horse", "हिनहिनाना!", "Neigh Neigh!", "घोड़ा हवा से बातें करते हुए बहुत तेज़ दौड़ता है!", "Horse runs super fast across the green field!", "🐴", AnimalSoundType.HORSE, palette[7]),
        AnimalLessonItem("ani_goat", "बकरी", "Goat", "में-में!", "Baa Baa!", "बकरी हरी-हरी पत्तियाँ खाती है और में-में बोलती है।", "Goat climbs hills and says Baa Baa!", "🐐", AnimalSoundType.GOAT, palette[2]),
        AnimalLessonItem("ani_tiger", "बाघ", "Tiger", "गुर्राना!", "Grrrr Roar!", "बाघ भारत का राष्ट्रीय पशु है, जिस पर काली धारियाँ होती हैं।", "Tiger has bright orange fur with black stripes!", "🐯", AnimalSoundType.TIGER, palette[5]),
        AnimalLessonItem("ani_rabbit", "खरगोश", "Rabbit", "चूँ-चूँ (फुदकना)!", "Squeak Hop!", "नन्हा खरगोश गाजर खाता है और फुदक-फुदक कर चलता है!", "Fluffy rabbit loves crunchy carrots and hops fast!", "🐰", AnimalSoundType.RABBIT, palette[3]),
        AnimalLessonItem("ani_peacock", "मोर", "Peacock", "पीहू-पीहू!", "Pihu Call!", "मोर भारत का राष्ट्रीय पक्षी है जो बारिश में सुंदर नाचता है!", "Peacock dances with colorful feathers in the rain!", "🦚", AnimalSoundType.PEACOCK, palette[6]),
        AnimalLessonItem("ani_bear", "भालू", "Bear", "गुर्र-गुर्र!", "Growl!", "मोटे भालू को मीठा शहद खाना बहुत अच्छा लगता है!", "Big cuddly bear loves eating sweet honey!", "🐻", AnimalSoundType.BEAR, palette[0])
    )

    val fruitsLessons: List<FruitLessonItem> = listOf(
        FruitLessonItem("fr_apple", "सेब", "Apple", "लाल, कुरकुरा और सेहतमंद सेब!", "Crunchy red apple keeps us strong every day!", "🍎", palette[5]),
        FruitLessonItem("fr_mango", "आम", "Mango", "पीला और रसीला फलों का राजा आम!", "Sweet yellow mango, the king of all fruits!", "🥭", palette[4]),
        FruitLessonItem("fr_banana", "केला", "Banana", "ताकत देने वाला मीठा पीला केला!", "Soft yellow banana gives us instant energy!", "🍌", palette[4]),
        FruitLessonItem("fr_orange", "संतरा", "Orange", "विटामिन सी से भरपूर खट्टा-मीठा संतरा!", "Juicy orange full of Vitamin C!", "🍊", palette[0]),
        FruitLessonItem("fr_grapes", "अंगूर", "Grapes", "छोटे-छोटे रसीले हरे और बैंगनी अंगूर!", "Sweet little grapes in a fun bunch!", "🍇", palette[3]),
        FruitLessonItem("fr_watermelon", "तरबूज", "Watermelon", "बाहर से हरा, अंदर से लाल और ठंडा तरबूज!", "Green outside, bright red and juicy inside!", "🍉", palette[2]),
        FruitLessonItem("fr_guava", "अमरूद", "Guava", "हरा और कुरकुरा मीठा अमरूद!", "Crunchy green guava from the garden tree!", "🍐", palette[2]),
        FruitLessonItem("fr_papaya", "पपीता", "Papaya", "पेट के लिए अच्छा मीठा नारंगी पपीता!", "Sweet orange papaya, healthy for our tummy!", "🍈", palette[0]),
        FruitLessonItem("fr_pomegranate", "अनार", "Pomegranate", "लाल मोती जैसे दानों वाला अनार!", "Ruby red seeds inside the pomegranate!", "🍒", palette[5]),
        FruitLessonItem("fr_pineapple", "अनानास", "Pineapple", "सिर पर ताज पहनने वाला खट्टा-मीठा अनानास!", "Tropical pineapple wearing a leafy crown!", "🍍", palette[4])
    )

    val rhymesLessons: List<RhymeItem> = listOf(
        RhymeItem(
            id = "rh_machhli",
            titleHi = "मछली जल की रानी है",
            titleEn = "Machhli Jal Ki Rani Hai",
            isHindiRhyme = true,
            emoji = "🐟",
            lines = listOf(
                "मछली जल की रानी है,",
                "जीवन उसका पानी है!",
                "हाथ लगाओ तो डर जाएगी,",
                "बाहर निकालो तो मर जाएगी,",
                "पानी में डालो तो तैर जाएगी!"
            ),
            cardColor = palette[6]
        ),
        RhymeItem(
            id = "rh_chanda",
            titleHi = "चंदा मामा दूर के",
            titleEn = "Chanda Mama Door Ke",
            isHindiRhyme = true,
            emoji = "🌙",
            lines = listOf(
                "चंदा मामा दूर के,",
                "पुए पकाएं बूर के!",
                "आप खाएं थाली में,",
                "मुन्ने को दें प्याली में!",
                "प्याली गई टूट, मुन्ना गया रूठ,",
                "लाएंगे नई प्यालियाँ बजा-बजा के तालियाँ!"
            ),
            cardColor = palette[7]
        ),
        RhymeItem(
            id = "rh_titli",
            titleHi = "तितली उड़ी बस पर चढ़ी",
            titleEn = "Titli Udi Bus Par Chadhi",
            isHindiRhyme = true,
            emoji = "🦋",
            lines = listOf(
                "तितली उड़ी, बस पर चढ़ी,",
                "सीट ना मिली तो रोने लगी!",
                "ड्राइवर ने बोला आजा मेरे पास,",
                "तितली बोली हट बदमाश, मेरा घर है पास!"
            ),
            cardColor = palette[3]
        ),
        RhymeItem(
            id = "rh_twinkle",
            titleHi = "ट्विंकल ट्विंकल लिटिल स्टार",
            titleEn = "Twinkle Twinkle Little Star",
            isHindiRhyme = false,
            emoji = "⭐",
            lines = listOf(
                "Twinkle, twinkle, little star,",
                "How I wonder what you are!",
                "Up above the world so high,",
                "Like a diamond in the sky!"
            ),
            cardColor = palette[4]
        ),
        RhymeItem(
            id = "rh_johny",
            titleHi = "जॉनी जॉनी यस पापा",
            titleEn = "Johny Johny Yes Papa",
            isHindiRhyme = false,
            emoji = "👦",
            lines = listOf(
                "Johny, Johny! Yes, Papa?",
                "Eating sugar? No, Papa!",
                "Telling lies? No, Papa!",
                "Open your mouth — Ha, ha, ha!"
            ),
            cardColor = palette[0]
        ),
        RhymeItem(
            id = "rh_baabaa",
            titleHi = "बा बा ब्लैक शीप",
            titleEn = "Baa, Baa, Black Sheep",
            isHindiRhyme = false,
            emoji = "🐑",
            lines = listOf(
                "Baa, baa, black sheep, have you any wool?",
                "Yes sir, yes sir, three bags full!",
                "One for the master, one for the dame,",
                "And one for the little boy who lives down the lane!"
            ),
            cardColor = palette[2]
        )
    )

    val badges: List<BadgeItem> = listOf(
        BadgeItem("b1", "नन्हा शुरुआत स्टार", "First Step Star", "पहला पाठ पूरा किया!", "Completed your very first lesson!", "🌟", 2),
        BadgeItem("b2", "स्वर चैंपियन", "Swar Champion", "हिंदी अक्षर उत्साह से सीखे!", "Practiced Hindi letters with joy!", "🪔", 10),
        BadgeItem("b3", "ABC एक्सप्लोरर", "ABC Explorer", "अंग्रेज़ी अक्षरों की दुनिया खोजी!", "Explored the English Alphabet!", "🔤", 20),
        BadgeItem("b4", "गिनती मास्टर", "Counting Master", "गिनती और संख्याओं के उस्ताद!", "Counted objects like a pro!", "🔢", 35),
        BadgeItem("b5", "रंगों का जादूगर", "Color Wizard", "सभी सुंदर रंगों को पहचाना!", "Learned bright and happy colors!", "🎨", 50),
        BadgeItem("b6", "प्रकृति और पशु मित्र", "Nature & Animal Friend", "जानवरों और फलों से दोस्ती की!", "Made friends with animals and fruits!", "🦁", 70),
        BadgeItem("b7", "खेल विजेता", "Game Superstar", "मज़ेदार खेल और क्विज़ जीते!", "Won stars in learning games!", "🎮", 100),
        BadgeItem("b8", "मस्ती की पाठशाला टॉपर", "Pathshala Grand Champion", "सुपर स्मार्ट बच्चा!", "Ultimate Masti Ki Pathshala Superstar!", "👑", 150)
    )
}
