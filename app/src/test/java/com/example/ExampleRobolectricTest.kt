package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.content.CurriculumRepository
import com.example.data.model.GameType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name and tagline from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        val tagline = context.getString(R.string.app_tagline)
        assertEquals("Masti Ki Pathshala", appName)
        assertEquals("Khelo, Masti Karo Aur Seekho!", tagline)
    }

    @Test
    fun `curriculum contains complete lessons and 10 games`() {
        assertEquals(13, CurriculumRepository.hindiSwar.size)
        assertEquals(26, CurriculumRepository.englishAlphabet.size)
        assertEquals(10, CurriculumRepository.countingLessons.size)
        assertEquals(100, CurriculumRepository.numbers1To100.size)
        assertEquals(10, CurriculumRepository.colorsLessons.size)
        assertTrue(CurriculumRepository.animalsLessons.size >= 10)
        assertTrue(CurriculumRepository.fruitsLessons.size >= 8)
        assertEquals(10, GameType.entries.size)
    }
}
