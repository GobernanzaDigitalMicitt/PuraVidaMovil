package gov.raon.micitt.settings

import android.os.Bundle
import android.view.View
import gov.raon.micitt.databinding.ActivitySettingUnsubscribeBinding
import gov.raon.micitt.di.common.BaseActivity

class SettingUnsubscribeActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingUnsubscribeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingUnsubscribeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.prev.visibility = View.VISIBLE
        binding.header.prev.setOnClickListener {
            finish()
        }

        binding.unsubCheckboxLl.setOnClickListener {
            changeCBstatus();
        }

        binding.unsubBtn.btnConfirm.visibility = View.GONE
        binding.unsubBtn.btnCancel.text = "Desactivación de servicio"
        binding.unsubBtn.btnConfirm.text = "Desactivación de servicio"



        binding.unsubBtn.btnConfirm.setOnClickListener {
            if(binding.unsubCheckbox.isChecked){
                getDialogBuilder {
                    it.title("Eliminación de servicio completada")
                    it.message("Tu eliminación de servicio ha sido completada. Si deseas utilizar el servicio nuevamente, por favor regístrate de nuevo.")
                    it.btnConfirm("Aceptar")

                    showDialog(it){ result, obj ->
                        if(result){
                            changeCBstatus()
                            // TODO 앱 서비스 해지 진행
                        }
                    }
                }
            }
        }
    }

    private fun changeCBstatus(){
        binding.unsubCheckbox.isChecked = !binding.unsubCheckbox.isChecked

        if(binding.unsubCheckbox.isChecked){
            binding.unsubBtn.btnCancel.visibility = View.GONE
            binding.unsubBtn.btnConfirm.visibility = View.VISIBLE
        } else {
            binding.unsubBtn.btnConfirm.visibility = View.GONE
            binding.unsubBtn.btnCancel.visibility = View.VISIBLE

        }
    }
}