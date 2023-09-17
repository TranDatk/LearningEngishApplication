package com.tnmd.learningenglishapp.screens.list_review

import android.app.Activity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.tnmd.learningenglishapp.LIST_COURSES
import com.tnmd.learningenglishapp.R
import com.tnmd.learningenglishapp.screens.list_words.WordsViewModel

@Composable
@ExperimentalMaterialApi
fun ScoreDialogReview(
    score: Int,
    onPlayAgain: () -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = { Text(text = stringResource(R.string.congratulations)) },
        text = { Text(text = stringResource(R.string.you_scored, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.updateScoreToFireStore(score)
                    openScreen(LIST_COURSES)
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onPlayAgain() // Gọi hàm onPlayAgain để chơi lại
                viewModel.updateIsPlayed()
            }) {
                Text(text = stringResource(R.string.play_again))

            }
        }
    )
}
