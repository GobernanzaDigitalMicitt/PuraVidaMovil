package gov.raon.micitt.ui.certificate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import gov.raon.micitt.databinding.ActivityCertDetailBinding
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.di.xml.Parser
import gov.raon.micitt.models.SaveDocumentModel
import gov.raon.micitt.ui.settings.SettingActivity
import gov.raon.micitt.utils.Log
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
        binding.header.prev.setOnClickListener {
            Intent(this, SettingActivity::class.java).also {
                finish()
            }
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
//        val xmlString2 = """{"strXml":"<NewDataSet>\r\n  <Table>\r\n    <ACTIVIDADES_ECONOMICAS>CULTIVO DE CAFE</ACTIVIDADES_ECONOMICAS>\r\n    <CODIGO_ACTIVIDAD>11401</CODIGO_ACTIVIDAD>\r\n    <TIPO_ESTADO>A</TIPO_ESTADO>\r\n    <FECHA_I_ACTIVIDAD>1996-12-01T00:00:00-06:00</FECHA_I_ACTIVIDAD>\r\n  </Table>\r\n  <Table1>\r\n    <MODELO>101</MODELO>\r\n    <DESCRIPCION_MODELO>Impuesto sobre la Renta/Impuesto sobre las Utilidades</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>1996-12-01T00:00:00-06:00</FECHA_INICIO>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>A</ESTADO>\r\n    <REGIMEN>Régimen General</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>103</MODELO>\r\n    <DESCRIPCION_MODELO>Retenciones en la Fuente</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2008-06-01T00:00:00-06:00</FECHA_INICIO>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>A</ESTADO>\r\n    <REGIMEN>Régimen General</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>137</MODELO>\r\n    <DESCRIPCION_MODELO>IVA Especial Agropecuario ANUAL</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2019-10-01T00:00:00-06:00</FECHA_INICIO>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>A</ESTADO>\r\n    <REGIMEN>Régimen Especial Agropecuario</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>180</MODELO>\r\n    <DESCRIPCION_MODELO>Impuesto a las Personas Jurídicas</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2017-09-01T00:00:00-06:00</FECHA_INICIO>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>A</ESTADO>\r\n    <REGIMEN>No Tiene</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>104</MODELO>\r\n    <DESCRIPCION_MODELO>Impuesto sobre el Valor Agregado (antes Impuesto General sobre las Ventas)</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2019-07-01T00:00:00-06:00</FECHA_INICIO>\r\n    <FECHA_FIN>2019-09-30T00:00:00-06:00</FECHA_FIN>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>I</ESTADO>\r\n    <REGIMEN>Régimen General</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>104</MODELO>\r\n    <DESCRIPCION_MODELO>Impuesto sobre el Valor Agregado (antes Impuesto General sobre las Ventas)</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2019-07-01T00:00:00-06:00</FECHA_INICIO>\r\n    <FECHA_FIN>2019-07-01T00:00:00-06:00</FECHA_FIN>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>I</ESTADO>\r\n    <REGIMEN>Régimen General</REGIMEN>\r\n  </Table1>\r\n  <Table2>\r\n    <IDENTIFICACION>800490629</IDENTIFICACION>\r\n    <nroRelacion>33284211</nroRelacion>\r\n    <nroInternoIDRepresentante>5296739</nroInternoIDRepresentante>\r\n    <NOMBRE>JUAN CESAR GARCES MELGAR</NOMBRE>\r\n    <ESTADO_CONTRIBUYENTE>No</ESTADO_CONTRIBUYENTE>\r\n    <FUENTE_CONTRIBUYENTE>Dirección General de Tributación</FUENTE_CONTRIBUYENTE>\r\n    <FECHA_DE_INICIO>2018-09-06T00:00:00-06:00</FECHA_DE_INICIO>\r\n  </Table2>\r\n  <Table4>\r\n    <METODOFACTURACION>Factura Electronica ( Emisor-Receptor Electrónico / Proveedores de sistemas gratuitos parala emisión de comprobantes electrónicos)</METODOFACTURACION>\r\n    <FECHAINICIOFACT>2019-09-12T00:00:00-06:00</FECHAINICIOFACT>\r\n    <NUMERODOCUMENTO>1401m19000338030</NUMERODOCUMENTO>\r\n  </Table4>\r\n  <Table5>\r\n    <Tipo_x0020_Régimen>Régimen Especial Agropecuario</Tipo_x0020_Régimen>\r\n    <Fecha_x0020_de_x0020_inicio>01/10/2019</Fecha_x0020_de_x0020_inicio>\r\n    <Documento_x0020_de_x0020_Alta>Alta Oficio REA</Documento_x0020_de_x0020_Alta>\r\n    <Documento_x0020_de_x0020_Baja />\r\n    <Estado>A</Estado>\r\n  </Table5>\r\n  <Table5>\r\n    <Tipo_x0020_Régimen>Régimen Especial Agropecuario</Tipo_x0020_Régimen>\r\n    <Fecha_x0020_de_x0020_inicio>01/10/2019</Fecha_x0020_de_x0020_inicio>\r\n    <Documento_x0020_de_x0020_Alta>Alta Oficio REA</Documento_x0020_de_x0020_Alta>\r\n    <Documento_x0020_de_x0020_Baja>Caso 113702</Documento_x0020_de_x0020_Baja>\r\n    <Estado>I</Estado>\r\n  </Table5>\r\n  <Table6>\r\n    <Año>2024</Año>\r\n    <Semestre>S</Semestre>\r\n    <FactorRetencion>0.00408</FactorRetencion>\r\n    <FechaVencimiento>2024-12-31T00:00:00-06:00</FechaVencimiento>\r\n    <FechaCarga>2024-06-01T03:00:05.92-06:00</FechaCarga>\r\n  </Table6>\r\n  <Table7>\r\n    <Año>2024</Año>\r\n    <FactorRetencion>0.00000</FactorRetencion>\r\n    <FechaVencimiento>2025-06-30T00:00:00-06:00</FechaVencimiento>\r\n    <FechaCarga>2024-06-03T03:00:05.86-06:00</FechaCarga>\r\n  </Table7>\r\n</NewDataSet>","strCondicion":"NO","strEsMoroso":"NO","strEsOmiso":"NO","strFechaActualizacion":"19/08/2020","strFechaDesinscripcion":"","strFechaInscripcion":"01/12/1996","strIdentificacion":"3101022491","strSistema":"ATV","nroInternoID":6421837,"strAdministracion":"Cartago","strEstadoTributario":"Inscrito","strNombreComercial":"","strRazonSocial":"COTIPE SOCIEDAD ANONIMA"}""".trimIndent()
        parser.parse(xmlString)
        val pItem = parser.getElements().groupBy { it.tableNum }
        adapter = CertDetailAdapter(pItem, cardInfo, parser.getEdited())
        binding.detailRv.layoutManager = LinearLayoutManager(this)
        binding.detailRv.adapter = adapter

    }
}
