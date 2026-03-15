package ru.rtmrslnv.androidpractice.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.rtmrslnv.androidpractice.models.JobInfo
import ru.rtmrslnv.androidpractice.ui.theme.AndroidPracticeTheme
import ru.rtmrslnv.androidpractice.viewmodels.JobInfoViewModel

@Composable
fun MainView(navController: NavController, jobInfoViewModel: JobInfoViewModel) {
    val jobInfos = jobInfoViewModel.jobInfos.value

    AndroidPracticeTheme() {
        Scaffold(modifier = Modifier.fillMaxWidth()) { padding ->
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
                items(jobInfos) { jobInfo ->
                    JobCard(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(64.dp),
                        jobInfo = jobInfo,
                        onClick = { navController.navigate("details/${it.id}") })
                }
            }
        }
    }
}

@Composable
fun JobCard(modifier: Modifier = Modifier, jobInfo: JobInfo, onClick: (JobInfo) -> Unit) {
    Card(modifier = modifier, onClick = { onClick(jobInfo) }) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                // TODO: load jobInfo.companyLogoUrl
                imageVector = Icons.Default.AccountBox,
                contentDescription = jobInfo.companyName
            )
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)) {
                Text(
                    modifier = Modifier.height(24.dp),
                    text = jobInfo.title,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(27,27,27)
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
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(41,41,41)
                    )

                    val salary = if (jobInfo.minSalary != null) "${jobInfo.minSalary} - ${jobInfo.maxSalary} ${jobInfo.currency}" else "N/A"
                    Text(
                        modifier = Modifier.height(24.dp),
                        text = salary,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (salary != "N/A") Color(255,163,26) else Color(128,128,128)
                    )
                }
            }
        }
    }
}