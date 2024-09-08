package gov.raon.micitt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gov.raon.micitt.databinding.ActivitySplashBinding
import gov.raon.micitt.ui.home.HomeActivity
import gov.raon.micitt.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathFactory

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
//            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}