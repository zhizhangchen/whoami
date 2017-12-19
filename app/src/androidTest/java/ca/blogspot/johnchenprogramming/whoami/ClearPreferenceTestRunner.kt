package ca.blogspot.johnchenprogramming.whoami

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import java.io.File


class ClearPreferenceTestRunner : AndroidJUnitRunner() {
    override fun callApplicationOnCreate(app: Application) {
        clearPreferences(app)
        super.callApplicationOnCreate(app)
    }

    private fun clearPreferences(context: Context) {
        val root = context.filesDir.parentFile
        val sharedPreferencesFileNames = File(root, "shared_prefs").list()
        if (sharedPreferencesFileNames != null) {
            for (fileName in sharedPreferencesFileNames) {
                context.getSharedPreferences(
                        fileName.replace(".xml", ""),
                        Context.MODE_PRIVATE
                ).edit().clear().apply()
            }
        }
    }
}
