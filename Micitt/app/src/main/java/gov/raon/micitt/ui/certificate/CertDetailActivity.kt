package gov.raon.micitt.ui.certificate

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import gov.raon.micitt.databinding.ActivityCertDetailBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.di.xml.Parser
import gov.raon.micitt.models.SaveDocumentModel
import gov.raon.micitt.utils.Util

/*
1번째 Table   > Actividades Económicas
3번째 Table1  > Obligaciones Tributarias      :: TIPO_OBLIGATION -> Classification
2번째 Table2  > Representantes Legales        :: nroRelacion / nroInternoIDRepresentatne 없음, 웹에서 표기 안함
5번째 Table3  > Registros Especiales          :: 없앰 추가 안해도뎀
6번째 Table4  > Metodo de Facturacion         :: Numberodocumento 웹에서 표기 안함
4번째 Table5  > Regimenes Especiales
7번째 Table6  > Factores de Retencion IVA
8번째 Table7  > Factories de Retencion Renta
 */

class CertDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityCertDetailBinding
    private lateinit var adapter: CertDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.prevRl.visibility = View.VISIBLE
        binding.header.prevRl.setOnClickListener {
            finish()
        }

        initDocInfo()
    }

    private fun initDocInfo() {
        val data: SaveDocumentModel = Gson().fromJson(intent.getStringExtra("cardObj"), SaveDocumentModel::class.java)
        val xmlData = Util.base64UrlDecode(data.eDoc)
        prepareDocument(xmlData, data)
    }

    private fun prepareDocument(xmlString: String, cardInfo: SaveDocumentModel) {
        val parser = Parser()
        parser.parse(xmlString)
        val pItem = parser.getElements().groupBy { it.tableNum }
        adapter = CertDetailAdapter(pItem, cardInfo, parser.getEdited())
        binding.detailRv.layoutManager = LinearLayoutManager(this)
        binding.detailRv.adapter = adapter
    }
}
