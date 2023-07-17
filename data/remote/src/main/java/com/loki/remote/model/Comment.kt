package com.loki.remote.model

import com.google.firebase.firestore.DocumentId

data class Comment(
    @DocumentId
    val id: String = "",
    val commentContent: String = "",
    val createdAt: Long? = null,
    val createdOn: Long? = null,
    val reportId: String = ""
)
