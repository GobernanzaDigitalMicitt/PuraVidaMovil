package gov.raon.micitt.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivityMainBinding
import gov.raon.micitt.models.SignUpModel
import gov.raon.micitt.ui.certificate.CertDetailActivity
import gov.raon.micitt.ui.settings.NoticeActivity
import gov.raon.micitt.utils.Util


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private val xml = """<NewDataSet>
  <Table>
    <ACTIVIDADES_ECONOMICAS>ACTIVIDADES PREOPERATIVAS O DE ORGANIZACION</ACTIVIDADES_ECONOMICAS>
    <CODIGO_ACTIVIDAD>960105</CODIGO_ACTIVIDAD>
    <TIPO_ESTADO>A</TIPO_ESTADO>
    <FECHA_I_ACTIVIDAD>2021-01-01T00:00:00-06:00</FECHA_I_ACTIVIDAD>
  </Table>
  <Table>
    <ACTIVIDADES_ECONOMICAS>PERSONA JURIDICA LEGALMENTE CONSTITUIDAS</ACTIVIDADES_ECONOMICAS>
    <CODIGO_ACTIVIDAD>960113</CODIGO_ACTIVIDAD>
    <TIPO_ESTADO>A</TIPO_ESTADO>
    <FECHA_I_ACTIVIDAD>2020-11-10T00:00:00-06:00</FECHA_I_ACTIVIDAD>
  </Table>
  <Table>
    <ACTIVIDADES_ECONOMICAS>PERSONA JURIDICA LEGALMENTE CONSTITUIDAS</ACTIVIDADES_ECONOMICAS>
    <CODIGO_ACTIVIDAD>960113</CODIGO_ACTIVIDAD>
    <TIPO_ESTADO>I</TIPO_ESTADO>
    <FECHA_I_ACTIVIDAD>2020-01-01T00:00:00-06:00</FECHA_I_ACTIVIDAD>
    <FECHA_F_ACTIVIDAD>2020-01-01T00:00:00-06:00</FECHA_F_ACTIVIDAD>
  </Table>
  <Table>
    <ACTIVIDADES_ECONOMICAS>PERSONA JURIDICA LEGALMENTE CONSTITUIDAS</ACTIVIDADES_ECONOMICAS>
    <CODIGO_ACTIVIDAD>960113</CODIGO_ACTIVIDAD>
    <TIPO_ESTADO>I</TIPO_ESTADO>
    <FECHA_I_ACTIVIDAD>2020-11-10T00:00:00-06:00</FECHA_I_ACTIVIDAD>
    <FECHA_F_ACTIVIDAD>2020-12-31T00:00:00-06:00</FECHA_F_ACTIVIDAD>
  </Table>
  <Table1>
    <MODELO>180</MODELO>
    <DESCRIPCION_MODELO>Impuesto a las Personas Jurídicas</DESCRIPCION_MODELO>
    <FECHA_INICIO>2017-09-01T00:00:00-06:00</FECHA_INICIO>
    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>
    <ESTADO>A</ESTADO>
    <REGIMEN>No Tiene</REGIMEN>
  </Table1>
  <Table1>
    <MODELO>101</MODELO>
    <DESCRIPCION_MODELO>Impuesto sobre la Renta/Impuesto sobre las Utilidades</DESCRIPCION_MODELO>
    <FECHA_INICIO>2020-11-10T00:00:00-06:00</FECHA_INICIO>
    <FECHA_FIN>2020-12-31T00:00:00-06:00</FECHA_FIN>
    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>
    <ESTADO>I</ESTADO>
    <REGIMEN>Régimen General</REGIMEN>
  </Table1>
  <Table2>
    <IDENTIFICACION>108470132</IDENTIFICACION>
    <nroRelacion>33496803</nroRelacion>
    <nroInternoIDRepresentante>883257</nroInternoIDRepresentante>
    <NOMBRE>OVED ANDRES MORALES GUERRERO</NOMBRE>
    <ESTADO_CONTRIBUYENTE>No</ESTADO_CONTRIBUYENTE>
    <FUENTE_CONTRIBUYENTE>Dirección General de Tributación</FUENTE_CONTRIBUYENTE>
    <FECHA_DE_INICIO>2016-01-01T00:00:00-06:00</FECHA_DE_INICIO>
  </Table2>
  <Table2>
    <IDENTIFICACION>108880740</IDENTIFICACION>
    <nroRelacion>32889693</nroRelacion>
    <nroInternoIDRepresentante>935878</nroInternoIDRepresentante>
    <NOMBRE>MONICA DE LOS ANGELES SEGOVIA CORDERO</NOMBRE>
    <ESTADO_CONTRIBUYENTE>Sí</ESTADO_CONTRIBUYENTE>
    <FUENTE_CONTRIBUYENTE>Registro Público Nacional</FUENTE_CONTRIBUYENTE>
    <FECHA_DE_INICIO>1997-09-17T00:00:00-06:00</FECHA_DE_INICIO>
    <FECHA_DE_FIN>2096-09-17T00:00:00-06:00</FECHA_DE_FIN>
  </Table2>
  <Table6>
    <Año>2024</Año>
    <Semestre>S</Semestre>
    <FactorRetencion>0.00000</FactorRetencion>
    <FechaVencimiento>2024-12-31T00:00:00-06:00</FechaVencimiento>
    <FechaCarga>2024-06-01T03:00:05.92-06:00</FechaCarga>
  </Table6>
  <Table7>
    <Año>2024</Año>
    <FactorRetencion>0.00000</FactorRetencion>
    <FechaVencimiento>2025-06-30T00:00:00-06:00</FechaVencimiento>
    <FechaCarga>2024-06-03T03:00:05.86-06:00</FechaCarga>
  </Table7>
</NewDataSet>""".trimIndent()

    private val xml2 = """<NewDataSet>\r\n  <Table>\r\n    <ACTIVIDADES_ECONOMICAS>CULTIVO DE CAFE</ACTIVIDADES_ECONOMICAS>\r\n    <CODIGO_ACTIVIDAD>11401</CODIGO_ACTIVIDAD>\r\n    <TIPO_ESTADO>A</TIPO_ESTADO>\r\n    <FECHA_I_ACTIVIDAD>1996-12-01T00:00:00-06:00</FECHA_I_ACTIVIDAD>\r\n  </Table>\r\n  <Table1>\r\n    <MODELO>101</MODELO>\r\n    <DESCRIPCION_MODELO>Impuesto sobre la Renta/Impuesto sobre las Utilidades</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>1996-12-01T00:00:00-06:00</FECHA_INICIO>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>A</ESTADO>\r\n    <REGIMEN>Régimen General</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>103</MODELO>\r\n    <DESCRIPCION_MODELO>Retenciones en la Fuente</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2008-06-01T00:00:00-06:00</FECHA_INICIO>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>A</ESTADO>\r\n    <REGIMEN>Régimen General</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>137</MODELO>\r\n    <DESCRIPCION_MODELO>IVA Especial Agropecuario ANUAL</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2019-10-01T00:00:00-06:00</FECHA_INICIO>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>A</ESTADO>\r\n    <REGIMEN>Régimen Especial Agropecuario</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>180</MODELO>\r\n    <DESCRIPCION_MODELO>Impuesto a las Personas Jurídicas</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2017-09-01T00:00:00-06:00</FECHA_INICIO>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>A</ESTADO>\r\n    <REGIMEN>No Tiene</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>104</MODELO>\r\n    <DESCRIPCION_MODELO>Impuesto sobre el Valor Agregado (antes Impuesto General sobre las Ventas)</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2019-07-01T00:00:00-06:00</FECHA_INICIO>\r\n    <FECHA_FIN>2019-09-30T00:00:00-06:00</FECHA_FIN>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>I</ESTADO>\r\n    <REGIMEN>Régimen General</REGIMEN>\r\n  </Table1>\r\n  <Table1>\r\n    <MODELO>104</MODELO>\r\n    <DESCRIPCION_MODELO>Impuesto sobre el Valor Agregado (antes Impuesto General sobre las Ventas)</DESCRIPCION_MODELO>\r\n    <FECHA_INICIO>2019-07-01T00:00:00-06:00</FECHA_INICIO>\r\n    <FECHA_FIN>2019-07-01T00:00:00-06:00</FECHA_FIN>\r\n    <TIPO_OBLIGACION>CONTRIBUYENTE</TIPO_OBLIGACION>\r\n    <ESTADO>I</ESTADO>\r\n    <REGIMEN>Régimen General</REGIMEN>\r\n  </Table1>\r\n  <Table2>\r\n    <IDENTIFICACION>800490629</IDENTIFICACION>\r\n    <nroRelacion>33284211</nroRelacion>\r\n    <nroInternoIDRepresentante>5296739</nroInternoIDRepresentante>\r\n    <NOMBRE>JUAN CESAR GARCES MELGAR</NOMBRE>\r\n    <ESTADO_CONTRIBUYENTE>No</ESTADO_CONTRIBUYENTE>\r\n    <FUENTE_CONTRIBUYENTE>Dirección General de Tributación</FUENTE_CONTRIBUYENTE>\r\n    <FECHA_DE_INICIO>2018-09-06T00:00:00-06:00</FECHA_DE_INICIO>\r\n  </Table2>\r\n  <Table4>\r\n    <METODOFACTURACION>Factura Electronica ( Emisor-Receptor Electrónico / Proveedores de sistemas gratuitos parala emisión de comprobantes electrónicos)</METODOFACTURACION>\r\n    <FECHAINICIOFACT>2019-09-12T00:00:00-06:00</FECHAINICIOFACT>\r\n    <NUMERODOCUMENTO>1401m19000338030</NUMERODOCUMENTO>\r\n  </Table4>\r\n  <Table5>\r\n    <Tipo_x0020_Régimen>Régimen Especial Agropecuario</Tipo_x0020_Régimen>\r\n    <Fecha_x0020_de_x0020_inicio>01/10/2019</Fecha_x0020_de_x0020_inicio>\r\n    <Documento_x0020_de_x0020_Alta>Alta Oficio REA</Documento_x0020_de_x0020_Alta>\r\n    <Documento_x0020_de_x0020_Baja />\r\n    <Estado>A</Estado>\r\n  </Table5>\r\n  <Table5>\r\n    <Tipo_x0020_Régimen>Régimen Especial Agropecuario</Tipo_x0020_Régimen>\r\n    <Fecha_x0020_de_x0020_inicio>01/10/2019</Fecha_x0020_de_x0020_inicio>\r\n    <Documento_x0020_de_x0020_Alta>Alta Oficio REA</Documento_x0020_de_x0020_Alta>\r\n    <Documento_x0020_de_x0020_Baja>Caso 113702</Documento_x0020_de_x0020_Baja>\r\n    <Estado>I</Estado>\r\n  </Table5>\r\n  <Table6>\r\n    <Año>2024</Año>\r\n    <Semestre>S</Semestre>\r\n    <FactorRetencion>0.00408</FactorRetencion>\r\n    <FechaVencimiento>2024-12-31T00:00:00-06:00</FechaVencimiento>\r\n    <FechaCarga>2024-06-01T03:00:05.92-06:00</FechaCarga>\r\n  </Table6>\r\n  <Table7>\r\n    <Año>2024</Año>\r\n    <FactorRetencion>0.00000</FactorRetencion>\r\n    <FechaVencimiento>2025-06-30T00:00:00-06:00</FechaVencimiento>\r\n    <FechaCarga>2024-06-03T03:00:05.86-06:00</FechaCarga>\r\n  </Table7>\r\n</NewDataSet>""".trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObservers()

    }
    private fun initView() {
        binding.tvSignup.setOnClickListener {
            val nId = binding.etNid.text.toString()

            val signUpModel = SignUpModel(Util.hashSHA256(nId).toString(), nId)

            if(nId!!.isNotEmpty()) {
                mainViewModel.signUp(this, signUpModel)

            } else {
                Toast.makeText(this, "nId is Empty", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvSignin.setOnClickListener {
            Intent(this, CertDetailActivity::class.java).also { intent ->
                intent.putExtra("xml",xml2)
                startActivity(intent)
            }
        }

        binding.tvSignup.setOnClickListener {
            Intent(this, NoticeActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun initObservers() {

    }
}