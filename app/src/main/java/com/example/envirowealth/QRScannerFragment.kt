package com.example.envirowealth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView // Add this import
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class QRScannerFragment : Fragment() {

    private lateinit var previewView: PreviewView
    private lateinit var pointsTextView: TextView // Reference to the points TextView
    private var cameraProvider: ProcessCameraProvider? = null // Make cameraProvider nullable
    private lateinit var barcodeScanner: BarcodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q_r_scanner, container, false)
        previewView = view.findViewById(R.id.preview_view)
        pointsTextView = view.findViewById(R.id.pointsTextView) // Initialize points TextView

        // Initialize Barcode Scanner
        barcodeScanner = BarcodeScanning.getClient()

        // Start the camera only if the fragment is attached
        if (isAdded) {
            startCamera()
        }

        return view
    }

    private fun startCamera() {
        // Check if fragment is still attached to the activity
        if (!isAdded) return

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // Ensure that we can access the context and lifecycle
            if (isAdded) {
                val preview = androidx.camera.core.Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), ImageAnalysis.Analyzer { image ->
                            analyzeImage(image)
                        })
                    }

                try {
                    cameraProvider?.unbindAll() // Null check to avoid issues if cameraProvider is null
                    cameraProvider?.bindToLifecycle(
                        viewLifecycleOwner,
                        androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    Log.e("QRScannerFragment", "Camera initialization failed", exc)
                }
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun analyzeImage(imageProxy: ImageProxy) {
        try {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                barcodeScanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            handleQRCode(barcode)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("QRScannerFragment", "QR Code detection failed", e)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                Log.e("QRScannerFragment", "No media image available for analysis.")
                imageProxy.close()
            }
        } catch (e: Exception) {
            Log.e("QRScannerFragment", "Error analyzing image", e)
            imageProxy.close()
        }
    }

    private fun handleQRCode(barcode: Barcode) {
        val rawValue = barcode.rawValue
        rawValue?.let {
            // Display QR Code value in a Toast message
            Toast.makeText(requireContext(), "QR Code Detected: $it", Toast.LENGTH_SHORT).show()
            Log.d("QRScannerFragment", "QR Code Value: $it")

            // Add points (10 points per scan for now)
            val pointsToAdd = 10
            PointsManager.addPoints(requireContext(), pointsToAdd)

            // Update the points display
            updatePointsDisplay()
        }
    }

    private fun updatePointsDisplay() {
        // Get the current points from PointsManager
        val currentPoints = PointsManager.getPoints(requireContext())

        // Update the TextView to show the current points
        pointsTextView.text = "Points: $currentPoints"
        Log.d("QRScannerFragment", "Current Points: $currentPoints")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Only unbind the camera and close the barcode scanner if the fragment is attached
        if (isAdded) {
            cameraProvider?.unbindAll() // Null check for cameraProvider
            barcodeScanner.close() // Close barcode scanner safely
        }
    }
}



