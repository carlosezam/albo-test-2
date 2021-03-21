package com.example.punky.ui.beerdeatils

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.punky.PunkyApplication
import com.example.punky.R
import com.example.punky.databinding.FragmentBeerDetailsBinding
import com.example.punky.utils.LoadingDrawable
import javax.inject.Inject
import kotlin.properties.Delegates


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



/**
 * A simple [Fragment] subclass.
 * Use the [BeerDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeerDetailsFragment : Fragment() {

    var beerId = 0
    private var imageTransitionName: String? = null

    private var _binding: FragmentBeerDetailsBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val vmodel: BeerDetailsViewModel by viewModels { viewModelFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context.applicationContext as PunkyApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beerId = arguments?.getInt( ARG_BEER_ID) ?: throw IllegalArgumentException("Beer Id is required")
        imageTransitionName = arguments?.getString(ARG_IMAGE_TRANSITION_NAME)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        vmodel.loadBeer( beerId )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBeerDetailsBinding.inflate(inflater, container, false).also { binding ->
        _binding = binding
        imageTransitionName?.let { binding.image.transitionName = it }

        val options = RequestOptions().apply {
            placeholder( LoadingDrawable( requireContext(), R.color.black) )
            error( R.drawable.ic_error_load_image )
        }

        vmodel.beerImage.observe( viewLifecycleOwner ){ imageUrl ->
            Glide.with( requireContext() )
                .load( imageUrl )
                .apply( options )
                .into( binding.image )
        }

    }.root


    companion object {
        const val ARG_IMAGE_TRANSITION_NAME = "image_transition_name"
        const val ARG_BEER_ID = "beer_id"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BeerDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_TRANSITION_NAME, param1)
                }
            }
    }
}