package ru.osport.newsdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.osport.newsdatabase.dao.ArticleDao
import ru.osport.newsdatabase.models.ArticleDBO

class NewsDataBase internal constructor(private val dataBase: NewsRoomDataBase) {
    val articlesDao: ArticleDao
        get() = dataBase.articlesDao()
}

@Database(entities = [ArticleDBO::class], exportSchema = false, version = 1)
internal abstract class NewsRoomDataBase : RoomDatabase() {
    abstract fun articlesDao(): ArticleDao
}

fun NewsDataBase(applicationContext: Context): NewsDataBase {
    val newsRoomDataBase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsRoomDataBase::class.java,
        "news"
    ).build()
    return NewsDataBase(newsRoomDataBase)
}