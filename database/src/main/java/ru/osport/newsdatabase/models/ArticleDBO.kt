package ru.osport.newsdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleDBO(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("source") @Embedded val source: SourceDBO,
    @ColumnInfo("author") val author: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("publishedAt") val publishedAt: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("url") val url: String,
    @ColumnInfo("urlToImage") val urlToImage: String
)

data class SourceDBO(
    @ColumnInfo("id") val id: String,
    @ColumnInfo("name") val name: String
)