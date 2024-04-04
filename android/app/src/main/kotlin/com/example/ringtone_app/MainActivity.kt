package com.example.ringtone_app

import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val channel = "ringtone_channel"
        var mediaPlayer = MediaPlayer();
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channel)
            .setMethodCallHandler { call, result ->
                when(call.method){
                    "getRingtones" -> {
                        val ringtones = getAllRingtones(this)
                        result.success(ringtones)
                    }
                    "play" -> {
                        val url = call.arguments as String
                        if (!mediaPlayer.isPlaying){
                            mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse(url))
                            mediaPlayer.start()
                        }else{
                            mediaPlayer.stop()
                            mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse(url))
                            mediaPlayer.start()
                        }


                    }
                }
            }
    }


private fun getAllRingtones(context: Context): MutableList<Map<String, String>> {
    val ringtoneManager = RingtoneManager(context)
    ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE)
    val cursor: Cursor = ringtoneManager.cursor
    val ringtoneList:MutableList<Map<String,String>> = mutableListOf()

    while (cursor.moveToNext()) {
        val ringtoneUri: Uri = ringtoneManager.getRingtoneUri(cursor.position)
        val ringtoneTitle : String = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
        ringtoneList.add(mapOf("title" to ringtoneTitle,"url" to ringtoneUri.toString()))
    }

    cursor.close()
    return ringtoneList
}
}
