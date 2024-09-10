package gov.raon.micitt.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivityHomeBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.models.AgencyModel
import gov.raon.micitt.models.CheckDocumentModel
import gov.raon.micitt.models.DocumentModel
import gov.raon.micitt.models.SignDocumentModel
import gov.raon.micitt.models.response.AgencyInfo
import gov.raon.micitt.models.xmlDataModel
import gov.raon.micitt.ui.main.AuthenticationDialog
import gov.raon.micitt.ui.settings.SettingActivity
import gov.raon.micitt.utils.Log
import gov.raon.micitt.utils.Util
import io.realm.Realm


@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private var hashedNid: String? = null
    private var hashedToken: String? = null
    private var eDocDataType: String? = null

    private var agencyAdapter: AgencyAdapter? = null
    private var documentAdapter: DocumentAdapter? = null
    private var selectDocumentModel: DocumentModel? = null
    private var selectDocumentAgencyName: String? = null

    private var authenticationDialog: AuthenticationDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.moreRl.visibility = View.VISIBLE
        binding.header.more.setOnClickListener {
            Intent(this, SettingActivity::class.java).also {
                startActivity(it)
            }
        }

        Realm.getDefaultInstance()

        hashedNid = intent.getStringExtra("hashedNid")
        hashedToken = intent.getStringExtra("hashedToken")

        initView()
        initObservers()

        homeViewModel.getDocumentList(hashedToken!!)
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

        homeViewModel.liveSaveDocumentDataList.observe(this) {
            if(it.size == 0) {
                binding.layerCertifiEmpty.visibility = View.VISIBLE
                binding.listCertifi.visibility = View.GONE
            } else {
                binding.layerCertifiEmpty.visibility = View.GONE
                binding.listCertifi.visibility = View.VISIBLE

                if(documentAdapter == null) {
                    documentAdapter = DocumentAdapter(this, it)
                    documentAdapter!!.setDocumentClickListener {
                        Log.d("oykwon", "docu : " + it.agencyName)

                        // 여기다가 추가하시면 됩니다!

                    }
                    binding.listCertifi.adapter = documentAdapter
                } else {

                }
            }
        }

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
            if (it.resultCode == "000") {
                var eDocData = Util.base64UrlDecode(it.resultData.eDoc)

                val data = Gson().fromJson(
                    eDocData,
                    xmlDataModel::class.java
                )

                val signDocumentModel = SignDocumentModel(
                    hashedToken!!,
                    Util.base64UrlEncode(data.strXml), eDocDataType!!
                )

                homeViewModel.updateDocument(
                    selectDocumentModel!!, data.strIdentificacion,
                    selectDocumentAgencyName!!, it.resultData.eDoc)
                Log.d("oykwon", "Save")

                homeViewModel.signDocument(signDocumentModel)

            } else {
                Log.d("oykwon", "Error Document")
                hideProgress()
            }
        }

        homeViewModel.liveSignDocumentStatus.observe(this) {

            authenticationDialog!!.hide()
            Toast.makeText(this,"Success", Toast.LENGTH_LONG).show()
            hideProgress()

            Log.d("oykwon", "Sign : " + it.resultData.signedEDoc)
        }

        homeViewModel.liveSignDocument.observe(this) {
            authenticationDialog = AuthenticationDialog(this, it.resultData.verificationCode)
            authenticationDialog!!.setListener {
                showProgress()

                homeViewModel.checkDocumentStatus(
                    CheckDocumentModel(
                        hashedToken!!,
                        it.resultData.requestId
                    )
                )
            }
            authenticationDialog!!.show()
        }
    }

    private fun setList(agencyInfos: MutableList<AgencyInfo>) {
        agencyAdapter = AgencyAdapter(this, agencyInfos)
        binding.listAgency.adapter = this@HomeActivity.agencyAdapter

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
        // Popup 추가해야함.!!

        selectDocumentAgencyName = item.agencyName

        selectDocumentModel = DocumentModel(
            hashedToken!!,
            item.agencyCode,
            "TSE",
            "XML",
            "TAX"
        )
        eDocDataType = selectDocumentModel!!.dataType
        homeViewModel.getDocument(selectDocumentModel!!)
    }
}