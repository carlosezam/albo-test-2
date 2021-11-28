package com.example.punky.data.network

import androidx.paging.PagingSource
import com.example.punky.data.local.entities.Beer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class PunkApiSourceTest {

    val punkBeerList:List<PunkBeer> = listOf(
        PunkBeer(1, "Beer 1", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(2, "Beer 2", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(3, "Beer 3", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(4, "Beer 4", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(5, "Beer 5", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(6, "Beer 6", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(7, "Beer 7", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(8, "Beer 8", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(9, "Beer 9", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(10, "Beer 10", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(11, "Beer 11", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(12, "Beer 12", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(13, "Beer 13", null, "lorem", "lorem", "lorem", emptyList() ),
        PunkBeer(14, "Beer 14", null, "lorem", "lorem", "lorem", emptyList() )
    )

    val punkApi = FakePunkiApi( punkBeerList )

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun initialLoad_shouldGetFirstElemnts(): Unit = runBlocking {
        val mockApi: IPunkApi = mock()
        val punkApiSource = PunkApiSource( mockApi )
        whenever( mockApi.getBeers(any(), any()) ).thenReturn( emptyList() )

        //val expected = PagingSource.LoadResult.Page( data = listOf<PunkBeer>(), null, null )
        val actual = punkApiSource.load( PagingSource.LoadParams.Refresh( key = null, loadSize = 3, placeholdersEnabled = false) )

        // debe intentar descargar de la pagina inicial, 3 elemntos
        verify( mockApi ).getBeers( 1, 3)
    }

    @Test
    fun onEmpyList_ShouldReturnNullKeys(): Unit = runBlocking {
        val mockApi: IPunkApi = mock()
        val punkApiSource = PunkApiSource( mockApi )
        whenever( mockApi.getBeers(any(), any()) ).thenReturn( emptyList() )


        val actual =punkApiSource.load( PagingSource.LoadParams.Refresh( key = null, loadSize = 3, placeholdersEnabled = false) )

        val expected = PagingSource.LoadResult.Page<String,PunkBeer>( data = emptyList(), null, null )

        Assert.assertEquals( expected, actual)
    }

    @Test
    fun whenLoad_nextKey_shouldBeNext(): Unit = runBlocking {
        val punkApiSource = PunkApiSource( punkApi, 1, 2 )

        val params : PagingSource.LoadParams<Int> = PagingSource.LoadParams.Refresh( key = null, loadSize = 3, placeholdersEnabled = false)
        val page = punkApiSource.load( params ) as PagingSource.LoadResult.Page<Int, PunkBeer>


        Assert.assertEquals( 2, page.nextKey)

    }
}

class FakePunkiApi(
    private val list: List<PunkBeer>
) : IPunkApi {
    override suspend fun getBeers(page: Int, perPage: Int): List<PunkBeer> {
        val from = page - 1
        val to = from + perPage
        return list.subList( from, to )
    }

}

class FakePunkiApiTest {

    @Test
    fun getBeers_shouldSlice(): Unit = runBlocking {
        val listMock : List<PunkBeer> = mock()
        val fakeApi = FakePunkiApi( listMock )

        fakeApi.getBeers(20, 10)


        verify( listMock ).subList( 19, 29 )
    }
}