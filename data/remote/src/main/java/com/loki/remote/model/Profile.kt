package com.loki.remote.model

import com.google.firebase.firestore.DocumentId

data class Profile(
    @DocumentId
    val id: String = "",
    val userName: String = "",
    val profileBackgroundColor: String = "",
    val userId: String = ""
)
