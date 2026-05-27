package ru.rtmrslnv.androidpractice.views

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.rtmrslnv.androidpractice.ui.theme.AndroidPracticeTheme
import ru.rtmrslnv.androidpractice.viewmodels.ProfileViewModel
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.time.format.DateTimeFormatter


@Composable
fun ProfileView(navController: NavController, profileViewModel: ProfileViewModel) {
    val profile by profileViewModel.profile.collectAsState()
    val name = profile.name
    val avatarUriString = profile.avatarUri
    val portfolioUrl = profile.portfolioUrl
    val ctx = LocalContext.current

    AndroidPracticeTheme() {
        Scaffold(modifier = Modifier.fillMaxWidth()) { padding ->
            Column(modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Профиль",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Button(
                        onClick = { navController.navigate("profileEdit") }
                    ) {
                        Image(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                        )
                    }
                }

                val avatarUri = avatarUriString.takeIf { it.isNotEmpty() }
                                                ?.let { Uri.parse(it) }

                if (avatarUri == null) {
                    Image(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                    )
                } else {
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = name.ifEmpty { "N/A" },
                    style = MaterialTheme.typography.titleLarge
                )
                Button(
                    onClick = { downloadAndOpen(ctx, portfolioUrl) }
                ) {
                    Text(
                        text = "Портфолио",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

fun downloadAndOpen(context: Context, url: String) {
    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val req = DownloadManager.Request(Uri.parse(url))
        .setTitle("Портфолио")
        .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, "portfolio.pdf")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setMimeType("application/pdf")

    val id = dm.enqueue(req)

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            val completedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (completedId != id) {
                return
            }
            context.unregisterReceiver(this)

            val uri = dm.getUriForDownloadedFile(id)
            if (uri == null) {
                Toast.makeText(ctx, "Ошибка загрузки файла", Toast.LENGTH_SHORT).show()
                return
            }

            val ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: "application/pdf"

            try {
                val open = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, mime)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                ctx.startActivity(open)
            } catch (e: Exception) {
                Toast.makeText(ctx, "Ошибка открытия файла", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
    } else {
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }
}
