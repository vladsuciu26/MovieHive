package com.example.movietestapp.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movietestapp.R
import com.example.movietestapp.data.dto.MovieData
import com.example.movietestapp.data.dto.ReplyData
import com.example.movietestapp.data.dto.ReviewData
import com.example.movietestapp.data.dto.details.DetailsData
import com.example.movietestapp.data.dto.results.FavoriteMovieRequestResults
import com.example.movietestapp.databinding.FragmentDetailBinding
import com.example.movietestapp.ui.Navigator
import com.example.movietestapp.ui.adapter.CustomMoviesAdapter
import com.example.movietestapp.ui.adapter.ReviewsAdapter
import com.example.movietestapp.ui.detail.viewmodel.MovieDetailViewModel
import com.example.movietestapp.ui.review.viewmodel.ReviewViewModel
import com.example.movietestapp.util.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

class MovieDetailFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var movieDetailData: DetailsData
    private var _binding: FragmentDetailBinding? = null
    private var listOfSimilarMovies: ArrayList<MovieData> = arrayListOf()
    private var customMoviesAdapter: CustomMoviesAdapter? = null

    private val reviewViewModel: ReviewViewModel by viewModels()
    private var listOfReviews: ArrayList<ReviewData> = arrayListOf()
    private var reviewsAdapter: ReviewsAdapter? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        setupBackToMoviesPageButton()
        setupSubmitReviewButton()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id: Int? = arguments?.getInt(Constants.MOVIE_KEY_ID)

        if (id != null) {
            collectState(id)
            viewModel.loadMovieVideos(id)
        }

        observeFavoriteResult()
        setupTrailerButton()
        observeTrailerUrl()
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
                    binding.genre1.text = state.movieDetailState.movieDetail.genres[0].name
                    binding.genre2.text = state.movieDetailState.movieDetail.genres[1].name
                    binding.genre3.text = state.movieDetailState.movieDetail.genres[2].name
                    val formattedRuntime = formatRuntime(state.movieDetailState.movieDetail.runtime)
                    binding.movieDuration.text = formattedRuntime
                    binding.detailMovieTitle.text = state.movieDetailState.movieDetail.originalTitle
                    binding.detailMovieDescription.text =
                        state.movieDetailState.movieDetail.overview
                    binding.favoriteImageView.setOnClickListener {
                        Log.d("FavoritesRepository", "Favorite icon clicked for movie id: ${movieDetailData.id}")
                        Snackbar.make(
                            requireView(),
                            "Movie added to favorites",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        viewModel.addToFavorites(Constants.ACCOUNT_ID, Constants.SESSION_ID, movieDetailData.id)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loadSimilarMovies(id)
            viewModel.uiSimilarMovieState.collect { state ->
                if (state.similarMovieState?.similarMovie != null) {
                    listOfSimilarMovies = state.similarMovieState.similarMovie
                    lifecycleScope.launch {
                        customMoviesAdapter = CustomMoviesAdapter(listOfSimilarMovies)
                        binding.similarMoviesRecyclerView.adapter = customMoviesAdapter
                        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.similarMoviesRecyclerView.layoutManager = layoutManager
                    }
                }
            }
        }

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) 
        val currentUserId = sharedPreferences.getString("userId", null)

        lifecycleScope.launch {
            reviewViewModel.loadReviewsByMovieId(id)
            reviewViewModel.uiReviewState.collect { state ->
                if (state.reviewState?.reviews != null) {
                    listOfReviews = state.reviewState.reviews
                    reviewsAdapter = ReviewsAdapter(
                        listOfReviews,
                        currentUserId,
                        onEditClick = { review -> showEditReviewDialog(review) },
                        onDeleteClick = { review -> deleteReview(review) },
                        onReplyClick = { review -> showReplyDialog(review) }
                    )
                    binding.reviewsRecyclerView.adapter = reviewsAdapter
                    binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
                }
            }
        }


    }

    private fun setupBackToMoviesPageButton() {
        binding.backToDevicesPageButton.setOnClickListener {
            lifecycleScope.launch {
                Navigator.getInstance().openHomeMoviesFragment()
            }
        }
    }

