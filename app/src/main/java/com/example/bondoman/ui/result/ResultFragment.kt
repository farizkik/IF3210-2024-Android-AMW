package com.example.bondoman.ui.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bondoman.core.data.Item
import com.example.bondoman.core.data.ParcelableItem
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null

    private val binding get() = _binding!!
    private val viewModel: ResultViewModel by viewModels()

    private val resultAdapter = ResultAdapter(arrayListOf())

    private var tempTransaction = Transaction("", "", 0L, 0L, "",0.0,0.0)

    private lateinit var items: List<ParcelableItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = resultAdapter
        }

        arguments?.let{
            items = ResultFragmentArgs.fromBundle(it).items.toList()
            val temp = ArrayList<Item>()
            var sum = 0.0
            items.forEach(){
                temp.add(Item(it))
                sum += Item(it).price
            }
            val time:Long = System.currentTimeMillis()
            tempTransaction.location = "Default"
            tempTransaction.latitude = 0.0
            tempTransaction.longitude = 0.0
            tempTransaction.title = "Transaction Scanned"
            tempTransaction.type = "PEMBELIAN"
            tempTransaction.nominal = sum.toLong()
            tempTransaction.creationTime = time

            Log.d("Result Fragment", items.toString())
            resultAdapter.updateItems(temp)
        }

        binding.uploadResultButtonReject.setOnClickListener {
            viewModel.saveTransaction(tempTransaction)
            val action = ResultFragmentDirections.actionResultFragmentToNavigationHome()
            Navigation.findNavController(binding.uploadRecyclerView).navigate(action)
        }

        binding.uploadResultButtonAccept.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToNavigationHome()
            Navigation.findNavController(binding.uploadRecyclerView).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}