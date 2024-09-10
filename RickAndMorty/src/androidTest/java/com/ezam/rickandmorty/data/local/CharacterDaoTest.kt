package com.ezam.rickandmorty.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class CharacterDaoTest{

    private lateinit var characterDao: CharacterDao
    private lateinit var database: RickandmortyDatabase

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RickandmortyDatabase::class.java)
            .build()
        characterDao = database.characterDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertOrIgnore_retorna_uno_cuando_no_existe(): Unit = runBlocking {
        val entity = CharacterEntity(
            id = 1,
            name = "Rick",
            status = "Alive",
            image = byteArrayOf(1,2,3,4,5)
        )
        val result = characterDao.insertOrIgnore(entity)
        assertEquals(1, result)
    }

    @Test
    fun insertOrIgnore_retorna_menosuno_cuando_existe(): Unit = runBlocking {
        val entity = CharacterEntity(
            id = 1,
            name = "Rick",
            status = "Alive",
            image = byteArrayOf(1,2,3,4,5)
        )
        characterDao.insertOrIgnore(entity)
        val result = characterDao.insertOrIgnore(entity)
        assertEquals(-1, result)
    }

    @Test
    fun update_retorna_cero_cuando_no_existe(): Unit = runBlocking {
        val entity = CharacterEntity(
            id = 1,
            name = "Rick",
            status = "Alive",
            image = byteArrayOf(1,2,3,4,5)
        )

        val result = characterDao.update(entity)
        assertEquals(0, result)
    }

    @Test
    fun update_actualiza_correctamente(): Unit = runBlocking {
        val entity = CharacterEntity(
            id = 1,
            name = "Rick",
            status = "Alive",
            image = byteArrayOf(1,2,3,4,5)
        )

        val dead = entity.copy(status = "Dead")

        characterDao.insertOrIgnore(entity)
        val result = characterDao.update(dead)
        val updated = characterDao.getById(1)

        assertEquals(1, result)
        assertEquals(dead, updated )
    }

    @Test
    fun upsert_inserta_entity_cuando_no_existe(): Unit = runBlocking {
        val entity = CharacterEntity(
            id = 1,
            name = "Rick",
            status = "Alive",
            image = byteArrayOf(1,2,3,4,5)
        )
        characterDao.insertOrIgnore(entity)
        val result = characterDao.insertOrIgnore(entity)
        assertEquals(-1, result)
    }

    @Test
    fun upsert_inserta_cuando_no_existe(): Unit = runBlocking {
        val entity = CharacterEntity(
            id = 1,
            name = "Rick",
            status = "Alive",
            image = byteArrayOf(1,2,3,4,5)
        )

        characterDao.upsert(entity)

        val result = characterDao.getById(1)

        assertEquals(entity, result)
    }

    @Test
    fun upsert_actualiza_cuando_ya_existe(): Unit = runBlocking {
        val entity = CharacterEntity(
            id = 1,
            name = "Rick",
            status = "Alive",
            image = byteArrayOf(1,2,3,4,5)
        )
        characterDao.upsert(entity)

        val clon = entity.copy( name = "Clon", status = "Cloned", image = byteArrayOf(3,2,1) )
        characterDao.upsert(clon)

        val result = characterDao.getById(1)

        assertEquals(clon, result)
    }
}