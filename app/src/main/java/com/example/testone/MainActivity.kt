package com.example.testone

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.admediationlibrary.AdLoader
import com.example.admediationlibrary.AdMediationManager
import com.example.admediationlibrary.AdStrategy

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    AdMediationManager.init(this)
    // Set ad strategy (Optional: Change to AdStrategy.PERCENTAGE_BASED if needed also setAdNetworkWeights)
    AdMediationManager.setAdStrategy(AdStrategy.ROUND_ROBIN)
    // Find the ad container
    val adContainer = findViewById<FrameLayout>(R.id.ad_container)
    AdLoader.loadAd(this, adContainer)
  }
}
