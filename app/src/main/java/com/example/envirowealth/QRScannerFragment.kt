package com.example.envirowealth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), { image ->
                        analyzeImage(image)
                    })
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("QRScannerFragment", "Camera initialization failed", exc)
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
            Toast.makeText(requireContext(), "QR Code Detected: $it", Toast.LENGTH_SHORT).show()
            Log.d("QRScannerFragment", "QR Code Value: $it")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider.unbindAll()
        barcodeScanner.close()
    }
}
