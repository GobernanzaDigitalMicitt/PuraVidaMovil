package gov.raon.micitt.di.local

import android.content.Context
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.Sort

interface LocalRepository {

    // Realm Database
    suspend fun create(creater: (realm: Realm) -> Unit)

    // Shared Pref
    fun removeAllPreferences(context: Context)
    fun setInt(context: Context, key: String, value: Int)
    fun setBoolean(context: Context, key: String, value: Boolean?)
    fun setString(context: Context, key: String, value: String)
    fun setLong(context: Context, key: String, value: Long)
    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int
    fun getBoolean(context: Context, key: String, defaultValue: Boolean?): Boolean
    fun getString(context: Context, key: String, defaultVal: String? = null): String?
    fun getLong(context: Context, key: String, defaultVal: Long): Long

}