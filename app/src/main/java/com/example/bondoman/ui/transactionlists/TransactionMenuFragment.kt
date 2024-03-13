package com.example.bondoman.ui.transactionlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentListBinding
class TransactionMenuFragment : Fragment(), ListAction {

    private var _binding: FragmentListBinding? = null
    private val transactionMenuAdapter = TransactionMenuAdapter(arrayListOf(), this)
    private lateinit var viewModel: TransactionMenuViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.transactionListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionMenuAdapter
        }

        binding.addTransaction.setOnClickListener{goToTransactionDetails()}

        viewModel = ViewModelProvider(this).get(TransactionMenuViewModel::class.java)

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.transactions.observe(this, Observer {transactionsList->
            binding.loadingView.visibility = View.GONE
            binding.transactionListView.visibility = View.VISIBLE
            transactionMenuAdapter.updateTransactions(transactionsList.sortedBy { it.creationTime })

        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.getTransactions()
    }

    private fun goToTransactionDetails(id:Long = 0L){
        val action = TransactionMenuFragmentDirections.actionGoToTransaction(id)
        Navigation.findNavController(binding.transactionListView).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(id: Long) {
        goToTransactionDetails(id)
    }
}