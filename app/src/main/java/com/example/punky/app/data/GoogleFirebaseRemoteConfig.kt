package com.example.punky.app.data

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.punky.core.data.RemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.coroutines.resume

class GoogleFirebaseRemoteConfig @Inject constructor(
    private val config: FirebaseRemoteConfig,
    private val timeoutMillis: Long = 7 * 1000L
) : RemoteConfig {

    override var isUpdated: Boolean = false
        private set

    override fun getString(key: String): String = config.getString(key)
    override fun getBoolean(key: String): Boolean = config.getBoolean(key)
    override fun getLong(key: String): Long = config.getLong(key)
    override fun getDouble(key: String): Double = config.getDouble(key)

    override suspend fun fetch(): RemoteConfig = withTimeoutOrNull(timeoutMillis) {
        withContext(Dispatchers.IO){
            suspendCancellableCoroutine { continuation ->

                Log.d("AppInit", "RemoteConfig ensureInitialized()")
                config.ensureInitialized().addOnCompleteListener {
                    Log.d("AppInit", "RemoteConfig initialized: ${it.isSuccessful}")

                    if(!it.isSuccessful){
                        continuation.resume(this@GoogleFirebaseRemoteConfig)
                        return@addOnCompleteListener
                    }

                    Log.d("AppInit", "RemoteConfig fetchAndActivate()")
                    config.fetchAndActivate().addOnCompleteListener {
                        Log.d("AppInit", "RemoteConfig fetchAndActivate: ${it.isSuccessful}")

                        isUpdated = it.isSuccessful
                        continuation.resume(this@GoogleFirebaseRemoteConfig)
                    }
                }

                continuation.invokeOnCancellation {
                    Log.d("AppInit", "RemoteConfig timeout $timeoutMillis")
                }
            }
        }
    } ?: this@GoogleFirebaseRemoteConfig

    override suspend fun all(): Map<String, String> {
        return buildMap {
            config.all.entries.forEach { (key, value) ->
                put( key, value.asString() )
            }
        }
    }
}