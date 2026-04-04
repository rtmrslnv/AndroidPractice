package ru.rtmrslnv.androidpractice.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.rtmrslnv.androidpractice.models.JobInfoUI
import ru.rtmrslnv.androidpractice.ui.theme.AndroidPracticeTheme
import ru.rtmrslnv.androidpractice.viewmodels.FavoritesViewModel
import ru.rtmrslnv.androidpractice.viewmodels.JobInfoViewModel


@Composable
fun FavoriesView(navController: NavController, favoritesViewModel: FavoritesViewModel) {
    val jobInfosState = favoritesViewModel.jobInfos.observeAsState(emptyList());
    val jobInfos = jobInfosState.value
    val listState = rememberLazyListState()
    val error = favoritesViewModel.error;

    LaunchedEffect(listState, jobInfos) {
        favoritesViewModel.loadJobs()
    }

    AndroidPracticeTheme() {
        Scaffold(modifier = Modifier.fillMaxWidth()) { padding ->
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(jobInfos) { jobInfo ->
                    JobCard(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(64.dp),
                        jobInfo = jobInfo,
                        onClick = { navController.navigate("favorites/${it.id}") },
                        favoriteOnClick = {})
                }
            }
        }
    }
}