package com.loki.ui.utils.ext


fun String.toInitials(): String {

    val userList = this.split(" ")
    val firstName = userList[0][0].toString().uppercase()
    val lastName = userList[1][0].toString().uppercase()

    return firstName + lastName
}