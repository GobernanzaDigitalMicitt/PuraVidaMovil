package gov.raon.micitt.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.BuildConfig
import gov.raon.micitt.R
import gov.raon.micitt.databinding.ActivityHomeBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.models.AgencyModel
import gov.raon.micitt.models.CheckDocumentModel
import gov.raon.micitt.models.DocumentModel
import gov.raon.micitt.models.SaveDocumentModel
import gov.raon.micitt.models.SignDocumentModel
import gov.raon.micitt.models.response.AgencyInfo
import gov.raon.micitt.models.xmlDataModel
import gov.raon.micitt.ui.certificate.CertDetailActivity
import gov.raon.micitt.ui.main.AuthenticationDialog
import gov.raon.micitt.ui.settings.SettingActivity
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
    private var documentAdapter: DocumentAdapter? = null
    private var selectDocumentModel: DocumentModel? = null
    private var selectDocumentAgencyName: String? = null

    private var authenticationDialog: AuthenticationDialog? = null
    private var listSaveDocumentModel: MutableList<SaveDocumentModel>? = null
    private var eDoc: String? = null
    private var isMiCertifi = true

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

        hashedNid = intent.getStringExtra("hashedNid")
        hashedToken = intent.getStringExtra("hashedToken")

        initView()
        initObservers()

        homeViewModel.getDocumentList(hashedToken!!)
    }

    private fun initView() {

        binding.layerMiCertifi.setOnClickListener {
            isMiCertifi = true

            updateCertiUIView()
        }

        binding.layerSoliCertifi.setOnClickListener {

            isMiCertifi = false

            updateCertiUIView()

            val agencyModel = AgencyModel("all")
            homeViewModel.getAgencyList(agencyModel)
        }

        binding.layerCertifiEmpty.setOnClickListener {
            isMiCertifi = false

            updateCertiUIView()

            val agencyModel = AgencyModel("all")
            homeViewModel.getAgencyList(agencyModel)
        }
    }

    private fun updateCertiUIView() {
        if(isMiCertifi) {
            binding.layerCertifi.visibility = View.VISIBLE
            binding.layerAgency.visibility = View.GONE

            binding.viewMiCerti.visibility = View.VISIBLE
            binding.viewSoliCerti.visibility = View.GONE

            binding.tvMiCertifi.setTextColor(getColor(R.color.micitt_theme))
            binding.tvSoliCertifi.setTextColor(getColor(R.color.Font_G60))
        } else {
            binding.layerCertifi.visibility = View.GONE
            binding.layerAgency.visibility = View.VISIBLE

            binding.viewMiCerti.visibility = View.GONE
            binding.viewSoliCerti.visibility = View.VISIBLE

            binding.tvMiCertifi.setTextColor(getColor(R.color.Font_G60))
            binding.tvSoliCertifi.setTextColor(getColor(R.color.micitt_theme))
        }
    }

    private fun updateUIView(saveDocumentModels: MutableList<SaveDocumentModel>) {
        if(saveDocumentModels != null && saveDocumentModels.size != 0) {
            binding.layerCertifiEmpty.visibility = View.GONE
            binding.listCertifi.visibility = View.VISIBLE
        } else {
            binding.layerCertifiEmpty.visibility = View.VISIBLE
            binding.listCertifi.visibility = View.GONE
        }

        updateCertiUIView()
    }

    private fun initObservers() {
        homeViewModel.liveSaveDocumentDataList.observe(this) { it ->
            listSaveDocumentModel = it

            if(documentAdapter == null) {
                documentAdapter = DocumentAdapter(this, it)
                documentAdapter!!.setDocumentClickListener {
                    val eDoc = Util.base64UrlDecode(it.eDoc)
                    Intent(this, CertDetailActivity::class.java).also { act->
                        act.putExtra("xml", eDoc)
                        startActivity(act)
                    }
                }

                documentAdapter!!.setOnButtonClicked {
                    getDialogBuilder {it2 ->
                        it2.title("Deseas eliminar este certificado?")
                        it2.message("El certificado y la información relacionada serán eliminados de inmediato y podrán ser emitidos nuevamente si es necesario.")
                        it2.btnConfirm("Eliminar")
                        it2.btnCancel("Cancelar")

                        showDialog(it2){ result, obj ->
                            if(result){
                                documentAdapter!!.deleteItem(it)
                                homeViewModel.deleteDocument(it)
                            }
                        }
                    }

                }
                binding.listCertifi.adapter = documentAdapter
            } else {
                it.reverse()
                documentAdapter!!.clear()
                documentAdapter!!.addList(it)
            }

            updateUIView(it)
        }

        homeViewModel.liveAgencyList.observe(this) {
            if (agencyAdapter == null) {
                Log.d("oykwon", "Create Adapter : " + it.size)
                it.reverse()
                setList(it)
            } else {
                it.reverse()
                agencyAdapter!!.clear()
                agencyAdapter!!.addList(it)
            }
        }

        homeViewModel.liveDocument.observe(this) {
            if (it.resultCode == "000") {
                eDoc = it.resultData.eDoc

                val eDocData = Util.base64UrlDecode(it.resultData.eDoc)

                val data = Gson().fromJson(
                    eDocData,
                    xmlDataModel::class.java
                )

                val signDocumentModel = SignDocumentModel(
                    hashedToken!!,
                    Util.base64UrlEncode(data.strXml), eDocDataType!!
                )

                homeViewModel.signDocument(signDocumentModel)

            } else {
                Log.d("oykwon", "Error Document")
                hideProgress()
            }
        }

        homeViewModel.liveSignDocumentStatus.observe(this) {
            authenticationDialog!!.hide()

            Log.d("oykwon", "Sign : " + it.resultData.signedEDoc)

            val eDocData = Util.base64UrlDecode(eDoc)
            val data = Gson().fromJson(eDocData, xmlDataModel::class.java)
            val fileName = "${BuildConfig.APP_NAME}_${data.strIdentificacion}"
            Util.saveFile(this@HomeActivity,fileName, eDocData)
            Util.saveFileExternal(this@HomeActivity,fileName,eDocData)

            homeViewModel.updateDocument(
                selectDocumentModel!!, data.strIdentificacion,
                selectDocumentAgencyName!!, eDoc!!)

            Toast.makeText(this,"Success", Toast.LENGTH_LONG).show()

            isMiCertifi = true
            homeViewModel.getDocumentList(hashedToken!!)

            hideProgress()
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
                it.title("Tipo de identificación")
                it.btnStyle(0)

                showDialog(it) { result, _ ->
                    showProgress()
                    if (result) {
                        getDocument(item, "TSE")
                    } else {
                        getDocument(item, "DIMEX")
                    }
                }
            }
        }
    }

    private fun getDocument(item: AgencyInfo, type : String) {
        // Popup 추가해야함.!!

        selectDocumentAgencyName = item.agencyName

        selectDocumentModel = DocumentModel(
            hashedToken!!,
            item.agencyCode,
            type,
            "JSON",
            "TAX"
        )
        eDocDataType = selectDocumentModel!!.dataType
        homeViewModel.getDocument(selectDocumentModel!!)
    }
}