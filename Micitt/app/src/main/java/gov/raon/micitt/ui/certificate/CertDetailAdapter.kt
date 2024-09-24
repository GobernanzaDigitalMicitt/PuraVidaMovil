package gov.raon.micitt.ui.certificate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gov.raon.micitt.R
import gov.raon.micitt.models.SaveDocumentModel
import gov.raon.micitt.ui.certificate.model.ParentItem
import gov.raon.micitt.utils.Log


class CertDetailAdapter(
    private val pItem: Map<Int, List<ParentItem>>,
    private val card: SaveDocumentModel,
    private val removedList: List<Pair<String, String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var listener: () -> Unit

    companion object TYPE {
        val VIEW_TYPE_DETAIL_TITLE = R.layout.cert_detail_item
        val VIEW_TYPE_CARD = R.layout.item_document
        val VIEW_TYPE_BUTTON = R.layout.layout_btn_more
    }

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = when (viewType) {
            VIEW_TYPE_DETAIL_TITLE -> LayoutInflater.from(parent.context).inflate(R.layout.cert_detail_item, parent, false)
            VIEW_TYPE_CARD -> LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.cert_detail_item, parent, false)
        }

        return when (viewType) {
            VIEW_TYPE_DETAIL_TITLE -> DetailTitleViewHolder(binding)
            VIEW_TYPE_CARD -> CardViewHolder(binding)
            else -> DetailTitleViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_CARD
        }
        return VIEW_TYPE_DETAIL_TITLE
    }

    override fun getItemCount(): Int {
        return pItem.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            when (holder) {
                is CardViewHolder -> {
                    holder.bind()
                }
            }
        } else {
            when (holder) {
                is DetailTitleViewHolder -> {
                    holder.bind(pItem.keys.toList()[position - 1])
                }
            }
        }
    }


    inner class DetailTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.detail_item)
        private val btnMore: RelativeLayout = itemView.findViewById(R.id.arrow_up_rl)
        private val arrowUp : View = itemView.findViewById(R.id.arrow_up)
        private val arrowDown : View = itemView.findViewById(R.id.arrow_down)
        private val elemContainer: LinearLayout = itemView.findViewById(R.id.cert_detail_elements)
        private val line: View = itemView.findViewById(R.id.cert_bl)

        private var arrowStatus = false

        fun bind(parentItem: Int) {
            title.text = titleList[parentItem]
            arrowDown.visibility=View.GONE

            btnMore.setOnClickListener {
                checkStatus()
                toggleVisibility(elemContainer, line)
                elemContainer.removeAllViews()
                pItem[parentItem]?.forEach { parent ->
                    parent.elements?.forEachIndexed { index, elem ->
                        val cView = LayoutInflater.from(itemView.context).inflate(R.layout.cert_detail, elemContainer, false)

                        val key: TextView = cView.findViewById(R.id.cert_key)
                        val value: TextView = cView.findViewById(R.id.cert_value)
                        val line2: View = cView.findViewById(R.id.cert_detail_bl)

                        if(!elem.key.isNullOrEmpty()){
                            key.text = fixed(titleList[parentItem], elem.key)
                            value.text = elem.value
                        }

                        elemContainer.addView(cView)

                        line2.visibility =
                            if (index == parent.elements.size - 1) View.VISIBLE else View.GONE
                    } ?:run{
                        val cView = LayoutInflater.from(itemView.context).inflate(R.layout.cert_detail, elemContainer, false)

                        val key: TextView = cView.findViewById(R.id.cert_key)
                        key.visibility= View.GONE

                        val value: TextView = cView.findViewById(R.id.cert_value)
                        value.visibility = View.GONE

                        val empty : TextView = cView.findViewById(R.id.cert_empty)
                        empty.visibility = View.VISIBLE

                        elemContainer.addView(cView)
                    }
                }

            }
        }

        private fun checkStatus() {
            arrowStatus = !arrowStatus
            arrowDown.visibility = if (arrowStatus) View.VISIBLE else View.GONE
            arrowUp.visibility = if (arrowStatus) View.GONE else View.VISIBLE
        }

        private fun toggleVisibility(vararg views: View) {
            views.forEach { view ->
                view.visibility = if (view.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }

    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val type: TextView = itemView.findViewById(R.id.tv_type)
        private val dataType: TextView = itemView.findViewById(R.id.tv_name)
        private val agency: TextView = itemView.findViewById(R.id.tv_vc)
        private val date: TextView = itemView.findViewById(R.id.tv_date)

        private val button : ImageView = itemView.findViewById(R.id.btn_delete)

        fun bind() {
            button.visibility = View.GONE
            type.text = card.dataFormat
            dataType.text = card.agencyName
            agency.text = "Issuer, Costa Rica"
            date.text = card.date
        }
    }

    private val titleList = mapOf(
        0 to "Actividades Económicas",
        1 to "Obligaciones Tributarias",
        2 to "Representantes Legales",
        4 to "Metodo de Facturacion",
        5 to "Regimenes Especiales",
        6 to "Factores de Retencion IVA",
        7 to "Factories de Retencion Renta",
        8 to "Información"
    )

    private fun fixed(tableNum: String?, key: String?): String {
        val result = when (tableNum) {
            "Información" -> when (key) {
                "strCondicion" -> "Estado Tributario"
                "strEsMorso" -> "Es Moroso"
                "strFechaActualizacion" -> "Fecha de Actualización"
                "strFechaDesinscripcion" -> "Fecha de Desinscrión"
                "strFechaInscripcion" -> "Fecha de Inscripción"

                "strIdentificacion" -> "Identificación"
                "strSistema" -> "Sistema"
                "strAdministracion" -> "Administración"
                "strEstadoTributario" -> "Estado Tributario"

                "strNombreComercial" -> "Nombre y/o Razón Social"
                "strRazonSocial" -> "Nombre y/o Razón Social\n"
                else -> ""
            }

            "Actividades Económicas" -> when (key) {
                "ACTIVIDADES_ECONOMICAS" -> "Nombre Actividad"
                "CODIGO_ACTIVIDAD" -> "Código Actividad"
                "TIPO_ESTADO" -> "Estado"
                "FECHA_I_ACTIVIDAD" -> "Fecha Inicio"
                else -> " "
            }

            "Obligaciones Tributarias" -> when (key) {
                "MODELO" -> "Modelo"
                "DESCRIPCION_MODELO" -> "Descripción"
                "FECHA_INICIO" -> "Fecha de Inicio"
                "FECHA_FIN" -> "Fecha de Fin"
                "TIPO_OBLIGACION" -> "Clasificación"
                "ESTADO" -> "Estado"
                "REGIMEN" -> "Regimen"
                else -> " "
            }

            "Representantes Legales" -> when (key) {
                "IDENTIFICACION" -> "Identificación"
                "nroRelacion" -> "nroRelacion"                              // ?????
                "nroInternoIDRepresentante" -> "nroInternoIDRepresentante"  // ?????
                "NOMBRE" -> "Nombre"
                "ESTADO_CONTRIBUYENTE" -> "Registrado como Obligado Tributario"
                "FUENTE_CONTRIBUYENTE" -> "Fuente de información"
                "FECHA_DE_INICIO" -> "Fecha de Inicio"
                else -> " "
            }

            "Metodo de Facturacion" -> when (key) {
                "METODOFACTURACION" -> "Método Facturación"
                "FECHAINICIOFACT" -> "Fecha Inicio"
                "NUMERODOCUMENTO" -> "NUMERO DOCUMENTO"
                else -> " "
            }

            "Regimenes Especiales" -> when (key) {
                "Tipo_x0020_Regimen" -> "Tipo Régimen"
                "Fecha_x0020_de_x0020_inicio" -> "Fecha de inicio"
                "Documento_x0020_de_x0020_Alta" -> "Documento de Alta"
                "Documento_x0020_de_x0020_Baja" -> "Documento de Baja"
                "Estado" -> "Estado"
                else -> " "
            }

            "Factores de Retencion IVA" -> when (key) {
                "Ano" -> "Año"
                "Semestre" -> "Semestre"
                "FactorRetencion" -> "Factor Retención"
                "FechaVencimiento" -> "Fecha Vencimiento"
                "FechaCarga" -> "Fecha Carga"
                else -> " "
            }

            "Factories de Retencion Renta" -> when (key) {
                "Ano" -> "Año"
                "FechaCarga" -> "Fecha Carga"
                "FechaVencimiento" -> "Fecha Vencimiento"
                "FactorRetencion" -> "Factor Retención"
                else -> " "
            }

            "Información" -> when (key) {

                else -> " "
            }

            else -> " "
        }
        return result
    }

}
