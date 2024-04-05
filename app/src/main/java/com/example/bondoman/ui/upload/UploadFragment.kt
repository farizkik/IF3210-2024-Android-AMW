package com.example.bondoman.ui.upload

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.bondoman.core.data.Item
import com.example.bondoman.core.data.ParcelableItem
import com.example.bondoman.databinding.FragmentHomeBinding
import com.example.bondoman.share_preference.PreferenceManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.FileOutputStream

class UploadFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager

    private lateinit var viewModel: UploadViewModel

    private lateinit var file: File

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle Permission granted/rejected
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in REQUIRED_PERMISSIONS && !it.value) {
                permissionGranted = false
            }
        }
        if (!permissionGranted) {
            Toast.makeText(
                requireContext(), "Permission request denied", Toast.LENGTH_SHORT
            ).show()
        } else {
            startCamera()
        }
    }

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            Log.d("Upload Fragment", "Selected uri: $uri")

            val inputStream = requireContext().contentResolver.openInputStream(uri)

            file = File.createTempFile("TEMP_FILE_", ".jpg", requireContext().cacheDir)
            val outputStream = FileOutputStream(file)

            inputStream!!.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            val mediaType = "image/jpeg"

            val part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                file.asRequestBody(mediaType.toMediaType())
            )

            val token = preferenceManager.getToken()

            viewModel.upload(token, part)
        } else {
            Log.d("Upload Fragment", "No media selected")
        }
    }

    private fun goToUploadResultFragment() {
        val items: Array<Item> = viewModel.items.value!!.items.toTypedArray()
        val parcelableItem: Array<ParcelableItem> = items.map {
            ParcelableItem(it.name, it.qty, it.price)
        }.toTypedArray()
        val action = UploadFragmentDirections.actionNavigationHomeToResultFragment(parcelableItem)
        Navigation.findNavController(binding.cameraView).navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permissions
        if (allPermisionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners to pick image
        binding.scanImagePickerButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Set up the listeners to take photo
        binding.imageCaptureButton.setOnClickListener {
            takePhoto()

            val mediaType = "image/jpeg"

            val part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                file.asRequestBody(mediaType.toMediaType())
            )

            val token = preferenceManager.getToken()

            viewModel.upload(token, part)
        }

        viewModel.uploadResponse.observe(viewLifecycleOwner) { res ->
            Log.d("Upload Fragment", res.toString())
            viewModel.setItems(res)

            goToUploadResultFragment()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { res ->
            Log.d("Upload Fragment", res.toString())

            if (res.toString() == "Unauthorized") {
                Toast.makeText(
                    requireContext(), "Token has expired, please log in again", Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(), "Server unreachable, please try again", Toast.LENGTH_LONG
                ).show()
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use cases
        val imageCapture = imageCapture ?: return

        // Create output options object which contains file + metadata
        file = File.createTempFile("TEMP_FILE_", ".jpg", requireContext().cacheDir)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.e(
                        "Upload Fragment",
                        "Photo capture failed: ${exception.message}",
                        exception
                    )
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("Upload Fragment", file.name)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraView.surfaceProvider)
            }

            imageCapture =
                ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

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
                Log.e("Upload Fragment", "Use case binding failed", exception)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermisionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).apply {
            add(
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                } else {
                    Manifest.permission.READ_MEDIA_IMAGES
                }
            )
        }.toTypedArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        preferenceManager = PreferenceManager(requireContext())

        viewModel = ViewModelProvider(this).get(UploadViewModel::class.java)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}