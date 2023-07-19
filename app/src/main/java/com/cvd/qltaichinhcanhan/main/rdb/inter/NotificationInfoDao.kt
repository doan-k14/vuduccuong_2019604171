package com.cvd.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cvd.qltaichinhcanhan.main.model.m_r.NotificationInfo

@Dao
interface NotificationInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notificationInfo: NotificationInfo)

    @Delete
    suspend fun delete(notificationInfo: NotificationInfo)

    @Update
    suspend fun update(notificationInfo: NotificationInfo)

    @Query("SELECT * FROM notificationInfo")
    fun getAllNotificationInfoLive(): LiveData<List<NotificationInfo>>

    @Query("SELECT * FROM notificationInfo")
    fun getAllNotificationInfo(): List<NotificationInfo>

    @Query("DELETE FROM notificationInfo")
    fun deleteAll()
}