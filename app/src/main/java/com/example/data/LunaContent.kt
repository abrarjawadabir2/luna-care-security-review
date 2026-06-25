package com.example.data

data class EducationArticle(
    val title: String,
    val slug: String,
    val category: String, // "Menstrual Cup", "Period & PMS", "Emotional Wellbeing"
    val content: String,
    val safetyNote: String? = null
)

data class SelfCareItem(
    val title: String,
    val description: String,
    val category: String, // "Breathing", "Grounding", "Stretch", "Comfort"
    val steps: List<String>
)

object LunaContent {
    val articles = listOf(
        EducationArticle(
            title = "What is a Menstrual Cup?",
            slug = "what-is-menstrual-cup",
            category = "Menstrual Cup",
            content = """
                A menstrual cup is a reusable female hygiene product. It is a small, flexible funnel-shaped cup made of silicone or latex rubber that you insert into your vagina to catch and collect period fluid. Cups can hold more liquid than other methods, and many people use them as an eco-friendly alternative to tampons.
                
                Unlike tampons or pads, a cup collects the flow rather than absorbing it, which helps prevent dryness and irritation.
            """.trimIndent(),
            safetyNote = "Always choose medical-grade silicone cups that are FDA-cleared or clinically approved."
        ),
        EducationArticle(
            title = "Benefits and Limitations",
            slug = "benefits-limitations",
            category = "Menstrual Cup",
            content = """
                Menstrual cups offer numerous benefits:
                • Budget-friendly: A single reusable cup lasts up to 10 years, saving money.
                • Eco-friendly: Reduces plastic and fiber waste significantly.
                • Longer wear: Can be worn for up to 8-12 hours depending on flow.
                • No odor: Collected fluid is sealed from exposure to air.
                
                Limitations include:
                • Learning curve: Insertion and removal require practice.
                • Messy: Emptying public restrooms can be awkward.
                • Fit issues: Finding the right size and shape may take trial and error.
            """.trimIndent(),
            safetyNote = "If you have an intrauterine device (IUD), consult your gynecologist to avoid accidentally dislodging it during removal."
        ),
        EducationArticle(
            title = "Choosing Your Cup Size",
            slug = "choosing-cup-size",
            category = "Menstrual Cup",
            content = """
                Most brands offer cups in two main sizes: Small (Size 1) and Large (Size 2).
                
                When choosing a size, ask yourself these questions:
                • Age: Under 30 years old usually starts with a Small cup.
                • Childbirth: If you have given birth vaginally, a Large cup is typically recommended.
                • Cervix position: A low cervix requires a shorter cup, while a high cervix requires a longer cup with a stem.
                • Flow intensity: Heaviers flows might benefit from a larger capacity cup.
            """.trimIndent(),
            safetyNote = "When in doubt, start with a smaller, softer cup to let your body adjust gradually."
        ),
        EducationArticle(
            title = "How to Fold a Cup",
            slug = "how-to-fold-cup",
            category = "Menstrual Cup",
            content = """
                Because a cup is wider than the vaginal opening, you must fold it for insertion. Here are the 3 most common folds:
                
                1. The C-Fold / U-Fold:
                   Press the sides of the cup together to flatten it. Then fold it in half lengthwise, forming the letter 'C' or 'U'.
                
                2. The Punch-Down Fold:
                   Place your finger on the top rim of the cup and press it down and inward towards the base of the cup. Remove your finger while squeezing the edges together to maintain the tight triangle shape.
                
                3. The 7-Fold:
                   Flatten the cup, then select one corner and fold it down diagonally towards the base. This forms the shape of a number '7'.
            """.trimIndent(),
            safetyNote = "Always wash your hands thoroughly with soap and water before folding your cup."
        ),
        EducationArticle(
            title = "Insertion Guide Safely",
            slug = "insertion-guide",
            category = "Menstrual Cup",
            content = """
                Follow these simple steps for safe insertion:
                
                1. Wash Hands: Strictly sanitize your hands first.
                2. Find a comfortable position: Squat down, sit on the toilet, or put one leg up on the bathtub.
                3. Fold the cup: Choose your preferred fold from the folding guide.
                4. Relax: Take a deep breath. Tense pelvic muscles make insertion much harder.
                5. Insert: Gently separate your outer lips, and direct the folded cup tilted back towards your spine, not straight up.
                6. Let go: Once fully inside (the base of the cup should sit just above the vaginal opening), let the cup pop open.
                7. Check the seal: Run a clean finger around the base of the cup. It should feel rounded and fully open. Rotate it slightly to ensure a vacuum seal.
            """.trimIndent(),
            safetyNote = "Do not force insertion. If you feel severe pain, stop. A water-based lubricant can make insertion smoother."
        ),
        EducationArticle(
            title = "Removal Guide Safely",
            slug = "removal-guide",
            category = "Menstrual Cup",
            content = """
                Removing a cup safely is all about breaking the vacuum seal. Never pull the cup down by the stem!
                
                1. Wash Hands: Keep hands perfectly clean.
                2. Position: Squat down to shorten your vaginal canal, making the cup easier to reach.
                3. Locate the base: Gently insert your thumb and index finger into your vagina until you feel the textured base of the cup (just above the stem).
                4. PINCH the base: Squeeze the base of the cup. This action breaks the vacuum seal.
                5. Pull gently: Once the seal is broken, gently walk the cup out, keeping it upright to prevent spills.
                6. Clean: Empty the content into the toilet, rinse with warm water, and prepare to reinsert.
            """.trimIndent(),
            safetyNote = "Never yank on the stem alone as the vacuum seal can cause discomfort or suction to your cervix. Always pinch the base to release the suction first."
        ),
        EducationArticle(
            title = "Hygiene, Cleaning and Sterilizing",
            slug = "cleaning-sterilizing",
            category = "Menstrual Cup",
            content = """
                Proper sanitization is critical to prevent yeast infections and bacterial vaginosis:
                
                • Between cycles: Boil your cup in a pot of water for 5-7 minutes. Ensure the cup does not touch the bottom or sides of the pot to prevent melting (use a whisk or tongs).
                • During your period: Wash the cup with cold water and an oil-free, unscented, mild soap. Rinse thoroughly before reinserting.
                • Air-dry and store: Let it dry completely and store in its breathable cotton pouch. Never store in a plastic airtight container!
            """.trimIndent(),
            safetyNote = "Do not wash your cup with dish soap, hand sanitizers, or bleach as these will damage the silicone and irritate your vaginal walls."
        ),
        EducationArticle(
            title = "Who Should Consult a Doctor",
            slug = "medical-caveats",
            category = "Menstrual Cup",
            content = """
                Menstrual cups are safe, but they are not suitable for everyone. Consult a healthcare professional before use if you have:
                • An Intrauterine Device (IUD)
                • Severe vaginal pain or pelvic floor dysfunction
                • Recent gynecological surgery or childbirth (less than 6 weeks)
                • Active pelvic infections or skin rashes
                • Had Toxic Shock Syndrome (TSS) in the past.
                
                If you use an IUD, always tell your partner or doctor, and check your strings regularly, because the cup suction can pull them.
            """.trimIndent(),
            safetyNote = "Stop using immediately if you experience persistent severe cramping, abnormal bad-smelling discharge, or high fever."
        ),
        EducationArticle(
            title = "Common PMS Symptoms & Cycle Guide",
            slug = "pms-cycle-guide",
            category = "Period & PMS",
            content = """
                Premenstrual Syndrome (PMS) includes a range of physical, emotional, and behavioral symptoms that develop in the days leading up to your period. These are driven by standard hormonal shifts (drop in estrogen and progesterone).
                
                Common physical signs:
                • Pelvic cramps, lower back pain, abdominal bloating.
                • Breast tenderness, fatigue, headache.
                
                Common emotional signs:
                • Extreme mood swings, anxiety, low mood, irritability.
                • Sleep changes or food cravings.
                
                Journaling and tracking help you identify your monthly patterns so you can plan extra self-care.
            """.trimIndent(),
            safetyNote = "Disclaimer: Predictions are estimates only. If you experience debilitating pain, heavy bleeding requiring changing pads hourly, seek medical advice."
        ),
        EducationArticle(
            title = "Managing PMS Anxiety and Mood",
            slug = "pms-anxiety-mood",
            category = "Emotional Wellbeing",
            content = """
                Hormonal changes in the luteal phase can disrupt neurotransmitters like serotonin, causing mood drops and anxiety.
                
                Helpful strategies:
                • Gentle movement: yoga or light walking releases endorphins.
                • Mindfulness: 5-minute breathing exercises soothe your nervous system.
                • Warm compresses: layout a warm water bottle on your belly to calm physical tension and reduce cramps.
                • Self-kindness: acknowledge that hormones are driving your emotional sensitivity, and give yourself permission to rest.
            """.trimIndent(),
            safetyNote = "Severe premenstrual mood drops may indicate PMDD (Premenstrual Dysphoric Disorder). If symptoms interfere with daily life, consult a healthcare provider."
        )
    )

