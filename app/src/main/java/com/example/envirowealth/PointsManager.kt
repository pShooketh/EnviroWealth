package com.example.envirowealth

import android.content.Context
import android.content.SharedPreferences

object PointsManager {

    private const val PREF_NAME = "enviro_wealth_prefs"
    private const val KEY_POINTS = "points"

    // Function to get points
    fun getPoints(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_POINTS, 0) // Default to 0 if no points are stored
    }

    // Function to add points
    fun addPoints(context: Context, points: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentPoints = getPoints(context)
        val newPoints = currentPoints + points
        prefs.edit().putInt(KEY_POINTS, newPoints).apply()
    }
}
