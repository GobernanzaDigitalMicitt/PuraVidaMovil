package gov.raon.micitt

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivitySplashBinding
import gov.raon.micitt.ui.main.MainActivity
import gov.raon.micitt.utils.PermissionHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private var permissionHelper: PermissionHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPushPermission()
    }

    fun checkPushPermission() {
        if (permissionHelper == null) {
            permissionHelper = PermissionHelper()
        }

        if (Build.VERSION.SDK_INT >= 33) {
            permissionHelper!!.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_MEDIA_LOCATION)
            ) { isGranted ->
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1500)
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                }

            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1500)
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestPermissionsResult(requestCode, grantResults);
    }

    override fun onBackPressed() {
        this.moveTaskToBack(true)
        this.finishAndRemoveTask()
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}