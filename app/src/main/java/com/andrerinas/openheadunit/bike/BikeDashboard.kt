package com.andrerinas.openheadunit.bike

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.andrerinas.openheadunit.App
import com.andrerinas.openheadunit.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object BikeDashboard {
    fun bind(fragment: Fragment, root: View) {
        root.findViewById<View>(R.id.bike_profile_button)?.setOnClickListener {
            val context = fragment.requireContext()
            val settings = App.provide(context).settings
            val profiles = BikeProfile.values()
            val selected = profiles.indexOfFirst {
                it.resolutionId == settings.resolutionId &&
                    it.useSurface == (settings.viewMode == com.andrerinas.openheadunit.utils.Settings.ViewMode.SURFACE)
            }.coerceAtLeast(0)
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.bike_profile_title)
                .setSingleChoiceItems(R.array.bike_profiles, selected) { dialog, which ->
                    BikeDefaults.applyProfile(settings, profiles[which])
                    dialog.dismiss()
                    com.andrerinas.openheadunit.utils.ToastUtils.showToast(context, R.string.bike_profile_applied, Toast.LENGTH_LONG)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }
}
