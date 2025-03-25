package com.example.admediationlibrary

import android.content.Context

object AdMediationManager {
  fun init(context: Context) {
    FirebaseConfigManager.init(context)
  }

  fun setAdStrategy(strategy: AdStrategy) {
    AdRequestDistributor.setAdStrategy(strategy)
  }

  fun setAdNetworkWeights(weights: Map<String, Int>) {
    AdRequestDistributor.setAdNetworkWeights(weights)
  }
}

