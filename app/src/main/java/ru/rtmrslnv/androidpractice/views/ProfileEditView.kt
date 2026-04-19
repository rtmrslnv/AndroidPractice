package ru.rtmrslnv.androidpractice.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.rtmrslnv.androidpractice.models.ProfileModel
import ru.rtmrslnv.androidpractice.ui.theme.AndroidPracticeTheme
import ru.rtmrslnv.androidpractice.viewmodels.ProfileViewModel
import java.io.File

@Composable
fun ProfileEditView(navController: NavController, profileViewModel: ProfileViewModel) {
    val ctx = LocalContext.current
    val name = remember { mutableStateOf(profileViewModel.profile.value.name) }
    val avatarUriString = rememberSaveable { mutableStateOf(profileViewModel.profile.value.avatarUri) }
    val portfolioUrl = remember { mutableStateOf(profileViewModel.profile.value.portfolioUrl) }
    val showImagePicker = remember { mutableStateOf(false) }


    val readPerm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val cameraPerm = Manifest.permission.CAMERA

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let { avatarUriString.value = it.toString() }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (!success) {
            avatarUriString.value = ""
        }
        else {
            avatarUriString.value = avatarUriString.value?.let { "$it?t=${System.currentTimeMillis()}" } ?: ""
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
        val readGranted = perms[readPerm] == true
        val cameraGranted = perms[cameraPerm] == true
        if (!readGranted || !cameraGranted) {
            Toast.makeText(ctx, "Нужны разрешения для редактирования профиля", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    LaunchedEffect(Unit) {
        val readOk = ContextCompat.checkSelfPermission(ctx, readPerm) == PackageManager.PERMISSION_GRANTED
        val camOk = ContextCompat.checkSelfPermission(ctx, cameraPerm) == PackageManager.PERMISSION_GRANTED
        if (!readOk || !camOk) {
            permissionLauncher.launch(arrayOf(readPerm, cameraPerm))
        }
    }

    fun createImageFileUri(context: Context): Uri {
        val file = File(context.cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
        file.createNewFile()
        val authority = "${context.packageName}.fileprovider"
        return FileProvider.getUriForFile(context, authority, file)
    }

    AndroidPracticeTheme {
        Scaffold(modifier = Modifier.fillMaxWidth()) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Редактирование",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Button(
                        onClick = {
                            profileViewModel.profile.value =
                                ProfileModel(name.value, avatarUriString.value ?: "", portfolioUrl.value)
                            profileViewModel.saveProfile()
                            navController.popBackStack()
                        }
                    ) {
                        Image(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                        )
                    }
                }

                val avatarUri = avatarUriString.value.takeIf { it.isNotEmpty() }?.let { Uri.parse(it) }

                if (avatarUri == null) {
                    Image(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .clickable { showImagePicker.value = true }
                    )
                } else {
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .clickable { showImagePicker.value = true }
                    )
                }

                OutlinedTextField(
                    value = name.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onValueChange = { name.value = it },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(),
                    placeholder = { Text(text = "Имя", modifier = Modifier.fillMaxWidth()) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, autoCorrectEnabled = true)
                )

                OutlinedTextField(
                    value = portfolioUrl.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onValueChange = { portfolioUrl.value = it },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(),
                    placeholder = { Text(text = "Ссылка на портфолио", modifier = Modifier.fillMaxWidth()) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, autoCorrectEnabled = true)
                )
            }
        }

        if (showImagePicker.value) {
            ChooseImageSource(
                onDismissRequest = { showImagePicker.value = false },
                onCameraClick = {
                    val camOk = ContextCompat.checkSelfPermission(ctx, cameraPerm) == PackageManager.PERMISSION_GRANTED
                    if (!camOk) {
                        permissionLauncher.launch(arrayOf(readPerm, cameraPerm))
                        return@ChooseImageSource
                    }
                    val uri = createImageFileUri(ctx)
                    takePictureLauncher.launch(uri)
                    avatarUriString.value = uri.toString()
                    showImagePicker.value = false
                },
                onGalleryClick = {
                    pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    showImagePicker.value = false
                }
            )
        }
    }
}

@Composable
private fun ChooseImageSource(
    onDismissRequest: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Изменение аватара") },
        text = {
            Column {
                ListItem(
                    headlineContent = { Text("Сделать фото") },
                    modifier = Modifier.clickable { onCameraClick() }
                )
                ListItem(
                    headlineContent = { Text("Выбрать из галереи") },
                    modifier = Modifier.clickable { onGalleryClick() }
                )
            }
        },
        confirmButton = {},
    )
}
