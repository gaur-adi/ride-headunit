package com.andrerinas.openheadunit.bike

import android.app.Application
import android.content.Context
import com.andrerinas.openheadunit.utils.Settings
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class, sdk = [28])
class BikeProfileTest {
    private val context: Context get() = RuntimeEnvironment.getApplication()
    private val settings get() = Settings(context)

    @Before fun reset() {
        context.getSharedPreferences(Settings.PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test fun `system toggle off selects landscape and on restores automatic rotation`() {
        settings.screenOrientation = Settings.ScreenOrientation.AUTO
        android.provider.Settings.System.putInt(context.contentResolver,
            android.provider.Settings.System.ACCELEROMETER_ROTATION, 0)
        assertEquals(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, settings.effectiveScreenOrientation)
        android.provider.Settings.System.putInt(context.contentResolver,
            android.provider.Settings.System.ACCELEROMETER_ROTATION, 1)
        assertEquals(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER, settings.effectiveScreenOrientation)
        settings.screenOrientation = Settings.ScreenOrientation.PORTRAIT
        android.provider.Settings.System.putInt(context.contentResolver,
            android.provider.Settings.System.ACCELEROMETER_ROTATION, 0)
        assertEquals(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, settings.effectiveScreenOrientation)
    }

    @Test fun `relaunch preserves a riders selected renderer and quality`() {
        BikeDefaults.install(context, settings)
        BikeDefaults.applyProfile(settings, BikeProfile.COMPATIBLE)
        settings.resolutionId = 3
        BikeDefaults.install(context, Settings(context))
        assertEquals(Settings.ViewMode.TEXTURE, settings.viewMode)
        assertEquals(3, settings.resolutionId)
        assertFalse(settings.debugVideoLowLatency)
    }

    @Test fun `compatibility recovers from sharp profile without leaving high bandwidth or experimental keys`() {
        BikeDefaults.applyProfile(settings, BikeProfile.SHARP)
        settings.forceSoftwareDecoding = true
        BikeDefaults.applyProfile(settings, BikeProfile.COMPATIBLE)
        val restored = Settings(context)
        assertEquals(2, restored.resolutionId)
        assertEquals(30, restored.fpsLimit)
        assertEquals(Settings.ViewMode.TEXTURE, restored.viewMode)
        assertFalse(restored.debugVideoLowLatency)
        assertFalse(restored.forceSoftwareDecoding)
    }

    @Test fun `performance preset preserves a paired phone and network identity`() {
        settings.nativePokeBtMacs = setOf("12:34:56:78:90:AB")
        settings.bluetoothAddress = "AB:90:78:56:34:12"
        BikeDefaults.applyProfile(settings, BikeProfile.SMOOTH)
        assertEquals(setOf("12:34:56:78:90:AB"), settings.nativePokeBtMacs)
        assertEquals("AB:90:78:56:34:12", settings.bluetoothAddress)
    }
}
