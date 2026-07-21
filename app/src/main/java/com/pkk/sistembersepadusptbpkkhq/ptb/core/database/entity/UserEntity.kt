package com.pkk.sistembersepadusptbpkkhq.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "role") val role: String = "",
    @ColumnInfo(name = "color") val color: String = "#2563eb",
    @ColumnInfo(name = "phone") val phone: String = "",
    @ColumnInfo(name = "image_url") val imageUrl: String = "",
    @ColumnInfo(name = "sign_url") val signUrl: String = "",
    @ColumnInfo(name = "cop_url") val copUrl: String = "",
    @ColumnInfo(name = "cached_at") val cachedAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "inbox")
data class InboxEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "masa") val masa: String = "",
    @ColumnInfo(name = "mesej") val mesej: String = "",
    @ColumnInfo(name = "jenis") val jenis: String = "",
    @ColumnInfo(name = "role") val role: String = "",
    @ColumnInfo(name = "dibaca") val dibaca: Boolean = false,
)

@Entity(tableName = "cache_meta")
data class CacheMetaEntity(
    @PrimaryKey val key: String = "",
    @ColumnInfo(name = "value") val value: String = "",
    @ColumnInfo(name = "expires_at") val expiresAt: Long = 0,
)
