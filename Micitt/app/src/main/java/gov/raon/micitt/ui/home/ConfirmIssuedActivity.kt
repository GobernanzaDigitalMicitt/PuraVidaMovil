package gov.raon.micitt.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import gov.raon.micitt.databinding.ActivityVcIssueCompleteBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.utils.Log
import gov.raon.micitt.utils.Util

class ConfirmIssuedActivity : BaseActivity() {
    private lateinit var binding : ActivityVcIssueCompleteBinding

    private lateinit var agencyCode : String
    private lateinit var type : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVcIssueCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        Log.d("DUKE","CONFIRMED ACTIVITY INIT VIEW")
        binding.header.prevRl.visibility = View.VISIBLE
        binding.header.prev.setOnClickListener {
            finish()
        }

        binding.vcCompleteBtn.btnCancel.visibility = View.GONE
        binding.vcCompleteBtn.btnConfirm.text = "Aceptar"
        binding.vcIssuedSample.tvDate.text = Util.getCurrentDate()

        binding.vcCompleteBtn.btnConfirm.setOnClickListener {
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

}