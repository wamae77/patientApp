package com.ke.patientapp.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 0,
    val password: String,
    val name: String,
    val email: String,
    val updatedAt: String,
    val createdAt: String,
    val token: String
)