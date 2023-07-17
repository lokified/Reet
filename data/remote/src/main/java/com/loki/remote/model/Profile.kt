package com.loki.remote.model

import com.google.firebase.firestore.DocumentId

data class Profile(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val userName: String = "",
    val profileBackgroundColor: Long? = null,
    val userId: String = ""
)
