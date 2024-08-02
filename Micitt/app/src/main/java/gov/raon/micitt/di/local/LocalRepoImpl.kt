package gov.raon.micitt.di.local

import android.content.Context
import io.realm.Realm

class LocalRepoImpl(val realm: Realm?) : LocalRepository {
    override suspend fun create(creater: (realm: Realm) -> Unit) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realmTransaction ->
                creater(realmTransaction)
            }
        }
    }

    class Where {
        var key: String? = null
        var value: String? = null
    }


    /// Shared Pref
    private val DEFAULT_PREF_NAME = "micitt"

    override fun removeAllPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    override fun setInt(context: Context, key: String, value: Int) {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    override fun setBoolean(context: Context, key: String, value: Boolean?) {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    override fun setString(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun setLong(context: Context, key: String, value: Long) {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    override fun getInt(context: Context, key: String, defaultValue: Int): Int {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, defaultValue)
    }

    override fun getBoolean(context: Context, key: String, defaultValue: Boolean?): Boolean {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, defaultValue!!)
    }

    override fun getString(context: Context, key: String, defaultVal: String?): String? {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultVal)
    }

    override fun getLong(context: Context, key: String, defaultVal: Long): Long {
        val sharedPreferences = context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(key, defaultVal)
    }

}