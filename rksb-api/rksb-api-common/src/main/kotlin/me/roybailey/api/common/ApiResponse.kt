package me.roybailey.api.common


data class ApiResponse<T>(
    var totalRecords: Int,
    var results: List<T>
)
