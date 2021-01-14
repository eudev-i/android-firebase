package com.example.firebaserealtime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebaserealtime.databinding.ActivityMainBinding
import android.util.Log
import com.google.firebase.database.*
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        conectDB()
        showData()

        bind.btnCreate.setOnClickListener(){
            var product = getProduct("Caneta Azul")
            var res = sendProdDB("1", product)
            Log.i("TAG", res)

            var product2 = getProduct("Azul Caneta")
            var res2 = sendProdDB("2", product2)
            Log.i("TAG", res)
        }

        bind.btnUpdate.setOnClickListener(){
            var product = getProduct("Carro Verde")
            var res = sendProdDB("1", product)
            Log.i("TAG", res)
        }

        bind.btnDelete.setOnClickListener(){
            deleteData("2")
        }
    }

    private fun conectDB(){
        database = FirebaseDatabase.getInstance()
        reference = database.getReference()
    }

    private fun getProduct(name: String): Product{
        return Product(name, 1,2.0)
    }

    private fun sendProdDB(id: String, product: Product): String{
        var res = reference
            .child(id)
            .setValue(product)
        return res.toString()
    }

    private fun updateData(id: String, product: Product): String{
        var res = reference
            .child(id)
            .setValue(product)
        return res.toString()
    }

    private fun deleteData(id: String): String{
        var res = reference
            .child(id)
            .removeValue()
        return res.toString()
    }

    private fun showData(){
        var listProd = arrayListOf<Product>()
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                dataSnapshot.children.forEach{
                    var prod = Gson().fromJson(it.value.toString(), Product::class.java)
                    listProd.add(prod)
                    Log.i("Prod", it.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Erro", error.toString())
            }
        })
    }
}