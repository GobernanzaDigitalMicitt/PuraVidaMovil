package gov.raon.micitt.di.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gov.raon.micitt.databinding.NotiItemBinding
import gov.raon.micitt.di.data.NoticeModel

class NoticeAdapter : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {
    var notiList = mutableListOf<NoticeModel>()
    var mItem: ((NoticeModel)-> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = NotiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun getItemCount(): Int = notiList.size

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(notiList[position])
    }

    fun setOnItemClickListener(listener: NoticeModel){

    }


    inner class NoticeViewHolder(private val binding: NotiItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notiData: NoticeModel) {
            if (notiData == notiList.lastOrNull()) { binding.notiBreakLine.visibility = View.GONE }

            binding.notiDate.text = notiData.date
            binding.notiTitle.text = notiData.title
            binding.notiSum.text = notiData.summary

            binding.notiItemRl.setOnClickListener{

            }
        }
    }
}
