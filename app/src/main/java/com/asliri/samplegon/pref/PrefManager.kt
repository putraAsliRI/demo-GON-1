package com.asliri.samplegon.pref

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {

    private val PREF_NAME = "MyAppPreferences"

    private val KEY_IS_ALREADY_REGISTER = "isAlreadyRegister"
    private val KEY_PHONE_NUMBER = "phoneNumber"
    private val KEY_EMAIL = "email"
    private val KEY_IS_FIRST_LAUNCH = "isFirstLaunch"

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // ----- isAlreadyRegister -----
    fun setAlreadyRegister(value: Boolean) {
        sharedPref.edit().putBoolean(KEY_IS_ALREADY_REGISTER, value).apply()
    }

    fun isAlreadyRegister(): Boolean {
        return sharedPref.getBoolean(KEY_IS_ALREADY_REGISTER, false)
    }

    // ----- Phone Number -----
    fun setPhoneNumber(phone: String) {
        sharedPref.edit().putString(KEY_PHONE_NUMBER, phone).apply()
    }

    fun getPhoneNumber(): String? {
        return sharedPref.getString(KEY_PHONE_NUMBER, null)
    }

    // ----- Email -----
    fun setEmail(email: String) {
        sharedPref.edit().putString(KEY_EMAIL, email).apply()
    }

    fun getEmail(): String? {
        return sharedPref.getString(KEY_EMAIL, null)
    }

    // ----- First Launch -----
    fun isFirstLaunch(): Boolean {
        val first = sharedPref.getBoolean(KEY_IS_FIRST_LAUNCH, true)
        if (first) {
            // kalau pertama kali, set jadi false
            sharedPref.edit().putBoolean(KEY_IS_FIRST_LAUNCH, false).apply()
        }
        return first
    }

    // ----- Clear All (misalnya pas logout) -----
    fun clear() {
        sharedPref.edit().clear().apply()
    }
}
