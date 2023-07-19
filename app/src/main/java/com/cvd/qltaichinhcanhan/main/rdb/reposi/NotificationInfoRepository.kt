package com.cvd.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.cvd.qltaichinhcanhan.main.model.m_r.NotificationInfo
import com.cvd.qltaichinhcanhan.main.rdb.inter.NotificationInfoDao

class NotificationInfoRepository(private val notificationInfoDao: NotificationInfoDao) {
    val allNotificationInfoLive: LiveData<List<NotificationInfo>> =
        notificationInfoDao.getAllNotificationInfoLive()
    val allNotificationInfo: List<NotificationInfo> = notificationInfoDao.getAllNotificationInfo()

    suspend fun insert(notificationInfo: NotificationInfo) {
        notificationInfoDao.insert(notificationInfo)
    }

    suspend fun delete(notificationInfo: NotificationInfo) {
        notificationInfoDao.delete(notificationInfo)
    }

    suspend fun update(notificationInfo: NotificationInfo) {
        notificationInfoDao.update(notificationInfo)
    }

    fun deleteAll(){
        notificationInfoDao.deleteAll()
    }
}
