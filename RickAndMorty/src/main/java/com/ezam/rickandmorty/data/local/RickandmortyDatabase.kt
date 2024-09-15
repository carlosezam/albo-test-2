package com.ezam.rickandmorty.data.local

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update

@Database(
    version = 1,
    entities = [CharacterEntity::class]
)
abstract class RickandmortyDatabase : RoomDatabase(){

    abstract fun characterDao(): CharacterDao

    companion object {
        const val DATABASE_NAME = "rickandmorty_db"
    }
}

@Dao
abstract class CharacterDao {

    @Query("SELECT * FROM character WHERE id = :id")
    abstract suspend fun getById(id: Int): CharacterEntity?

    @Query("SELECT * FROM character WHERE id BETWEEN (:page -1) * 20 + 1 AND :page * 20")
    abstract suspend fun getPage(page: Int): List<CharacterEntity>?

    // upsert list items
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    abstract suspend fun insertOrIgnore(characters: List<CharacterEntity>) : List<Long>

    @Update
    abstract suspend fun update(characters: List<CharacterEntity>) : Int

    @Transaction
    open suspend fun upsert(characters: List<CharacterEntity>){

        val inserts = insertOrIgnore(characters)

        val forUpdate = characters.filterIndexed { index, _ ->  inserts.getOrNull(index) == 1L }

        update(forUpdate)
    }

    // upsert individual items
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    abstract suspend fun insertOrIgnore(character: CharacterEntity) : Long

    @Update
    abstract suspend fun update(character: CharacterEntity) : Int

    @Transaction
    open suspend fun upsert(character: CharacterEntity){
        if( insertOrIgnore(character) == -1L ){
            update(character)
        }
    }
}


@Entity(tableName = "character")
data class CharacterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (status != other.status) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}