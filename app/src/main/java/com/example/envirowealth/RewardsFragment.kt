package com.example.envirowealth

import android.os.Bundle
import android.util.Log // Import for logging
import androidx.fragment.app.Fragment
import com.example.envirowealth.databinding.FragmentRewardsBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RewardsFragment : Fragment(R.layout.fragment_rewards) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentRewardsBinding.inflate(inflater, container, false)

        // Log message for debugging
        Log.d("RewardsFragment", "Fragment View Created")

        // No button handling logic required here anymore

        return binding.root
    }
}
