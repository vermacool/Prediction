package com.example.firebaseprediction

import android.content.Context
import android.content.SharedPreferences



/**
 * Created by Manish Verma on 08,Jul,2019
 * Girnarsoft Pvt. Ltd.,
 * Delhi NCR, India
 */
class PreferenceUtils(context: Context) {
    val PREF_NAME = "PLACE_PREFERENCE"
    val LAUNCH_TIME_KEY = "launch"

    init {
        preference =  context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    }

   companion object {
       var preference :SharedPreferences? = null

       }

       fun setLaunchPref(isFirstTime: Boolean){
            var editorData = preference?.edit()
            editorData?.putBoolean(LAUNCH_TIME_KEY,isFirstTime)
            editorData?.apply()
       }

       fun getLaunchCount():Boolean?{
           return preference?.getBoolean(LAUNCH_TIME_KEY,true)!!
       }

   }

