package gov.raon.micitt.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.BuildConfig
import gov.raon.micitt.databinding.ActivitySettingBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.ui.main.MainActivity
import gov.raon.micitt.utils.Log


@AndroidEntryPoint
class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: NotiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.prevRl.visibility = View.VISIBLE
        binding.header.prevRl.setOnClickListener {
            finish()
        }

        binding.settingLogout.setOnClickListener {
            getDialogBuilder {
                it.title("Deseas cerrar sesión?")
                it.message("Incluso después de cerrar sesión, puedes verificar los certificados emitidos iniciando sesión con el mismo nID.")
                it.btnConfirm("Cerrar sesión")
                it.btnCancel("Cancelar")

                showDialog(it) { result, _ ->
                    if (result) {
                        showProgress()
                        val hashToken = sharedPreferences.getString("hashedToken", "null")
                        viewModel.logOut<JsonObject>(this, hashToken!!)

                        viewModel.logoutLiveList.observe(this) { it ->
                            if (it.resultCode == "000") {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                finish()
                                startActivity(intent)
                                Runtime.getRuntime().exit(0)
                            } else {
                                checkSession(this,it.resultCode)
                            }
                        }
                    }
                }
            }
        }

        viewModel.liveErrorData.observe(this){
            checkSession(this,it.resultCode)
        }

        binding.settingPrivacy.setOnClickListener{
            Toast.makeText(this, "TOBE DEVELOPED", Toast.LENGTH_SHORT).show()
        }
        binding.settingCondition.setOnClickListener{
            Toast.makeText(this, "TOBE DEVELOPED", Toast.LENGTH_SHORT).show()
        }

        binding.settingRevokeService.setOnClickListener {
            getDialogBuilder {
                Intent(this, SettingUnsubscribeActivity::class.java).also { intent ->
                    startActivity(intent)
                }
            }
        }

        val nid = sharedPreferences.getString("nid","null")
        binding.infoNidString.text = nid

        val userName = sharedPreferences.getString("userName","null")
        binding.infoNameString.text=userName

        val version = "v.${BuildConfig.VERSION_NAME}"
        binding.settingGuideAppVersion.text = version
    }
}