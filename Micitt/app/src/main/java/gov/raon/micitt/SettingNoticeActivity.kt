package gov.raon.micitt

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import gov.raon.micitt.databinding.ActivitySettingNoticeBinding
import gov.raon.micitt.di.adapter.NoticeAdapter
import gov.raon.micitt.di.common.BaseActivity
import gov.raon.micitt.di.data.NoticeModel

class SettingNoticeActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingNoticeBinding
    private lateinit var mAdapter: NoticeAdapter
    private val mData = mutableListOf<NoticeModel>()

    private var isLastPage = false
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.prev.visibility = View.VISIBLE
        binding.header.prev.setOnClickListener {
            finish()
        }

        with(mData) {
            add(
                NoticeModel(
                    "2024.01.01",
                    "testing1",
                    "이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 이거슨 테스트여 "
                )
            )
            add(NoticeModel("2024.01.02", "testing2", "이거슨 테스트여2"))
            add(NoticeModel("2024.01.03", "testing3", "이거슨 테스트여3"))
            add(NoticeModel("2024.01.04", "testing4", "이거슨 테스트여4"))
            add(NoticeModel("2024.01.05", "testing5", "이거슨 테스트여5"))
            add(NoticeModel("2024.01.06", "testing6", "이거슨 테스트여6"))
        }

        val adapter = NoticeAdapter()
        adapter.notiList = mData

        setNotifications()

//        binding.notiList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                val position = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
//                val totalCnt = recyclerView.adapter?.itemCount?.minus(1)
//
//                if (position == totalCnt && !isLastPage && totalCnt >= 0) {
//                    page ++
//
//                }
//            }
//        })

    }

    private fun getIsLastPage(dbSize: Int) {
        val rvPageSplit = (mAdapter.itemCount / 10.0f).toString().split(".")
        val dbPageSplit = (dbSize / 10.0f).toString().split(".")

        val rvPage = if (rvPageSplit[1] == "0") {
            rvPageSplit[0].toInt()
        } else {
            (rvPageSplit[0].toInt()) + 1
        }

        val dbPage = if (dbPageSplit[1] == "0") {
            dbPageSplit[0].toInt()
        } else {
            (dbPageSplit[0].toInt()) + 1
        }

        isLastPage = rvPage == dbPage
    }

    private fun setNotifications() {
        val adapter = NoticeAdapter()
        adapter.notiList = mData
        if (adapter.itemCount > 0) {
            binding.notiList.adapter = adapter
            binding.notiList.layoutManager = LinearLayoutManager(this)

            binding.noAlert.visibility = View.GONE
        }

    }

}