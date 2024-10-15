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
        val testing = "{\"strXml\":\"<NewDataSet>\\r\\n  <Table>\\r\\n    <ACTIVIDADES_ECONOMICAS>ACTIVIDADES DE ARQUITECTURA E INGENIERIA</ACTIVIDADES_ECONOMICAS>\\r\\n    <CODIGO_ACTIVIDAD>742102</CODIGO_ACTIVIDAD>\\r\\n    <TIPO_ESTADO>A</TIPO_ESTADO>\\r\\n    <FECHA_I_ACTIVIDAD>2021-12-01T00:00:00-06:00</FECHA_I_ACTIVIDAD>\\r\\n  </Table>\\r\\n  <Table>\\r\\n    <ACTIVIDADES_ECONOMICAS>ACTIVIDADES DE ARQUITECTURA E INGENIERIA</ACTIVIDADES_ECONOMICAS>\\r\\n    <CODIGO_ACTIVIDAD>742102</CODIGO_ACTIVIDAD>\\r\\n    <TIPO_ESTADO>I</TIPO_ESTADO>\\r\\n    <FECHA_I_ACTIVIDAD>2006-03-01T00:00:00-06:00</FECHA_I_ACTIVIDAD>\\r\\n    <FECHA_F_ACTIVIDAD>2019-09-30T00:00:00-06:00</FECHA_F_ACTIVIDAD>\\r\\n  </Table>\\r\\n  <Table1>\\r\\n    <MODELO>101</MODELO>\\r\\n    <DESCRIPCION_MODELO>Impuesto sobre la Renta/Impuesto sobre las Utilidades</DESCRIPCION_MODELO>\\r\\n    <FECHA_INICIO>2021-12-01T00:00:00-06:00</FECHA_INICIO>\\r\\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\\r\\n    <ESTADO>A</ESTADO>\\r\\n    <REGIMEN>Régimen General</REGIMEN>\\r\\n  </Table1>\\r\\n  <Table1>\\r\\n    <MODELO>104</MODELO>\\r\\n    <DESCRIPCION_MODELO>Impuesto sobre el Valor Agregado (antes Impuesto General sobre las Ventas)</DESCRIPCION_MODELO>\\r\\n    <FECHA_INICIO>2021-12-01T00:00:00-06:00</FECHA_INICIO>\\r\\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\\r\\n    <ESTADO>A</ESTADO>\\r\\n    <REGIMEN>Régimen General</REGIMEN>\\r\\n  </Table1>\\r\\n  <Table1>\\r\\n    <MODELO>101</MODELO>\\r\\n    <DESCRIPCION_MODELO>Impuesto sobre la Renta/Impuesto sobre las Utilidades</DESCRIPCION_MODELO>\\r\\n    <FECHA_INICIO>2006-03-01T00:00:00-06:00</FECHA_INICIO>\\r\\n    <FECHA_FIN>2019-09-30T00:00:00-06:00</FECHA_FIN>\\r\\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\\r\\n    <ESTADO>I</ESTADO>\\r\\n    <REGIMEN>Régimen General</REGIMEN>\\r\\n  </Table1>\\r\\n  <Table1>\\r\\n    <MODELO>104</MODELO>\\r\\n    <DESCRIPCION_MODELO>Impuesto sobre el Valor Agregado (antes Impuesto General sobre las Ventas)</DESCRIPCION_MODELO>\\r\\n    <FECHA_INICIO>2019-07-01T00:00:00-06:00</FECHA_INICIO>\\r\\n    <FECHA_FIN>2019-09-30T00:00:00-06:00</FECHA_FIN>\\r\\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\\r\\n    <ESTADO>I</ESTADO>\\r\\n    <REGIMEN>Régimen General</REGIMEN>\\r\\n  </Table1>\\r\\n  <Table4>\\r\\n    <METODOFACTURACION>Factura Electronica ( Emisor-Receptor Electrónico )</METODOFACTURACION>\\r\\n    <FECHAINICIOFACT>2021-12-01T00:00:00-06:00</FECHAINICIOFACT>\\r\\n    <NUMERODOCUMENTO>1401i21000283376</NUMERODOCUMENTO>\\r\\n  </Table4>\\r\\n  <Table6>\\r\\n    <Año>2024</Año>\\r\\n    <Semestre>S</Semestre>\\r\\n    <FactorRetencion>0.05310</FactorRetencion>\\r\\n    <FechaVencimiento>2024-12-31T00:00:00-06:00</FechaVencimiento>\\r\\n    <FechaCarga>2024-06-01T03:00:05.92-06:00</FechaCarga>\\r\\n  </Table6>\\r\\n  <Table7>\\r\\n    <Año>2024</Año>\\r\\n    <FactorRetencion>0.01760</FactorRetencion>\\r\\n    <FechaVencimiento>2025-06-30T00:00:00-06:00</FechaVencimiento>\\r\\n    <FechaCarga>2024-06-03T03:00:05.86-06:00</FechaCarga>\\r\\n  </Table7>\\r\\n</NewDataSet>\",\"strCondicion\":\"NO\",\"strEsMoroso\":\"NO\",\"strEsOmiso\":\"SI\",\"strFechaActualizacion\":\"18/12/2021\",\"strFechaDesinscripcion\":\"\",\"strFechaInscripcion\":\"01/12/2021\",\"strIdentificacion\":\"0111230875\",\"strSistema\":\"ATV\",\"nroInternoID\":1224458,\"strAdministracion\":\"Heredia\",\"strEstadoTributario\":\"Inscrito\",\"strNombreComercial\":\"\",\"strRazonSocial\":\"DANIEL ARNOLDO MORENO CONEJO\"}"
        parser.parse(testing)
        val pItem = parser.getElements().groupBy { it.tableNum }
        adapter = CertDetailAdapter(pItem, cardInfo, parser.getEdited())
        binding.detailRv.layoutManager = LinearLayoutManager(this)
        binding.detailRv.adapter = adapter
    }
}
