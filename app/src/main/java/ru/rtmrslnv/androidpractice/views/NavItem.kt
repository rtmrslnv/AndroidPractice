package ru.rtmrslnv.androidpractice.views

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val route: String,
    val text: String,
    val icon: ImageVector
)
