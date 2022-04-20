package com.example.taxibookinguserapplication.util

import android.content.Context
import android.content.SharedPreferences


class SharedPreferenceUtils private constructor(protected var mContext: Context) {
    private val mSharedPreferences: SharedPreferences
    private val mSharedPreferencesEditor: SharedPreferences.Editor?

    /**
     * to get key is stored in DB
     */
    fun getContainsKey(key: String?): Boolean {
        return mSharedPreferences.contains(key)
    }

    /**
     * Stores String value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: String?) {
        mSharedPreferencesEditor!!.putString(key, value)
        mSharedPreferencesEditor.commit()
    }

    /**
     * Stores String value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setStringValue(key: String?, value: String?) {
        mSharedPreferencesEditor!!.putString(key, value)
        mSharedPreferencesEditor.commit()
    }

    /**
     * Stores int value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: Int) {
        mSharedPreferencesEditor!!.putInt(key, value)
        mSharedPreferencesEditor.commit()
    }

    /**
     * Stores Double value in String format in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: Double) {
        setValue(key, java.lang.Double.toString(value))
    }

    /**
     * Stores long value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: Long) {
        mSharedPreferencesEditor!!.putLong(key, value)
        mSharedPreferencesEditor.commit()
    }

    /**
     * Stores boolean value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setBoolanValue(key: String?, value: Boolean) {
        mSharedPreferencesEditor!!.putBoolean(key, value)
        mSharedPreferencesEditor.commit()
    }

    /**
     * Retrieves String value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getStringValue(key: String?, defaultValue: String?): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    /**
     * Retrieves int value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getIntValue(key: String?, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    /**
     * Retrieves long value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getLongValue(key: String?, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    /**
     * Retrieves boolean value from preference
     *
     * @param keyFlag      key of preference
     * @param defaultValue default value if no key found
     */
    fun getBoolanValue(keyFlag: String?, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(keyFlag, defaultValue)
    }

    /**
     * Removes key from preference
     *
     * @param key key of preference that is to be deleted
     */
    fun removeKey(key: String?) {
        if (mSharedPreferencesEditor != null) {
            mSharedPreferencesEditor.remove(key)
            mSharedPreferencesEditor.commit()
        }
    }

    /**
     * Clears all the preferences stored
     */
    fun clear() {
        mSharedPreferencesEditor!!.clear().commit()
    }

    fun clearPrefData() {
//        mSharedPreferencesEditor
//                .putString(MOBILE, "")
//                .putInt(CURRENCY, 0)
//                .putString(PAYMENTSUCESS, "")
//                .putString(CURRENCY_SYMBOL, "")
//                .putString(AUTH_TOKEN, "")
//                .putBoolean(REGISTERED, false)
//                .apply();
    }

    companion object {
        private var mSharedPreferenceUtils: SharedPreferenceUtils? = null

        /**
         * Creates single instance of SharedPreferenceUtils
         *
         * @param context context of Activity or Service
         * @return Returns instance of SharedPreferenceUtils
         */
        @Synchronized
        fun getInstance(context: Context): SharedPreferenceUtils? {
            if (mSharedPreferenceUtils == null) {
                mSharedPreferenceUtils = SharedPreferenceUtils(context.applicationContext)
            }
            return mSharedPreferenceUtils
        }
    }

    init {
        mSharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        mSharedPreferencesEditor = mSharedPreferences.edit()
    }
}