//    private fun setupSubmitReviewButton() {
//        binding.submitReviewButton.setOnClickListener {
//            val content = binding.reviewEditText.text.toString()
//            val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//            val userId = sharedPreferences.getString("userId", null)
//            val username = sharedPreferences.getString("username", null)
//            val movieId = arguments?.getInt(Constants.MOVIE_KEY_ID)
//
//
//            Log.d("Review", "Submit button clicked")
//            Log.d("Review", "Content: $content")
//            Log.d("Review", "UserId: $userId")
//            Log.d("Review", "Username: $username")
//            Log.d("Review", "MovieId: $movieId")
//
//            if (userId != null && username != null) {
//                if (movieId != null) {
//                    reviewViewModel.addReview(content, userId, username, movieId)
//                    binding.reviewEditText.text.clear()
//                    Snackbar.make(binding.root, "Review submitted", Snackbar.LENGTH_SHORT).show()
//                } else {
//                    Snackbar.make(binding.root, "Failed to submit review: MovieId is null", Snackbar.LENGTH_SHORT).show()
//                }
//            } else {
//                Snackbar.make(binding.root, "Failed to submit review: User not logged in", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun setupSubmitReviewButton() {
        binding.submitReviewButton.setOnClickListener {
            val content = binding.reviewEditText.text.toString()
            val ratingText = binding.reviewRatingInput.text.toString()
            val rating = ratingText.toIntOrNull() ?: 0 // Convertim textul în număr, dacă este valid
            val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("userId", null)
            val username = sharedPreferences.getString("username", null)
            val movieId = arguments?.getInt(Constants.MOVIE_KEY_ID)

            if (userId != null && username != null) {
                if (movieId != null) {
//                    reviewViewModel.addReview(content, userId, username, movieId, rating)
                    reviewViewModel.addReview(content, movieId, rating)
                    binding.reviewEditText.text.clear()
                    binding.reviewRatingInput.text.clear() // Curățăm câmpul de rating după submit
                    Snackbar.make(binding.root, "Review submitted", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Failed to submit review: MovieId is null", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(binding.root, "Failed to submit review: User not logged in", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

//    private fun showEditReviewDialog(review: ReviewData) {
//        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_review, null)
//        val editText = dialogView.findViewById<EditText>(R.id.review_content_edit_text)
//        editText.setText(review.content)
//        val saveButton = dialogView.findViewById<Button>(R.id.save_review_button)
//
//        val dialog = AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .create()
//
//        saveButton.setOnClickListener {
//            val newContent = editText.text.toString()
//            reviewViewModel.updateReview(review, newContent)
//            Log.d("Review", "Edit button clicked")
//            Log.d("Review", "New content: $newContent")
//            Snackbar.make(binding.root, "Review updated", Snackbar.LENGTH_SHORT).show()
//            dialog.dismiss()
//
//            reviewsAdapter?.updateReviews(reviewViewModel.uiReviewState.value.reviewState?.reviews ?: arrayListOf())
//        }
//
//        dialog.show()
//    }

    private fun showEditReviewDialog(review: ReviewData) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_review, null)
        val editText = dialogView.findViewById<EditText>(R.id.review_content_edit_text)
        val ratingInput = dialogView.findViewById<EditText>(R.id.review_rating_input)
        editText.setText(review.content)
        ratingInput.setText(review.rating.toString())

        val saveButton = dialogView.findViewById<Button>(R.id.save_review_button)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        saveButton.setOnClickListener {
            val newContent = editText.text.toString()
            val newRating = ratingInput.text.toString().toIntOrNull() ?: review.rating
            reviewViewModel.updateReview(review, newContent, newRating)
            Snackbar.make(binding.root, "Review updated", Snackbar.LENGTH_SHORT).show()
            dialog.dismiss()

            reviewsAdapter?.updateReviews(reviewViewModel.uiReviewState.value.reviewState?.reviews ?: arrayListOf())
        }

        dialog.show()
    }

    private fun showReplyDialog(review: ReviewData) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_reply_review, null)
        val editText = dialogView.findViewById<EditText>(R.id.reply_content_edit_text)
        val saveButton = dialogView.findViewById<Button>(R.id.submit_reply_button)

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val replyUsername = sharedPreferences.getString("username", null)
        val userId = sharedPreferences.getString("userId", null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        saveButton.setOnClickListener {
            val replyContent = editText.text.toString()
            if (userId != null) {
                reviewViewModel.addReply(review, replyContent, replyUsername ?: "Anonymous", review.movieId, userId)
                Log.d("Review", "Reply button clicked")
                Log.d("Review", "Reply content: $replyContent")
                Snackbar.make(binding.root, "Reply added", Snackbar.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Snackbar.make(binding.root, "Failed to add reply: User not logged in", Snackbar.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }


    private fun deleteReview(review: ReviewData) {
        reviewViewModel.deleteReview(review)
        Log.d("Review", "Delete button clicked")
        Log.d("Review", "Review ID: ${review.id}")
        Snackbar.make(binding.root, "Review deleted", Snackbar.LENGTH_SHORT).show()

        reviewsAdapter?.updateReviews(reviewViewModel.uiReviewState.value.reviewState?.reviews ?: arrayListOf())
    }


    private fun observeFavoriteResult() {
        lifecycleScope.launch {
            viewModel.uiAddFavoriteMovieResult.collect { result ->
                result.addFavoriteMoviesState?.let {
                    if (it.favoriteResponse != null) {
                        Log.d("FavoritesRepository", "Successfully added to favorites: ${it.favoriteResponse}")
                        Snackbar.make(
                            requireView(),
                            "Movie successfully added to favorites",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.e("FavoritesRepository", "Failed to add to favorites")
                        Snackbar.make(
                            requireView(),
                            "Failed to add movie to favorites",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    viewModel.resetFavoriteResult()
                }
            }
        }
    }


    private fun setupTrailerButton() {
        binding.playButtonTrailer.setOnClickListener {
            viewModel.trailerUrl.value?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }

    private fun observeTrailerUrl() {
        lifecycleScope.launch {
            viewModel.trailerUrl.collect { url ->
                binding.playButtonTrailer.visibility = if (url != null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun formatRuntime(runtime: Int): String {
        val hours = runtime / 60
        val minutes = runtime % 60
        return String.format("%dh %02dm", hours, minutes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}