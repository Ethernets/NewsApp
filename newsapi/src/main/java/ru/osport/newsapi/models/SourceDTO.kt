package ru.osport.newsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceDTO(
    @SerialName("id") val id: String,
    @SerialName("mame") val name: String
)