package com.example.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "child_profile")
data class ChildProfileEntity(
    @PrimaryKey val id: Int = 1,
    val childName: String = "गोलू (Golu)",
    val avatarEmoji: String = "🐘",
    val ageGroupId: String = "3-4",
    val languageCode: String = "hi", // Default language is Hindi as requested
    val totalStars: Int = 5,
    val learningMinutes: Int = 5
)

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val itemId: String,
    val subjectId: String,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_scores")
data class QuizScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameTypeId: String,
    val gameTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val starsEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface PathshalaDao {
    @Query("SELECT * FROM child_profile WHERE id = 1")
    fun getProfileFlow(): Flow<ChildProfileEntity?>

    @Query("SELECT * FROM child_profile WHERE id = 1")
    suspend fun getProfileOnce(): ChildProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: ChildProfileEntity)

    @Query("SELECT * FROM lesson_progress ORDER BY completedAt DESC")
    fun getAllLessonProgressFlow(): Flow<List<LessonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonProgress(progress: LessonProgressEntity)

    @Query("SELECT * FROM quiz_scores ORDER BY timestamp DESC LIMIT 30")
    fun getRecentQuizScoresFlow(): Flow<List<QuizScoreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizScore(score: QuizScoreEntity)

    @Query("DELETE FROM lesson_progress")
    suspend fun clearLessonProgress()

    @Query("DELETE FROM quiz_scores")
    suspend fun clearQuizScores()
}

@Database(
    entities = [
        ChildProfileEntity::class,
        LessonProgressEntity::class,
        QuizScoreEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pathshalaDao(): PathshalaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "masti_ki_pathshala.db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class PathshalaRepository(private val dao: PathshalaDao) {
    val profileFlow: Flow<ChildProfileEntity?> = dao.getProfileFlow()
    val lessonProgressFlow: Flow<List<LessonProgressEntity>> = dao.getAllLessonProgressFlow()
    val quizScoresFlow: Flow<List<QuizScoreEntity>> = dao.getRecentQuizScoresFlow()

    suspend fun ensureDefaultProfile(): ChildProfileEntity {
        val existing = dao.getProfileOnce()
        if (existing != null) return existing
        val defaultProfile = ChildProfileEntity()
        dao.upsertProfile(defaultProfile)
        return defaultProfile
    }

    suspend fun updateLanguage(languageCode: String) {
        val current = ensureDefaultProfile()
        dao.upsertProfile(current.copy(languageCode = languageCode))
    }

    suspend fun updateAgeGroup(ageGroupId: String) {
        val current = ensureDefaultProfile()
        dao.upsertProfile(current.copy(ageGroupId = ageGroupId))
    }

    suspend fun updateChildProfile(name: String, avatarEmoji: String, ageGroupId: String) {
        val current = ensureDefaultProfile()
        dao.upsertProfile(
            current.copy(
                childName = name.ifBlank { "गोलू (Golu)" },
                avatarEmoji = avatarEmoji,
                ageGroupId = ageGroupId
            )
        )
    }

    suspend fun addStars(stars: Int) {
        if (stars <= 0) return
        val current = ensureDefaultProfile()
        dao.upsertProfile(current.copy(totalStars = current.totalStars + stars))
    }

    suspend fun addLearningMinutes(minutes: Int) {
        if (minutes <= 0) return
        val current = ensureDefaultProfile()
        dao.upsertProfile(current.copy(learningMinutes = current.learningMinutes + minutes))
    }

    suspend fun markLessonCompleted(itemId: String, subjectId: String, bonusStars: Int = 2) {
        dao.insertLessonProgress(LessonProgressEntity(itemId = itemId, subjectId = subjectId))
        addStars(bonusStars)
    }

    suspend fun recordQuizScore(
        gameTypeId: String,
        gameTitle: String,
        score: Int,
        totalQuestions: Int,
        starsEarned: Int
    ) {
        dao.insertQuizScore(
            QuizScoreEntity(
                gameTypeId = gameTypeId,
                gameTitle = gameTitle,
                score = score,
                totalQuestions = totalQuestions,
                starsEarned = starsEarned
            )
        )
        addStars(starsEarned)
    }

    suspend fun resetAllProgress() {
        dao.clearLessonProgress()
        dao.clearQuizScores()
        val current = ensureDefaultProfile()
        dao.upsertProfile(current.copy(totalStars = 5, learningMinutes = 0))
    }
}
