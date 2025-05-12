package com.example.elderlycare2.data.remote.request

class AddScheduleRequest (
    val schedule: String,
    val frequency: String,
    val startTime: String,
    val endTime: String,
    val assignedTo: String
)