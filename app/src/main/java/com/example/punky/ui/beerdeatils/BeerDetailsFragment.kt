package com.example.punky.ui.beerdeatils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.punky.PunkyApplication
import com.example.punky.R
import com.example.punky.databinding.FragmentBeerDetailsBinding
import com.example.punky.utils.LoadingDrawable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



/**
 * A simple [Fragment] subclass.
 * Use the [BeerDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BeerDetailsFragment : Fragment() {

    var beerId = 0
    private var imageTransitionName: String? = null
    private var imageUrl: String? = null

    private var _binding: FragmentBeerDetailsBinding? = null
    private val binding: FragmentBeerDetailsBinding get() = _binding!!

    private val vmodel: BeerDetailsViewModel by viewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beerId = arguments?.getInt( ARG_BEER_ID) ?: throw IllegalArgumentException("Beer Id is required")
        imageTransitionName = arguments?.getString(ARG_IMAGE_TRANSITION_NAME)
        imageUrl =arguments?.getString(ARG_IMAGE_URL)

        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        imageUrl?.let{
            //postponeEnterTransition()
            //loadImage( it )
        }

        vmodel.loadBeer( beerId )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBeerDetailsBinding.inflate(inflater, container, false).also { binding ->
        _binding = binding
        imageTransitionName?.let { binding.image.transitionName = it }



        vmodel.beerDetails.observe( viewLifecycleOwner ){
            updateUi( it ?: return@observe )
        }

    }.root

    private fun updateUi(beer: UiBeerDetails ){
        binding.collapsingToolbarLayout.title = beer.name
        binding.nameText.text = beer.name
        binding.taglineText.text = beer.tagline
        binding.descriptionText.text = beer.description
        binding.firstBrewedText.text = beer.firstBrewed
        binding.foodPairingText.text = beer.foodPairing.fold(""){ acum, next -> "$acum• $next\n" }

        loadImage( beer.imageUrl )
    }

    private fun loadImage(imageUrl: String?){
        imageUrl ?: return

        val options = RequestOptions().apply {
            placeholder( LoadingDrawable( requireContext(), R.color.black) )
            error( R.drawable.ic_error_load_image )
            dontTransform()
        }
        Glide.with( requireContext() )
            .load( imageUrl )
            .apply( options )
            .listener(glideListener)
            .into( binding.image )
    }
    private val glideListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }

    }
    companion object {
        const val ARG_IMAGE_TRANSITION_NAME = "image_transition_name"
        const val ARG_IMAGE_URL = "image_url"
        const val ARG_BEER_ID = "beer_id"
    }
}
