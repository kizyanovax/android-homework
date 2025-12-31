package com.example.hw_3.profile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hw_3.core.Routes
import com.example.hw_3.core.ui.theme.*
import com.example.hw_3.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current

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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Аватар
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                if (profile.avatarUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(profile.avatarUri)
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ФИО
            Text(
                text = profile.fullName.ifEmpty { "Не указано" },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = DnDGold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Должность
            if (profile.position.isNotEmpty()) {
                Text(
                    text = profile.position,
                    style = MaterialTheme.typography.titleMedium,
                    color = DnDBeige.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Карточка с информацией
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DnDDarkGray.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Резюме
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Резюме",
                                style = MaterialTheme.typography.titleMedium,
                                color = DnDGold,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (profile.resumeUrl.isNotEmpty()) {
                                    profile.resumeUrl
                                } else {
                                    "Не указано"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = DnDBeige.copy(alpha = 0.8f)
                            )
                        }
                        if (profile.resumeUrl.isNotEmpty()) {
                            Button(
                                onClick = {
                                    // Открыть резюме
                                    openResume(context, profile.resumeUrl)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DnDGold
                                ),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "Открыть",
                                    color = DnDDarkBrown,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка редактирования
            Button(
                onClick = {
                    navController.navigate(Routes.EditProfile.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DnDGold
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    modifier = Modifier.size(24.dp),
                    tint = DnDDarkBrown
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Редактировать профиль",
                    color = DnDDarkBrown,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun openResume(context: android.content.Context, url: String) {
    try {
        // Пытаемся открыть URL напрямую
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
        intent.data = android.net.Uri.parse(url)
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        
        // Проверяем, есть ли приложение для открытия
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Если нет приложения, показываем сообщение
            android.widget.Toast.makeText(
                context,
                "Не найдено приложение для открытия файла",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    } catch (e: Exception) {
        android.widget.Toast.makeText(
            context,
            "Не удалось открыть резюме: ${e.message}",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}

