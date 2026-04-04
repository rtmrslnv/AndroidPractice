package ru.rtmrslnv.androidpractice.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.rtmrslnv.androidpractice.models.SettingsModel
import ru.rtmrslnv.androidpractice.models.SortMode
import ru.rtmrslnv.androidpractice.ui.theme.AndroidPracticeTheme
import ru.rtmrslnv.androidpractice.viewmodels.SettingsViewModel

@Composable
fun SettingsView(navController: NavController, settingsViewModel: SettingsViewModel) {
    val searchQuery = remember { mutableStateOf(settingsViewModel.settings.value.q) }
    val sortMode = remember { mutableStateOf(settingsViewModel.settings.value.sortMode) }
    val expanded = remember { mutableStateOf(false) }
    AndroidPracticeTheme() {
        Scaffold(modifier = Modifier.fillMaxWidth()) { padding ->
            Column(modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery.value,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { searchQuery.value = it },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(),
                    placeholder = {
                        Text(
                            text = "Запрос",
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, autoCorrectEnabled = true)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp)
                        .clickable {
                            expanded.value = true
                        }
                ) {
                    Text(
                        text = "Сортировка: "
                    )

                    Text(
                        text = sortMode.value.russianName
                    )

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        SortMode.values().forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.russianName) },
                                onClick = {
                                    sortMode.value = it
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        settingsViewModel.settings.value = SettingsModel(searchQuery.value, sortMode.value)
                        settingsViewModel.saveSettings()
                    }
                ) {
                    Text(text = "Применить")
                }

                Button(
                    onClick = {
                        searchQuery.value = ""
                        sortMode.value = SortMode.RELEVANT
                        settingsViewModel.settings.value = SettingsModel(searchQuery.value, sortMode.value)
                        settingsViewModel.saveSettings()
                    }
                ) {
                    Text(text = "Сбросить")
                }
            }
        }
    }
}