package gov.raon.micitt

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import gov.raon.micitt.utils.Util

@HiltAndroidApp
class MicittApplication : MultiDexApplication() {
    companion object {
        lateinit var prefs: SharedPreferences
        lateinit var hashedNid: String
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        prefs = applicationContext.getSharedPreferences("micitt", Context.MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun setNid(nID:String) {
        hashedNid = Util.hashSHA256(nID).toString()
    }

    fun getHashedNid(): String {
        return hashedNid
    }


}