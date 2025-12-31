package com.example.hw_3.screens

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hw_3.MainActivity
import com.example.hw_3.data.preferences.Profile
import com.example.hw_3.notifications.NotificationHelper
import com.example.hw_3.ui.theme.*
import com.example.hw_3.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.*
import java.util.Calendar
import java.util.regex.Pattern
import com.example.hw_3.notifications.AlarmReceiver

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<String?>(null) }
    var favoriteClassTime by remember { mutableStateOf("") }
    
    // Синхронизируем локальные состояния с профилем из ViewModel при первой загрузке
    var isInitialized by remember { mutableStateOf(false) }
    LaunchedEffect(profile) {
        if (!isInitialized) {
            fullName = profile.fullName
            resumeUrl = profile.resumeUrl
            position = profile.position
            avatarUri = profile.avatarUri
            favoriteClassTime = profile.favoriteClassTime ?: ""
            isInitialized = true
        }
    }
    
    // Состояние для TimePicker
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = profile.favoriteClassTime?.let {
            try {
                it.split(":")[0].toInt()
            } catch (e: Exception) {
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            }
        } ?: Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute = profile.favoriteClassTime?.let {
            try {
                it.split(":")[1].toInt()
            } catch (e: Exception) {
                Calendar.getInstance().get(Calendar.MINUTE)
            }
        } ?: Calendar.getInstance().get(Calendar.MINUTE)
    )
    
    // Валидация времени (формат HH:mm)
    val timePattern = Pattern.compile("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    val isTimeValid = favoriteClassTime.isEmpty() || timePattern.matcher(favoriteClassTime).matches()
    val timeError = if (!isTimeValid && favoriteClassTime.isNotEmpty()) "Формат: HH:mm" else null

    // Разрешения для камеры и галереи (зависит от версии Android)
    val permissionsList = remember {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+)
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // Android 10-12 (API 29-32)
            listOf(
                Manifest.permission.CAMERA
            )
        } else {
            // Android 9 и ниже (API 28 и ниже)
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }
    
    val permissionsState = rememberMultiplePermissionsState(permissions = permissionsList)
    
    // Разрешение на уведомления (для Android 13+)
    val notificationPermissionState = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }
    
    // Состояние для диалога запроса разрешения на уведомления
    var showNotificationPermissionDialog by remember { mutableStateOf(false) }
    var shouldOpenTimePickerAfterPermission by remember { mutableStateOf(false) }

    // Файл для фото с камеры
    val photoFile = remember { File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg") }
    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // Launcher для выбора изображения из галереи
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatarUri = it.toString()
        }
    }

    // Launcher для камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            avatarUri = photoUri.toString()
        }
    }

    // Диалог выбора источника фото
    var showImageSourceDialog by remember { mutableStateOf(false) }

    // Функция для запроса разрешений и открытия камеры
    fun openCamera() {
        if (permissionsState.allPermissionsGranted) {
            cameraLauncher.launch(photoUri)
        } else {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    // Функция для открытия галереи
    fun openGallery() {
        // Для Android 10+ (API 29+) не нужны разрешения для выбора изображений через ACTION_GET_CONTENT
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            galleryLauncher.launch("image/*")
        } else {
            // Для старых версий Android проверяем разрешения
            if (permissionsState.allPermissionsGranted) {
                galleryLauncher.launch("image/*")
            } else {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DnDDarkBrown, DnDDarkGray)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Заголовок
            Text(
                text = "Редактирование профиля",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = DnDGold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Аватар с возможностью изменения
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(120.dp)
                    .clickable { showImageSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(avatarUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Аватар",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(DnDGold.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Аватар",
                            modifier = Modifier.size(60.dp),
                            tint = DnDGold
                        )
                    }
                }
                // Иконка редактирования
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DnDGold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Изменить фото",
                        modifier = Modifier.size(20.dp),
                        tint = DnDDarkBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Поля ввода
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DnDDarkGray.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // ФИО
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("ФИО", color = DnDBeige) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DnDBeige,
                            unfocusedTextColor = DnDBeige,
                            focusedBorderColor = DnDGold,
                            unfocusedBorderColor = DnDBeige.copy(alpha = 0.5f),
                            focusedLabelColor = DnDGold,
                            unfocusedLabelColor = DnDBeige.copy(alpha = 0.7f)
                        )
                    )

                    // Должность
                    OutlinedTextField(
                        value = position,
                        onValueChange = { position = it },
                        label = { Text("Должность", color = DnDBeige) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DnDBeige,
                            unfocusedTextColor = DnDBeige,
                            focusedBorderColor = DnDGold,
                            unfocusedBorderColor = DnDBeige.copy(alpha = 0.5f),
                            focusedLabelColor = DnDGold,
                            unfocusedLabelColor = DnDBeige.copy(alpha = 0.7f)
                        )
                    )

                    // URL резюме
                    OutlinedTextField(
                        value = resumeUrl,
                        onValueChange = { resumeUrl = it },
                        label = { Text("URL резюме/портфолио", color = DnDBeige) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://example.com/resume.pdf", color = DnDBeige.copy(alpha = 0.5f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DnDBeige,
                            unfocusedTextColor = DnDBeige,
                            focusedBorderColor = DnDGold,
                            unfocusedBorderColor = DnDBeige.copy(alpha = 0.5f),
                            focusedLabelColor = DnDGold,
                            unfocusedLabelColor = DnDBeige.copy(alpha = 0.7f)
                        )
                    )
                    
                    // Время любимой пары
                    OutlinedTextField(
                        value = favoriteClassTime,
                        onValueChange = { favoriteClassTime = it },
                        label = { Text("Время любимой пары", color = DnDBeige) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("HH:mm", color = DnDBeige.copy(alpha = 0.5f)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    // Проверяем разрешение на уведомления для Android 13+
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                        val permissionStatus = notificationPermissionState?.status
                                        if (permissionStatus is PermissionStatus.Granted) {
                                            showTimePicker = true
                                        } else {
                                            shouldOpenTimePickerAfterPermission = true
                                            showNotificationPermissionDialog = true
                                        }
                                    } else {
                                        // Для старых версий Android разрешение не требуется
                                        showTimePicker = true
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = "Выбрать время",
                                    tint = DnDGold
                                )
                            }
                        },
                        isError = !isTimeValid && favoriteClassTime.isNotEmpty(),
                        supportingText = timeError?.let {
                            {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DnDBeige,
                            unfocusedTextColor = DnDBeige,
                            focusedBorderColor = if (!isTimeValid && favoriteClassTime.isNotEmpty()) MaterialTheme.colorScheme.error else DnDGold,
                            unfocusedBorderColor = if (!isTimeValid && favoriteClassTime.isNotEmpty()) MaterialTheme.colorScheme.error else DnDBeige.copy(alpha = 0.5f),
                            focusedLabelColor = DnDGold,
                            unfocusedLabelColor = DnDBeige.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка "Готово"
            Button(
                onClick = {
                    val updatedProfile = Profile(
                        fullName = fullName,
                        avatarUri = avatarUri,
                        resumeUrl = resumeUrl,
                        position = position,
                        favoriteClassTime = if (favoriteClassTime.isNotEmpty() && isTimeValid) favoriteClassTime else null
                    )
                    viewModel.updateProfile(updatedProfile)
                    
                    // Планируем уведомление, если время указано и валидно
                    if (favoriteClassTime.isNotEmpty() && isTimeValid) {
                        scheduleNotification(context, favoriteClassTime, fullName)
                    }
                    
                    navController.popBackStack()
                },
                enabled = isTimeValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DnDGold,
                    disabledContainerColor = DnDGold.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Готово",
                    color = DnDDarkBrown,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }

    // Диалог запроса разрешения на уведомления
    if (showNotificationPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationPermissionDialog = false },
            title = {
                Text(
                    text = "Разрешение на уведомления",
                    color = DnDGold,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Для отправки напоминаний о начале любимой пары приложению необходимо разрешение на отправку уведомлений.",
                    color = DnDBeige
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNotificationPermissionDialog = false
                        notificationPermissionState?.launchPermissionRequest()
                    }
                ) {
                    Text("Разрешить", color = DnDGold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showNotificationPermissionDialog = false
                        shouldOpenTimePickerAfterPermission = false
                    }
                ) {
                    Text("Отмена", color = DnDBeige)
                }
            },
            containerColor = DnDDarkGray,
            titleContentColor = DnDGold,
            textContentColor = DnDBeige
        )
    }
    
    // Открываем TimePicker после получения разрешения
    LaunchedEffect(notificationPermissionState?.status, shouldOpenTimePickerAfterPermission) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU && 
            shouldOpenTimePickerAfterPermission) {
            val permissionStatus = notificationPermissionState?.status
            if (permissionStatus is PermissionStatus.Granted) {
                shouldOpenTimePickerAfterPermission = false
                showTimePicker = true
            } else if (permissionStatus is PermissionStatus.Denied) {
                // Пользователь отклонил разрешение
                shouldOpenTimePickerAfterPermission = false
            }
        }
    }
    
    // Диалог TimePicker
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = {
                Text(
                    text = "Выберите время",
                    color = DnDGold,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val hour = String.format("%02d", timePickerState.hour)
                        val minute = String.format("%02d", timePickerState.minute)
                        favoriteClassTime = "$hour:$minute"
                        showTimePicker = false
                    }
                ) {
                    Text("ОК", color = DnDGold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTimePicker = false }
                ) {
                    Text("Отмена", color = DnDBeige)
                }
            },
            containerColor = DnDDarkGray,
            titleContentColor = DnDGold,
            textContentColor = DnDBeige
        )
    }
    
    // Диалог выбора источника фото
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = {
                Text(
                    text = "Выберите источник",
                    color = DnDGold,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openGallery()
                                showImageSourceDialog = false
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Галерея",
                            tint = DnDGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Галерея",
                            color = DnDBeige,
                            fontSize = 16.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openCamera()
                                showImageSourceDialog = false
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Камера",
                            tint = DnDGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Камера",
                            color = DnDBeige,
                            fontSize = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showImageSourceDialog = false }
                ) {
                    Text("Отмена", color = DnDBeige)
                }
            },
            containerColor = DnDDarkGray,
            titleContentColor = DnDGold,
            textContentColor = DnDBeige
        )
    }
}

