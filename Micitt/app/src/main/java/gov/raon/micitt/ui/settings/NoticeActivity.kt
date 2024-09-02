package gov.raon.micitt.ui.settings

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivitySettingNoticeBinding
import gov.raon.micitt.di.adapter.NoticeAdapter
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.models.NotificationModel
import gov.raon.micitt.models.response.NotificationData
import gov.raon.micitt.utils.Log
import kotlinx.serialization.json.JsonObject

@AndroidEntryPoint
class NoticeActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingNoticeBinding
    private val notiViewModel: NotiViewModel by viewModels()

    private lateinit var notiAdapter: NoticeAdapter

    private val pageNum = 1 // 현재 페이지 갯수
    private var pageCnt = 3 // 페이지 내에 있는 공지사항 갯수 (둘다 0인 경우 전체 출력)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.prev.visibility = View.VISIBLE
        binding.header.prev.setOnClickListener {
            finish()
        }

        initObservers()

        val notificationModel = NotificationModel(pageNum, pageCnt)
        notiViewModel.getNotice<JsonObject>(this, notificationModel)

    }

    private fun initObservers() {
        notiViewModel.liveList.observe(this) {
            if (binding.notiList.adapter == null) {
                if (it.resultData.notificationList.size > 0) {
                    binding.notiEmpty.visibility = View.GONE
                } else {
                    binding.notiEmpty.visibility = View.VISIBLE
                }
                setNotifications(it.resultData.notificationCnt, it.resultData.notificationList)
            } else {
                (binding.notiList.adapter as NoticeAdapter).addList(it.resultData.notificationList)
            }
        }
    }

    private fun setNotifications(notiCnt: Int, notiList: MutableList<NotificationData>) {
        val adapter = NoticeAdapter(notiCnt, notiList)
        binding.notiList.layoutManager = LinearLayoutManager(this)
        binding.notiList.adapter = adapter
        adapter.setMoreNotification {
            val size = (binding.notiList.adapter as NoticeAdapter).itemCount
            val notificationModel = NotificationModel(0, 0)
            notiViewModel.getNotice<JsonObject>(this,notificationModel)
        }
    }
}