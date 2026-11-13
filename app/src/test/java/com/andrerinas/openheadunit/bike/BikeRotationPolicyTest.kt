package com.andrerinas.openheadunit.bike

import org.junit.Assert.*
import org.junit.Test

class BikeRotationPolicyTest {
    @Test fun `unnegotiated and square video never request reconnect`() {
        assertFalse(BikeRotationPolicy.needsReconnect(true, 0, 0))
        assertFalse(BikeRotationPolicy.needsReconnect(false, 720, 0))
        assertFalse(BikeRotationPolicy.needsReconnect(true, 720, 720))
    }
    @Test fun `a turn back before settle matches the current stream`() {
        assertTrue(BikeRotationPolicy.needsReconnect(false, 720, 1280))
        assertFalse(BikeRotationPolicy.needsReconnect(true, 720, 1280))
    }
    @Test fun `same aspect and reversed landscape do not renegotiate`() {
        assertFalse(BikeRotationPolicy.needsReconnect(false, 1280, 720))
        assertFalse(BikeRotationPolicy.needsReconnect(true, 720, 1280))
        assertTrue(BikeRotationPolicy.needsReconnect(true, 1280, 720))
    }
}
