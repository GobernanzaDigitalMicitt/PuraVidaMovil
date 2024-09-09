package gov.raon.micitt.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivityHomeBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.models.AgencyModel
import gov.raon.micitt.models.CheckAuthModel
import gov.raon.micitt.models.CheckDocumentModel
import gov.raon.micitt.models.DocumentModel
import gov.raon.micitt.models.SignDocumentModel
import gov.raon.micitt.models.response.AgencyInfo
import gov.raon.micitt.ui.main.AuthenticationDialog
import gov.raon.micitt.utils.Log
import gov.raon.micitt.utils.Util


@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private var hashedNid: String? = null
    private var hashedToken: String? = null
    private var eDocDataType: String? = null

    private var agencyAdapter: AgencyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hashedNid = intent.getStringExtra("hashedNid")
        hashedToken = intent.getStringExtra("hashedToken")

        initView()
        initObservers()
    }

    private fun initView() {

        binding.layerMiCertifi.setOnClickListener {
            binding.layerCertifiEmpty.visibility = View.VISIBLE
            binding.layerCertifi.visibility = View.GONE
            binding.viewMiCerti.visibility = View.VISIBLE
            binding.viewSoliCerti.visibility = View.GONE
        }

        binding.layerSoliCertifi.setOnClickListener {
            binding.layerCertifiEmpty.visibility = View.GONE
            binding.layerCertifi.visibility = View.VISIBLE
            binding.viewMiCerti.visibility = View.GONE
            binding.viewSoliCerti.visibility = View.VISIBLE

            val agencyModel = AgencyModel("all")
            homeViewModel.getAgencyList(agencyModel)
        }

        binding.layerCertifiEmpty.setOnClickListener {
            binding.layerCertifiEmpty.visibility = View.GONE
            binding.layerCertifi.visibility = View.VISIBLE

            val agencyModel = AgencyModel("all")
            homeViewModel.getAgencyList(agencyModel)
        }

    }

    private fun initObservers() {
        homeViewModel.liveAgencyList.observe(this) {
            if (agencyAdapter == null) {
                Log.d("oykwon", "Create Adapter : " + it.size)
                it.reverse()
                setList(it)
            } else {
                agencyAdapter!!.clear()
                it.reverse()
                agencyAdapter!!.addList(it)
            }
        }

        homeViewModel.liveDocument.observe(this) {
            Log.d("oykwon", "eDoc : " + it.resultData.eDoc)
            Log.d("oykwon", "eDoc : " + Util.base64UrlDecode(it.resultData.eDoc))

            if (it.resultCode == "000") {
                val signDocumentModel = SignDocumentModel(
                    hashedToken!!,
                    it.resultData.eDoc, eDocDataType!!
                )

                homeViewModel.signDocument(signDocumentModel)

            } else {
                Log.d("oykwon", "Error Document")
                hideProgress()
            }
        }

        homeViewModel.liveSignDocument.observe(this) {
            val authenticationDialog = AuthenticationDialog(this, it.resultData.verificationCode)
            authenticationDialog.setListener {
                showProgress()

                homeViewModel.checkDocumentStatus(CheckDocumentModel(hashedToken!!, it.resultData.requestId))
            }
            authenticationDialog.show()
        }
    }

    private fun setList(agencyInfos: MutableList<AgencyInfo>) {
        agencyAdapter = AgencyAdapter(this, agencyInfos)
        binding.list.adapter = this@HomeActivity.agencyAdapter

        agencyAdapter!!.setEmitirListener { item ->
            getDialogBuilder {
                it.title("발급")
                it.message("발급받으시겠습니까?")
                it.btnConfirm("발급")
                it.btnCancel("취소")

                showDialog(it) { result, _ ->
                    if (result) {
                        showProgress()

                        getDocument(item)
                    }
                }
            }
        }
    }

    private fun getDocument(item: AgencyInfo) {

        // 추후에 UI 변경 되어야함..

        var documentModel = DocumentModel(
            hashedToken!!,
            item.agencyCode,
            "TSE",
            "XML",
            "TAX"
        )

        eDocDataType = documentModel.dataType

        homeViewModel.getDocument(documentModel)
    }
}