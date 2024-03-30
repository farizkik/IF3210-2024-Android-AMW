package com.example.bondoman.ui.settings

import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.MainActivity
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.share_preference.PreferenceManager
import com.example.bondoman.ui.login.LoginActivity


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingsViewModel

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        preferenceManager = context?.let { PreferenceManager(it) }!!
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener{
            val intent = Intent("com.example.bondoman.action")
            intent.putExtra("message", "hello")
            requireContext().sendBroadcast(intent)
            Log.d("intent send", "ok")
        }

        binding.logoutButton.setOnClickListener {
            preferenceManager.removePref()
            Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}