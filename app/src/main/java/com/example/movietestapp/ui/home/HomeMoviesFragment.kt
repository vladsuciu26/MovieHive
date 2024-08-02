package com.example.movietestapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.databinding.FragmentHomeMoviesBinding
import com.example.movietestapp.ui.adapter.HomeMoviesAdapter
import com.example.movietestapp.ui.home.filter.FilterDialogFragment
import com.example.movietestapp.ui.home.viewmodel.HomeViewModel
import com.example.movietestapp.util.NetworkUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeMoviesFragment : Fragment(), FilterDialogFragment.FilterDialogListener {

    private lateinit var adapterMovies: HomeMoviesAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var listOfMovies: ArrayList<MovieData> = arrayListOf()

    private var _binding: FragmentHomeMoviesBinding? = null
    private val binding get() = _binding!!

    private val genresMap = mapOf(
        "Action" to 28,
        "Adventure" to 12,
        "Animation" to 16,
        "Comedy" to 35,
        "Crime" to 80,
        "Documentary" to 99,
        "Drama" to 18,
        "Family" to 10751,
        "Fantasy" to 14,
        "History" to 36,
        "Horror" to 27,
        "Music" to 10402,
        "Mystery" to 9648,
        "Romance" to 10749,
        "Science Fiction" to 878,
        "TV Movie" to 10770,
        "Thriller" to 53,
        "War" to 10752,
        "Western" to 37
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMoviesBinding.inflate(inflater, container, false)
        val view = binding.root
        setupPopularMoviesRecyclerView()
        setupTopRatedMoviesRecyclerView()
        setupUpcomingMoviesRecyclerView()

        setupNavigationButtons()

        setupSearch()

        setupFilter()

        return view
    }

    private fun setupPopularMoviesRecyclerView() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                viewModel.loadPopularMovies()
                viewModel.uiPopularMoviesState.collect { state ->
                    if (state.popularMoviesState?.popularMovies != null) {
                        listOfMovies = state.popularMoviesState.popularMovies
                        lifecycleScope.launch {
                            adapterMovies = HomeMoviesAdapter(listOfMovies)
                            binding.recyclerViewPopularMovies.adapter = adapterMovies
                            val layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            binding.recyclerViewPopularMovies.layoutManager = layoutManager
                        }
                    }
                }
            }
        } else {
            showSnackbar(requireView(), "No internet connection!")
        }
    }

    private fun setupTopRatedMoviesRecyclerView() {
        lifecycleScope.launch {
            viewModel.loadTopRatedMovies()
            viewModel.uiTopRatedMoviesState.collect { state ->
                if (state.topRatedMoviesState?.topRatedMovies != null) {
                    listOfMovies = state.topRatedMoviesState.topRatedMovies
                    lifecycleScope.launch {
                        adapterMovies = HomeMoviesAdapter(listOfMovies)
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
                        adapterMovies = HomeMoviesAdapter(listOfMovies)
                        binding.recyclerViewUpcomingMovies.adapter = adapterMovies
                        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.recyclerViewUpcomingMovies.layoutManager = layoutManager
                    }
                }
            }
        }
    }

    private fun setupNavigationButtons() {
        binding.buttonPreviousPage.setOnClickListener {
            viewModel.loadPreviousPagePopular()
            viewModel.loadPreviousPageTopRated()
            viewModel.loadPreviousPageUpcoming()
        }

        binding.buttonNextPage.setOnClickListener {
            viewModel.loadNextPagePopular()
            viewModel.loadNextPageTopRated()
            viewModel.loadNextPageUpcoming()
        }

        lifecycleScope.launch {
            viewModel.uiPopularMoviesState.collect { state ->
                val currentPage = viewModel.getCurrentPagePopular()
                binding.buttonPreviousPage.isEnabled = currentPage > 1
            }
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    viewModel.searchMovies(s.toString())
                } else {
                    showPopularMovies()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        lifecycleScope.launch {
            viewModel.uiSearchMoviesState.collect { state ->
                if (state.searchMoviesState?.searchMovies != null) {
                    showSearchResults(state.searchMoviesState.searchMovies)
                } else {
                    showPopularMovies()
                }
            }
        }
    }

    private fun showSearchResults(movies: ArrayList<MovieData>) {
        adapterMovies = HomeMoviesAdapter(movies)
        binding.recyclerViewSearchResults.adapter = adapterMovies
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL, false)

        binding.searchResultsContainer.visibility = View.VISIBLE
        binding.searchResultsTextView.visibility = View.VISIBLE

        binding.recyclerViewPopularMovies.visibility = View.GONE
        binding.popularMoviesTextView.visibility = View.GONE

        binding.recyclerViewUpcomingMovies.visibility = View.GONE
        binding.upcomingMoviesTextView.visibility = View.GONE

        binding.recyclerViewTopRatedMovies.visibility = View.GONE
        binding.topRatedMoviesTextView.visibility = View.GONE

        binding.buttonPreviousPage.visibility = View.GONE
        binding.buttonNextPage.visibility = View.GONE
    }

    private fun showPopularMovies() {
        binding.searchResultsContainer.visibility = View.GONE
        binding.searchResultsTextView.visibility = View.GONE

        binding.recyclerViewPopularMovies.visibility = View.VISIBLE
        binding.popularMoviesTextView.visibility = View.VISIBLE


    }

    private fun setupFilter() {
        binding.filterButton.setOnClickListener {
            val filterDialog = FilterDialogFragment()
            filterDialog.setTargetFragment(this, 0)
            filterDialog.show(parentFragmentManager, "FilterDialog")
        }
    }

    override fun onApplyFilter(year: Int?, sortBy: String?, genres: String?, keywords: String?) {
        val genreId = genresMap[genres] // Map the genre name to its ID
        lifecycleScope.launch {
            viewModel.loadFilteredMovies(year, sortBy, genreId?.toString(), keywords)
        }
    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}