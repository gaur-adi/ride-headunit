package com.andrerinas.openheadunit.bike

/** Explicit, reversible presets. Compatibility keeps the same stream and changes only rendering. */
enum class BikeProfile(val resolutionId: Int, val fps: Int, val useSurface: Boolean, val lowLatency: Boolean) {
    SMOOTH(2, 30, true, true),
    SHARP(3, 30, true, true),
    COMPATIBLE(2, 30, false, false)
}
