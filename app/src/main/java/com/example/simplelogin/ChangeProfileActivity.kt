package com.example.simplelogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import java.util.*

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
                    intent.getStringExtra("login")?.let { ProfileEdit(name = it, password = intent.getStringExtra("password")!!, phonenumber = intent.getStringExtra("phonenumber")!!) }
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
fun ProfileEdit(name: String, password: String, phonenumber: String){

    val context = LocalContext.current;
    val db: AppDatabase = AppDatabase.getDatabase(context)

    val rememberCoroutineScope = rememberCoroutineScope();

    var login by remember { mutableStateOf(name) };
    var password by remember { mutableStateOf(password) };

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        Text(text = phonenumber)
        OutlinedTextField(value = login, onValueChange = {login = it})
        OutlinedTextField(value = password, onValueChange = {password = it})
        Button(onClick = {

            val currentTime = Calendar.getInstance().time

            val sessionActive = SessionManagerUtil.isSessionActive(currentTime, context)

            if (sessionActive){

                rememberCoroutineScope.launch {

                    var possible_user: User = db.getUserDao().getUserForReg(login)

                    if ((possible_user == null) or (login == name)){

                        db.getUserDao().updateUserProfile(name, login, password)

                        val intent = Intent(context, MainActivity::class.java)

                        intent.putExtra("login", login)

                        intent.putExtra("password", password)

                        intent.putExtra("phonenumber", phonenumber)

                        context.startActivity(intent)

                    }else{

                        Toast.makeText(
                            context,
                            "Логин занят",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

            }else{

                if (SessionManagerUtil.getRefreshToken(context) == SessionManagerUtil.getRefreshTokenFromServer(context)){

                    val prev = SessionManagerUtil.getAccessToken(context)

                    SessionManagerUtil.storeAccessionToken(context, prev!!)

                    SessionManagerUtil.startUserSession(context, 15)

                    rememberCoroutineScope.launch {

                        var possible_user: User = db.getUserDao().getUserForReg(login)

                        if ((possible_user == null) or (login == name)){

                            db.getUserDao().updateUserProfile(name, login, password)

                            val intent = Intent(context, MainActivity::class.java)

                            intent.putExtra("login", login)

                            intent.putExtra("password", password)

                            intent.putExtra("phonenumber", phonenumber)

                            context.startActivity(intent)

                        }else{

                            Toast.makeText(
                                context,
                                "Логин занят",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                }else{

                    SessionManagerUtil.endUserSession(context)

                    val intent = Intent(context, LaunchActivity::class.java)

                    context.startActivity(intent)

                }

            }


        }) {
            Text("Сохранить")
        }
        Button(onClick = {

            val currentTime = Calendar.getInstance().time

            val sessionActive = SessionManagerUtil.isSessionActive(currentTime, context)

            if (sessionActive){

                val intent = Intent(context, MainActivity::class.java)

                intent.putExtra("login", name)

                intent.putExtra("password", password)

                intent.putExtra("phonenumber", phonenumber)

                context.startActivity(intent)

            }else{

                if (SessionManagerUtil.getRefreshToken(context) == SessionManagerUtil.getRefreshTokenFromServer(context)){

                    val prev = SessionManagerUtil.getAccessToken(context)

                    SessionManagerUtil.storeAccessionToken(context, prev!!)

                    SessionManagerUtil.startUserSession(context, 15)

                    val intent = Intent(context, MainActivity::class.java)

                    intent.putExtra("login", name)

                    intent.putExtra("password", password)

                    intent.putExtra("phonenumber", phonenumber)

                    context.startActivity(intent)

                }else{

                    SessionManagerUtil.endUserSession(context)

                    val intent = Intent(context, LaunchActivity::class.java)

                    context.startActivity(intent)

                }

            }

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