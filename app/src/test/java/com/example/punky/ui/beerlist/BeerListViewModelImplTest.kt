package com.example.punky.ui.beerlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.punky.data.PunkApiRepository
import com.example.punky.data.local.entities.Beer
import com.example.punky.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class BeerListViewModelImplTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope( testDispatcher )

    @get:Rule
    var taskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getCommand() {
        Dispatchers.setMain( testDispatcher )
    }

    @Test
    fun getBeers() = testScope.runBlockingTest {
        // setup
        val beer = Beer(1, "name", null, "tagline", "description", "fb", emptyList() )

        val pagingData = PagingData.from( listOf(beer) )

        val repository = mock<PunkApiRepository>{
            on { getStream() } doReturn flow { emit(pagingData) }
        }
        val vmodel = BeerListViewModelImpl( repository )

        // execute
        val first = vmodel.beers.first()

        // test
        val differ = AsyncPagingDataDiffer( diffCallback = MyDiffCallback(), updateCallback = NoopListCallback(), mainDispatcher = testDispatcher )
        differ.submitData( first )
        advanceUntilIdle()

        val expected = BeerItem(1, beer.name, beer.tagline, beer.imageUrl)
        val actual = differ.snapshot().items.first()
        Assert.assertEquals( expected, actual )
    }

    @Test
    fun clickOnBeerItem() {

        val vmodel = BeerListViewModelImpl(mock())

        val observer = mock<Observer<Event<BeerListCommand>>>()
        vmodel.command.observeForever( observer )

        val item = mock<BeerItem>()
        vmodel.clickOnBeerItem( item, 1)

        verify( observer ).onChanged( argThat { peekContent() == BeerListCommand.OpenBeerDetails(item, 1) } )
    }



    class NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    class MyDiffCallback : DiffUtil.ItemCallback<BeerItem>(){
        override fun areItemsTheSame(oldItem: BeerItem, newItem: BeerItem): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: BeerItem, newItem: BeerItem): Boolean = oldItem == newItem

    }
}