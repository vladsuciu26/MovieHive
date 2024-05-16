package com.example.movietestapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.databinding.FragmentHomeMoviesBinding
import com.example.movietestapp.ui.adapter.MoviesAdapter
import com.example.movietestapp.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeMoviesFragment : Fragment() {

    private lateinit var adapterMovies: MoviesAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var listOfMovies: ArrayList<MovieData> = arrayListOf()

    private var _binding: FragmentHomeMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMoviesBinding.inflate(inflater, container, false)
        val view = binding.root
        setupPopularMoviesRecyclerView()
        setupTopRatedMoviesRecyclerView()
        setupUpcomingMoviesRecyclerView()
        return view
    }

    private fun setupPopularMoviesRecyclerView() {
        lifecycleScope.launch {
            viewModel.loadPopularMovies()
            viewModel.uiPopularMoviesState.collect { state ->
                if (state.popularMoviesState?.popularMovies != null) {
                    listOfMovies = state.popularMoviesState.popularMovies
                    lifecycleScope.launch {
                        adapterMovies = MoviesAdapter(listOfMovies)
                        binding.recyclerViewPopularMovies.adapter = adapterMovies
                        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.recyclerViewPopularMovies.layoutManager = layoutManager
                    }
                }
            }
        }
    }

    private fun setupTopRatedMoviesRecyclerView() {
        lifecycleScope.launch {
            viewModel.loadTopRatedMovies()
            viewModel.uiTopRatedMoviesState.collect { state ->
                if (state.topRatedMoviesState?.topRatedMovies != null) {
                    listOfMovies = state.topRatedMoviesState.topRatedMovies
                    lifecycleScope.launch {
                        adapterMovies = MoviesAdapter(listOfMovies)
                        binding.recyclerViewTopRatedMovies.adapter = adapterMovies
                        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.recyclerViewTopRatedMovies.layoutManager = layoutManager
                    }
                }
            }
        }
    }

    private fun setupUpcomingMoviesRecyclerView() {
        lifecycleScope.launch {
            viewModel.loadUpcomingMovies()
            viewModel.uiUpcomingMoviesState.collect { state ->
                if (state.upcomingMoviesState?.upcomingMovies != null) {
                    listOfMovies = state.upcomingMoviesState.upcomingMovies
                    lifecycleScope.launch {
                        adapterMovies = MoviesAdapter(listOfMovies)
                        binding.recyclerViewUpcomingMovies.adapter = adapterMovies
                        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.recyclerViewUpcomingMovies.layoutManager = layoutManager
                    }
                }
            }
        }
    }

}