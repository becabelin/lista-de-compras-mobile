package com.example.lista_de_compras_mobile
data class Item(
    val name: String,
    val unit: String,
    val category: String,
    val completedStatus: Boolean = false
)

