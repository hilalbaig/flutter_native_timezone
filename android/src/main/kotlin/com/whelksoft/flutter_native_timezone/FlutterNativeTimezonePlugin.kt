package com.whelksoft.flutter_native_timezone

import android.os.Build
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.time.ZoneId
import java.util.*

class FlutterNativeTimezonePlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        setupMethodChannel(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "getLocalTimezone" -> result.success(getLocalTimezone())
            "getAvailableTimezones" -> result.success(getAvailableTimezones())
            else -> result.notImplemented()
        }
    }

    private fun getLocalTimezone(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZoneId.systemDefault().id
        } else {
            TimeZone.getDefault().id
        }
    }

    private fun getAvailableTimezones(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZoneId.getAvailableZoneIds().toList()
        } else {
            TimeZone.getAvailableIDs().toList()
        }
    }

    private fun setupMethodChannel(messenger: BinaryMessenger) {
        channel = MethodChannel(messenger, "flutter_native_timezone")
        channel.setMethodCallHandler(this)
    }
}
