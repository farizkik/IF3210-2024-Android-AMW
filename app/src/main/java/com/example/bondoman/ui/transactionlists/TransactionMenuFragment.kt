package com.example.bondoman.ui.transactionlists

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bondoman.databinding.FragmentListBinding
import com.example.bondoman.receiver.MyBroadcastListener
import com.example.bondoman.receiver.MyBroadcastReceiver


class TransactionMenuFragment : Fragment(), ListAction, MyBroadcastListener {

    private var _binding: FragmentListBinding? = null
    private val transactionMenuAdapter = TransactionMenuAdapter(arrayListOf(), this)
    private lateinit var viewModel: TransactionMenuViewModel
    private var toggle: Boolean = false

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

        binding.addTransaction.setOnClickListener{
            if(toggle) {
                goToTransactionDetails(0L, "Random")
                toggle = false
            }
                else
            goToTransactionDetails()}

        viewModel = ViewModelProvider(this).get(TransactionMenuViewModel::class.java)

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.transactions.observe(viewLifecycleOwner, Observer {transactionsList->
            binding.loadingView.visibility = View.GONE
            binding.transactionListView.visibility = View.VISIBLE
            transactionMenuAdapter.updateTransactions(transactionsList.sortedBy { it.creationTime })

        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.getTransactions()
    }

    private fun goToTransactionDetails(id:Long = 0L, rand:String = ""){
        val action = TransactionMenuFragmentDirections.actionGoToTransaction(id,rand)
        Navigation.findNavController(binding.transactionListView).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(id: Long) {
        goToTransactionDetails(id)
    }

    private lateinit var receiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        receiver = MyBroadcastReceiver(this)
        val intentFilter = IntentFilter("com.example.bondoman.action")
        requireContext().registerReceiver(receiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)

    }
    override fun onBroadcastReceived(value: String?) {
        Log.d("adsadas", "asdasd")
        toggle=true
    }
}