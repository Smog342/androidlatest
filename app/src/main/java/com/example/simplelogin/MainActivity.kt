package com.example.simplelogin

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplelogin.ui.theme.SimpleLoginTheme
import java.util.Calendar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = getIntent()

        setContent {
            SimpleLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    intent.getStringExtra("login")?.let { Greeting(name = it, password = intent.getStringExtra("password")!!, phonenumber = intent.getStringExtra("phonenumber")!!) };
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, password: String, phonenumber: String) {

    val context = LocalContext.current;

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        Button(onClick = {

            val currentTime = Calendar.getInstance().time

            val sessionActive = SessionManagerUtil.isSessionActive(currentTime, context)

            if (sessionActive){

                val intent = Intent(context, ChangeProfileActivity::class.java)

                intent.putExtra("login", name)

                intent.putExtra("password", password)

                intent.putExtra("phonenumber", phonenumber)

                context.startActivity(intent)

            }else{

                if (SessionManagerUtil.getRefreshToken(context) == SessionManagerUtil.getRefreshTokenFromServer(context)){

                    val prev = SessionManagerUtil.getAccessToken(context)

                    SessionManagerUtil.storeAccessionToken(context, prev!!)

                    SessionManagerUtil.startUserSession(context, 15)

                    val intent = Intent(context, ChangeProfileActivity::class.java)

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
            Text(text = "$name")
        }
        Text(text = "Приветствуем, $name!")

        if (context.getSharedPreferences("SESSION_PREFERENCES", 0).getString("SESSION_ACCESS_TOKEN", "") == "admin"){

            Text(text = "Вы админ")

            Button(onClick = {

                val currentTime = Calendar.getInstance().time

                val sessionActive = SessionManagerUtil.isSessionActive(currentTime, context)

                if (sessionActive){

                    val intent = Intent(context, EditUserActivity::class.java)

                    intent.putExtra("login", name)

                    intent.putExtra("password", password)

                    intent.putExtra("phonenumber", phonenumber)

                    context.startActivity(intent)

                }else{

                    if (SessionManagerUtil.getRefreshToken(context) == SessionManagerUtil.getRefreshTokenFromServer(context)){

                        val prev = SessionManagerUtil.getAccessToken(context)

                        SessionManagerUtil.storeAccessionToken(context, prev!!)

                        SessionManagerUtil.startUserSession(context, 15)

                        val intent = Intent(context, EditUserActivity::class.java)

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

                Text(text = "Админпанель")

            }

        }

        Button(onClick = {

            SessionManagerUtil.endUserSession(context)

            val intent = Intent(context, LaunchActivity::class.java)

            context.startActivity(intent)

        }) {

            Text(text = "Выйти")

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimpleLoginTheme {
//        Greeting(name = "Ilya")
    }
}