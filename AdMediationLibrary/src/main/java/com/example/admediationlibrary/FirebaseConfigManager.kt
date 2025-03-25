package com.example.admediationlibrary

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.json.JSONObject

object FirebaseConfigManager {
  private lateinit var remoteConfig: FirebaseRemoteConfig
  private var adUnitIds: MutableMap<String, String> = mutableMapOf()

  fun init(context: Context) {
    FirebaseApp.initializeApp(context)

    remoteConfig = FirebaseRemoteConfig.getInstance()
    val configSettings = FirebaseRemoteConfigSettings.Builder()
      .setMinimumFetchIntervalInSeconds(0)
      .build()

    remoteConfig.setConfigSettingsAsync(configSettings)

    fetchRemoteConfig()
  }

  private fun fetchRemoteConfig() {
    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
      Log.d("AdMedia", "fetchRemoteConfig: ${task.isSuccessful}")
      if (task.isSuccessful) {
        val configData = remoteConfig.getString("ad_unit_ids")
        parseConfigData(configData)
        Log.d("AdMedia", "Fetched Ad Unit IDs: $adUnitIds")
      } else {
        Log.e("AdMedia", "Failed to fetch Remote Config")
      }
    }

  }

  private fun parseConfigData(jsonData: String) {
    try {
      val jsonObject = JSONObject(jsonData)
      adUnitIds["AdMob"] = jsonObject.optString("AdMob", "default_admob_id")
      adUnitIds["AppLovin"] = jsonObject.optString("AppLovin", "default_applovin_id")
      adUnitIds["Adx"] = jsonObject.optString("Adx", "default_adx_id")
    } catch (e: Exception) {
      Log.e("AdMedia", "Error parsing Remote Config JSON", e)
    }
  }

  private fun parseConfig(json: String) {
    try {
      val jsonObject = JSONObject(json)
      adUnitIds.clear()

      for (key in jsonObject.keys()) {
        jsonObject.getJSONObject(key).let {
          adUnitIds[key] = it.getString("ad_units")
        }
      }
    } catch (e: Exception) {
      Log.e("AdMedia", " Error parsing Firebase JSON: ${e.message}")
    }
  }

  fun getAdUnitId(network: String): String {
    return adUnitIds[network] ?: "default_ad_unit"
  }
}



