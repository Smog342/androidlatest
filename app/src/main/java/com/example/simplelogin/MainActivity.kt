package com.example.simplelogin

import android.content.Intent
import android.content.Context
import android.os.Bundle
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

            val intent = Intent(context, ChangeProfileActivity::class.java)

            intent.putExtra("login", name)

            intent.putExtra("password", password)

            intent.putExtra("phonenumber", phonenumber)

            context.startActivity(intent)

        }) {
            Text(text = "$name")
        }
        Text(text = "Приветствуем, $name!")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimpleLoginTheme {
//        Greeting(name = "Ilya")
    }
}