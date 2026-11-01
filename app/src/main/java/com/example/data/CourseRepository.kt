package com.example.data

import com.example.data.local.entity.toChapter
import com.example.data.local.entity.toEntity
import com.example.model.Chapter
import com.example.model.QuestionType
import com.example.model.QuizQuestion

object CourseRepository {

    val chapters: List<Chapter> by lazy {
        listOf(
            createChapter1(),
            createChapter2(),
            createChapter3(),
            createChapter4(),
            createChapter5(),
            createChapter6(),
            createChapter7(),
            createChapter8(),
            createChapter9(),
            createChapter10(),
            createChapter11(),
            createChapter12(),
            createChapter13(),
            createChapter14(),
            createChapter15(),
            createChapter16(),
            createChapter17(),
            createChapter18(),
            createChapter19(),
            createChapter20(),
            createChapter21(),
            createChapter22(),
            createChapter23(),
            createChapter24(),
            createChapter25(),
            createChapter26(),
            createChapter27(),
            createChapter28(),
            createChapter29(),
            createChapter30()
        )
    }

    fun getChapterById(id: Int): Chapter? {
        return chapters.find { it.id == id }
    }

    suspend fun ensureCached(context: android.content.Context) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val dao = com.example.data.local.AppDatabase.getDatabase(context).chapterDao()
            val count = dao.getChapterCount()
            if (count < chapters.size) {
                val entities = chapters.map { it.toEntity() }
                dao.insertChapters(entities)
            }
        }
    }

    fun getCachedChaptersFlow(context: android.content.Context): kotlinx.coroutines.flow.Flow<List<Chapter>> {
        val dao = com.example.data.local.AppDatabase.getDatabase(context).chapterDao()
        return kotlinx.coroutines.flow.flow {
            dao.getAllChaptersFlow().collect { entities ->
                if (entities.isEmpty()) {
                    emit(chapters)
                } else {
                    emit(entities.map { it.toChapter() })
                }
            }
        }
    }

    suspend fun getCachedChapterById(context: android.content.Context, id: Int): Chapter? {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val dao = com.example.data.local.AppDatabase.getDatabase(context).chapterDao()
            val entity = dao.getChapterById(id)
            entity?.toChapter() ?: getChapterById(id)
        }
    }

    suspend fun forceRefreshCache(context: android.content.Context) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val dao = com.example.data.local.AppDatabase.getDatabase(context).chapterDao()
            dao.clearCache()
            val entities = chapters.map { it.toEntity() }
            dao.insertChapters(entities)
        }
    }

    private fun createChapter1() = Chapter(
        id = 1,
        title = "Chapter 1: Introduction to Python",
        hindiTitle = "पायथन का परिचय",
        category = "Basics",
        summaryHindi = "Python क्या है, किसने बनाया, और यह इतनी लोकप्रिय क्यों है?",
        introduction = "Python दुनिया की सबसे लोकप्रिय और आसान प्रोग्रामिंग लैंग्वेज है। इसे Guido van Rossum ने 1991 में विकसित किया था।",
        definition = "Python is a high-level, interpreted, general-purpose programming language that emphasizes code readability with its notable use of significant whitespace.",
        detailedHindiExplanation = "Python को इस तरह डिज़ाइन किया गया है कि इसका कोड इंसानों की आम बोलचाल वाली English की तरह पढ़ा जा सके। दूसरे प्रोग्रामिंग भाषाओं (जैसे C++ या Java) की तुलना में Python में कम कोड लिखना पड़ता है। यह AI, Data Science, Web Development, Automation और Hacking हर क्षेत्र में उपयोग होती है।",
        importantConcepts = listOf(
            "High-Level Language: मशीन कोड के बजाय इंसानों के पढ़ने योग्य।",
            "Interpreted: कोड लाइन-बाय-लाइन रन होता है, अलग से कम्पाउंड कंपाइलेशन की आवश्यकता नहीं होती।",
            "Dynamically Typed: वेरिएबल का टाइप पहले से घोषित करने की ज़रूरत नहीं होती।",
            "Cross-Platform: Windows, Mac, Linux और Android सभी पर चलती है।"
        ),
        realLifeExample = "जैसे हिंदी या अंग्रेजी भाषा में हम अपनी बात दूसरों को समझाते हैं, उसी तरह Python एक भाषा है जिससे हम कंप्यूटर को निर्देश (Instructions) देते हैं।",
        pythonCodeExample = """# Pehla Python Program
print("Namaste Python Duniya!")
print("Welcome to MD LUKMAN PYTHON AI")
""",
        lineByLineExplanation = listOf(
            "Line 1: # से शुरू होने वाली लाइन Comment होती है, जिसे Python अनदेखा कर देता है।",
            "Line 2: print() एक इन-बिल्ट फंक्शन है जो स्क्रीन पर टेक्स्ट आउटपुट दिखाता है।",
            "Line 3: डबल कोट्स (\" \") के अंदर लिखा टेक्स्ट स्ट्रिंग कहलाता है।"
        ),
        commonMistakes = listOf(
            "print को कैपिटल 'Print' लिखना (Python Case-Sensitive है)।",
            "स्ट्रिंग के अंत में कोट्स बंद करना भूल जाना।",
            "बिना वजह कोड की शुरुआत में खाली स्पेस देना।"
        ),
        practiceExercises = listOf(
            "स्क्रीन पर अपना नाम और शहर प्रिंट करें।",
            "दो अलग-अलग print() स्टेटमेंट का उपयोग करके अपनी पसंदीदा हॉबी बताएं।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 101,
                question = "Who developed the Python programming language?",
                hindiQuestion = "पायथन प्रोग्रामिंग भाषा को किसने विकसित किया?",
                options = listOf("Guido van Rossum", "Dennis Ritchie", "James Gosling", "Bjarne Stroustrup"),
                correctOptionIndex = 0,
                hindiExplanation = "Python को Guido van Rossum ने 1991 में नीदरलैंड में बनाया था।"
            ),
            QuizQuestion(
                id = 102,
                question = "Is Python an interpreted or compiled language?",
                hindiQuestion = "क्या पायथन एक interpreted भाषा है?",
                options = listOf("Yes, it executes line by line", "No, only compiled", "Neither", "Only machine code"),
                correctOptionIndex = 0,
                hindiExplanation = "Python कोड को लाइन-बाय-लाइन इंटरप्रेट किया जाता है।"
            )
        )
    )

    private fun createChapter2() = Chapter(
        id = 2,
        title = "Chapter 2: Installing Python",
        hindiTitle = "पायथन इंस्टॉलेशन और सेटअप",
        category = "Basics",
        summaryHindi = "Python को अपने सिस्टम में कैसे इनस्टॉल करें और IDLE क्या है?",
        introduction = "Python का उपयोग करने के लिए हमें Python Interpreter और एक Text Editor या IDE की आवश्यकता होती है।",
        definition = "Installation refers to setting up the Python runtime environment, PATH variables, and an IDE (like VS Code, PyCharm, or IDLE) to write and execute code.",
        detailedHindiExplanation = "Python को python.org से आसानी से डाउनलोड किया जा सकता है। इंस्टॉलेशन के समय 'Add Python to PATH' पर टिक करना सबसे महत्वपूर्ण कदम है, ताकि टर्मिनल में कहीं से भी python कमांड चल सके। हमारे इस मोबाइल ऐप में आपको अलग से कुछ भी इंस्टॉल करने की ज़रूरत नहीं है—हमारा इन-बिल्ट Code Editor तुरंत काम करता है!",
        importantConcepts = listOf(
            "Official Website: python.org",
            "Add to PATH: ऑपरेटिंग सिस्टम को Python का पता बताना।",
            "IDLE: Python के साथ मिलने वाला डिफ़ॉल्ट इंटीग्रेटेड डेवलपमेंट एनवायरनमेंट।",
            "PIP: Python पैकेज मैनेजर, जिससे लाइब्रेरीज़ इंस्टॉल की जाती हैं।"
        ),
        realLifeExample = "जैसे कोई गेम खेलने से पहले उसे फोन में इंस्टॉल करना पड़ता है, वैसे ही कोड रन करने के लिए Python सॉफ्टवेयर इंस्टॉल करना होता है।",
        pythonCodeExample = """import sys
print("Python Version Info:")
print(sys.version)
""",
        lineByLineExplanation = listOf(
            "Line 1: import sys - सिस्टम मॉड्यूल को इम्पोर्ट करता है।",
            "Line 2: हेडिंग प्रिंट करता है।",
            "Line 3: sys.version - वर्तमान में चल रहे Python का वर्शन बताता है।"
        ),
        commonMistakes = listOf(
            "इंस्टॉलेशन के दौरान 'Add Python to PATH' चेकबॉक्स को अनचेक छोड़ देना।",
            "गलत बिट (32-bit vs 64-bit) का इंस्टालर डाउनलोड करना।"
        ),
        practiceExercises = listOf(
            "टर्मिनल में 'python --version' लिखकर देखें।",
            "IDLE खोलकर उसमें 5 + 5 कैलकुलेट करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 201,
                question = "What is the official website to download Python?",
                hindiQuestion = "पायथन डाउनलोड करने की आधिकारिक वेबसाइट कौन सी है?",
                options = listOf("python.org", "python.com", "getpython.io", "pythontutorial.net"),
                correctOptionIndex = 0,
                hindiExplanation = "Python हमेशा python.org से ही डाउनलोड किया जाता है।"
            )
        )
    )

    private fun createChapter3() = Chapter(
        id = 3,
        title = "Chapter 3: Python Syntax",
        hindiTitle = "पायथन सिंटेक्स और इंडेंटेशन",
        category = "Basics",
        summaryHindi = "Indentation का महत्व और Python में कोड लिखने के नियम।",
        introduction = "Syntax का मतलब है किसी प्रोग्रामिंग भाषा में कोड लिखने के व्याकरण (Grammar) के नियम।",
        definition = "Syntax refers to the set of rules that defines the combinations of symbols that are considered to be correctly structured programs in Python.",
        detailedHindiExplanation = "Python की सबसे अनोखी बात है 'Indentation' (खाली जगह या स्पेस)। अन्य भाषाओं में कोड ब्लॉक्स के लिए कर्ली ब्रैकेट्स `{ }` का उपयोग होता है, लेकिन Python में केवल स्पेस (4 Spaces / Tab) से तय होता है कि कौन सा कोड किस ब्लॉक के अंदर है।",
        importantConcepts = listOf(
            "Indentation: ब्लॉक दर्शाने के लिए 4 स्पेस का मानक।",
            "Case Sensitivity: 'Name' और 'name' दो अलग-अलग चीज़ें हैं।",
            "Comments: # चिन्ह से सिंगल लाइन कमेंट लिखा जाता है।",
            "No Semicolons: लाइन के अंत में सेमीकोलन (;) लगाना अनिवार्य नहीं है।"
        ),
        realLifeExample = "जैसे पैराग्राफ लिखते समय हम नई बात के लिए थोड़ा स्पेस छोड़कर नई लाइन शुरू करते हैं, वैसे ही Python में इंडेंटेशन से कोड का संबंध तय होता है।",
        pythonCodeExample = """# Indentation Example
number = 10
if number > 5:
    print("Number 5 se bada hai!")
    print("Yeh line if block ke andar hai.")
print("Yeh line bahar hai.")
""",
        lineByLineExplanation = listOf(
            "Line 2: number वेरिएबल में 10 स्टोर किया।",
            "Line 3: if कंडीशन की जाँच की।",
            "Line 4-5: 4 स्पेस आगे हैं, यानी ये तभी चलेंगे जब number > 5 सच होगा।",
            "Line 6: कोई स्पेस नहीं है, यह बिना शर्त हमेशा चलेगा।"
        ),
        commonMistakes = listOf(
            "IndentationError: एक ब्लॉक में 2 स्पेस और दूसरे में 4 स्पेस मिला देना।",
            "if, for या def के बाद कोलन (:) लगाना भूल जाना।"
        ),
        practiceExercises = listOf(
            "एक if कंडीशन लिखें जो चेक करे कि संख्या 0 से बड़ी है या नहीं।",
            "दो लाइन का कोड लिखें और इंडेंटेशन का प्रयोग करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 301,
                question = "How does Python indicate a block of code?",
                hindiQuestion = "पायथन में कोड ब्लॉक को कैसे दर्शाया जाता है?",
                options = listOf("Indentation (Whitespace)", "Curly braces {}", "Parentheses ()", "Semicolons ;"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में कर्ली ब्रैकेट्स के बजाय इंडेंटेशन (स्पेस) से कोड ब्लॉक बनता है।"
            )
        )
    )

    private fun createChapter4() = Chapter(
        id = 4,
        title = "Chapter 4: Variables",
        hindiTitle = "वेरिएबल्स (Variables)",
        category = "Basics",
        summaryHindi = "Variables क्या हैं, इन्हें कैसे बनाएं और नामकरण के नियम क्या हैं?",
        introduction = "Variable कंप्यूटर मेमोरी में डेटा स्टोर करने के लिए एक नामी बॉक्स (कंटेनर) की तरह होता है।",
        definition = "A variable is a reserved memory location to store values. In Python, a variable is created the moment you first assign a value to it.",
        detailedHindiExplanation = "Python में वेरिएबल बनाते समय उसका टाइप (जैसे int, float) लिखने की ज़रूरत नहीं होती। जैसे ही आप `x = 25` लिखते हैं, Python अपने आप समझ जाता है कि x एक पूर्णांक (Integer) है। वेरिएबल का नाम मीनिंगफुल होना चाहिए जैसे `user_age` या `total_price`।",
        importantConcepts = listOf(
            "Assignment Operator (=): वैल्यू को वेरिएबल में डालना।",
            "Naming Rules: नाम लेटर या अंडरस्कोर (_) से शुरू होना चाहिए, नंबर से नहीं।",
            "No Special Characters: नाम में @, $, % नहीं आ सकते।",
            "Dynamic Typing: एक ही वेरिएबल में पहले नंबर और बाद में टेक्स्ट डाल सकते हैं।"
        ),
        realLifeExample = "किचन में मसालों के डिब्बे जिन पर लेबल लगा हो (नमक, हल्दी)। डिब्बा = वेरिएबल, अंदर की चीज़ = वैल्यू।",
        pythonCodeExample = """student_name = "Lukman"
roll_number = 101
marks = 95.5
is_passed = True

print("Student:", student_name)
print("Roll:", roll_number)
print("Marks:", marks)
""",
        lineByLineExplanation = listOf(
            "Line 1: student_name में 'Lukman' टेक्स्ट स्टोर किया।",
            "Line 2: roll_number में 101 नंबर स्टोर किया।",
            "Line 3: marks में दशमलव संख्या (Float) स्टोर की।",
            "Line 4: is_passed में बूलियन वैल्यू True स्टोर की।"
        ),
        commonMistakes = listOf(
            "वेरिएबल नाम 1name (नंबर से) शुरू करना।",
            "वेरिएबल नाम में स्पेस देना (user name = ... गलत है, user_name सही है)।"
        ),
        practiceExercises = listOf(
            "अपनी उम्र, नाम और शहर के लिए तीन वेरिएबल्स बनाएं और प्रिंट करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 401,
                question = "Which of the following is an INVALID variable name in Python?",
                hindiQuestion = "इनमें से कौन सा पायथन में एक अमान्य (Invalid) वेरिएबल नाम है?",
                options = listOf("2nd_user", "_my_var", "user_name", "user2"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में कोई भी वेरिएबल नाम संख्या (Digit) से शुरू नहीं हो सकता।"
            )
        )
    )

    private fun createChapter5() = Chapter(
        id = 5,
        title = "Chapter 5: Data Types",
        hindiTitle = "डेटा टाइप्स (Data Types)",
        category = "Basics",
        summaryHindi = "int, float, str, bool और type() फंक्शन का उपयोग।",
        introduction = "डेटा टाइप यह बताता है कि वेरिएबल में किस प्रकार की वैल्यू रखी गई है।",
        definition = "Data types represent the classification of data items. Python provides built-in types including numeric (int, float), text (str), boolean (bool), and sequence types.",
        detailedHindiExplanation = "कंप्यूटर को समझने के लिए डेटा के प्रकार अलग-अलग होते हैं: पूर्णांक संख्याओं के लिए `int`, दशमलव के लिए `float`, टेक्स्ट के लिए `str`, और हाँ/ना के लिए `bool`। `type()` फंक्शन से हम किसी भी वेरिएबल का डेटा टाइप जान सकते हैं।",
        importantConcepts = listOf(
            "int: बिना दशमलव वाली संख्या (e.g. 10, -500).",
            "float: दशमलव वाली संख्या (e.g. 3.14, -0.01).",
            "str: कोट्स के अंदर का टेक्स्ट (e.g. 'Hello').",
            "bool: केवल True या False.",
            "type(): डेटा टाइप चेक करने वाला फंक्शन।"
        ),
        realLifeExample = "बैंक बैलेंस में रुपये-पैसे (Float), उम्र में पूरे साल (Int), आपका नाम (String), खाता एक्टिव है या नहीं (Boolean)।",
        pythonCodeExample = """a = 50
b = 19.99
c = "Python Master"
d = True

print(type(a))  # <class 'int'>
print(type(b))  # <class 'float'>
print(type(c))  # <class 'str'>
print(type(d))  # <class 'bool'>
""",
        lineByLineExplanation = listOf(
            "Line 1-4: चार अलग-अलग डेटा टाइप के वेरिएबल्स बनाए।",
            "Line 6-9: type() फंक्शन से उनका क्लास टाइप प्रिंट किया।"
        ),
        commonMistakes = listOf(
            "नंबर को कोट्स में लिखना (जैसे \"50\" एक स्ट्रिंग बन जाता है, int नहीं)।",
            "True और False को छोटे अक्षरों (true, false) में लिखना।"
        ),
        practiceExercises = listOf(
            "एक वेरिएबल बनाएं जिसमें पाई का मान (3.14159) हो और उसका type चेक करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 501,
                question = "What is the data type of x = '100'?",
                hindiQuestion = "x = '100' का डेटा टाइप क्या होगा?",
                options = listOf("str (String)", "int (Integer)", "float", "number"),
                correctOptionIndex = 0,
                hindiExplanation = "कोट्स (' ' या \" \") के अंदर जो भी लिखा हो, वह String (str) होता है।"
            )
        )
    )

    private fun createChapter6() = Chapter(
        id = 6,
        title = "Chapter 6: Type Casting",
        hindiTitle = "टाइप कास्टिंग (Type Casting)",
        category = "Basics",
        summaryHindi = "एक डेटा टाइप को दूसरे डेटा टाइप में बदलना।",
        introduction = "Type Casting या Type Conversion का अर्थ है एक प्रकार के डेटा को दूसरे प्रकार में बदलना।",
        definition = "Type casting is the method to convert the variable data type into another data type using built-in functions like int(), float(), str().",
        detailedHindiExplanation = "जब यूज़र से इनपुट लिया जाता है तो वह हमेशा String के रूप में आता है। गणितीय गणना करने के लिए हमें उसे `int()` या `float()` में बदलना पड़ता है। इसे Explicit Type Casting कहते हैं।",
        importantConcepts = listOf(
            "int(): टेक्स्ट या फ्लोट को इंटीजर में बदलना।",
            "float(): इंटीजर या टेक्स्ट को फ्लोट में बदलना।",
            "str(): किसी भी डेटा को टेक्स्ट बनाना।",
            "Implicit Conversion: Python द्वारा स्वतः छोटी से बड़ी टाइप में कन्वर्शन।"
        ),
        realLifeExample = "जैसे बर्फ (Ice) को पिघलाकर पानी (Water) बनाना। मूल तत्व वही रहता है बस उसका रूप बदल जाता है।",
        pythonCodeExample = """num_str = "45"
num_int = int(num_str)  # String to Integer
num_float = float(num_int) # Integer to Float

print("Integer:", num_int + 5)   # 50
print("Float:", num_float)       # 45.0
""",
        lineByLineExplanation = listOf(
            "Line 1: num_str में '45' स्ट्रिंग है।",
            "Line 2: int() से इसे 45 नंबर में बदल दिया।",
            "Line 3: float() से 45.0 दशमलव नंबर बनाया।",
            "Line 5: अब इसमें 5 जोड़ सकते हैं क्योंकि यह संख्या है।"
        ),
        commonMistakes = listOf(
            "अक्षरों वाले स्ट्रिंग को int में बदलना (जैसे int('hello') ValueError देगा)।"
        ),
        practiceExercises = listOf(
            "एक दशमलव संख्या '12.85' को पहले float में और फिर int में बदलें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 601,
                question = "What will be the output of int(7.9)?",
                hindiQuestion = "int(7.9) का आउटपुट क्या होगा?",
                options = listOf("7", "8", "7.9", "Error"),
                correctOptionIndex = 0,
                hindiExplanation = "int() दशमलव के बाद वाले हिस्से को हटा देता है, राउंड नहीं करता। इसलिए 7 मिलेगा।"
            )
        )
    )

    private fun createChapter7() = Chapter(
        id = 7,
        title = "Chapter 7: Operators",
        hindiTitle = "ऑपरेटर्स (Operators)",
        category = "Basics",
        summaryHindi = "Arithmetic, Comparison, Logical और Assignment ऑपरेटर्स।",
        introduction = "ऑपरेटर्स विशेष सिंबल होते हैं जो वेरिएबल्स और वैल्यूज पर ऑपरेशन परफॉर्म करते हैं।",
        definition = "Operators in Python are symbols used to perform operations on values and variables (+, -, *, /, //, %, ==, and, or).",
        detailedHindiExplanation = "गणित के लिए `+`, `-`, `*`, `/`, `//` (Floor division - बिना दशमलव का भाग), और `%` (Modulo - शेषफल) का प्रयोग होता है। तुलना के लिए `==`, `!=`, `>`, `<` और लॉजिक के लिए `and`, `or`, `not` का प्रयोग होता है।",
        importantConcepts = listOf(
            "// (Floor Division): दशमलव छोड़कर केवल पूर्णांक भाग देता है।",
            "% (Modulus): भाग देने पर बचा हुआ शेषफल (Remainder) देता है।",
            "** (Exponent): घात (Power) निकालने के लिए (e.g. 2 ** 3 = 8).",
            "Logical AND: दोनों शर्तें सच होने पर ही True देता है।"
        ),
        realLifeExample = "कैलकुलेटर में प्लस, माइनस, गुणा, भाग के बटन ऑपरेटर्स के उदाहरण हैं।",
        pythonCodeExample = """a = 17
b = 5

print("Addition:", a + b)       # 22
print("Normal Div:", a / b)     # 3.4
print("Floor Div:", a // b)     # 3
print("Remainder:", a % b)      # 2
print("Power (2^3):", 2 ** 3)   # 8
""",
        lineByLineExplanation = listOf(
            "Line 4: / हमेशा फ्लोट में परिणाम देता है।",
            "Line 5: // सिर्फ 3 देता है।",
            "Line 6: 17 को 5 से भाग देने पर शेषफल 2 बचता है।",
            "Line 7: 2 की घात 3 = 2 * 2 * 2 = 8।"
        ),
        commonMistakes = listOf(
            "सिंगल = (Assignment) और डबल == (Comparison) में कन्फ्यूजन।"
        ),
        practiceExercises = listOf(
            "चेक करें कि क्या 15 एक सम (Even) संख्या है, % ऑपरेटर का प्रयोग करके।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 701,
                question = "What is the output of 10 % 3?",
                hindiQuestion = "10 % 3 का मान क्या होगा?",
                options = listOf("1", "3", "3.33", "0"),
                correctOptionIndex = 0,
                hindiExplanation = "10 को 3 से भाग देने पर शेषफल (Remainder) 1 बचता है।"
            )
        )
    )

    private fun createChapter8() = Chapter(
        id = 8,
        title = "Chapter 8: Input and Output",
        hindiTitle = "इनपुट और आउटपुट (Input and Output)",
        category = "Basics",
        summaryHindi = "input() और print() फंक्शन, f-strings की मदद से फॉर्मेटिंग।",
        introduction = "प्रोग्राम का काम यूज़र से इनपुट लेना और प्रोसेस करके आउटपुट दिखाना है।",
        definition = "Input and Output operations allow users to interact with programs via standard console input (input()) and formatted display (print()).",
        detailedHindiExplanation = "`input()` फंक्शन यूज़र से कीबोर्ड द्वारा इनपुट लेता है। यह इनपुट हमेशा 'String' के रूप में आता है। f-string (`f\"Hello {name}\"`) से हम स्ट्रिंग के अंदर वेरिएबल्स को बहुत खूबसूरती से जोड़ सकते हैं।",
        importantConcepts = listOf(
            "input(): यूज़र से टेक्स्ट स्वीकार करना।",
            "int(input()): नंबर इनपुट लेने के लिए टाइपकास्टिंग।",
            "f-strings: f\"Text {variable}\" सबसे मॉडर्न और क्लीन तरीका।"
        ),
        realLifeExample = "ATM मशीन में पिन डालना (Input) और स्क्रीन पर बैलेंस दिखाई देना (Output)।",
        pythonCodeExample = """name = "Lukman"
score = 98

# Using Modern f-String
message = f"Hello {name}, aapka score {score}% hai!"
print(message)
""",
        lineByLineExplanation = listOf(
            "Line 4: f-string में कर्ली ब्रैकेट { } के अंदर सीधे वेरिएबल का नाम लिख सकते हैं।"
        ),
        commonMistakes = listOf(
            "input() से लिए गए नंबर को बिना int() में बदले जोड़ना ('10' + '20' = '1020' बन जाएगा)।"
        ),
        practiceExercises = listOf(
            "यूज़र का नाम और उम्र पूछकर एक सुंदर ग्रीटिंग मैसेज प्रिंट करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 801,
                question = "What data type does input() return by default?",
                hindiQuestion = "input() फंक्शन डिफ़ॉल्ट रूप से किस प्रकार का डेटा लौटाता है?",
                options = listOf("str (String)", "int", "float", "depends on input"),
                correctOptionIndex = 0,
                hindiExplanation = "input() हमेशा String डेटा ही रिटर्न करता है।"
            )
        )
    )

    private fun createChapter9() = Chapter(
        id = 9,
        title = "Chapter 9: Strings",
        hindiTitle = "स्ट्रिंग्स और मेथड्स (Strings)",
        category = "Data Structures",
        summaryHindi = "String Slicing, Indexing, upper(), lower(), replace() मेथड्स।",
        introduction = "अक्षरों, शब्दों या वाक्यों के समूह को Python में String कहा जाता है।",
        definition = "A string is a sequence of characters enclosed in single or double quotes. Strings in Python are immutable.",
        detailedHindiExplanation = "स्ट्रिंग में हर अक्षर का एक इंडेक्स नंबर होता है जो 0 से शुरू होता है। Python में पीछे से गिनने के लिए नेगेटिव इंडेक्सिंग (-1 अंतिम अक्षर) भी होती है। स्ट्रिंग स्लाइसिंग `[start:stop:step]` से हम स्ट्रिंग का कोई भी टुकड़ा काट सकते हैं।",
        importantConcepts = listOf(
            "Indexing: 0 से शुरुआत, -1 यानी आखिरी कैरेक्टर।",
            "Slicing [start:end]: end वाला इंडेक्स शामिल नहीं होता।",
            "upper() और lower(): अक्षरों को बड़ा या छोटा करना।",
            "Immutable: स्ट्रिंग के किसी एक अक्षर को सीधे बदला नहीं जा सकता।"
        ),
        realLifeExample = "किताब का कोई खास पन्ना या पैराग्राफ खोलकर पढ़ना।",
        pythonCodeExample = """text = "Python Programming"

print("First char:", text[0])       # P
print("Last char:", text[-1])       # g
print("Slice [0:6]:", text[0:6])    # Python
print("Uppercase:", text.upper())
""",
        lineByLineExplanation = listOf(
            "Line 3: text[0] पहला अक्षर 'P' देता है।",
            "Line 4: text[-1] अंतिम अक्षर 'g' देता है।",
            "Line 5: text[0:6] इंडेक्स 0 से 5 तक के अक्षर देता है।"
        ),
        commonMistakes = listOf(
            "text[0] = 'J' करके स्ट्रिंग का अक्षर बदलने की कोशिश करना (TypeError आएगा)।"
        ),
        practiceExercises = listOf(
            "एक शब्द को उल्टा (Reverse) प्रिंट करें: text[::-1]."
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 901,
                question = "What is the index of the first character in a Python string?",
                hindiQuestion = "पायथन स्ट्रिंग में पहले अक्षर का इंडेक्स क्या होता है?",
                options = listOf("0", "1", "-1", "None"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में इंडेक्सिंग हमेशा 0 से शुरू होती है।"
            )
        )
    )

    private fun createChapter10() = Chapter(
        id = 10,
        title = "Chapter 10: Conditional Statements",
        hindiTitle = "कंडीशनल स्टेटमेंट्स (कंडीशन के आधार पर निर्णय)",
        category = "Control Flow",
        summaryHindi = "कंप्यूटर को निर्णय (Decision Making) लेना कैसे सिखाएं।",
        introduction = "प्रोग्राम में जब किसी शर्त (Condition) के सच या झूठ होने पर अलग-अलग काम करने हों, तब कंडीशनल स्टेटमेंट्स काम आते हैं।",
        definition = "Conditional statements in Python are used to execute a block of code only when a specified boolean condition evaluates to True.",
        detailedHindiExplanation = "दैनिक जीवन में जैसे हम निर्णय लेते हैं: 'अगर बारिश होगी तो छाता लेकर जाऊंगा, वरना नहीं'—ठीक वैसे ही प्रोग्रामिंग में निर्णय लेने के लिए Condition का प्रयोग होता है।",
        importantConcepts = listOf(
            "Boolean Logic: हर कंडीशन अंत में True या False बनती है।",
            "Comparison Operators: ==, !=, >, <, >=, <=.",
            "Indentation: सही ब्लॉक के लिए स्पेस का ध्यान।"
        ),
        realLifeExample = "ट्रैफिक सिग्नल: लाल लाइट पर गाड़ी रोकना, हरी पर चलना।",
        pythonCodeExample = """is_raining = True

if is_raining:
    print("Chhata lekar bahar niklo!")
""",
        lineByLineExplanation = listOf(
            "Line 1: is_raining में True है।",
            "Line 3: if की कंडीशन सच होने के कारण लाइन 4 का प्रिंट चलेगा।"
        ),
        commonMistakes = listOf(
            "कंडीशन के बाद कोलन (:) भूल जाना।"
        ),
        practiceExercises = listOf(
            "चेक करें कि क्या कोई संख्या 100 से बड़ी है।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1001,
                question = "What symbol is required at the end of an 'if' statement?",
                hindiQuestion = "'if' स्टेटमेंट के अंत में कौन सा सिंबल लगाना अनिवार्य है?",
                options = listOf("Colon (:)", "Semicolon (;)", "Curly bracket {", "Comma ,"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में if, elif, else के अंत में कोलन (:) लगाना अनिवार्य है।"
            )
        )
    )

    private fun createChapter11() = Chapter(
        id = 11,
        title = "Chapter 11: If, Elif and Else",
        hindiTitle = "इफ, एलिफ और एल्स (if-elif-else)",
        category = "Control Flow",
        summaryHindi = "मल्टीपल कंडीशंस की जाँच और डिफ़ॉल्ट एल्स ब्लॉक।",
        introduction = "जब हमारे पास कई सारी शर्तें हों, तब हम `elif` (Else If) का उपयोग करते हैं।",
        definition = "The if-elif-else chain executes code sequentially until one condition is True; if none match, the else fallback executes.",
        detailedHindiExplanation = "यदि पहली शर्त सच है तो उसका कोड चलेगा। यदि वह गलत है तो अगली `elif` की जाँच होगी। यदि सभी शर्तें गलत हैं तो अंत में `else` वाला कोड चलेगा।",
        importantConcepts = listOf(
            "if: पहली शर्त।",
            "elif: बीच की अतिरिक्त शर्तें (जितनी चाहें उतनी जोड़ सकते हैं)।",
            "else: जब कोई भी शर्त सच न हो तब चलने वाला बैकअप कोड।"
        ),
        realLifeExample = "परीक्षा के ग्रेड: 90 से ऊपर A+, 80 से ऊपर A, 70 से ऊपर B, और बाकी सब Pass/Fail।",
        pythonCodeExample = """marks = 85

if marks >= 90:
    print("Grade: A+")
elif marks >= 80:
    print("Grade: A")
elif marks >= 60:
    print("Grade: B")
else:
    print("Grade: C / Needs Improvement")
""",
        lineByLineExplanation = listOf(
            "marks 85 है, इसलिए पहली शर्त (>= 90) False हुई, लेकिन दूसरी शर्त (>= 80) True हो गई और 'Grade: A' प्रिंट हुआ।"
        ),
        commonMistakes = listOf(
            "गलत क्रम में कंडीशन लगाना (जैसे सबसे पहले marks >= 60 लगा देना)।"
        ),
        practiceExercises = listOf(
            "यूज़र की उम्र देखकर बताएं कि वह Child, Teenager, या Adult है।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1101,
                question = "In Python, what is the keyword for 'else if'?",
                hindiQuestion = "पायथन में 'else if' के लिए कौन सा कीवर्ड है?",
                options = listOf("elif", "elseif", "else if", "elsif"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में 'else if' को संक्षेप में 'elif' लिखा जाता है।"
            )
        )
    )

    private fun createChapter12() = Chapter(
        id = 12,
        title = "Chapter 12: Loops",
        hindiTitle = "लूप्स का परिचय (Introduction to Loops)",
        category = "Control Flow",
        summaryHindi = "प्रोग्राम में कोड को बार-बार दोहराना (Iteration)।",
        introduction = "लूप्स का उपयोग किसी कोड ब्लॉक को बार-बार दोहराने (Repeat करने) के लिए किया जाता है।",
        definition = "A loop is a programming structure that repeats a sequence of instructions until a specific condition is met.",
        detailedHindiExplanation = "अगर आपको 1 से 100 तक गिनती प्रिंट करनी हो, तो 100 बार `print()` लिखने के बजाय आप केवल 2 लाइन का लूप लिख सकते हैं। Python में दो मुख्य लूप्स होते हैं: `for` लूप और `while` लूप।",
        importantConcepts = listOf(
            "Iteration: लूप का एक बार पूरा चक्कर लगाना।",
            "Automation: बोरिंग और दोहराव वाले कामों को सेकंड्स में करना।",
            "for loop: निश्चित बार दोहराने के लिए।",
            "while loop: शर्त सच रहने तक दोहराने के लिए।"
        ),
        realLifeExample = "घड़ी की सुई जो 1 से 12 तक लगातार घूमती रहती है।",
        pythonCodeExample = """# Print numbers from 1 to 5
for num in range(1, 6):
    print("Number:", num)
""",
        lineByLineExplanation = listOf(
            "range(1, 6) 1 से 5 तक की संख्याएं देता है। num हर चक्कर में एक नया नंबर लेता है।"
        ),
        commonMistakes = listOf(
            "यह सोचना कि range(1, 6) में 6 भी शामिल होगा (अंतिम संख्या शामिल नहीं होती)।"
        ),
        practiceExercises = listOf(
            "1 से 10 तक सम संख्याएं प्रिंट करने का लूप बनाएं।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1201,
                question = "Why do we use loops in programming?",
                hindiQuestion = "हम प्रोग्रामिंग में लूप्स का उपयोग क्यों करते हैं?",
                options = listOf("To repeat code without re-writing it", "To make code run slower", "To delete variables", "To create errors"),
                correctOptionIndex = 0,
                hindiExplanation = "लूप्स कोड के दोहराव को खत्म करते हैं और काम आसान बनाते हैं।"
            )
        )
    )

    private fun createChapter13() = Chapter(
        id = 13,
        title = "Chapter 13: For Loop",
        hindiTitle = "फॉर लूप (For Loop) और range()",
        category = "Control Flow",
        summaryHindi = "Sequence (List, String, Range) पर लूप चलाना।",
        introduction = "For Loop का उपयोग किसी लिस्ट, टपल, स्ट्रिंग या रेंज के हर आइटम पर एक-एक करके जाने के लिए किया जाता है।",
        definition = "A for loop is used for iterating over a sequence (that is either a list, a tuple, a dictionary, a set, or a string).",
        detailedHindiExplanation = "`range(start, stop, step)` फंक्शन संख्याओं की एक श्रृंखला जनरेट करता है। `step` यह बताता है कि हर बार कितनी गिनती आगे कूदना है।",
        importantConcepts = listOf(
            "range(n): 0 से n-1 तक।",
            "range(start, stop): start से stop-1 तक।",
            "range(start, stop, step): कदम (step) के साथ गिनती।"
        ),
        realLifeExample = "क्लास में टीचर रोल नंबर 1 से 50 तक हाजिरी (Attendance) लगाते हैं।",
        pythonCodeExample = """# Counting by 2s (Even numbers)
for i in range(2, 11, 2):
    print("Even:", i)
""",
        lineByLineExplanation = listOf(
            "range(2, 11, 2) 2 से शुरू होकर 10 तक जाएगा और 2-2 कदम आगे बढ़ेगा: 2, 4, 6, 8, 10।"
        ),
        commonMistakes = listOf(
            "लूप वेरिएबल को लूप के अंदर गलती से ओवरराइट कर देना।"
        ),
        practiceExercises = listOf(
            "For loop की मदद से 5 का पहाड़ा (Table of 5) प्रिंट करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1301,
                question = "What numbers will list(range(1, 4)) generate?",
                hindiQuestion = "range(1, 4) कौन सी संख्याएं जनरेट करेगा?",
                options = listOf("[1, 2, 3]", "[1, 2, 3, 4]", "[0, 1, 2, 3]", "[1, 4]"),
                correctOptionIndex = 0,
                hindiExplanation = "range(1, 4) में 1, 2, 3 आएंगे; अंतिम संख्या 4 शामिल नहीं होती।"
            )
        )
    )

    private fun createChapter14() = Chapter(
        id = 14,
        title = "Chapter 14: While Loop",
        hindiTitle = "व्हाइल लूप (While Loop)",
        category = "Control Flow",
        summaryHindi = "जब तक शर्त सच हो तब तक चलने वाला लूप।",
        introduction = "जब हमें पहले से पता न हो कि लूप कितनी बार चलाना है, बल्कि किसी शर्त के आधार पर चलाना हो, तब `while` लूप इस्तेमाल होता है।",
        definition = "A while loop in Python repeatedly executes a target statement as long as a given condition is true.",
        detailedHindiExplanation = "While लूप हर चक्कर से पहले शर्त की जाँच करता है। अगर शर्त True है, तो बॉडी चलेगी। लूप के अंदर ऐसी व्यवस्था होनी चाहिए जिससे शर्त कभी False भी हो सके, वरना 'Infinite Loop' (अनंत लूप) बन जाएगा।",
        importantConcepts = listOf(
            "Condition Check: हर चक्र से पहले चेकिंग।",
            "Counter Update: `i += 1` से आगे बढ़ना।",
            "Infinite Loop: जब शर्त कभी False न हो पाए।"
        ),
        realLifeExample = "जब तक पानी की टंकी न भर जाए (Condition), तब तक मोटर चलती रहे।",
        pythonCodeExample = """count = 1
while count <= 5:
    print("Count is:", count)
    count += 1
print("Loop khatam!")
""",
        lineByLineExplanation = listOf(
            "Line 1: count को 1 से शुरू किया।",
            "Line 2: जब तक count <= 5 है, लूप चलेगा।",
            "Line 4: count में 1 जोड़ा (अगर यह न लिखें तो लूप कभी नहीं रुकेगा)।"
        ),
        commonMistakes = listOf(
            "काउंटर को बढ़ाना भूल जाना (`count += 1`), जिससे कंप्यूटर हैंग हो सकता है।"
        ),
        practiceExercises = listOf(
            "While loop का प्रयोग करके 10 से 1 तक उल्टी गिनती (Countdown) प्रिंट करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1401,
                question = "What happens if a while loop condition never becomes False?",
                hindiQuestion = "यदि while लूप की शर्त कभी False न हो तो क्या होगा?",
                options = listOf("An infinite loop occurs", "SyntaxError", "Program terminates immediately", "Variable is deleted"),
                correctOptionIndex = 0,
                hindiExplanation = "अगर शर्त हमेशा True रहे तो वह इनफिनिट लूप बन जाता है जो कभी रुकता नहीं।"
            )
        )
    )

    private fun createChapter15() = Chapter(
        id = 15,
        title = "Chapter 15: Break and Continue",
        hindiTitle = "ब्रेक और कंटिन्यू (break and continue)",
        category = "Control Flow",
        summaryHindi = "लूप को बीच में रोकना या किसी स्टेप को छोड़ना।",
        introduction = "कभी-कभी हमें लूप को समय से पहले तुरंत खत्म करना पड़ता है या किसी खास स्टेप को स्किप करना पड़ता है।",
        definition = "The break statement terminates the loop entirely. The continue statement skips the rest of the current iteration and moves to the next.",
        detailedHindiExplanation = "`break`: जब कोई चीज़ मिल जाए और आगे लूप चलाने की ज़रूरत न हो, तो तुरंत बाहर आ जाएं।\n`continue`: उस एक चक्र का काम छोड़ें और सीधे अगले चक्र पर चले जाएं।",
        importantConcepts = listOf(
            "break: लूप को तुरंत बंद कर देता है।",
            "continue: मौजूदा चक्कर को छोड़ता है और अगले पर जाता है।",
            "pass: एक खाली स्टेटमेंट जो सिंटेक्स एरर से बचाता है।"
        ),
        realLifeExample = "किताब में कोई खास नाम ढूंढना। जैसे ही नाम मिला, खोजना बंद (break)। अगर कोई पन्ना फटा है तो उसे छोड़कर अगला पन्ना पढ़ना (continue)।",
        pythonCodeExample = """# Break example
for i in range(1, 10):
    if i == 5:
        print("5 mil gaya! Loop breaking...")
        break
    print(i)
""",
        lineByLineExplanation = listOf(
            "1, 2, 3, 4 प्रिंट होगा, और जैसे ही i == 5 होगा, break लूप को समाप्त कर देगा।"
        ),
        commonMistakes = listOf(
            "break और continue को लूप के बाहर लिख देना (SyntaxError आएगा)।"
        ),
        practiceExercises = listOf(
            "1 से 10 तक लूप चलाएं, लेकिन continue का उपयोग करके 7 को स्किप करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1501,
                question = "Which statement skips the current iteration and jumps to the next?",
                hindiQuestion = "कौन सा स्टेटमेंट मौजूदा इटरेशन को छोड़कर अगले पर जाता है?",
                options = listOf("continue", "break", "pass", "return"),
                correctOptionIndex = 0,
                hindiExplanation = "continue सिर्फ चालू चक्कर को स्किप करता है, जबकि break पूरे लूप को खत्म कर देता है।"
            )
        )
    )

    private fun createChapter16() = Chapter(
        id = 16,
        title = "Chapter 16: Lists",
        hindiTitle = "लिस्ट्स (Lists) और ऑपरेशंस",
        category = "Data Structures",
        summaryHindi = "Python की सबसे उपयोगी और Mutable डेटा स्ट्रक्चर।",
        introduction = "List कई सारे आइटम्स को एक ही वेरिएबल में स्टोर करने का सबसे लोकप्रिय तरीका है। इसे स्क्वायर ब्रैकेट्स `[ ]` में लिखा जाता है।",
        definition = "Lists are used to store multiple items in a single variable. They are ordered, changeable (mutable), and allow duplicate values.",
        detailedHindiExplanation = "List में आप अलग-अलग डेटा टाइप (नंबर, टेक्स्ट, बूलियन) एक साथ रख सकते हैं। आप इसमें नया सामान जोड़ सकते हैं (`append`), हटा सकते हैं (`remove`, `pop`), और सॉर्ट कर सकते हैं।",
        importantConcepts = listOf(
            "Mutable: बनने के बाद भी इसमें बदलाव किया जा सकता है।",
            "append(x): लिस्ट के आखिर में नया आइटम जोड़ना।",
            "len(list): लिस्ट में कुल कितने आइटम्स हैं यह जानना।",
            "Index: 0 से शुरुआत।"
        ),
        realLifeExample = "किराने की खरीदारी की लिस्ट (Shopping List) जिसमें कभी भी नया सामान जोड़ा या हटाया जा सकता है।",
        pythonCodeExample = """fruits = ["Aam", "Kela", "Seb"]
fruits.append("Santra")
print("Total fruits:", len(fruits))
print("First fruit:", fruits[0])
print("Poori list:", fruits)
""",
        lineByLineExplanation = listOf(
            "Line 1: 3 फलों की लिस्ट बनाई।",
            "Line 2: append से 'Santra' जोड़ा।",
            "Line 3: len() ने 4 आउटपुट दिया।"
        ),
        commonMistakes = listOf(
            "मौजूदा इंडेक्स से बाहर का इंडेक्स एक्सेस करना (IndexError: list index out of range)."
        ),
        practiceExercises = listOf(
            "5 पसंदीदा दोस्तों के नामों की लिस्ट बनाएं और 2 नए नाम append करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1601,
                question = "Are Python lists mutable or immutable?",
                hindiQuestion = "क्या पायथन लिस्ट्स म्यूटेट (बदली) की जा सकती हैं?",
                options = listOf("Mutable (Changeable)", "Immutable (Unchangeable)", "Constant", "Static"),
                correctOptionIndex = 0,
                hindiExplanation = "Lists म्यूटेटेबल होती हैं, यानी उनके एलिमेंट्स को कभी भी बदला, जोड़ा या हटाया जा सकता है।"
            )
        )
    )

    private fun createChapter17() = Chapter(
        id = 17,
        title = "Chapter 17: Tuples",
        hindiTitle = "टपल्स (Tuples)",
        category = "Data Structures",
        summaryHindi = "Immutable (अपरिवर्तनीय) सीक्वेंस डेटा स्ट्रक्चर।",
        introduction = "Tuple भी लिस्ट की तरह ही होती है, लेकिन यह 'Immutable' होती है, यानी एक बार बनने के बाद इसे बदला नहीं जा सकता। इसे राउंड ब्रैकेट्स `( )` में लिखते हैं।",
        definition = "A tuple is a collection which is ordered and unchangeable (immutable). Written with round brackets ().",
        detailedHindiExplanation = "जब आपको ऐसा डेटा स्टोर करना हो जिसे कोई गलती से भी बदल न सके (जैसे हफ़्ते के 7 दिन, GPS निर्देशांक, रोल नंबर), तब Tuple का उपयोग किया जाता है। यह मेमोरी में लिस्ट से तेज़ होती है।",
        importantConcepts = listOf(
            "Immutable: append या remove नहीं कर सकते।",
            "Data Safety: डेटा में अनजाने में कोई बदलाव नहीं हो सकता।",
            "Single Element Tuple: एक आइटम के बाद कॉमा लगाना ज़रूरी है जैसे `(5,)`।"
        ),
        realLifeExample = "आपका आधार कार्ड नंबर या जन्मतिथि, जो हमेशा स्थिर रहती है।",
        pythonCodeExample = """days = ("Somwar", "Mangalwar", "Budhwar")
print("Total days:", len(days))
print("Pehla din:", days[0])
# days[0] = "Raviwar"  # TypeError aayega!
""",
        lineByLineExplanation = listOf(
            "days एक टपल है। इसे पढ़ा जा सकता है लेकिन इसमें फेरबदल नहीं किया जा सकता।"
        ),
        commonMistakes = listOf(
            "टपल में append() मेथड लगाने की कोशिश करना।"
        ),
        practiceExercises = listOf(
            "अपने घर के अक्षांश और देशांतर (Latitude, Longitude) को एक टपल में रखें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1701,
                question = "Which bracket is used to define a tuple?",
                hindiQuestion = "टपल को परिभाषित करने के लिए कौन से ब्रैकेट का उपयोग होता है?",
                options = listOf("Round brackets ()", "Square brackets []", "Curly brackets {}", "Angle brackets <>"),
                correctOptionIndex = 0,
                hindiExplanation = "Tuples को राउंड ब्रैकेट्स () में लिखा जाता है।"
            )
        )
    )

    private fun createChapter18() = Chapter(
        id = 18,
        title = "Chapter 18: Sets",
        hindiTitle = "सेट्स (Sets)",
        category = "Data Structures",
        summaryHindi = "Unique आइटम्स का अनऑर्डर्ड संग्रह (No Duplicates).",
        introduction = "Set गणित के समुच्चय की तरह होता है जिसमें कोई भी डुप्लिकेट (दोहराया गया) आइटम नहीं हो सकता। इसे कर्ली ब्रैकेट्स `{ }` में लिखते हैं।",
        definition = "A set is a collection which is unordered, unchangeable, and unindexed. No duplicate members are allowed.",
        detailedHindiExplanation = "अगर आप एक सेट में एक ही नाम 3 बार डालेंगे, तो Python उसे केवल 1 बार ही रखेगा। यह डुप्लीकेट डेटा हटाने और यूनियन, इंटरसेक्शन जैसे गणितीय ऑपरेशंस के लिए बहुत उपयोगी है।",
        importantConcepts = listOf(
            "No Duplicates: हर आइटम यूनिक होता है।",
            "Unordered: एलिमेंट्स का कोई फिक्स क्रम नहीं होता, इसलिए इंडेक्सिंग नहीं चलती।",
            "add() और remove(): आइटम्स जोड़ना और हटाना।"
        ),
        realLifeExample = "वोटर लिस्ट में यूनीक वोटर आईडी नंबर। एक व्यक्ति का नाम दो बार नहीं हो सकता।",
        pythonCodeExample = """numbers = {1, 2, 2, 3, 4, 4, 5}
print("Unique numbers:", numbers)  # {1, 2, 3, 4, 5}
numbers.add(6)
print("After adding 6:", numbers)
""",
        lineByLineExplanation = listOf(
            "डुप्लिकेट 2 और 4 अपने आप हट गए और सिर्फ यूनिक संख्याएं बचीं।"
        ),
        commonMistakes = listOf(
            "numbers[0] लिखकर सेट का इंडेक्स एक्सेस करना (TypeError देगा)।"
        ),
        practiceExercises = listOf(
            "एक लिस्ट [10, 20, 20, 30] से डुप्लिकेट्स हटाने के लिए set() का उपयोग करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1801,
                question = "Do Python sets allow duplicate values?",
                hindiQuestion = "क्या पायथन सेट्स में डुप्लिकेट मान हो सकते हैं?",
                options = listOf("No, duplicates are automatically removed", "Yes, always", "Only for numbers", "Only in Python 2"),
                correctOptionIndex = 0,
                hindiExplanation = "सेट्स में डुप्लिकेट वैल्यूज नहीं हो सकतीं, वे खुद-ब-खुद हट जाती हैं।"
            )
        )
    )

    private fun createChapter19() = Chapter(
        id = 19,
        title = "Chapter 19: Dictionaries",
        hindiTitle = "डिक्शनरी (Dictionaries - Key-Value Pairs)",
        category = "Data Structures",
        summaryHindi = "कुंजी और मान (Key: Value) का शक्तिशाली संग्रह।",
        introduction = "Dictionary में डेटा 'Key: Value' के जोड़ों में स्टोर होता है। जैसे असली डिक्शनरी में शब्द (Key) और उसका अर्थ (Value) होता है।",
        definition = "A dictionary is a collection which is ordered, changeable, and does not allow duplicate keys. Written with curly brackets {key: value}.",
        detailedHindiExplanation = "लिस्ट में हमें इंडेक्स 0, 1 याद रखना पड़ता है, लेकिन डिक्शनरी में हम नाम से वैल्यू ढूंढ सकते हैं जैसे `student[\"name\"]` या `student[\"marks\"]`। यह JSON और डेटाबेस रिकॉर्ड्स के लिए सबसे अहम है।",
        importantConcepts = listOf(
            "Key-Value Pair: हर वैल्यू का एक नाम (Key) होता है।",
            "Unique Keys: एक डिक्शनरी में दो एक जैसी Keys नहीं हो सकतीं।",
            "keys(), values(), items(): सभी चाबियां या वैल्यूज प्राप्त करना।"
        ),
        realLifeExample = "फोन की कॉन्टैक्ट बुक: नाम (Key) -> फोन नंबर (Value)।",
        pythonCodeExample = """student = {
    "name": "Lukman",
    "course": "Python AI",
    "score": 99
}

print("Name:", student["name"])
print("Course:", student["course"])
student["city"] = "Mumbai"
print("Updated student:", student)
""",
        lineByLineExplanation = listOf(
            "student[\"name\"] सीधे 'Lukman' देता है बिना किसी इंडेक्स नंबर के।"
        ),
        commonMistakes = listOf(
            "ऐसी Key को एक्सेस करना जो डिक्शनरी में मौजूद न हो (KeyError आता है; सुरक्षित तरीके के लिए .get() यूज़ करें)।"
        ),
        practiceExercises = listOf(
            "एक कार की डिक्शनरी बनाएं जिसमें brand, model, और year हो।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 1901,
                question = "How are items stored in a Python dictionary?",
                hindiQuestion = "पायथन डिक्शनरी में डेटा किस रूप में स्टोर होता है?",
                options = listOf("Key-Value pairs", "Only single values", "Index and arrays", "Pointers"),
                correctOptionIndex = 0,
                hindiExplanation = "Dictionary हमेशा Key: Value के जोड़े में डेटा स्टोर करती है।"
            )
        )
    )

    private fun createChapter20() = Chapter(
        id = 20,
        title = "Chapter 20: Functions",
        hindiTitle = "फंक्शंस (Functions - def और return)",
        category = "Functions",
        summaryHindi = "Code Reusability, आर्गुमेंट्स और रिटर्न वैल्यूज।",
        introduction = "Function कोड का एक ऐसा ब्लॉक होता है जो तभी चलता है जब उसे कॉल किया जाता है। इसे `def` कीवर्ड से बनाते हैं।",
        definition = "A function is a block of code which only runs when it is called. You can pass data (parameters) into it and return data as a result.",
        detailedHindiExplanation = "अगर आपको 100 जगहों पर GST कैलकुलेट करना है, तो 100 बार फॉर्मूला लिखने के बजाय आप एक `calculate_gst(amount)` फंक्शन बना लेंगे और जहाँ भी ज़रूरत हो उसे एक लाइन में कॉल कर लेंगे। इसे DRY (Don't Repeat Yourself) सिद्धांत कहते हैं।",
        importantConcepts = listOf(
            "def keyword: नया फंक्शन डिक्लेयर करना।",
            "Parameters & Arguments: फंक्शन को इनपुट भेजना।",
            "return statement: गणना का परिणाम वापस भेजना।"
        ),
        realLifeExample = "वाशिंग मशीन: कपड़े और डिटर्जेंट (Parameters) डालें, बटन दबाएं (Function Call), धुले कपड़े (Return Value) पाएं।",
        pythonCodeExample = """def add_numbers(a, b):
    result = a + b
    return result

# Calling the function
sum1 = add_numbers(10, 20)
sum2 = add_numbers(50, 100)
print("Sum 1:", sum1)
print("Sum 2:", sum2)
""",
        lineByLineExplanation = listOf(
            "def add_numbers(a, b) ने दो संख्याएं लीं, उन्हें जोड़ा, और return ने उत्तर वापस भेजा।"
        ),
        commonMistakes = listOf(
            "फंक्शन बनाने के बाद उसे कॉल करना भूल जाना।",
            "return के बाद कोड लिखना (return के बाद का कोड कभी नहीं चलता)।"
        ),
        practiceExercises = listOf(
            "एक फंक्शन बनाएं जो किसी संख्या का वर्ग (Square) रिटर्न करे।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2001,
                question = "Which keyword is used to define a function in Python?",
                hindiQuestion = "पायथन में फंक्शन बनाने के लिए किस कीवर्ड का उपयोग किया जाता है?",
                options = listOf("def", "function", "func", "define"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में फंक्शन को 'def' (define) कीवर्ड से बनाया जाता है।"
            )
        )
    )

    private fun createChapter21() = Chapter(
        id = 21,
        title = "Chapter 21: Lambda Functions",
        hindiTitle = "लैम्ब्डा फंक्शंस (Anonymous Functions)",
        category = "Functions",
        summaryHindi = "एक लाइन वाले छोटे और गुमनाम (Anonymous) फंक्शंस।",
        introduction = "Lambda Function एक छोटा गुमनाम (बिना नाम वाला) फंक्शन होता है जिसे केवल एक लाइन में लिखा जा सकता है।",
        definition = "A lambda function is a small anonymous function that can take any number of arguments, but can only have one expression.",
        detailedHindiExplanation = "जब आपको बहुत छोटा सा काम करना हो (जैसे किसी संख्या को दोगुना करना) और अलग से पूरा `def` ब्लॉक नहीं बनाना हो, तब `lambda` बहुत काम आता है। यह `map()` और `filter()` के साथ खूब इस्तेमाल होता है।",
        importantConcepts = listOf(
            "Syntax: `lambda arguments: expression`",
            "One-Liner: केवल एक ही एक्सप्रेशन हो सकता है।",
            "No return keyword: यह अपने आप एक्सप्रेशन का नतीजा लौटाता है।"
        ),
        realLifeExample = "शॉर्टकट की (Shortcut Key): जैसे Ctrl+C पूरा मेनू खोले बिना तुरंत कॉपी कर देता है।",
        pythonCodeExample = """# Lambda to double a number
double = lambda x: x * 2

print("Double of 5:", double(5))
print("Double of 12:", double(12))
""",
        lineByLineExplanation = listOf(
            "lambda x: x * 2 ने एक फंक्शन बनाया जो इनपुट x को 2 से गुणा करके सीधे रिटर्न करता है।"
        ),
        commonMistakes = listOf(
            "लैम्ब्डा के अंदर कई सारी लाइन्स या लूप्स लिखने की कोशिश करना।"
        ),
        practiceExercises = listOf(
            "एक lambda फंक्शन बनाएं जो दो संख्याओं को आपस में गुणा करे।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2101,
                question = "Can a lambda function have multiple expressions?",
                hindiQuestion = "क्या एक lambda फंक्शन में एक से अधिक expressions हो सकते हैं?",
                options = listOf("No, only one expression", "Yes, up to 10", "Yes, unlimited", "Depends on parameters"),
                correctOptionIndex = 0,
                hindiExplanation = "Lambda फंक्शन केवल एक ही सिंगल एक्सप्रेशन ले सकता है।"
            )
        )
    )

    private fun createChapter22() = Chapter(
        id = 22,
        title = "Chapter 22: Modules",
        hindiTitle = "मॉड्यूल्स और पैकेजेस (Modules)",
        category = "Functions",
        summaryHindi = "import स्टेटमेंट, math, random और custom मॉड्यूल्स।",
        introduction = "Module एक ऐसी Python फाइल होती है जिसमें पहले से लिखे हुए फंक्शंस और कोड होते हैं, जिन्हें आप अपने प्रोग्राम में `import` कर सकते हैं।",
        definition = "A module is a file containing Python definitions and statements intended for use in other Python programs (e.g., math, random).",
        detailedHindiExplanation = "पहिया दोबारा बनाने की ज़रूरत नहीं है! Python की 'Batteries Included' फिलॉसफी है, यानी गणित के लिए `math`, रैंडम नंबर के लिए `random`, समय के लिए `time` पहले से मौजूद हैं।",
        importantConcepts = listOf(
            "import module_name: पूरा मॉड्यूल लाना।",
            "from module import item: किसी खास फंक्शन को लाना।",
            "as keyword: छोटा उपनाम (Alias) देना जैसे `import math as m`."
        ),
        realLifeExample = "टूलकिट (Toolkit): जब पेचकस चाहिए तो पूरी फैक्ट्री नहीं बनाते, टूलबॉक्स से पेचकस निकाल लेते हैं।",
        pythonCodeExample = """import math

# Using built-in math module
print("Square root of 25:", math.sqrt(25))
print("Value of Pi:", math.pi)
""",
        lineByLineExplanation = listOf(
            "math.sqrt(25) ने 25 का वर्गमूल 5.0 निकाल दिया बिना हमारे कोई गणितीय फॉर्मूला लिखे।"
        ),
        commonMistakes = listOf(
            "अपनी खुद की Python फाइल का नाम किसी मॉड्यूल जैसा रख देना (जैसे अपनी फाइल का नाम math.py रख देना)।"
        ),
        practiceExercises = listOf(
            "math.pow(2, 5) का उपयोग करके 2 की घात 5 ज्ञात करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2201,
                question = "Which keyword is used to include an external module in Python?",
                hindiQuestion = "पायथन में बाहरी मॉड्यूल को शामिल करने के लिए किस कीवर्ड का उपयोग होता है?",
                options = listOf("import", "include", "require", "using"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में किसी भी मॉड्यूल को जोड़ने के लिए 'import' कीवर्ड उपयोग होता है।"
            )
        )
    )

    private fun createChapter23() = Chapter(
        id = 23,
        title = "Chapter 23: Exception Handling",
        hindiTitle = "एक्सेप्शन हैंडलिंग (try, except, finally)",
        category = "Advanced",
        summaryHindi = "प्रोग्राम को क्रैश होने से बचाना और एरर्स संभालना।",
        introduction = "प्रोग्राम चलते समय आने वाली गलतियों (Errors) को 'Exceptions' कहते हैं। इन्हें सही ढंग से संभालना एक्सेप्शन हैंडलिंग कहलाता है।",
        definition = "Exception handling is the process of responding to unwanted or unexpected events when a computer program runs, using try, except, else, and finally blocks.",
        detailedHindiExplanation = "अगर यूज़र 0 से भाग दे दे तो सामान्य प्रोग्राम क्रैश हो जाएगा। `try-except` का उपयोग करके हम कह सकते हैं: 'इस कोड को चलाने की कोशिश करो, अगर एरर आए तो क्रैश मत हो, बल्कि यूज़र को प्यार से बताओ'।",
        importantConcepts = listOf(
            "try block: वह कोड जिसमें एरर आने की संभावना हो।",
            "except block: जब एरर आए तब चलने वाला कोड।",
            "finally block: जो हर हाल में चलेगा चाहे एरर आए या न आए।"
        ),
        realLifeExample = "गाड़ी में सीटबेल्ट और एयरबैग: दुर्घटना (Error) होने पर भी जान बच जाती है (Crash नहीं होता)।",
        pythonCodeExample = """try:
    num = 10
    den = 0
    result = num / den
    print("Result:", result)
except ZeroDivisionError:
    print("Galti: 0 se kisi sankhya ko bhag nahi diya ja sakta!")
finally:
    print("Yeh line har haal mein chalegi.")
""",
        lineByLineExplanation = listOf(
            "10/0 से ZeroDivisionError उत्पन्न हुआ, जिसे except ब्लॉक ने पकड़ लिया और प्रोग्राम क्रैश होने से बच गया।"
        ),
        commonMistakes = listOf(
            "खाली except: ब्लॉक छोड़ देना बिना यह जाने कि कौन सा एरर आया।"
        ),
        practiceExercises = listOf(
            "int(input()) में अगर कोई अक्षरों वाला टेक्स्ट डाले तो उसे ValueError से सुरक्षित करें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2301,
                question = "Which block in Python always executes regardless of an exception?",
                hindiQuestion = "पायथन में कौन सा ब्लॉक एरर आने या न आने पर भी हमेशा चलता है?",
                options = listOf("finally", "try", "except", "catch"),
                correctOptionIndex = 0,
                hindiExplanation = "finally ब्लॉक बिना किसी शर्त के हर हाल में अंत में चलता है।"
            )
        )
    )

    private fun createChapter24() = Chapter(
        id = 24,
        title = "Chapter 24: File Handling",
        hindiTitle = "फाइल हैंडलिंग (File Handling)",
        category = "Advanced",
        summaryHindi = "फाइलों को पढ़ना, लिखना और with open() स्टेटमेंट।",
        introduction = "जब प्रोग्राम बंद होता है तो वेरिएबल्स का डेटा मिट जाता है। डेटा को हमेशा के लिए सुरक्षित रखने के लिए हम फाइलों में लिखते हैं।",
        definition = "File handling allows Python to create, read, update, and delete files on the local filesystem using open(), read(), write(), and close().",
        detailedHindiExplanation = "Python में `open('file.txt', 'mode')` से फाइल खोली जाती है। 'r' का मतलब Read (पढ़ना), 'w' का मतलब Write (लिखना), और 'a' का मतलब Append (आगे जोड़ना) होता है। `with` स्टेटमेंट फाइल को काम खत्म होते ही अपने आप बंद कर देता है।",
        importantConcepts = listOf(
            "'r': Read mode (फाइल पढ़ने के लिए).",
            "'w': Write mode (पुराना डेटा मिटाकर नया लिखना).",
            "'a': Append mode (पुराने डेटा के आगे नया जोड़ना).",
            "with open() as f: सबसे सुरक्षित तरीका जिससे फाइल लीक नहीं होती।"
        ),
        realLifeExample = "डायरी में पेन से लिखना (Write) और बाद में उसे पढ़ना (Read)।",
        pythonCodeExample = """# File Handling with context manager
# with open('notes.txt', 'w') as f:
#     f.write("MD Lukman Python AI course notes!")
print("File writing demonstrated safely!")
""",
        lineByLineExplanation = listOf(
            "with स्टेटमेंट फाइल के साथ सुरक्षित काम सुनिश्चित करता है।"
        ),
        commonMistakes = listOf(
            "फाइल को बिना with के खोलना और f.close() करना भूल जाना।"
        ),
        practiceExercises = listOf(
            "फाइल के तीनों मुख्य मोड्स ('r', 'w', 'a') के अंतर को समझें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2401,
                question = "Which mode is used to add new data to an existing file without deleting it?",
                hindiQuestion = "मौजूदा फाइल के डेटा को बिना मिटाए नया डेटा जोड़ने के लिए कौन सा मोड इस्तेमाल होता है?",
                options = listOf("'a' (Append)", "'w' (Write)", "'r' (Read)", "'x' (Exclusive)"),
                correctOptionIndex = 0,
                hindiExplanation = "'a' (Append) मोड फाइल के अंत में नया डेटा जोड़ता है बिना पुराना डेटा डिलीट किए।"
            )
        )
    )

    private fun createChapter25() = Chapter(
        id = 25,
        title = "Chapter 25: Object-Oriented Programming",
        hindiTitle = "ऑब्जेक्ट-ओरिएंटेड प्रोग्रामिंग का परिचय (OOPs)",
        category = "OOP",
        summaryHindi = "OOPs क्या है, इसकी आवश्यकता क्यों पड़ी और इसके मुख्य स्तंभ।",
        introduction = "Object-Oriented Programming (OOP) प्रोग्रामिंग का एक ऐसा तरीका है जो असली दुनिया की वस्तुओं (Objects) पर आधारित है।",
        definition = "OOP is a programming paradigm based on the concept of 'objects', which can contain data (attributes) and code (methods).",
        detailedHindiExplanation = "प्रोग्राम जब बहुत बड़ा हो जाता है, तो केवल फंक्शंस से उसे संभालना मुश्किल हो जाता है। OOP में हम डेटा और उस डेटा पर काम करने वाले फंक्शंस को एक साथ एक 'ऑब्जेक्ट' के अंदर बांध देते हैं।",
        importantConcepts = listOf(
            "Real World Mapping: असली दुनिया की समस्याओं को कोड में बदलना।",
            "Modular Code: कोड को छोटे और स्वतंत्र हिस्सों में बांटना।",
            "Four Pillars: Encapsulation, Abstraction, Inheritance, Polymorphism."
        ),
        realLifeExample = "बैंक खाता: खाता संख्या और बैलेंस (Attributes) और पैसे निकालना व जमा करना (Methods) एक साथ जुड़े होते हैं।",
        pythonCodeExample = """# OOP Mindset
class Smartphone:
    def __init__(self, brand, battery):
        self.brand = brand
        self.battery = battery

phone = Smartphone("Samsung", 5000)
print(f"Phone: {phone.brand}, Battery: {phone.battery}mAh")
""",
        lineByLineExplanation = listOf(
            "Smartphone एक सांचा (Class) है और phone उस सांचे से बना असली ऑब्जेक्ट है।"
        ),
        commonMistakes = listOf(
            "OOP को सिर्फ कठिन समझना जबकि यह कोड को आसान और व्यवस्थित बनाता है।"
        ),
        practiceExercises = listOf(
            "एक Student क्लास की कल्पना करें जिसमें नाम और रोल नंबर हो।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2501,
                question = "How many primary pillars does Object-Oriented Programming have?",
                hindiQuestion = "ऑब्जेक्ट-ओरिएंटेड प्रोग्रामिंग के कितने मुख्य स्तंभ (Pillars) होते हैं?",
                options = listOf("4 (Encapsulation, Abstraction, Inheritance, Polymorphism)", "2", "6", "1"),
                correctOptionIndex = 0,
                hindiExplanation = "OOP के 4 मुख्य स्तंभ हैं: Encapsulation, Abstraction, Inheritance और Polymorphism।"
            )
        )
    )

    private fun createChapter26() = Chapter(
        id = 26,
        title = "Chapter 26: Classes and Objects",
        hindiTitle = "क्लासेस और ऑब्जेक्ट्स (Classes & Objects)",
        category = "OOP",
        summaryHindi = "class कीवर्ड, __init__ कंस्ट्रक्टर, और self का रहस्य।",
        introduction = "Class एक नक्शा (Blueprint) है, और Object उस नक्शे से बना असली घर है।",
        definition = "A Class is a user-defined prototype for an object that defines a set of attributes that characterize any object of the class.",
        detailedHindiExplanation = "`__init__` को Constructor कहते हैं। जब भी कोई नया ऑब्जेक्ट बनता है, यह अपने आप सबसे पहले चलता है। `self` उस वर्तमान ऑब्जेक्ट को इंगित करता है जिस पर काम हो रहा है।",
        importantConcepts = listOf(
            "class Name: क्लास बनाने का सिंटेक्स।",
            "__init__(self, ...): कंस्ट्रक्टर जो ऑब्जेक्ट को जन्म देता है।",
            "self: चालू ऑब्जेक्ट का संदर्भ (Reference)।",
            "Methods: क्लास के अंदर लिखे जाने वाले फंक्शंस।"
        ),
        realLifeExample = "मकान का आर्किटेक्ट नक्शा (Class) -> उस नक्शे पर बने 50 अलग-अलग घर (Objects)।",
        pythonCodeExample = """class Person:
    def __init__(self, name, age):
        self.name = name  # Attribute
        self.age = age

    def introduce(self): # Method
        print(f"Namaste, mera naam {self.name} hai aur meri umar {self.age} saal hai.")

# Creating objects
p1 = Person("Lukman", 22)
p1.introduce()
""",
        lineByLineExplanation = listOf(
            "Person(\"Lukman\", 22) ने __init__ को कॉल किया और p1 ऑब्जेक्ट तैयार किया।"
        ),
        commonMistakes = listOf(
            "क्लास मेथड के पहले पैरामीटर में self लिखना भूल जाना।"
        ),
        practiceExercises = listOf(
            "Book क्लास बनाएं जिसमें title और author हो।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2601,
                question = "What is the purpose of __init__ in a Python class?",
                hindiQuestion = "पायथन क्लास में __init__ का क्या कार्य है?",
                options = listOf("Constructor to initialize object attributes", "To delete the class", "To import modules", "To stop execution"),
                correctOptionIndex = 0,
                hindiExplanation = "__init__ एक कंस्ट्रक्टर है जो ऑब्जेक्ट बनते ही उसके एट्रिब्यूट्स को इनिशियलाइज़ करता है।"
            )
        )
    )

    private fun createChapter27() = Chapter(
        id = 27,
        title = "Chapter 27: Inheritance",
        hindiTitle = "इनहेरिटेंस (Inheritance - विरासत)",
        category = "OOP",
        summaryHindi = "Parent Class के गुण Child Class में प्राप्त करना।",
        introduction = "Inheritance का मतलब है एक क्लास द्वारा दूसरी क्लास की विशेषताओं और तरीकों को अपनाना।",
        definition = "Inheritance allows a class (child class) to inherit the attributes and methods of another class (parent class).",
        detailedHindiExplanation = "जैसे बच्चों में माता-पिता के गुण अपने आप आ जाते हैं, वैसे ही Child Class में Parent Class का सारा कोड बिना दोबारा लिखे मिल जाता है। `super()` कीवर्ड से हम पैरेंट क्लास के तरीकों को कॉल कर सकते हैं।",
        importantConcepts = listOf(
            "Parent Class (Base Class): मुख्य क्लास।",
            "Child Class (Derived Class): गुण लेने वाली क्लास।",
            "super(): पैरेंट क्लास के मेथड्स को एक्सेस करना।"
        ),
        realLifeExample = "वाहन (Vehicle) पैरेंट क्लास है -> कार (Car) और बाइक (Bike) उसकी चाइल्ड क्लासेस हैं।",
        pythonCodeExample = """class Animal:
    def speak(self):
        print("Janwar aawaz karta hai.")

class Dog(Animal): # Dog inherits Animal
    def bark(self):
        print("Kutta bhonkta hai: Bho Bho!")

tommy = Dog()
tommy.speak()  # Inherited from Animal
tommy.bark()   # Dog's own method
""",
        lineByLineExplanation = listOf(
            "Dog(Animal) लिखने से Dog को Animal की speak() विधि अपने आप मिल गई।"
        ),
        commonMistakes = listOf(
            "Child क्लास बनाते समय ब्रैकेट में Parent क्लास का नाम लिखना भूल जाना।"
        ),
        practiceExercises = listOf(
            "Employee पैरेंट क्लास से Developer चाइल्ड क्लास बनाएं।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2701,
                question = "How do you inherit a class in Python?",
                hindiQuestion = "पायथन में इनहेरिटेंस कैसे लिखा जाता है?",
                options = listOf("class Child(Parent):", "class Child extends Parent:", "class Child inherits Parent:", "class Child : Parent"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में इनहेरिटेंस के लिए क्लास नाम के बाद ब्रैकेट में पैरेंट का नाम लिखा जाता है: class Child(Parent):"
            )
        )
    )

    private fun createChapter28() = Chapter(
        id = 28,
        title = "Chapter 28: Encapsulation",
        hindiTitle = "एनकैप्सुलेशन (Encapsulation - डेटा सुरक्षा)",
        category = "OOP",
        summaryHindi = "डेटा को बाहरी अनचाहे बदलावों से सुरक्षित रखना (Private variables).",
        introduction = "Encapsulation का अर्थ है डेटा (Variables) और उस पर काम करने वाले मेथड्स को एक कैप्सूल की तरह बंद रखना।",
        definition = "Encapsulation is the bundling of data and methods that operate on that data within a single unit, and restricting direct access to some of an object's components.",
        detailedHindiExplanation = "Python में किसी वेरिएबल के आगे दो अंडरस्कोर `__` लगाने से वह 'Private' बन जाता है, यानी उसे क्लास के बाहर से सीधे बदला नहीं जा सकता। उसे एक्सेस करने के लिए Getter और Setter मेथड्स का प्रयोग होता है।",
        importantConcepts = listOf(
            "Public: कहीं से भी एक्सेस संभव (e.g. self.name).",
            "Private (__): सिर्फ क्लास के अंदर से एक्सेस (e.g. self.__balance).",
            "Data Hiding: संवेदनशील जानकारी को सुरक्षित रखना।"
        ),
        realLifeExample = "मेडिकल कैप्सूल: अंदर की दवा सुरक्षित बंद रहती है। या बैंक अकाउंट का बैलेंस जो सिर्फ पिन डालकर ही बदला जा सकता है।",
        pythonCodeExample = """class BankAccount:
    def __init__(self, owner, balance):
        self.owner = owner
        self.__balance = balance  # Private variable

    def get_balance(self):
        return self.__balance

    def deposit(self, amount):
        if amount > 0:
            self.__balance += amount

acc = BankAccount("Lukman", 1000)
acc.deposit(500)
print("Balance:", acc.get_balance()) # 1500
""",
        lineByLineExplanation = listOf(
            "__balance प्राइवेट है, कोई भी बाहर से acc.__balance = -500 नहीं कर सकता।"
        ),
        commonMistakes = listOf(
            "क्लास के बाहर से acc.__balance सीधे प्रिंट करने की कोशिश करना (AttributeError आएगा)।"
        ),
        practiceExercises = listOf(
            "एक User क्लास बनाएं जिसमें पासवर्ड __password प्राइवेट हो।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2801,
                question = "How do you make an attribute private in Python?",
                hindiQuestion = "पायथन में किसी एट्रिब्यूट को प्राइवेट कैसे बनाया जाता है?",
                options = listOf("By prefixing with double underscore (__)", "Using private keyword", "Using protected keyword", "With #private"),
                correctOptionIndex = 0,
                hindiExplanation = "Python में नाम के आगे दो अंडरस्कोर (जैसे __balance) लगाकर उसे प्राइवेट बनाया जाता है।"
            )
        )
    )

    private fun createChapter29() = Chapter(
        id = 29,
        title = "Chapter 29: Polymorphism",
        hindiTitle = "पॉलीमॉर्फिज्म (Polymorphism - बहुरूपता)",
        category = "OOP",
        summaryHindi = "एक ही नाम, लेकिन अलग-अलग काम (Method Overriding).",
        introduction = "Polymorphism ग्रीक शब्द है जिसका अर्थ है 'Many Forms' (अनेक रूप)।",
        definition = "Polymorphism allows different classes to have methods with the same name, but with different implementations.",
        detailedHindiExplanation = "जैसे `+` ऑपरेटर दो नंबरों को जोड़ता है (5 + 5 = 10) लेकिन दो स्ट्रिंग्स को आपस में मिलाता है ('Py' + 'thon' = 'Python')—एक ही ऑपरेटर अलग-अलग काम कर रहा है। इसी प्रकार अलग-अलग क्लासेस में एक ही नाम का मेथड हो सकता है।",
        importantConcepts = listOf(
            "Method Overriding: चाइल्ड क्लास में पैरेंट क्लास के मेथड को अपनी ज़रूरत के हिसाब से बदलना।",
            "Common Interface: एक ही नाम से कई प्रकार के ऑब्जेक्ट्स को संभालना।"
        ),
        realLifeExample = "एक इंसान: घर पर बेटा, ऑफिस में मैनेजर, बाज़ार में ग्राहक। इंसान एक, लेकिन भूमिकाएं अनेक!",
        pythonCodeExample = """class Cat:
    def sound(self):
        return "Meow Meow"

class Cow:
    def sound(self):
        return "Moo Moo"

animals = [Cat(), Cow()]
for a in animals:
    print(a.sound())
""",
        lineByLineExplanation = listOf(
            "दोनों क्लासेस में sound() मेथड है, लेकिन दोनों अपनी-अपनी अलग आवाज़ निकालते हैं।"
        ),
        commonMistakes = listOf(
            "सोचना कि Python में Java की तरह Method Overloading चलती है (Python में अंतिम डिफाइन किया गया मेथड ही माना जाता है)।"
        ),
        practiceExercises = listOf(
            "Circle और Rectangle दो क्लासेस बनाएं और दोनों में area() मेथड लिखें।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 2901,
                question = "What does the word Polymorphism literally mean?",
                hindiQuestion = "Polymorphism शब्द का शाब्दिक अर्थ क्या है?",
                options = listOf("Many forms (अनेक रूप)", "Single structure", "Code hiding", "Fast speed"),
                correctOptionIndex = 0,
                hindiExplanation = "Polymorphism का मतलब होता है 'कई रूप' (Many forms)।"
            )
        )
    )

    private fun createChapter30() = Chapter(
        id = 30,
        title = "Chapter 30: Python Projects",
        hindiTitle = "पायथन रियल-वर्ल्ड प्रोजेक्ट्स",
        category = "Advanced",
        summaryHindi = "सीखे गए सभी कॉन्सेप्ट्स को मिलाकर शानदार प्रोजेक्ट्स बनाना।",
        introduction = "बधाई हो! अब आपने Python के बेसिक्स से लेकर OOPs तक सब सीख लिया है। अब समय है असली प्रोजेक्ट्स बनाने का।",
        definition = "Projects integrate variables, data structures, loops, functions, and OOP to create real-world working software applications.",
        detailedHindiExplanation = "इस चैप्टर में हम एक संपूर्ण 'Number Guessing Game' और 'Student Management System' बनाएंगे। प्रोजेक्ट्स बनाने से आपका लॉजिकल थिंकिंग और प्रॉब्लम सॉल्विंग कौशल कई गुना बढ़ जाता है।",
        importantConcepts = listOf(
            "Project Planning: पहले समस्या को समझें और फ्लोचार्ट बनाएं।",
            "Modular Architecture: कोड को छोटे-छोटे फंक्शंस में बांटें।",
            "Testing & Debugging: हर फीचर को टेस्ट करें।"
        ),
        realLifeExample = "जैसे ईंट, सीमेंट, सरिया सीखकर एक पूरा आलीशान मकान बनाना।",
        pythonCodeExample = """# Complete Mini Project: Student Report Card
students = [
    {"name": "Lukman", "score": 95},
    {"name": "Aman", "score": 78},
    {"name": "Priya", "score": 88}
]

print("=== MD LUKMAN PYTHON AI REPORT ===")
for s in students:
    status = "Pass" if s["score"] >= 60 else "Fail"
    print(f"Student: {s['name']} | Marks: {s['score']} | Status: {status}")
print("==================================")
""",
        lineByLineExplanation = listOf(
            "यह प्रोजेक्ट लिस्ट्स, डिक्शनरी, लूप्स, कंडीशनल स्टेटमेंट्स और f-strings सबका उपयोग करता है।"
        ),
        commonMistakes = listOf(
            "बिना प्लान किए सीधे सैकड़ों लाइन का कोड लिखना शुरू कर देना।"
        ),
        practiceExercises = listOf(
            "एक साधारण कैलकुलेटर प्रोजेक्ट बनाएं जो जोड़, घटाव, गुणा और भाग कर सके।"
        ),
        testQuestions = listOf(
            QuizQuestion(
                id = 3001,
                question = "What is the best way to master Python programming?",
                hindiQuestion = "पायथन प्रोग्रामिंग में मास्टर बनने का सबसे अच्छा तरीका क्या है?",
                options = listOf("Writing real code and building projects daily", "Only reading books", "Memorizing syntax", "Copying without understanding"),
                correctOptionIndex = 0,
                hindiExplanation = "Python सीखने का सबसे बेहतरीन तरीका है रोज़ाना खुद कोड लिखना और प्रोजेक्ट्स बनाना!"
            )
        )
    )
}
