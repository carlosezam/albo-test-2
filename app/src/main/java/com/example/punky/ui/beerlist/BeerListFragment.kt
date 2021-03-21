package com.example.punky.ui.beerlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.punky.PunkyApplication
import com.example.punky.R

import com.example.punky.databinding.FragmentBeerListBinding
import com.example.punky.ui.beerdeatils.BeerDetailsFragment
import com.example.punky.utils.EventObserver
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [BeerListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeerListFragment : Fragment() {


    private var _binding: FragmentBeerListBinding? = null
    private val binding: FragmentBeerListBinding  get() = _binding!!


    @Inject lateinit var viewmodelFactory: ViewModelProvider.Factory
    private val vmodel: BeerListViewModel by viewModels { viewmodelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context.applicationContext as PunkyApplication).appComponent.inject( this )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBeerListBinding.inflate( inflater, container, false).also { _binding = it }.root


    var adapter : BeerAdapter? = null

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()

        vmodel.command.observe( viewLifecycleOwner, EventObserver{
            when( it ){
                is BeerListCommand.OpenBeerDetails -> showBeerDetails( it.beerId, sharedImage )
            }
        })

    }

    var sharedImage: View? = null
    private fun setupListAdapter(){
        adapter = BeerAdapter { binding, item, position ->
            //showBeerDetails( item.id, binding.mediaImage )
            sharedImage = binding.mediaImage
            vmodel.clickOnBeerItem( item )
        }

        postponeEnterTransition()
        binding.beerList.adapter = adapter
        binding.beerList.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

        lifecycleScope.launch {
            vmodel.beers.collectLatest {
                adapter?.submitData( it )
            }
        }
    }

    private fun showBeerDetails(id: Int, sharedImage: View?){

        var bundle = bundleOf(
            BeerDetailsFragment.ARG_BEER_ID to id,
            BeerDetailsFragment.ARG_IMAGE_TRANSITION_NAME to sharedImage?.transitionName
        )


        val extras = sharedImage?.let{
            FragmentNavigator.Extras.Builder()
                .addSharedElement(sharedImage, sharedImage.transitionName)
                .build()
        }

            findNavController().navigate(R.id.to_details, bundle, null, extras)
       }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}