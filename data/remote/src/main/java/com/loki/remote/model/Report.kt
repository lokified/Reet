package com.loki.remote.model

import com.google.firebase.firestore.DocumentId

data class Report(
    @DocumentId
    val id: String = "",
    val reportContent: String = "",
    val createdAt: Long? = null,
    val createdOn: Long? = null,
    val userId: String = ""
)
