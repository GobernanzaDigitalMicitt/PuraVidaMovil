package gov.raon.micitt.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gov.raon.micitt.R
import gov.raon.micitt.models.response.AgencyInfo
import gov.raon.micitt.utils.Log

class AgencyAdapter(val context: Context, val itemList: MutableList<AgencyInfo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mEmitirListener: ((AgencyInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_agency, parent, false)
        return AgencyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AgencyViewHolder).bind(itemList[position], mEmitirListener)
    }

    fun setEmitirListener(listener: (AgencyInfo) -> Unit) {
        this.mEmitirListener = listener
    }

    fun addList(list: MutableList<AgencyInfo>) {
        this.itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    inner class AgencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvAgencyName: TextView
        var layerAgencyEmitir: ViewGroup
        var tvAgencyEmitir: TextView

        init {
            tvAgencyName = view.findViewById(R.id.tv_agency_name)
            layerAgencyEmitir = view.findViewById(R.id.layer_agency_emitir)
            tvAgencyEmitir = view.findViewById(R.id.tv_agency_emitir)
        }

        fun bind(item: AgencyInfo, mEmitirListener: ((AgencyInfo) -> Unit)?) {

            tvAgencyName.text = item.agencyName

            // 예외처리해야함

            if(item.agencyCode == "0000" || item.agencyCode == "9999") {
                tvAgencyName.setTextColor(context.getColor(R.color.Font_G60))
                tvAgencyEmitir.text = "Emitido"
            } else {
                layerAgencyEmitir.setOnClickListener {
                    Log.d("oykwon", "item : " + item.agencyName)
                    mEmitirListener?.let { it1 -> it1(item) }
                }
            }
        }

    }

}