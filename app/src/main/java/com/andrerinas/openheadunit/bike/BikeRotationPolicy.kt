package com.andrerinas.openheadunit.bike

/** Projection dimensions are negotiated per session; only an aspect change needs renegotiation. */
object BikeRotationPolicy {
    const val SETTLE_MS = 1200L

    fun automaticOrientation(systemAutoRotate: Boolean): Int =
        if (systemAutoRotate) android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
        else android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    fun needsReconnect(portrait: Boolean, videoWidth: Int, videoHeight: Int): Boolean {
        if (videoWidth <= 0 || videoHeight <= 0 || videoWidth == videoHeight) return false
        return portrait != (videoHeight > videoWidth)
    }
}
