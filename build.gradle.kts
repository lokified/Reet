// Top-level build file where you can add configuration options common to all sub-projects/modules.



@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.firebase.perfomance) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.spotless)
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}