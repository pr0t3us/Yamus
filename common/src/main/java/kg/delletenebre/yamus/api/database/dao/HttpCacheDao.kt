package kg.delletenebre.yamus.api.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kg.delletenebre.yamus.api.database.table.HttpCacheEntity

@Dao
interface HttpCacheDao {
    @Query("SELECT * FROM http_cache WHERE url=:url")
    fun get(url: String): HttpCacheEntity?

    @Query("SELECT * FROM http_cache")
    fun getAll(): List<HttpCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg httpCache: HttpCacheEntity)
}