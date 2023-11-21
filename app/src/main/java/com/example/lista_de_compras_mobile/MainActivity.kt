package com.example.lista_de_compras_mobile

import ApiService
import CustomAdapter
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var itemNameEditText: EditText
    private lateinit var unitNumberPicker: NumberPicker
    private lateinit var categorySpinner: Spinner
    private lateinit var editTextCategory: EditText
    private lateinit var checkBoxCompleted: CheckBox
    private lateinit var addButton: Button
    private lateinit var listViewItems: ListView

    private val shoppingItems = mutableListOf<Item>()
    private val categories = arrayOf(
        "Frios e laticínios",
        "Adega e bebidas",
        "Higiene e limpeza",
        "Hortifruti e mercearia",
        "Padaria",
        "Enlatados",
        "Cereais",
        "Rotisseria"
    )

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fiap-shopping-list.herokuapp.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemNameEditText = findViewById(R.id.editTextName)
        unitNumberPicker = findViewById(R.id.editTextUnit)
        categorySpinner = findViewById(R.id.spinnerCategory)
        editTextCategory = findViewById(R.id.editTextCategory)
        checkBoxCompleted = findViewById(R.id.checkBoxCompleted)
        addButton = findViewById(R.id.buttonAdd)
        listViewItems = findViewById(R.id.listViewItems)

        unitNumberPicker.minValue = 1
        unitNumberPicker.maxValue = 100

        val categoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        val adapter = CustomAdapter(this, shoppingItems)
        listViewItems.adapter = adapter

        addButton.setOnClickListener {
            val itemName = itemNameEditText.text.toString()
            val unit = unitNumberPicker.value.toString()
            val category = categorySpinner.selectedItem.toString()
            val completedStatus = checkBoxCompleted.isChecked

            if (itemName.isNotEmpty() && unit.isNotEmpty() && category.isNotEmpty()) {
                val newItem = Item(itemName, unit, category, completedStatus)
                shoppingItems.add(newItem)
                adapter.notifyDataSetChanged()

                sendItemToApi(newItem)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendItemToApi(item: Item) {
        val call: Call<Void> = apiService.addItem(item)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Item adicionado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Erro ao adicionar item",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Falha de rede", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
