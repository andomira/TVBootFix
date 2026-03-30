package com.tvresort.bootfix

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import kotlin.concurrent.thread

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        thread {
            val maxAttempts = 60
            var attempts = 0
            while (attempts < maxAttempts) {
                try {
                    val value = Settings.Secure.getString(
                        context.contentResolver, "tv_user_setup_complete"
                    )
                    if (value == "1") {
                        Settings.Secure.putString(
                            context.contentResolver, "tv_user_setup_complete", "0"
                        )
                        break
                    }
                } catch (e: Exception) {
                    // WRITE_SECURE_SETTINGS not granted yet
                }
                Thread.sleep(5000)
                attempts++
            }
        }
    }
}
