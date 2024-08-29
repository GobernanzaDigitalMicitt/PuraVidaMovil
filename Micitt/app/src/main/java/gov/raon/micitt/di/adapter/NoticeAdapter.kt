package gov.raon.micitt.di.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import gov.raon.micitt.R
import gov.raon.micitt.models.response.NotificationData
import gov.raon.micitt.models.response.NotificationRes

class NoticeAdapter(private val notiCnt: Int, private val notiList:MutableList<NotificationData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var footerListener: () -> Unit

    companion object TYPE{
        val VIEW_TYPE_BODY = R.layout.noti_item
        val VIEW_TYPE_FOOTER = R.layout.layout_btn_more
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = when(viewType){
            VIEW_TYPE_BODY ->
                LayoutInflater.from(parent.context).inflate(R.layout.noti_item, parent, false)
                else ->
                LayoutInflater.from(parent.context).inflate(R.layout.layout_btn_more, parent, false)
        }
        when(viewType){
            VIEW_TYPE_FOOTER -> return FooterViewHolder(binding)
        }

        return NoticeViewHolder(binding)
    }

    override fun getItemCount(): Int = notiList.size

    fun setMoreNotification(listener: () -> Unit){
        this.footerListener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        notiList.let {
            if (position == it.size-1) {
                (holder as FooterViewHolder).bind()
            } else {
                (holder as NoticeViewHolder).bind(it[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == notiCnt){
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_BODY
        }
    }

    fun setOnItemClickListener(listener: NotificationRes) {

    }


    inner class NoticeViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {
        private val date: TextView = binding.findViewById(R.id.noti_date)
        private val title: TextView = binding.findViewById(R.id.noti_title)
        private val summary: TextView = binding.findViewById(R.id.noti_sum)
        private val line: View = binding.findViewById(R.id.noti_break_line)

        fun bind(notiData: NotificationData) {
            date.text = notiData.createdDt
            title.text = notiData.title
            summary.text = notiData.content

            if(notiData == notiList.last()){
                line.visibility = View.GONE
            } else {
                line.visibility = View.VISIBLE
            }
        }
    }

    inner class FooterViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {
        private val footer: ConstraintLayout = binding.findViewById(R.id.btn_more_cl)
        fun bind() {
            footer.setOnClickListener {
                footerListener()
            }
        }

    }
}
