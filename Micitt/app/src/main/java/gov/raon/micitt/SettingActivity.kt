package gov.raon.micitt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import gov.raon.micitt.databinding.SettingMainBinding
import gov.raon.micitt.di.common.BaseActivity

class SettingActivity : BaseActivity() {
    private lateinit var binding: SettingMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.prev.visibility = View.VISIBLE
        binding.header.prev.setOnClickListener {
            // TODO
        }

        binding.settingLogout.setOnClickListener {
            getDialogBuilder {
                it.title("Do you want to Logout?")
                it.message("Even after you logout, you can verify the certificates issued by\nlogging in with the same nID.")
                it.btnConfirm("Logout")

                showDialog(it) { result, obj ->
                    if (result) {
                        // TODO
                    }
                }
            }
        }

        binding.settingNoticeMore.setOnClickListener {
            // TODO
        }

        binding.settingRevokeService.setOnClickListener {
            getDialogBuilder {
                // TODO
            }
        }

    }
}