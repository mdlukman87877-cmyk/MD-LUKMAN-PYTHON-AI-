package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {

    @Query("SELECT * FROM cached_chapters ORDER BY id ASC")
    fun getAllChaptersFlow(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM cached_chapters ORDER BY id ASC")
    suspend fun getAllChapters(): List<ChapterEntity>

    @Query("SELECT * FROM cached_chapters WHERE id = :id LIMIT 1")
    suspend fun getChapterById(id: Int): ChapterEntity?

    @Query("SELECT COUNT(*) FROM cached_chapters")
    suspend fun getChapterCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity)

    @Query("DELETE FROM cached_chapters")
    suspend fun clearCache()
}
