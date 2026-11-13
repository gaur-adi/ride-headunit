package com.andrerinas.openheadunit.bike

import android.app.Application
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.andrerinas.openheadunit.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class, sdk = [28])
class BikeDashboardLayoutTest {
    private fun checkDashboard(width: Int, height: Int) {
        val context = ContextThemeWrapper(RuntimeEnvironment.getApplication(), R.style.AppTheme)
        val parent = FrameLayout(context)
        val root = LayoutInflater.from(context).inflate(R.layout.fragment_home, parent, false)
        parent.addView(root)
        parent.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
        parent.layout(0, 0, width, height)
        assertEquals("Dashboard must fill its FrameLayout, not collapse under a weight", height, root.height)
        val controls = root.findViewById<View>(R.id.main_buttons_container)
        assertTrue("Controls must have space below the header", controls.height > 0)
        assertTrue("Controls must fit below the header", controls.bottom <= root.height)
        assertTrue(root.findViewById<View>(R.id.bike_profile_button).height > 0)
    }

    @Test @Config(qualifiers = "w900dp-h400dp-land")
    fun `landscape header leaves visible space for connection controls`() = checkDashboard(1800, 800)

    @Test @Config(qualifiers = "w400dp-h900dp-port")
    fun `portrait header leaves visible space for connection controls`() = checkDashboard(800, 1800)
}
