package com.example.punky.ui.beerlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.punky.R
import com.example.punky.data.PunkApiRepository
import com.example.punky.data.PunkyListViewModel
import com.example.punky.data.PunkyListViewModelFactory
import com.example.punky.data.network.PunkApi
import com.example.punky.databinding.FragmentBeerListBinding
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


/**
 * A simple [Fragment] subclass.
 * Use the [BeerListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeerListFragment : Fragment() {


    private var _binding: FragmentBeerListBinding? = null
    private val binding: FragmentBeerListBinding  get() = _binding!!

    lateinit var vmFactory : PunkyListViewModelFactory

    private val vm: BeerListViewModel by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val client = HttpClient(CIO) {
            install(JsonFeature){
                val json = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
                serializer = KotlinxSerializer( json )
            }
        }
        val api = PunkApi( client )

        vmFactory = PunkyListViewModelFactory( PunkApiRepository(api) )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentBeerListBinding.inflate( inflater, container, false).also { _binding = it }.root
        //return inflater.inflate(R.layout.fragment_beer_list, container, false)
    }

    var adapter : BeerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BeerAdapter()
        binding.beerList.adapter = adapter

       lifecycleScope.launch {
           vm.beers.collectLatest {
               adapter?.submitData( it )
           }
       }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}