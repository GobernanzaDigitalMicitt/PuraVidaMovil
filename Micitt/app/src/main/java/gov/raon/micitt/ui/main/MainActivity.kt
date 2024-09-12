package gov.raon.micitt.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivityMainBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.models.CheckAuthModel
import gov.raon.micitt.models.SignModel
import gov.raon.micitt.models.response.SignRes
import gov.raon.micitt.ui.home.HomeActivity
import gov.raon.micitt.utils.Log
import gov.raon.micitt.utils.Util


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var nId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObservers()
    }

    private fun initView() {
        binding.tvSignup.setOnClickListener {
            handleSignUp()
        }

        binding.tvSignin.setOnClickListener {
            handleSignIn()
        }
    }

    private fun handleSignUp() {
        val nId = binding.etNid.text.toString()

        if(nId.length<10){
            showToast("nId should be  10 digits")
            return
        }

        if (nId.isNotEmpty()) {
            val signModel = SignModel(Util.hashSHA256(nId).toString(), nId)
            showProgress()
            mainViewModel.reqSignUp(this, signModel)
        } else {
            showToast("nId is Empty")
        }
    }

    private fun handleSignIn() {
        nId = binding.etNid.text.toString()
        if(nId!!.length<10){
            showToast("nId should be  10 digits")
            return
        }
        if (!nId.isNullOrEmpty()) {
            val signModel = SignModel(Util.hashSHA256(nId!!).toString(), null)
            showProgress()
            mainViewModel.reqSignIn(this, signModel)
        } else {
            showToast("nId is Empty")
        }
    }

    private fun initObservers() {
        mainViewModel.liveSignUpResponse.observe(this) { response ->
            handleAuthResponse(response, isSignUp = true)
        }

        mainViewModel.liveSignInResponse.observe(this) { response ->
            handleAuthResponse(response, isSignUp = false)
        }

        mainViewModel.liveCheckSignInStatus.observe(this) {
            navigateToHome(it.resultData.hashedToken)
        }

        mainViewModel.liveCheckSignUpStatus.observe(this) {
            hideProgress()
            showToast("SignUp Complete")
        }

        mainViewModel.liveSignErrorResponse.observe(this) {
            handleError(it)
        }

        mainViewModel.liveCheckAuthErrorResponse.observe(this) {
            handleError(it)
        }
    }

    private fun handleAuthResponse(response: SignRes, isSignUp: Boolean) {
        hideProgress()

        val authDialog = AuthenticationDialog(this, response.resultData.verificationCode).apply {
            setListener {
                showProgress()
                val checkAuthModel = CheckAuthModel(response.resultData.requestId)

                if (isSignUp) {
                    mainViewModel.reqCheckSignUpStatus(checkAuthModel)
                } else {
                    mainViewModel.reqSignInStatus(checkAuthModel)
                }
            }
        }

        authDialog.show()
    }

    private fun navigateToHome(hashedToken: String) {
        hideProgress()
        editor.putString("nid",nId)
        editor.putString("hashedToken", hashedToken)
        editor.apply()
        Intent(this, HomeActivity::class.java).also { intent ->
            Log.d("oykwon", "navi : " + hashedToken)

            intent.putExtra("hashedNid", Util.hashSHA256(nId!!))
            intent.putExtra("hashedToken", hashedToken)
            startActivity(intent)
        }
    }

    private fun handleError(errorMessage: String) {
        hideProgress()
        showToast(errorMessage)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
