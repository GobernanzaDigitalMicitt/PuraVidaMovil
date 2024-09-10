package gov.raon.micitt.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import gov.raon.micitt.databinding.ActivitySettingBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.ui.main.MainActivity

class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.prevRl.visibility = View.VISIBLE
        binding.header.prev.setOnClickListener {
            finish()
        }

        binding.settingLogout.setOnClickListener {
            getDialogBuilder {
                it.title("Do you want to Logout?")
                it.message("Even after you logout, you can verify the certificates issued by\nlogging in with the same nID.")
                it.btnConfirm("Logout")
                it.btnCancel("Cancel")

                showDialog(it) { result, _ ->
                    if (result) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        finish()
                        startActivity(intent)
                        Runtime.getRuntime().exit(0)
                    }
                }
            }
        }

        binding.settingNoticeMore.setOnClickListener {
            Intent(this, NoticeActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }

        binding.settingRevokeService.setOnClickListener {
            getDialogBuilder {
                Intent(this, SettingUnsubscribeActivity::class.java).also { intent ->
                    startActivity(intent)
                }
            }
        }
    }
}