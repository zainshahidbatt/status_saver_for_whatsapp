package com.example.statussaver.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.ithebk.statussaver.R
import com.example.statussaver.data.Status
import com.example.statussaver.ui.status.SavedStatusFragment
import com.example.statussaver.ui.status.StatusFragment


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "MAIN_ACTIVITY"
    private var showCount = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)
        if (supportActionBar != null) {
            supportActionBar?.elevation = 0f
        }

        init(savedInstanceState)
//        if(BuildConfig.DEBUG) {
//            val testDeviceIds = listOf(AdRequest.DEVICE_ID_EMULATOR)
//            val config = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
//            MobileAds.setRequestConfiguration(config)
//        }
//        MobileAds.initialize(this) {}
//        val adRequest = AdRequest.Builder().build()
//
//
//        InterstitialAd.load(
//            this,
//            if(BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-7898163058261734/7476853276",
//            adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    mInterstitialAd = null
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    mInterstitialAd = interstitialAd
//                }
//            })
//
//
//        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//            override fun onAdDismissedFullScreenContent() {
//                Log.d(TAG, "Ad was dismissed.")
//            }
//
//            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
//                Log.d(TAG, "Ad failed to show.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                Log.d(TAG, "Ad showed fullscreen content.")
//               // mInterstitialAd = null;
//            }
//        }


    }
    fun loadFullScreenAd() {
//        println(showCount)
//        if (mInterstitialAd != null && showCount %3 == 0) {
//            mInterstitialAd?.show(this)
//        } else {
//            Log.d("TAG", "The interstitial ad wasn't ready yet.")
//        }
        showCount++;
    }

    override fun onResume() {
        super.onResume()
    }

    private fun init(savedInstanceState: Bundle?) {
        mainViewModel.init()
        mainViewModel.statusList.observe(this, Observer { statusList ->
            setBottomNavigation(savedInstanceState, statusList)
        })
    }


    private fun setBottomNavigation(
        savedInstanceState: Bundle?,
        statusList: List<Status>
    ) {
        val statusListStr = Gson().toJson(statusList)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if (savedInstanceState == null) {
            val fragment = StatusFragment.newInstance(statusListStr, R.id.navigation_images)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
        navView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            loadFullScreenAd()
            when (menuItem.itemId) {
                R.id.navigation_images, R.id.navigation_videos -> {
                    val fragment = StatusFragment.newInstance(statusListStr, menuItem.itemId)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_saved -> {
                    val fragment = SavedStatusFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true

                }
            }
            false
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val appPackageName = packageName; // getPackageName() from Context or Activity object
        if (item.itemId == R.id.action_rate) {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")));
            } catch (ignore: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")));
            }
        }
        else if(item.itemId == R.id.action_share) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"Try this Awesome App 'Status Saver' which helps you in Saving all the WhatsApp Statuses..!\n" +
                    "https://play.google.com/store/apps/details?id=$appPackageName")
            startActivity(Intent(intent))
        }
        return true;
    }
}
