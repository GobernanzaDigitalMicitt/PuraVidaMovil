package gov.raon.micitt.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import gov.raon.micitt.databinding.ActivityVcIssueCompleteBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.utils.Util

class ConfirmIssuedActivity : BaseActivity() {

    private lateinit var binding : ActivityVcIssueCompleteBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        binding = ActivityVcIssueCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

        binding.vcCompleteNid
        binding.vcCompleteName
        binding.vcCompleteIssuedDate

        binding.header.prevRl.visibility = View.VISIBLE
        binding.header.prevRl.setOnClickListener {
            finish()
        }

        binding.vcCompleteBtn.btnCancel.visibility = View.GONE
        binding.vcCompleteBtn.btnConfirm.text = "Aceptar"

        /* vc detail */
        val date = Util.getCurrentDate()
        binding.vcIssuedSample.btnDelete.visibility = View.GONE
        binding.vcIssuedSample.tvName.text = intent.getStringExtra("agencyName")
        binding.vcIssuedSample.tvDate.text = date
        binding.vcIssuedSample.tvType.text = intent.getStringExtra("dataFormat")

        binding.vcCompleteNid.text = sharedPreferences.getString("nid","null")
        binding.vcCompleteName.text = sharedPreferences.getString("name","null")
        binding.vcCompleteIssuedDate.text = date


        binding.vcCompleteBtn.btnConfirm.setOnClickListener {
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

}