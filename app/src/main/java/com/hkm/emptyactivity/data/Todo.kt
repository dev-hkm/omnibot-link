package com.hkm.emptyactivity.data

import java.util.UUID

data class Todo(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val priority: Priority = Priority.MEDIUM
)

enum class Priority {
    LOW, MEDIUM, HIGH
}
