package com.example.qltaichinhcanhan.main.model.m_r

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notificationInfo",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["idAccount"],
            childColumns = ["idAccount"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NotificationInfo(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idNotification")
    val idNotification: Int = 0,
    var nameNotification: String? = null,
    var notificationFrequency: String? = null,
    var notificationReminderStartTime: Long? = null,
    var notificationTime: Long? = null,
    var notificationNote: String? = null,
    var isNotificationSelected: Boolean? = null,
    var idAccount: Int? = null,
) : Parcelable

