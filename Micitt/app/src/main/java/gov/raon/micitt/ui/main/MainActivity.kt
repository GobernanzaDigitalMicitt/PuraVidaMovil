package gov.raon.micitt.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
    private lateinit var authDialog : AuthenticationDialog

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

        binding.tvSignin.btnConfirm.visibility = View.GONE
        binding.tvSignin.btnConfirm.text = "Iniciar sesión"
        binding.tvSignin.btnCancel.text = "Iniciar sesión"
        binding.etNid.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                if (input.length > 9) {
                    binding.tvSignin.btnConfirm.visibility = View.VISIBLE
                    binding.tvSignin.btnCancel.visibility = View.GONE
                } else {
                    binding.tvSignin.btnConfirm.visibility = View.GONE
                    binding.tvSignin.btnCancel.visibility = View.VISIBLE
                }
            }
        })

        binding.tvSignin.btnCancel.setOnClickListener {
            Toast.makeText(this,"Por favor introduzca al menos 9 dígitos",Toast.LENGTH_SHORT).show()
        }
        binding.tvSignin.btnConfirm.setOnClickListener {
            handleSignIn()
        }
    }

    private fun handleSignUp() {
        val nId = binding.etNid.text.toString()

        if(nId.length<10 || nId.isEmpty()){
            showToast("nId debería ser 9 al menos dígitos")
            return
        }

        if (nId.isNotEmpty()) {
            getDialogBuilder {
                it.title("Requiere autenticación GAUDI")
                it.message("Después de completar la autenticación en la app GAUDI, presiona el botón de autenticación completada.")
                it.btnConfirm("Autenticación")
                it.btnCancel("Cancelar")

                showDialog(it){result,_->
                    if(result){
                        val signModel = SignModel(Util.hashSHA256(nId).toString(), nId)
                        showProgress()
                        mainViewModel.reqSignUp(this, signModel)
                    }
                }
            }
        }
    }

    private fun handleSignIn() {
        nId = binding.etNid.text.toString()
        if(nId!!.length < 9){
            showToast("Por favor introduzca al menos 9 dígitos")
            return
        }
        if (!nId.isNullOrEmpty()) {
            val signModel = SignModel(Util.hashSHA256(nId!!).toString(), null)
            showProgress()
            mainViewModel.reqSignIn(this, signModel)
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
            navigateToHome(it.resultData.hashedToken, it.resultData.userName)
        }

        mainViewModel.liveCheckSignUpStatus.observe(this) {
            hideProgress()
            authDialog.dismiss()
            showToast("Registro Completo")
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

        authDialog = AuthenticationDialog(this, response.resultData.verificationCode).apply {
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

    private fun navigateToHome(hashedToken: String, userName: String) {
        hideProgress()
        authDialog.dismiss()
        editor.putString("nid",nId)
        editor.putString("hashedToken", hashedToken)
        editor.putString("userName",userName)
        editor.apply()
        Intent(this, HomeActivity::class.java).also { intent ->
            intent.putExtra("hashedNid", Util.hashSHA256(nId!!))
            intent.putExtra("hashedToken", hashedToken)
            startActivity(intent)
            finish()
        }
    }

    private fun handleError(errorMessage: String) {
        Log.d("ERROR :: $errorMessage")
        hideProgress()
        showToast(errorMessage)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        getDialogBuilder { it ->
            it.title("Logout")
            it.message("Quieres cerrar sesión en la aplicación?")
            it.btnConfirm("Sí")
            it.btnCancel("No")
            showDialog(it) { result, obj ->
                if (result) {
                    this.moveTaskToBack(true)
                    this.finishAndRemoveTask()
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            }
        }
    }

}
