package com.example.envirowealth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var pointsTextView: TextView
    private lateinit var rewardsButton: Button  // Declare the button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the TextView for points display
        pointsTextView = view.findViewById(R.id.pointsTextView)

        // Find the Rewards Button
        rewardsButton = view.findViewById(R.id.rewards_button)

        // Set up button click listener to navigate to RewardsFragment
        rewardsButton.setOnClickListener {
            // Navigate to the RewardsFragment
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, RewardsFragment())
            transaction.addToBackStack(null)  // Allows the user to navigate back
            transaction.commit()
        }

        // Update the points display
        updatePointsDisplay()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Update points whenever the fragment is resumed
        updatePointsDisplay()
    }

    private fun updatePointsDisplay() {
        // Get the current points from PointsManager
        val currentPoints = PointsManager.getPoints(requireContext())

        // Update the TextView with the current points
        pointsTextView.text = "Points: $currentPoints"
    }
}
