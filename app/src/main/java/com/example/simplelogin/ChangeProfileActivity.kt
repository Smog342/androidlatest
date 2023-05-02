package com.example.simplelogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplelogin.ui.theme.SimpleLoginTheme
import kotlinx.coroutines.launch

class ChangeProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = getIntent()

        setContent {
            SimpleLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    intent.getStringExtra("login")?.let { ProfileEdit(name = it) }
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String) {
    Text(text = "Hello $name!")
}



@Composable
fun ProfileEdit(name: String){


    val context = LocalContext.current;
    val db: AppDatabase = AppDatabase.getDatabase(context)
    var current_user: User = User(1, "89053771403", "Ilya", "1234")
    val rememberCoroutineScope = rememberCoroutineScope();

    fun coroutine(context: Context){

        rememberCoroutineScope.launch {

            var return_user = db.getUserDao().getUserForReg(name)

            current_user = return_user

        }

    }

    coroutine(context)

    var login by remember { mutableStateOf(current_user.login.toString()) };
    var password by remember { mutableStateOf(current_user.password.toString()) };

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        current_user.phonenumber?.let { Text(text = it) }
        OutlinedTextField(value = login, onValueChange = {login = it})
//        password?.let { s -> OutlinedTextField(value = s, onValueChange = {password = it}) }
        OutlinedTextField(value = password, onValueChange = {password = it})
        Button(onClick = { /*TODO*/ }) {
            Text("Сохранить")
        }
        Button(onClick = {

            val intent = Intent(context, MainActivity::class.java)

            intent.putExtra("login", login)

            context.startActivity(intent)

        }) {
            Text("Вернуться")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    SimpleLoginTheme {
        Greeting3("Android")
    }
}