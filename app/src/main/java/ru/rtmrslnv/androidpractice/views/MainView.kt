package ru.rtmrslnv.androidpractice.views

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import ru.rtmrslnv.androidpractice.models.JobInfo
import ru.rtmrslnv.androidpractice.models.JobInfoUI
import ru.rtmrslnv.androidpractice.ui.theme.AndroidPracticeTheme
import ru.rtmrslnv.androidpractice.viewmodels.JobInfoViewModel

@Composable
fun MainView(navController: NavController, jobInfoViewModel: JobInfoViewModel) {
    val jobInfosState = jobInfoViewModel.jobInfos.collectAsState(emptyList());
    val jobInfos = jobInfosState.value
    val listState = rememberLazyListState()
    val error = jobInfoViewModel.error;

    LaunchedEffect(listState, jobInfos) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            lastVisible to total
        }.collect { (lastVisible, total) ->
            if (jobInfoViewModel.mustUpdate() || total > 0 && lastVisible >= total - 3) {
                jobInfoViewModel.loadJobs()
            }
        }
    }

    AndroidPracticeTheme() {
        Scaffold(modifier = Modifier.fillMaxWidth()) { padding ->
            if (error.value) {
                AlertDialog(
                    icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = "") },
                    title = { Text(text = "Проблемы с подключением") },
                    text = { Text(text = "Не удалось загрузить данные") },
                    onDismissRequest = { },
                    confirmButton = {
                        TextButton(onClick = { jobInfoViewModel.error.value = false }) {
                            Text(text = "ОК")
                        }
                    }
                )
            } else {
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
                            onClick = { navController.navigate("details/${it.id}") },
                            favoriteOnClick = {
                                jobInfoViewModel.save(it)
                                Toast.makeText(navController.context, "Сохранено в избранное", Toast.LENGTH_LONG).show()
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun JobCard(modifier: Modifier = Modifier, jobInfo: JobInfoUI, onClick: (JobInfoUI) -> Unit, favoriteOnClick: (JobInfoUI) -> Unit) {
    Card(modifier = modifier, onClick = { onClick(jobInfo) }) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)),
                bitmap = jobInfo.companyLogo,
                contentDescription = jobInfo.companyName
            )
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)) {
                Text(
                    modifier = Modifier.height(24.dp),
                    text = jobInfo.title,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.height(24.dp),
                        text = jobInfo.companyName,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        modifier = Modifier.height(24.dp),
                        text = jobInfo.salary,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (jobInfo.hasSalary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                }
            }

            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = { favoriteOnClick(jobInfo) },
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}