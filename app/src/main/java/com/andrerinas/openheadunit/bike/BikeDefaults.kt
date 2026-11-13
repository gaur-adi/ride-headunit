package com.andrerinas.openheadunit.bike

import android.content.Context
import com.andrerinas.openheadunit.utils.Settings
import com.andrerinas.openheadunit.connection.wifi.WifiLauncherMode

object BikeDefaults {
    const val REDUCE_MOTION = true
    private const val INSTALLED = "bike-defaults-v1"

    /** Apply once, not on every launch: a rider's later adjustments always win. */
    fun install(context: Context, settings: Settings) {
        val prefs = context.getSharedPreferences(Settings.PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(INSTALLED, false)) return
        applyProfile(settings, BikeProfile.SMOOTH)
        settings.appTheme = Settings.AppTheme.DARK
        settings.screenOrientation = Settings.ScreenOrientation.AUTO
        settings.connectionModes = setOf(Settings.ConnectionMode.WIFI)
        settings.primaryConnection = Settings.ConnectionKind.NATIVE_AA
        settings.wifiConnectionMode = WifiLauncherMode.NATIVE
        settings.vehicleDisplayName = "Ride Headunit"
        settings.vehicleType = 3
        settings.showHomeWidgets = false
        settings.showHomeFavoriteApps = false
        settings.showHomeSpeed = false
        settings.showHomeClock = true
        settings.homeButtonScalePercent = 100
        settings.useGradientBackground = false
        settings.autoConnectDelaySeconds = 0
        prefs.edit().putBoolean(INSTALLED, true).apply()
    }

    fun applyProfile(settings: Settings, profile: BikeProfile) {
        settings.resolutionId = profile.resolutionId
        settings.fpsLimit = profile.fps
        settings.viewMode = if (profile.useSurface) Settings.ViewMode.SURFACE else Settings.ViewMode.TEXTURE
        settings.videoCodec = "H.264"
        settings.forceSoftwareDecoding = false
        // The existing decoder ladder falls back safely when a codec rejects optional keys.
        settings.debugVideoLowLatency = profile.lowLatency
    }
}
