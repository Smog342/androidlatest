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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simplelogin.ui.theme.SimpleLoginTheme
import com.google.accompanist.pager.*
import io.reactivex.rxjava3.core.Maybe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class LaunchActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun Authorization(){

    var login by remember { mutableStateOf("") };
    var password by remember { mutableStateOf("") };
    val context = LocalContext.current;
    val rememberCoroutineScope = rememberCoroutineScope();


    fun Enter(context: Context){

        if ((login == "") or (password == "")){

            Toast.makeText(
                context,
                "Пожалуйста введите логин и пароль",
                Toast.LENGTH_SHORT
            ).show()

        }else{

            val db: AppDatabase = AppDatabase.getDatabase(context)

            rememberCoroutineScope.launch {

                var possible_user : User  = db.getUserDao().getUserForLog(login, password)

                if (possible_user == null){

                    Toast.makeText(
                        context,
                        "Логин или пароль введены неверно",
                        Toast.LENGTH_SHORT
                    ).show()

                }else{

                    Toast.makeText(
                        context,
                        "Успешная авторизация",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(context, MainActivity::class.java)

                    intent.putExtra("login", login)

                    intent.putExtra("password", possible_user.password);

                    intent.putExtra("phonenumber", possible_user.phonenumber);

                    context.startActivity(intent)

                }

            }

        }

    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        OutlinedTextField(value = login, placeholder = {Text(text = "Логин")}, onValueChange = {login = it})
        OutlinedTextField(value = password, placeholder = {Text(text = "Пароль")}, onValueChange = {password = it})
        Button(onClick = {

            Enter(context)

        }) {
            Text("Авторизоваться")
        }

    }
}

@Composable
fun Registration(){

    var phonenumber by remember { mutableStateOf("") };
    var login by remember { mutableStateOf("") };
    var password by remember { mutableStateOf("") };
    var password2 by remember { mutableStateOf("") };
    val context = LocalContext.current;
    val rememberCoroutineScope = rememberCoroutineScope();


    fun Enter(context: Context){

        if ((phonenumber == "") or (login == "") or (password == "") or (password2 == "")){

            Toast.makeText(
                context,
                "Пожалуйста введите все данные",
                Toast.LENGTH_SHORT
            ).show()

        }else if (password != password2){

            Toast.makeText(
                context,
                "Введённые пароли не совпадают",
                Toast.LENGTH_SHORT
            ).show()

        }else{

            var user: User = User(null, phonenumber, login, password)


            val db: AppDatabase = AppDatabase.getDatabase(context)


            rememberCoroutineScope.launch {

                var possible_user: User = db.getUserDao().getUserForReg(login)

                if (possible_user == null){

                    db.getUserDao().insertUser(user)

                    Toast.makeText(
                        context,
                        "Успешная регистрация",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(context, MainActivity::class.java)

                    intent.putExtra("login", login)

                    intent.putExtra("password", password);

                    intent.putExtra("phonenumber", phonenumber);

                    context.startActivity(intent)

                }else{

                    Toast.makeText(
                        context,
                        "Логин занят",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

        }

    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        OutlinedTextField(value = phonenumber, placeholder = { Text(text = "Номер телефона")} , onValueChange = {phonenumber = it})
        OutlinedTextField(value = login, placeholder = {Text(text = "Логин")}, onValueChange = {login = it})
        OutlinedTextField(value = password, placeholder = {Text(text = "Пароль")}, onValueChange = {password = it})
        OutlinedTextField(value = password2, placeholder = {Text(text = "Пароль")}, onValueChange = {password2 = it})
        Button(onClick = {

            Enter(context)

        }) {
            Text("Зарегистрироваться")
        }

    }

}

@Composable
@ExperimentalPagerApi
@ExperimentalMaterialApi
fun MainScreen(){

    val tabs = listOf(
        TabItem.Authorization,
        TabItem.Registration
    );
    val pagerState = rememberPagerState(pageCount = tabs.size);

    Scaffold(
        topBar = { TopBar() },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }

}

@Composable
fun TopBar(){

    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name) , fontSize = 18.sp)},
        backgroundColor = colorResource(id = R.color.black),
        contentColor = Color.White
    )

}

@Composable
@ExperimentalPagerApi
@ExperimentalMaterialApi
fun Tabs(tabs: List<TabItem>, pagerState: PagerState){

    val scope = rememberCoroutineScope();

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colorResource(id = R.color.black),
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            // OR Tab()
            LeadingIconTab(
                icon = { Icon(painter = painterResource(id = tab.icon), contentDescription = "") },
                text = { Text(tab.title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }

}

@ExperimentalPagerApi
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        tabs[page].screen()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    SimpleLoginTheme {
        Greeting2("Android")
    }
}