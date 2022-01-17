package com.libertosforever.note.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.libertosforever.note.R
import com.libertosforever.note.billing.BillingManager
import com.libertosforever.note.databinding.ActivityMainBinding
import com.libertosforever.note.dialogs.NewListDialog
import com.libertosforever.note.fragments.FragmentManager
import com.libertosforever.note.fragments.NoteFragment
import com.libertosforever.note.fragments.ShopListNamesFragment
import com.libertosforever.note.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private lateinit var defPreferences: SharedPreferences
    private var currentMenuItemId = R.id.shop_list
    private var currentTheme = ""
    private var iAd: InterstitialAd? = null
    private var adShowCounter = 0
    private var adShowCounterMax = 3
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        defPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPreferences.getString("theme_key", "orange").toString()
        setTheme(getSelectedTheme())

        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()
        if (!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) loadInterAd()
    }

    private fun loadInterAd() {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    iAd = ad
                }
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    iAd = null
                }
            })
    }

    private fun showInterAd(adListener: AdListener) {
        if (iAd != null && adShowCounter > adShowCounterMax && !pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) {
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdFailedToShowFullScreenContent(ad: AdError) {
                    iAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            adShowCounter = 0
            iAd?.show(this)

        } else {
            adShowCounter++
            adListener.onFinish()
        }
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemReselectedListener {
            when (it.itemId) {

                R.id.settings -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity :: class.java))
                        }

                    })

                }

                R.id.notes -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            currentMenuItemId = R.id.notes
                            FragmentManager.setFragment(NoteFragment.newInstance(), this@MainActivity)
                        }

                    })
                }

                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }

                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
        if (defPreferences.getString("theme_key", "orange") != currentTheme) recreate()

    }

    private fun getSelectedTheme(): Int {
        return if (defPreferences.getString("theme_key", "orange") == "orange") {
            R.style.Theme_NoteOrange
        } else {
            R.style.Theme_NoteBlue
        }
    }

    override fun onClick(name: String) {
        Log.d("MyLog", "Name: $name")
    }

    interface AdListener {
        fun onFinish()
    }
}