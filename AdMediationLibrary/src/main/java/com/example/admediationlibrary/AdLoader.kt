package com.example.admediationlibrary

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.applovin.adview.AppLovinAdView
import com.applovin.sdk.AppLovinAdSize
import com.applovin.sdk.AppLovinAdLoadListener
import com.applovin.sdk.AppLovinAd
import com.google.android.gms.ads.AdView

object AdLoader {
  fun loadAd(context: Context, container: FrameLayout) {
    val selectedNetwork = AdRequestDistributor.selectAdNetwork()
    val adUnitId = FirebaseConfigManager.getAdUnitId(selectedNetwork)

    Log.d("AdMedia", "Trying to load ad from $selectedNetwork")

    when (selectedNetwork) {
      "AdMob" -> loadAdMobAd(context, container, adUnitId)
      "AppLovin" -> loadAppLovinAd(context, container)
      "Adx" -> loadAdMobAd(context, container, adUnitId)
      else -> fallbackAd(context, container)
    }
  }

  private fun loadAdMobAd(context: Context, container: FrameLayout, adUnitId: String) {
    val adView = AdView(context)
    adView.adUnitId = adUnitId
    adView.setAdSize(AdSize.BANNER)
    container.addView(adView)

    adView.adListener = object : AdListener() {
      override fun onAdLoaded() {
        Log.d("AdMedia", "AdMob ad loaded successfully")
      }

      override fun onAdFailedToLoad(error: LoadAdError) {
        Log.e("AdMedia", "AdMob failed to load: ${error.message}")
        fallbackAd(context, container)
      }
    }
    adView.loadAd(AdRequest.Builder().build())
  }

  private fun loadAppLovinAd(context: Context, container: FrameLayout) {
    Log.d("AdMedia", "Loading AppLovin ad...")
    val adView = AppLovinAdView(AppLovinAdSize.BANNER, context)
    container.addView(adView)
    adView.setAdLoadListener(object : AppLovinAdLoadListener {
      override fun adReceived(ad: AppLovinAd?) {
        Log.d("AdMedia", "AppLovin ad loaded successfully")
      }

      override fun failedToReceiveAd(errorCode: Int) {
        Log.e("AdMedia", "AppLovin failed to load: Error Code $errorCode")
        fallbackAd(context, container)
      }
    })
    adView.loadNextAd()
  }

  private fun fallbackAd(context: Context, container: FrameLayout) {
    Log.d("AdMedia", "Falling back to another ad network...")
    loadAd(context, container)  // Retry with the next ad network
  }
}