package gov.raon.micitt.ui.main

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import gov.raon.micitt.databinding.DialogAuthenticationBinding
import gov.raon.micitt.databinding.DialogCommonBinding

class AuthenticationDialog(context: Context, authCode: String?) :
    Dialog(context, android.R.style.Theme_Translucent) {

    private var binding: DialogAuthenticationBinding = DialogAuthenticationBinding.inflate(layoutInflater)
    private lateinit var listener : ()-> Unit
    private lateinit var testListener : ()-> Unit

    init{
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
//        setCancelable(false)

        binding.tvResultCode.text = authCode

        binding.layerAuth.setOnClickListener {
            listener()
        }

        binding.tvAuthTest.setOnClickListener {
            testListener()
        }

    }

    fun setListener(listener: ()-> Unit) {
        this.listener = listener
    }

    fun setRefreshTime(authCode: String) {
        binding.tvResultCode.text = authCode
        // Refresh Timer
    }
}