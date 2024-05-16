package com.example.movietestapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.databinding.FragmentDetailBinding
import com.example.movietestapp.ui.Navigator
import com.example.movietestapp.ui.detail.viewmodel.MovieDetailViewModel
import com.example.movietestapp.util.Constants
import kotlinx.coroutines.launch

class MovieDetailFragment : Fragment() {
    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var movieDetailData: MovieData
    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        setupBackToMoviesPageButton()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id: Int? = arguments?.getInt(Constants.MOVIE_KEY_ID)
        if (id != null) {
            collectState(id)
        }
    }

    private fun setupBackToMoviesPageButton() {
        binding.backToDevicesPageButton.setOnClickListener {
            lifecycleScope.launch {
                Navigator.getInstance().openHomeMoviesFragment()
            }
        }
    }

    private fun collectState(id: Int) {
        lifecycleScope.launch {
            viewModel.loadMovieDetail(id)
            viewModel.uiMovieDetailState.collect { state ->
                if (state.movieDetailState?.movieDetail != null) {
                    movieDetailData = state.movieDetailState.movieDetail
                    val imageUrl =
                        "${Constants.IMAGE_BASE_URL}${state.movieDetailState.movieDetail.posterPath}"
                    val imgUrl = "${Constants.IMAGE_BASE_URL}${state.movieDetailState.movieDetail.backdropPath}"
                    Glide.with(binding.detailMovieCover.context)
                        .load(imgUrl)
                        .into(binding.detailMovieCover)
                    Glide.with(binding.detailMovieImg.context)
                        .load(imageUrl)
                        .into(binding.detailMovieImg)
                    binding.detailMovieTitle.text = state.movieDetailState.movieDetail.originalTitle
                    binding.detailMovieDescription.text =
                        state.movieDetailState.movieDetail.overview
                }
            }
        }
    }

}