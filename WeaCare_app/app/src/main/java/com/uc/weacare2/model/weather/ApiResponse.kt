package com.uc.weacare2.model.weather
data class ApiResponse <T> (
    val success: Boolean,
    val message: String,
    val data: T
    )