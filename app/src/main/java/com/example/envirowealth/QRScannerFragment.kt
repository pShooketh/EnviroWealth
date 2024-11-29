package com.example.envirowealth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Preview
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
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var barcodeScanner: BarcodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q_r_scanner, container, false)
        previewView = view.findViewById(R.id.preview_view)

        // Initialize Barcode Scanner
        barcodeScanner = BarcodeScanning.getClient()

        // Start the camera
        startCamera()

        return view
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // Set up the Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            try {
                // Bind camera and analysis use case
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                    preview
                )
                setupBarcodeAnalysis()
            } catch (exc: Exception) {
                Log.e("QRScannerFragment", "Camera initialization failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setupBarcodeAnalysis() {
        // Continuously analyze the camera feed for QR codes
        previewView.setOnTouchListener { view, motionEvent ->
            // Perform click for accessibility compliance
            if (motionEvent.action == android.view.MotionEvent.ACTION_UP) {
                view.performClick()
            }
            // Add logic here if you want to handle touch events
            false
        }

        // Process frames with ML Kit
        processImage()
    }

    private fun processImage() {
        // Safely retrieve the bitmap from the PreviewView
        val bitmap = previewView.bitmap

        if (bitmap != null) {
            // Create an InputImage using the non-null bitmap
            val image = InputImage.fromBitmap(bitmap, 0)

            // Pass the image to ML Kit for barcode detection
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        handleQRCode(barcode)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("QRScannerFragment", "QR Code detection failed", e)
                }
        } else {
            Log.e("QRScannerFragment", "Failed to retrieve bitmap from PreviewView")
        }
    }

    private fun handleQRCode(barcode: Barcode) {
        val rawValue = barcode.rawValue
        rawValue?.let {
            Toast.makeText(requireContext(), "QR Code Detected: $it", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider.unbindAll()
        barcodeScanner.close()
    }
}
