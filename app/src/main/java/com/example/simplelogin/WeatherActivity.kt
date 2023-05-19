package com.example.simplelogin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.example.simplelogin.ui.theme.SimpleLoginTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import java.net.URL

class WeatherActivity : ComponentActivity() {
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
                    Weather(intent.getStringExtra("login")!!, intent.getStringExtra("password")!!, intent.getStringExtra("phonenumber")!!)
                }
            }
        }
    }
}

@Composable
fun Weather(name: String, password: String, phonenumber: String) {

    val context = LocalContext.current;

    var city by remember { mutableStateOf("") }

    var img by remember { mutableStateOf("https://openweathermap.org/img/wn/02n.png") }

    var temp by remember { mutableStateOf("") }
    var humidity by remember { mutableStateOf("") }
    var pressure by remember { mutableStateOf("") }

    val key = "6cf736ddae3cba3a19bcac52f0607be4"

    val rememberCoroutineScope = rememberCoroutineScope()

    var painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/02n.png")



    Column(verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .background(Color.LightGray)
        .fillMaxSize()) {

        OutlinedTextField(value = city, placeholder = { Text(text = "Город")}, onValueChange = {city = it})

        Text(text = temp)
        Text(text = humidity)
        Text(text = pressure)

        AsyncImage(model = "https://openweathermap.org/img/wn/10d@2x.png", contentDescription = "", modifier = Modifier.size(50.dp))

        Button(onClick = {

            val query = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric"

            var  resultJSON = ""

            runBlocking {

                try {

                    withTimeout(5000){

                        launch(Dispatchers.IO){

                            resultJSON = URL(query).readText()

                        }

                    }

                }catch (e:Exception){

                    resultJSON = "error"

                }

            }

            val jsonObj = JSONObject(resultJSON)

            val main = jsonObj.getJSONObject("main")

            temp = "Температура: " + main.getString("temp") + " °C"

            humidity = "Влажность: " + main.getString("humidity")

            pressure = "Давление: " + main.getString("pressure") + " Па"

            println(temp)
            println(humidity)
            println(pressure)

//            val resultJSON = URL(query).readText()
//
//            val jsonObj = JSONObject(resultJSON)
//
//            val main = jsonObj.getJSONObject("main")

//            temp = "Температура: " + main.getString("temp") + " °C"
//
//            humidity = "Влажность: " + main.getString("humidity")
//
//            pressure = "Давление: " + main.getString("pressure") + " Па"

        }) {
            Text(text = "Погода по городу")
        }

        Button(onClick = {

            val intent = Intent(context, MainActivity::class.java)

            intent.putExtra("login", name)

            intent.putExtra("password", password);

            intent.putExtra("phonenumber", phonenumber);

            context.startActivity(intent)

        }) {
            Text(text = "Вернуться")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview5() {
    SimpleLoginTheme {
//        Greeting5("Android")
    }
}