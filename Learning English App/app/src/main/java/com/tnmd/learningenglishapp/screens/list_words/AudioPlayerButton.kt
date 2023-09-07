package com.tnmd.learningenglishapp.screens.list_words

import android.media.AudioManager
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun AudioPlayer(url: String,
                modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val mediaPlayer = MediaPlayer()
    Button(
        modifier = Modifier
            .width(300.dp)
            .padding(7.dp),
        onClick = {
            var audioUrl = url
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer.setDataSource(audioUrl)
                mediaPlayer.prepare()
                mediaPlayer.start()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(ctx, "Audio started playing..", Toast.LENGTH_SHORT).show()
        }) {
        // on below line we are specifying
        // text for button.
        Text(text = "Play Audio")
    }
}