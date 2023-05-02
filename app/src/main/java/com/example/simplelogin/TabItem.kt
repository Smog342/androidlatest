package com.example.simplelogin

import androidx.compose.runtime.Composable

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int, var title: String, var screen: ComposableFun) {
    object Authorization : TabItem(R.drawable.ic_launcher_background, "Авторизация", { com.example.simplelogin.Authorization() })
    object Registration : TabItem(R.drawable.ic_launcher_background, "Регистрация", { com.example.simplelogin.Registration()})
}
