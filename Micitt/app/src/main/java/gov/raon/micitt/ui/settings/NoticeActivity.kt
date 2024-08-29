package gov.raon.micitt.ui.settings

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivitySettingNoticeBinding
import gov.raon.micitt.di.adapter.NoticeAdapter
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.models.response.NotificationData
import kotlinx.serialization.json.JsonObject

@AndroidEntryPoint
class NoticeActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingNoticeBinding
    private val notiViewModel : NotiViewModel by viewModels()

    private val pageNum = 0
    private var pageCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.prev.visibility = View.VISIBLE
        binding.header.prev.setOnClickListener {
            finish()
        }

        initObservers()
        notiViewModel.getNotice<JsonObject>(this, 0, 0)

    }

    private fun initObservers(){
        notiViewModel.liveList.observe(this){
            if(it.resultData != null){
                binding.notiEmpty.visibility = View.GONE
                setNotifications(it.resultData[0].notificationCnt, it.resultData[0].notificationList)
            } else {
                binding.notiTitle.visibility = View.VISIBLE
            }
        }
    }

    private fun setNotifications(notiCnt: Int, notiList: MutableList<NotificationData>) {
        val adapter = NoticeAdapter(notiCnt, notiList)
        binding.notiList.adapter = adapter
        adapter.setMoreNotification {
            val currentSize = (binding.notiList.adapter as NoticeAdapter).itemCount
            notiViewModel.getNotice<JsonObject>(this,currentSize/pageCnt, pageCnt)

        }
    }
}