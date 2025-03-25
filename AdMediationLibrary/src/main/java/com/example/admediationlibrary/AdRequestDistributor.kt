package com.example.admediationlibrary

import android.util.Log

object AdRequestDistributor {
  private var lastAdNetworkIndex = 0
  private var adStrategy: AdStrategy = AdStrategy.ROUND_ROBIN
  private var adNetworkWeights: Map<String, Int> = mapOf(
    "AdMob" to 50,
    "IronSource" to 10,
    "AppLovin" to 40
  )

  private val adNetworks = listOf("AdMob", "IronSource", "AppLovin")

  fun setAdStrategy(strategy: AdStrategy) {
    adStrategy = strategy
  }

  fun setAdNetworkWeights(weights: Map<String, Int>) {
    adNetworkWeights = weights
  }

  fun selectAdNetwork(): String {
    return when (adStrategy) {
      AdStrategy.ROUND_ROBIN -> selectRoundRobin()
      AdStrategy.PERCENTAGE_BASED -> selectByPercentage()
    }
  }

  private fun selectRoundRobin(): String {
    val network = adNetworks[lastAdNetworkIndex % adNetworks.size]
    lastAdNetworkIndex++
    Log.d("AdMedia", "Selected Network (Round-Robin): $network")
    return network
  }

  private fun selectByPercentage(): String {
    val totalWeight = adNetworkWeights.values.sum()
    val randomValue = (1..totalWeight).random()

    var cumulativeWeight = 0
    for ((network, weight) in adNetworkWeights) {
      cumulativeWeight += weight
      if (randomValue <= cumulativeWeight) {
        Log.d("AdMedia", "Selected Network (Percentage-Based): $network")
        return network
      }
    }
    return "AdMob"  // Default fallback
  }
}
