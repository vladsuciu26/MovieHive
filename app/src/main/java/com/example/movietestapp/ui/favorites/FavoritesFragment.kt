package com.example.movietestapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.data.dto.details.DetailsData
import com.example.movietestapp.databinding.FragmentDetailBinding
import com.example.movietestapp.databinding.FragmentFavoritesBinding
import com.example.movietestapp.ui.Navigator
import com.example.movietestapp.ui.adapter.CustomMoviesAdapter
import com.example.movietestapp.ui.adapter.FavoriteMoviesAdapter
import com.example.movietestapp.ui.detail.viewmodel.MovieDetailViewModel
import com.example.movietestapp.ui.favorites.viewmodel.FavoritesViewModel
import com.example.movietestapp.util.Constants
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private var listOfFavoriteMovies: ArrayList<MovieData> = arrayListOf()
//    private var customMoviesAdapter: CustomMoviesAdapter? = null
    private var favoriteMoviesAdapter: FavoriteMoviesAdapter? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        setupBackToMoviesPageButton()
        setupRecyclerView()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectState()
    }

    private fun setupBackToMoviesPageButton() {
        binding.backToDevicesPageButton.setOnClickListener {
            lifecycleScope.launch {
                Navigator.getInstance().openHomeMoviesFragment()
            }
        }
    }

    private fun setupRecyclerView() {
        favoriteMoviesAdapter = FavoriteMoviesAdapter(listOfFavoriteMovies)
        binding.favoriteMoviesRecyclerView.adapter = favoriteMoviesAdapter
        binding.favoriteMoviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun collectState() {
        lifecycleScope.launch {
            viewModel.loadFavoriteMovies(Constants.ACCOUNT_ID, Constants.SESSION_ID)
            viewModel.uiFavoriteMovieState.collect { state ->
                state.favoriteMoviesState?.favoriteMovie?.let {
                    listOfFavoriteMovies = it
                    favoriteMoviesAdapter?.updateData(listOfFavoriteMovies)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}