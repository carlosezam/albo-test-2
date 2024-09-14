package com.example.punky.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.punky.di.InAppUpdateModule
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.common.truth.Truth.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var appUpdateManager: FakeAppUpdateManager

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun testImmediateUpdate_Completes(){
        appUpdateManager.setUpdateAvailable(20)
        appUpdateManager.setUpdatePriority(5)

        ActivityScenario.launch(MainActivity::class.java)

        assertThat(appUpdateManager.isImmediateFlowVisible).isTrue()

        // Simulate user's and download behavior.
        appUpdateManager.userAcceptsUpdate()

        appUpdateManager.downloadStarts()

        appUpdateManager.downloadCompletes()

        // Validate that update is completed and app is restarted.
        assertTrue(appUpdateManager.isInstallSplashScreenVisible)
    }


}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [InAppUpdateModule::class]
)
interface TestInAppUpdateModule{

    companion object {

        @Provides
        @Singleton
        fun providesFakeAppUpdateManager(@ApplicationContext context: Context) : FakeAppUpdateManager {
            return FakeAppUpdateManager(context)
        }
    }

    @Binds
    fun providesAppUpdateManager(impl: FakeAppUpdateManager) : AppUpdateManager


}