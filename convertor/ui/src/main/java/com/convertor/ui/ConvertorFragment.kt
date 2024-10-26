package com.convertor.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.convertor.ui.databinding.FragmentConvertorBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConvertorFragment : Fragment() {

    private var _binding:  FragmentConvertorBinding? = null

    val binding get() = _binding!!


    private val viewModel: ConvertorViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConvertorBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelData()
        observeConvertedListData()
        amountChangeListner()
    }

    private fun amountChangeListner() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setAmount(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModelData() {
       viewModel.currencies.observe(viewLifecycleOwner) { list ->
           val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, list)
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
           binding.spinner.adapter = adapter

           binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
               override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    viewModel.setSelectedCurrencyIndex(position)
               }

               override fun onNothingSelected(parent: AdapterView<*>) {

               }
           }
       }
    }


    private fun observeConvertedListData() {
        viewModel.convertedListData.observe(viewLifecycleOwner) { list ->
               if (!list.isNullOrEmpty()) {
                   binding.convertedListView.apply {
                       visibility = View.VISIBLE
                       adapter = ConvertedDataListAdapter(list)

                   }
               }
            else{
                    binding.convertedListView.apply {
                        visibility = View.GONE
                    }
               }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}