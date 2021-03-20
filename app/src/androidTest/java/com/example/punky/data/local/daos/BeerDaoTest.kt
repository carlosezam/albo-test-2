package com.example.punky.data.local.daos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.punky.data.local.PunkyDatabase
import com.example.punky.data.local.entities.Beer
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class BeerDaoTest{

    private lateinit var beerDao: BeerDao
    private lateinit var db: PunkyDatabase
    private lateinit var faker: Faker

    //private val testDispatcher = TestCoroutineDispatcher()
    //private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder( context, PunkyDatabase::class.java)
            //.setTransactionExecutor(testDispatcher.asExecutor())
            //.setQueryExecutor(testDispatcher.asExecutor())
            .build()
        beerDao = db.beerDao()

        faker = Faker()
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun whenUpserting_newBeer_shouldBeInserted() = runBlocking {
        val beer = fakeBeer().copy( id=  1)
        beerDao.upsert( beer )
        val result = beerDao.getById( 1 )
        assertNotSame( beer, result)
        assertEquals( beer, result)
    }

    @Test
    fun whenUpserting_existingBeer_shouldBeUpdated() = runBlocking {
        val initialBeer = fakeBeer().copy(id = 1 )
        beerDao.upsert( initialBeer )

        val newBeer = fakeBeer().copy(id = 1 )
        beerDao.upsert( newBeer )

        val current = beerDao.getById( 1 )

        assertNotEquals( initialBeer, newBeer)
        assertEquals( newBeer, current)
    }

    private fun fakeBeer()  = Beer(
            id = Random.nextInt(),
            name = faker.beer.name(),
            imageUrl = null,
            tagline = faker.company.name(),
            description = faker.lorem.words(),
            first_brewed = "10/10",
            food_pairing = emptyList()
        )

}