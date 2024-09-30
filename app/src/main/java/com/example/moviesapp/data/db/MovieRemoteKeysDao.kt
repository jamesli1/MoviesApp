package com.example.moviesapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.data.model.MovieRemoteKeys

@Dao
interface MovieRemoteKeysDao {

    @Query("SELECT * FROM remote_keys WHERE id = 0")
    suspend fun getRemoteKeys(): MovieRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: MovieRemoteKeys)

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllRemoteKeys()
}