package com.example.firebaseprediction

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var bundle: Bundle
    private var TAG = MainActivity::class.java.canonicalName

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bundle = Bundle()
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        var preferenceUtils = PreferenceUtils(this@MainActivity)
        if(preferenceUtils.getLaunchCount()!=null && preferenceUtils.getLaunchCount()!!){
            preferenceUtils.setLaunchPref(false)
            bundle.getBoolean(FirebaseAnalytics.Param.ITEM_NAME, true)
            firebaseAnalytics.logEvent("FIRST_LAUNCH", bundle)

        }

        bundle.getBoolean(FirebaseAnalytics.Param.ITEM_NAME, true)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)

        fab.setOnClickListener { view ->
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Call Placed")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "fab")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }


        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        // -------------------------------------------------------------------------------------
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                var d = Log.d(TAG, "Key: $key Value: $value")
            }
        }
        // [END handle_data_extras]



        subscribeButton.setOnClickListener {
            Log.d(TAG, "Subscribing to weather topic")
            // [START subscribe_topics]
            FirebaseMessaging.getInstance().subscribeToTopic("game")
                .addOnCompleteListener { task ->
                    var msg = getString(R.string.msg_subscribed)
                    if (!task.isSuccessful) {
                        msg = getString(R.string.msg_subscribe_failed)
                    }
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }
            subscribeButton.visibility = View.GONE
            // [END subscribe_topics]
        }


        fab.setOnClickListener {
            // Get token
            // [START retrieve_current_token]
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })
            // [END retrieve_current_token]
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Setting")
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Setting")
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
                true
            }
            R.id.action_purchase -> {
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Handbag")
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"Accessories" )
                bundle.putLong(FirebaseAnalytics.Param.QUANTITY,1)
                bundle.putDouble(FirebaseAnalytics.Param.PRICE,1000.80)
                bundle.putString(FirebaseAnalytics.Param.CURRENCY,"INR")
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, bundle)
                true
            }
            R.id.action_game -> {
                bundle.putLong(FirebaseAnalytics.Param.SCORE, 2300)
                bundle.putLong(FirebaseAnalytics.Param.LEVEL, 3)
                bundle.putString(FirebaseAnalytics.Param.CHARACTER, "avenger")
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE, bundle)
                true

            }
            R.id.action_theme -> {
                var calendar = Calendar.getInstance();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3")
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"Accessories" )
                bundle.putLong(FirebaseAnalytics.Param.QUANTITY,1)
                bundle.putDouble(FirebaseAnalytics.Param.PRICE,1000.80)
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Handbag")
                bundle.putString(FirebaseAnalytics.Param.CURRENCY,"INR")
                bundle.putString(FirebaseAnalytics.Param.START_DATE,"${Calendar.DAY_OF_MONTH }  - ${Calendar.MONTH} - ${Calendar.YEAR}")
                bundle.putString(FirebaseAnalytics.Param.END_DATE,"${Calendar.DAY_OF_MONTH }  - ${Calendar.MONTH+1} - ${Calendar.YEAR}")
                bundle.putString(FirebaseAnalytics.Param.ORIGIN,"DELHI")
                bundle.putString(FirebaseAnalytics.Param.DESTINATION,"GURUGRAM")
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, bundle)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