    val selfCareTips = listOf(
        SelfCareItem(
            title = "4-7-8 Breathing Exercise",
            description = "A powerful breathing technique to soothe anxiety and calm the nervous system instantly.",
            category = "Breathing",
            steps = listOf(
                "Exhale completely through your mouth, making a whoosh sound.",
                "Close your mouth and inhale quietly through your nose to a mental count of 4.",
                "Hold your breath for a count of 7.",
                "Exhale completely through your mouth, making a whoosh sound to a count of 8.",
                "Repeat the cycle 4 times."
            )
        ),
        SelfCareItem(
            title = "5-4-3-2-1 Grounding Method",
            description = "Ground yourself during moments of panic or overwhelming stress by connecting with your senses.",
            category = "Grounding",
            steps = listOf(
                "Name 5 things you can SEE around you.",
                "Name 4 things you can TOUCH or feel (the chair under you, your clothes).",
                "Name 3 things you can HEAR (traffic, wind, ticking clock).",
                "Name 2 things you can SMELL.",
                "Name 1 thing you can TASTE."
            )
        ),
        SelfCareItem(
            title = "Gentle Period Stretch",
            description = "Relieve back tension and pelvic cramps using this gentle child's pose.",
            category = "Stretch",
            steps = listOf(
                "Kneel on the floor with your knees wide and big toes touching.",
                "Sit back on your heels and lower your torso forward between your thighs.",
                "Extend your arms out fully in front, resting your forehead on your mat.",
                "Breathe deeply into your lower back and belly.",
                "Hold for 2-3 minutes, focusing on relaxing your pelvic muscles."
            )
        ),
        SelfCareItem(
            title = "Warm Comfort Support",
            description = "Easing pelvic cramping and general body chills during your period.",
            category = "Comfort",
            steps = listOf(
                "Prepare a warm heating pad or hot water bottle wrapped in a soft towel.",
                "Place it gently on your lower abdomen or lower back.",
                "Lie down in a comfortable position, tucking your knees towards your chest.",
                "Sip a cup of warm chamomile or ginger tea.",
                "Rest for 20-30 minutes."
            )
        ),
        SelfCareItem(
            title = "Hydration Reminder",
            description = "Staying hydrated reduces water retention, bloating, and fatigue.",
            category = "Comfort",
            steps = listOf(
                "Drink a full glass of water right now.",
                "Keep a refillable bottle near your workplace.",
                "Integrate warm water, herbal teas, or water-dense fruits.",
                "Limit caffeine and high-sodium foods which increase bloating."
            )
        )
    )
}
