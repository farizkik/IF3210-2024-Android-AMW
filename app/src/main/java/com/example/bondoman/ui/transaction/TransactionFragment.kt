package com.example.bondoman.ui.transaction

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.bondoman.R
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.databinding.FragmentTransactionBinding

class TransactionFragment : Fragment() {
    private var transactionId = 0L
    private val viewModel: TransactionViewModel by viewModels()
    private var currentTransaction = Transaction("", "", 0L, 0L, "")

    private var _binding: FragmentTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val view = binding.root
        observeViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        arguments?.let {
            transactionId = TransactionFragmentArgs.fromBundle(it).transactionID
        }

        if(transactionId != 0L){
            viewModel.getTransaction(transactionId)
        }

        binding.saveButton.setOnClickListener{
            Log.d("Observer", "Observer triggered with success")
            if(binding.titleView.text.toString() != "" || binding.typeView.text.toString() != "") {
                val time:Long = System.currentTimeMillis()
                currentTransaction.title =binding.titleView.text.toString()
                currentTransaction.type = binding.typeView.text.toString()
                if(currentTransaction.id == 0L){
                    currentTransaction.creationTime = time
                }
                viewModel.saveTransaction(currentTransaction)
                viewModel.saved.postValue(false)

            }
            Navigation.findNavController(it).popBackStack()
        }



    }

    private fun observeViewModel(){
        viewModel.saved.observe(viewLifecycleOwner, Observer<Boolean> {it->
            Log.d("Observer", "Observer triggered with yippie")
            if(it) {
                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
//                Navigation.findNavController(binding.titleView).popBackStack()
            } else{
                Toast.makeText(context, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
            }
        }
        )

        viewModel.currentTransaction.observe(viewLifecycleOwner, Observer{transaction->
            transaction?.let {
                currentTransaction = it
                binding.titleView.setText(it.title, TextView.BufferType.EDITABLE)
                binding.typeView.setText(it.type, TextView.BufferType.EDITABLE)
            }

        })
    }

}