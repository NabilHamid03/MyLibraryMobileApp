package he2b.be.mylibrary.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.common.InputImage
import he2b.be.mylibrary.ui.viewmodels.IsbnScannerViewModel

@OptIn(ExperimentalGetImage::class)
@Composable
fun IsbnScannerScreen(
    onBarcodeScanned: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val viewModel: IsbnScannerViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.scanEvents.collect { barcode ->
            onBarcodeScanned(barcode)
        }
    }

    BackHandler(enabled = true) { onBack() }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

    DisposableEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().apply {
            surfaceProvider = previewView.surfaceProvider
        }

        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val executor = ContextCompat.getMainExecutor(context)
        analysis.setAnalyzer(executor) { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees
                )
                viewModel.processImage(image) {
                    imageProxy.close()
                }
            } else {
                imageProxy.close()
            }
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis
            )
        } catch (e: Exception) {
            Log.e("Camera", "Error binding camera", e)
            onBack()
        }

        onDispose {
            cameraProvider.unbindAll()
            viewModel.resetScannerState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}
