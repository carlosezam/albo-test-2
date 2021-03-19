package com.example.punky.ui.beerlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.punky.R
import com.example.punky.data.network.PunkApi
import com.example.punky.databinding.FragmentBeerListBinding
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BeerListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeerListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentBeerListBinding? = null
    private val binding: FragmentBeerListBinding  get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentBeerListBinding.inflate( inflater, container, false).also { _binding = it }.root
        //return inflater.inflate(R.layout.fragment_beer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BeerAdapter()
        binding.beerList.adapter = adapter



        lifecycleScope.launchWhenResumed {
            val client = HttpClient(CIO) {
                install(JsonFeature){
                    val json = kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    }
                    serializer = KotlinxSerializer( json )
                }
            }
            val api = PunkApi( client )

            val beers = api.getBeers(1, 20).map {
                BeerItem( it.name, it.description, it.image_url )
            }

            adapter.submitList( beers )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}