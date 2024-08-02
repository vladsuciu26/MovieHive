package com.example.movietestapp.ui.home.filter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.movietestapp.R
import com.example.movietestapp.databinding.FragmentFilterDialogBinding

//class FilterDialogFragment : DialogFragment() {
//
//    private var _binding: FragmentFilterDialogBinding? = null
//    private val binding get() = _binding!!
//
//    interface FilterDialogListener {
//        fun onApplyFilter(year: Int?, sortBy: String?, genres: String?, keywords: String?)
//    }
//
//    private var listener: FilterDialogListener? = null
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        _binding = FragmentFilterDialogBinding.inflate(layoutInflater)
//
//        val builder = AlertDialog.Builder(requireActivity())
//        builder.setView(binding.root)
//            .setTitle("Filter")
//            .setPositiveButton("Apply Filters") { _, _ ->
//                val year = binding.yearEditText.text.toString().toIntOrNull()
//                val sortBy = binding.sortBySpinner.selectedItem as String?
//                val genres = binding.genresSpinner.selectedItem as String?
//                val keywords = binding.keywordsEditText.text.toString()
//
//                listener?.onApplyFilter(year, sortBy, genres, keywords)
//            }
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//        setupSpinners()
//
//        return builder.create()
//    }
//
//    private fun setupSpinners() {
//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.sort_by_options,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.sortBySpinner.adapter = adapter
//        }
//
//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.genres_options,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.genresSpinner.adapter = adapter
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        listener = try {
//            targetFragment as FilterDialogListener?
//        } catch (e: ClassCastException) {
//            throw ClassCastException("$context must implement FilterDialogListener")
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

class FilterDialogFragment : DialogFragment() {

    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!

    interface FilterDialogListener {
        fun onApplyFilter(year: Int?, sortBy: String?, genres: String?, keywords: String?)
    }

    private var listener: FilterDialogListener? = null

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentFilterDialogBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
            .setTitle("Filter")
            .setPositiveButton("Apply Filters") { _, _ ->
                val year = binding.yearEditText.text.toString().toIntOrNull()
                val sortBy = binding.sortBySpinner.selectedItem as String?
                val genres = binding.genresSpinner.selectedItem as String?
                val keywords = binding.keywordsEditText.text.toString()

                listener?.onApplyFilter(year, sortBy, genres, keywords)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        setupSpinners()

        return builder.create()
    }

    private fun setupSpinners() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_by_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sortBySpinner.adapter = adapter
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            genresMap.keys.toList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.genresSpinner.adapter = adapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            targetFragment as FilterDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement FilterDialogListener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
