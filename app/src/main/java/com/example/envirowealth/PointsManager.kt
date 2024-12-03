package com.example.envirowealth

import android.content.Context
import android.content.SharedPreferences

object PointsManager {

    private const val PREF_NAME = "enviro_wealth_prefs"
    private const val KEY_POINTS = "points"
    private const val KEY_SCANNED_CODES = "scanned_codes"

    // Function to get points
    fun getPoints(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_POINTS, 0) // Default to 0 if no points are stored
    }

    // Function to add points (if the code has not been scanned before)
    fun addPointsIfNotScanned(context: Context, code: String, points: Int): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val scannedCodes = prefs.getStringSet(KEY_SCANNED_CODES, mutableSetOf()) ?: mutableSetOf()

        return if (scannedCodes.contains(code)) {
            // Item has already been scanned
            false
        } else {
            // Add the code to scanned set
            scannedCodes.add(code)
            prefs.edit()
                .putStringSet(KEY_SCANNED_CODES, scannedCodes)
                .apply()

            // Update the points
            val currentPoints = getPoints(context)
            prefs.edit()
                .putInt(KEY_POINTS, currentPoints + points)
                .apply()

            true
        }
    }

    // Function to check if an item has already been scanned
    fun isScanned(context: Context, code: String): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val scannedCodes = prefs.getStringSet(KEY_SCANNED_CODES, mutableSetOf()) ?: mutableSetOf()
        return scannedCodes.contains(code)
    }
}