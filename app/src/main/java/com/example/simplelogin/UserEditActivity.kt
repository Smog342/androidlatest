package com.example.simplelogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplelogin.ui.theme.SimpleLoginTheme
import kotlinx.coroutines.launch

class UserEditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UserScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting4(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun UserScreen() {
    var selectedUserIds by remember { mutableStateOf(emptySet<Int>()) }
    val rememberCoroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context);
    var users by remember {
        mutableStateOf(emptyList<User>())
    }
    var currentUser by remember {
        mutableStateOf(User(0, "", "", "", ""))
    }
    val sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)


    LaunchedEffect(key1 = true) {
        users = db.getUserDao().getUsers()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            itemsIndexed(users) { _, user ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = user.role == "admin",
                        onCheckedChange = { isChecked ->
                            users = users.map { if (it.id == user.id) it.copy(role = if (isChecked) "admin" else "user") else it }
                            if (isChecked) {
                                selectedUserIds = selectedUserIds + user.id!!
                            } else {
                                selectedUserIds = selectedUserIds - user.id!!
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = user.login ?: "")
                }
            }
        }

        Button(
            onClick = {
                val adminUserIds = selectedUserIds.toList()
                val userRole = "user"
                val adminRole = "admin"

                rememberCoroutineScope.launch {
                    // Обновление ролей пользователей
                    users.map { it.id }.forEach { userId ->
                        if (adminUserIds.contains(userId)) {
                            db.getUserDao().updateUserRole(userId!!, adminRole)
                        } else {
                            db.getUserDao().updateUserRole(userId!!, userRole)
                        }
                    }

                    currentUser =
                        sharedPreferences.getString("username", "")
                            ?.let { db.getUserDao().getUserForReg(it) }!!
                    db.getUserDao().updateUserRole(currentUser.id!!, "admin")
                }

                selectedUserIds = emptySet()

                val intent = Intent(context, MainActivity::class.java)


                /*intent.putExtra("username", currentUser.username)
                intent.putExtra("phoneNumber", currentUser.phoneNumber)
                intent.putExtra("password", currentUser.password)
                intent.putExtra("role", currentUser.role)*/

                context.startActivity(intent)
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Сохранить")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    SimpleLoginTheme {
        Greeting4("Android")
    }
}