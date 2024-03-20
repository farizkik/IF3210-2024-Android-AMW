package com.example.bondoman.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentHomeBinding
import com.example.bondoman.service.ConnectivityObserver
import com.example.bondoman.service.NetworkConnectivityObserver
import com.example.bondoman.ui.network.NetworkOfflineFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var connectivityObserver: ConnectivityObserver

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        connectivityObserver = NetworkConnectivityObserver(requireContext())

        lifecycleScope.launch {
            observeConnectivity()
        }

        return root
    }

    private suspend fun observeConnectivity() {
        connectivityObserver.observe().collect { status ->
            if (status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost) {
                showNoNetworkFragment()
            } else {
                hideNoNetworkFragment()
            }
        }
    }

    private fun showNoNetworkFragment() {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.network_offline_fragment_home, NetworkOfflineFragment())
            commit()
        }
        binding.textHome.visibility = View.GONE
    }

    private fun hideNoNetworkFragment() {
        val fragment = childFragmentManager.findFragmentById(R.id.network_offline_fragment_home)

        if (fragment != null) {
            childFragmentManager.beginTransaction().apply {
                remove(fragment)
                commit()
            }
        }

        binding.textHome.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}