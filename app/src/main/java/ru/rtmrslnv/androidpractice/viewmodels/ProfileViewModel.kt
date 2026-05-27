package ru.rtmrslnv.androidpractice.viewmodels;

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application;
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.rtmrslnv.androidpractice.models.ProfileModel
import ru.rtmrslnv.androidpractice.models.ProfileRepository
import ru.rtmrslnv.androidpractice.views.NotifyReceiver
import java.time.LocalTime
import java.util.Calendar

@HiltViewModel
class ProfileViewModel @Inject constructor(application: Application, private val profileRepository: ProfileRepository) : AndroidViewModel(application) {
    private val _profile = MutableStateFlow<ProfileModel>(ProfileModel("", "", "", LocalTime.MIN))
    public val profile: StateFlow<ProfileModel> get() = _profile

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _profile.value = profileRepository.load()
    }

    fun saveProfile() {
        profileRepository.save(profile.value)
    }

    fun scheduleFavoriteClassNotification(context: Context) {
        return scheduleFavoriteClassNotification(context, profile.value.name, profile.value.favoriteClassTime)
    }

    private fun scheduleFavoriteClassNotification(context: Context, name: String, time: LocalTime) {
        val now = Calendar.getInstance()
        val fire = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (fire.before(now)) {
            fire.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(context, NotifyReceiver::class.java).apply {
            putExtra("name", name)
        }
        val requestCode = 1001
        val pending = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, fire.timeInMillis, pending)
    }

    fun updateName(name: String) {
        _profile.update { it.copy(name = name) }
    }

    fun updateAvatarUri(uri: String) {
        _profile.update { it.copy(avatarUri = uri) }
    }

    fun updatePortfolioUrl(portfolioUrl: String) {
        _profile.update { it.copy(portfolioUrl = portfolioUrl) }
    }

    fun updateFavoriteClassTime(time: LocalTime) {
        _profile.update { it.copy(favoriteClassTime = time) }
    }
}