private fun scheduleNotification(context: Context, timeString: String, userName: String) {
    try {
        android.util.Log.d("EditProfileScreen", "Scheduling notification for time: $timeString, user: $userName")
        
        val parts = timeString.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            // Если время уже прошло сегодня, планируем на завтра
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        
        android.util.Log.d("EditProfileScreen", "Notification scheduled for: ${calendar.time}, current time: ${System.currentTimeMillis()}")
        
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Отменяем предыдущее уведомление, если оно было
        val cancelIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.example.hw_3.ACTION_ALARM"
        }
        val cancelPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            cancelIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(cancelPendingIntent)
        
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.example.hw_3.ACTION_ALARM"
            putExtra("user_name", userName)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        // Для Android 12+ (API 31+) проверяем разрешение на точные будильники
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            if (alarmManager.canScheduleExactAlarms()) {
                // Разрешение есть, используем setAlarmClock
                val showIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val showPendingIntent = PendingIntent.getActivity(
                    context,
                    1,
                    showIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                val alarmClockInfo = android.app.AlarmManager.AlarmClockInfo(
                    calendar.timeInMillis,
                    showPendingIntent
                )
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
                android.util.Log.d("EditProfileScreen", "Alarm set using setAlarmClock")
            } else {
                // Разрешения нет, используем setWindow (менее точный, но работает без разрешения)
                android.util.Log.w("EditProfileScreen", "SCHEDULE_EXACT_ALARM permission not granted, using setWindow")
                alarmManager.setWindow(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    60000, // окно в 1 минуту
                    pendingIntent
                )
                android.util.Log.d("EditProfileScreen", "Alarm set using setWindow (less precise)")
            }
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // Android 6-11 (API 23-30): используем setExactAndAllowWhileIdle
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                android.util.Log.d("EditProfileScreen", "Alarm set using setExactAndAllowWhileIdle")
            } catch (e: SecurityException) {
                android.util.Log.e("EditProfileScreen", "Permission denied for setExactAndAllowWhileIdle", e)
                // Fallback: используем setWindow
                alarmManager.setWindow(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    60000,
                    pendingIntent
                )
                android.util.Log.d("EditProfileScreen", "Alarm set using fallback setWindow")
            }
        } else {
            // Android 5 и ниже
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            android.util.Log.d("EditProfileScreen", "Alarm set using setExact")
        }
    } catch (e: Exception) {
        android.util.Log.e("EditProfileScreen", "Error scheduling notification", e)
        e.printStackTrace()
    }
}

