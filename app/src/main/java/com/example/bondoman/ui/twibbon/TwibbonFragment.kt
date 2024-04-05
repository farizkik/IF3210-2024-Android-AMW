package com.example.bondoman.ui.twibbon

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentTwibbonBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TwibbonFragment : Fragment() {

    private lateinit var viewModel: TwibbonViewModel

    private var _binding: FragmentTwibbonBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null

    private var isCapture = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(TwibbonViewModel::class.java)

        _binding = FragmentTwibbonBinding.inflate(inflater, container, false)

        return binding.root
    }
    private fun allPermisionsGranted() = TwibbonFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle Permission granted/rejected
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in TwibbonFragment.REQUIRED_PERMISSIONS && !it.value) {
                permissionGranted = false
            }
        }
        if (!permissionGranted) {
            Toast.makeText(
                requireContext(),
                "Permission request denied",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            startCamera()
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermisionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }


        binding.captureButton.setOnClickListener {
            Log.d("Twibbon Fragment", isCapture.toString())
            if (isCapture) {
                binding.imageOverview.visibility = View.GONE
                startCamera()
                isCapture = false
            } else {
                takePhoto()
                isCapture = true
            }
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use cases
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStory entry
        val name = SimpleDateFormat(TwibbonFragment.FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                requireContext().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.e("Twibbon Fragment", "Photo capture failed: ${exception.message}", exception)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo captured successfully: ${outputFileResults.savedUri}"
                    Log.d("Twibbon Fragment", msg)
                    displayCapturedImage(outputFileResults.savedUri!!)
                }
            }
        )
    }

    private fun displayCapturedImage(imageUri: Uri) {
        // Show captured image in imageOverview with twibbon overlay
        binding.imageOverview.setImageURI(imageUri)
        binding.imageOverview.visibility = View.VISIBLE
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(TwibbonFragment.REQUIRED_PERMISSIONS)
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val twibbonBitmap = BitmapFactory.decodeResource(resources, R.drawable.twibon_pbd)

            binding.twibbonOverlay.setImageBitmap(twibbonBitmap)
            binding.twibbonOverlay.visibility = View.VISIBLE

            // Select back camera as default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exception: Exception) {
                Log.e("Twibbon Fragment", "Use case binding failed", exception)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

}