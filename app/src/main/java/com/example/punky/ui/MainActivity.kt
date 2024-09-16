package com.example.punky.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import coil.compose.rememberAsyncImagePainter
import com.example.punky.ui.components.LocalBroadcastReceiver
import com.example.punky.ui.components.imageRequest
import com.example.punky.ui.components.rememberGifImageLoader
import com.example.punky.ui.dialogs.FullScreenImageDialog
import com.ezam.rickandmorty.R
import com.ezam.rickandmorty.ui.character.CharacterCardViewModel
import com.ezam.rickandmorty.ui.character.CharacterCardScreen
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.ktx.updatePriority
import com.punky.core.utils.createAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appUpdateManager: AppUpdateManager

    private val vmodel: CharacterCardViewModel by viewModels()

    private val updateResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode != RESULT_OK) {
                val action = createAction(ACTION_LOCK_SCREEN)
                LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(action))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForUpdates()
        setContent {

            var blockScreen by remember {
                mutableStateOf(false)
            }

            LocalBroadcastReceiver(systemAction = createAction(ACTION_LOCK_SCREEN)) {
                blockScreen = true
            }

            if (blockScreen) {
                LockScreen(closeApp = ::closeApp, checkForUpdates = ::checkForUpdates)
            } else {
                val state by vmodel.getState().collectAsStateWithLifecycle()

                CharacterCardScreen(state = state) {
                    vmodel.nexRandom()
                }

                LaunchedEffect(Unit) {
                    vmodel.nexRandom()
                }
            }
        }
    }


    private fun checkForUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            Log.v("checkForUpdates", "updateAvailability: ${info.updateAvailability()}")
            Log.v("checkForUpdates", "updatePriority: ${info.updatePriority}")
            Log.v(
                "checkForUpdates",
                "IMMEDIATE: ${info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)}"
            )
            Log.v(
                "checkForUpdates",
                "FLEXIBLE: ${info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)}"
            )
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {

                if (info.updatePriority == 5 && info.isImmediateUpdateAllowed) {
                    val options = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    appUpdateManager.startUpdateFlowForResult(info, updateResult, options)
                }
            }
        }.addOnFailureListener {
            Log.v("checkForUpdates", "message: ${it.message}")
            Log.v("checkForUpdates", "cause: ${it.cause}")
            Log.v("checkForUpdates", "stackTrace: ${it.stackTrace}")
        }
    }

    private fun closeApp() {
        finishAndRemoveTask()
    }

    companion object {
        const val ACTION_LOCK_SCREEN = "action_lock_screen"
    }


}

@Composable
fun LockScreen(closeApp: () -> Unit, checkForUpdates: () -> Unit) {
    val giftPortalPainter = rememberAsyncImagePainter(
        model = imageRequest(R.drawable.portal), imageLoader = rememberGifImageLoader()
    )

    FullScreenImageDialog(
        imagePainter = giftPortalPainter,
        title = "New update is available",
        body = "The current version is no longer supported. We apologize for any inconvenience we may have caused.",
        okButtonText = "Update now",
        cancelButtonText = "No, thanks! Close app",
        onUpdateClick = { checkForUpdates() },
        onCloseClick = { closeApp() }
    )
}

