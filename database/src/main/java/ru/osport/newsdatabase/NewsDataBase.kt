package ru.osport.newsdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.osport.newsdatabase.dao.ArticleDao
import ru.osport.newsdatabase.models.ArticleDBO

@Database(entities = [ArticleDBO::class], version = 1)
abstract class NewsDataBase: RoomDatabase() {
    abstract fun articlesDao(): ArticleDao
}

fun NewsDataBase(applicationContext: Context): NewsDataBase{
    return Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsDataBase::class.java,
        "news"
    ).build()
}