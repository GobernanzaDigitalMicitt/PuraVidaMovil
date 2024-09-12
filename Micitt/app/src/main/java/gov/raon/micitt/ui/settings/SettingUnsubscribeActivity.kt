package gov.raon.micitt.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivitySettingUnsubscribeBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.ui.main.MainActivity

// TODO 서버 API 연동해서 탈퇴 진행해야함

@AndroidEntryPoint
class SettingUnsubscribeActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingUnsubscribeBinding
    private val viewModel: NotiViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        binding = ActivitySettingUnsubscribeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.prevRl.visibility = View.VISIBLE
        binding.header.prev.setOnClickListener {
            finish()
        }

        binding.unsubCheckboxLl.setOnClickListener {
            binding.unsubCheckbox.isChecked = !binding.unsubCheckbox.isChecked
            changeCBstatus();
        }
        binding.unsubCheckbox.setOnClickListener {
            changeCBstatus();
        }

        binding.unsubBtn.btnConfirm.visibility = View.GONE
        binding.unsubBtn.btnCancel.text = "Desactivación de servicio"
        binding.unsubBtn.btnConfirm.text = "Desactivación de servicio"



        binding.unsubBtn.btnConfirm.setOnClickListener {
            if (binding.unsubCheckbox.isChecked) {
                getDialogBuilder {
                    it.title("Eliminación de servicio completada")
                    it.message("Tu eliminación de servicio ha sido completada. Si deseas utilizar el servicio nuevamente, por favor regístrate de nuevo.")
                    it.btnConfirm("Aceptar")
                    showDialog(it) { result, obj ->
                        if (result) {
                            val hashToken = sharedPreferences.getString("hashedToken", "null")
                            viewModel.withdraw<JsonObject>(this, hashToken!!)

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            finish()
                            startActivity(intent)
                            Runtime.getRuntime().exit(0)

                        }
                    }
                }
            }
        }
    }

    private fun changeCBstatus() {
        if (binding.unsubCheckbox.isChecked) {
            binding.unsubBtn.btnCancel.visibility = View.GONE
            binding.unsubBtn.btnConfirm.visibility = View.VISIBLE
        } else {
            binding.unsubBtn.btnConfirm.visibility = View.GONE
            binding.unsubBtn.btnCancel.visibility = View.VISIBLE

        }
    }
}