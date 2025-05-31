package he2b.be.mylibrary.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class IsbnScannerViewModel : ViewModel() {
    private val _scanEvents = Channel<String>(Channel.CONFLATED)
    val scanEvents = _scanEvents.receiveAsFlow()

    private val barcodeScanner: BarcodeScanner by lazy { BarcodeScanning.getClient() }
    private var hasScanned = false
        private set

    fun processImage(image: InputImage, onComplete: () -> Unit) {
        if (hasScanned) {
            onComplete()
            return
        }

        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    val found = barcodes.firstOrNull {
                        it.valueType == Barcode.TYPE_ISBN || it.rawValue != null
                    }
                    found?.rawValue?.let { barcodeValue ->
                        hasScanned = true
                        viewModelScope.launch {
                            _scanEvents.send(barcodeValue)
                        }
                    }
                }
                onComplete()
            }
            .addOnFailureListener {
                onComplete()
            }
    }

    fun resetScannerState() {
        hasScanned = false
    }
}
