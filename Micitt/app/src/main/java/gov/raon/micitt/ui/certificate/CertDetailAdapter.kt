package gov.raon.micitt.ui.certificate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gov.raon.micitt.R
import gov.raon.micitt.ui.certificate.model.ParentItem
import gov.raon.micitt.utils.Log

/*
1번째 Table   > Actividades Económicas
2번째 Table2  > Representantes Legales        :: nroRelacion / nroInternoIDRepresentatne 없음, 웹에서 표기 안함
3번째 Table1  > Obligaciones Tributarias      :: TIPO_OBLIGATION -> Classification
4번째 Table5  > Regimenes Especiales
6번째 Table4  > Metodo de Facturacion         :: Numberodocumento 웹에서 표기 안함
7번째 Table6  > Factores de Retencion IVA
8번째 Table7  > Factories de Retencion Renta
 */

class CertDetailAdapter(
    private val pItem: Map<Int, List<ParentItem>>,
    private val removedList : List<Pair<String,String>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: () -> Unit

    companion object TYPE {
        val VIEW_TYPE_DETAIL_TITLE = R.layout.cert_detail_item
        val VIEW_TYPE_CARD = R.layout.cert_detail_item // 나중에 CardView 레이아웃으로 수정할 예정
    }

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.cert_detail_item, parent, false)

        return when (viewType) {
            VIEW_TYPE_CARD -> DetailTitleViewHolder(binding)
            else -> DetailTitleViewHolder(binding)
        }
    }

    fun setCertDetail(listener: () -> Unit) {
        this.listener = listener
    }

    // 뷰 타입 반환 (현재 모든 항목은 DETAIL 타입으로 처리)
    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_DETAIL_TITLE
    }

    override fun getItemCount(): Int {
        return pItem.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailTitleViewHolder) {
            holder.bind(pItem.keys.toList()[position])
        }
    }

    inner class CardViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {
        // TODO: 나중에 CardView 관련 코드 추가
    }

    // DetailViewHolder 클래스
    inner class DetailTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.detail_item)
        private val btnMore: RelativeLayout = itemView.findViewById(R.id.arrow_up_rl)
        private val elemContainer: LinearLayout = itemView.findViewById(R.id.cert_detail_elements)
        private val line : View = itemView.findViewById(R.id.cert_bl)

//        private val parser = Parser()

        // ParentItem을 바인딩하는 함수
        fun bind(parentItem: Int) {
            title.text = tableTitle(parentItem)

//            Log.d("DUKE","PARSER : ${parser.getDocument()}")

            btnMore.setOnClickListener {
                elemContainer.visibility = if (elemContainer.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                line.visibility = if (line.visibility == View.VISIBLE) View.GONE else View.VISIBLE

                pItem[parentItem]!!.toList().forEach { it1 ->
                    elemContainer.removeAllViews()
                    for(elem in it1.elements){
                        val cView = LayoutInflater.from(itemView.context).inflate(R.layout.cert_detail, elemContainer, false)
                        val key : TextView = cView.findViewById(R.id.cert_key)
                        val value : TextView = cView.findViewById(R.id.cert_value)
                        Log.d("DUKE","key : $key // value : $value" )

                        key.text = elem.key
                        value.text = elem.value

                        elemContainer.addView(cView)

                    }
                }
            }

        }
    }

    // tableNum에 따른 제목을 반환하는 함수
    private fun tableTitle(tableNum: Int): String {
        val titleList = mapOf(
            0 to "Actividades Económicas",
            1 to "Obligaciones Tributarias",
            2 to "Representantes Legales",
            4 to "Metodo de Facturacion",
            5 to "Regimenes Especiales",
            6 to "Factores de Retencion IVA",
            7 to "Factories de Retencion Renta",
            8 to "Información"
        )

        return titleList[tableNum]!!
    }
}
