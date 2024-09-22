package gov.raon.micitt.di.common

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import gov.raon.micitt.R
import gov.raon.micitt.ui.main.MainActivity

open class BaseActivity : AppCompatActivity() {

    private var progress: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        progress = ProgressDialog(this)
    }

    fun showDialog(
        builder: MDialog.Builder,
        listener: (result: Boolean, obj: Any?) -> Unit
    ): Dialog {
        val dialog = builder.build()

        runOnUiThread {
            dialog.setListener { result, obj ->
                dialog.dismiss()
                listener(result, obj)
            }
            if (!isDestroyed) dialog.show()
        }

        return dialog
    }

    fun getDialogBuilder(listener: (MDialog.Builder) -> Unit) = runOnUiThread {
        val builder = MDialog.Builder(this)
        listener(builder)
    }

    fun isProgress(): Boolean {
        return progress != null && progress!!.isShowing
    }

    fun showProgress() = runOnUiThread {
        showProgress(null)
    }

    fun showProgress(listener: (() -> Unit)?) = runOnUiThread {
        if (progress == null) {
            progress = ProgressDialog(this)
        }

        progress!!.setOnShowListener {
            try {
                listener!!()
            } catch (e: Exception) {
            }
        }

        if (!isDestroyed) progress!!.show()
    }

    fun hideProgress() = runOnUiThread {
        if (progress != null && progress!!.isShowing) {
            progress!!.dismiss()
        }
    }

    fun capturePrevention() {
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    fun checkSession(ctx: Context, resultCode: String) {
        val isSessionValid = resultCode != "902" && resultCode != "903"
        if (!isSessionValid) {
            Toast.makeText(ctx, "Session Ended Please Log-in again", Toast.LENGTH_SHORT).show()
            Intent(ctx, MainActivity::class.java).also { act ->
                startActivity(act)
                finish()
            }
        }
    }
}