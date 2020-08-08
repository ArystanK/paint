package kz.arctan.paint

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var paintSize = 10
    private var color : Int = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val metrics = resources.displayMetrics
        paintView!!.init(metrics)
        pickColor.setOnClickListener {
            ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton("OK",
                    object : ColorEnvelopeListener {
                        override fun onColorSelected(
                            envelope: ColorEnvelope,
                            fromUser: Boolean
                        ) {
                            paintView!!.setColor(envelope.color)
                            pickColor.setBackgroundColor(envelope.color)
                        }
                    })
                .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                .attachBrightnessSlideBar(true) // default is true. If false, do not show the BrightnessSlideBar.
                .show()
        }
        eraser.setOnClickListener {
            color = Color.WHITE
            paintView!!.setColor(color)
        }
        clearAll.setOnClickListener { paintView!!.clear() }
        buttonPlus.setOnClickListener {
            paintSize += 4
            if (paintSize >= 60) {
                paintSize = 60
            }
            paintSizeView.text = paintSize.toString()
            paintView!!.setBrushSize(paintSize.toFloat())
        }
        buttonMinus.setOnClickListener {
            paintSize -= 4
            if (paintSize < 1) {
                paintSize = 1
            }
            paintSizeView.text = paintSize.toString()
            paintView!!.setBrushSize(paintSize.toFloat())
        }
    }
}
