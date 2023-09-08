package com.tnmd.learningenglishapp.screens.list_words

import android.media.AudioManager
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun AudioPlayer(url: String, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() } // Sử dụng remember để giữ nguyên MediaPlayer giữa các lần render

    Button(
        modifier = Modifier
            .width(300.dp)
            .padding(7.dp),
        onClick = {
            mediaPlayer.reset() // Đặt lại MediaPlayer trước khi phát âm thanh
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }) {
        Text(text = "Play Audio")
    }
}