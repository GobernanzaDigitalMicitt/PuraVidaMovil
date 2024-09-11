package gov.raon.micitt.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gov.raon.micitt.R
import gov.raon.micitt.models.SaveDocumentModel
import gov.raon.micitt.utils.Log

class DocumentAdapter(val context: Context, val itemList: MutableList<SaveDocumentModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var documentClickListener: ((SaveDocumentModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DocumentViewHolder).bind(itemList[position], documentClickListener)
    }

    fun setDocumentClickListener(listener: (SaveDocumentModel) -> Unit) {
        this.documentClickListener = listener
    }

    fun addList(list: MutableList<SaveDocumentModel>) {
        this.itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    inner class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val layerDocument: ViewGroup
        init {
            layerDocument = view.findViewById(R.id.layer_document)
        }

        fun bind(item: SaveDocumentModel, documentClickListener: ((SaveDocumentModel) -> Unit)?) {
            layerDocument.setOnClickListener {
                documentClickListener?.let { it1 -> it1(item) }
            }
        }

    }

}