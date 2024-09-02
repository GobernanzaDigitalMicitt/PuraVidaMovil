package gov.raon.micitt.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivityMainBinding
import gov.raon.micitt.models.SignUpModel
import gov.raon.micitt.settings.SettingActivity
import gov.raon.micitt.ui.settings.NoticeActivity
import gov.raon.micitt.utils.Log
import gov.raon.micitt.utils.Util


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObservers()

    }
    private fun initView() {
        binding.tvSignup.setOnClickListener {
            val nId = binding.etNid.text.toString()

            val signUpModel = SignUpModel(Util.hashSHA256(nId).toString(), nId)

            if(nId!!.isNotEmpty()) {
                mainViewModel.signUp(this, signUpModel)

            } else {
                Toast.makeText(this, "nId is Empty", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvSignin.setOnClickListener {
            Intent(this, SettingActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun initObservers() {

    }
}