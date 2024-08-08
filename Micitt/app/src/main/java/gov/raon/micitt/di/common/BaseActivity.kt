package gov.raon.micitt.di.common

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    fun showDialog(builder: MDialog.Builder, listener: (result: Boolean, obj: Any?) -> Unit) : Dialog {
        val dialog = builder.build()

        runOnUiThread {
            dialog.setListener{ result, obj->
                dialog.dismiss()
                listener(result, obj)
            }
            if(!isDestroyed) dialog.show()
        }

        return dialog
    }

    fun getDialogBuilder(listener: (MDialog.Builder)-> Unit) = runOnUiThread {
        val builder = MDialog.Builder(this)
        listener(builder)
    }

}