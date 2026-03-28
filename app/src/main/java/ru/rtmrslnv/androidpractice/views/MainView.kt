package ru.rtmrslnv.androidpractice.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.rtmrslnv.androidpractice.models.JobInfoUI
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
fun JobCard(modifier: Modifier = Modifier, jobInfo: JobInfoUI, onClick: (JobInfoUI) -> Unit) {
    Card(modifier = modifier, onClick = { onClick(jobInfo) }) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = jobInfo.companyLogo,
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
        }
    }
}