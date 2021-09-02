package com.jmm.brsap.ludo2021

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.jmm.brsap.ludo2021.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var players = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noOfPlayers = arrayOf("2 Players","3 Players","4 Players")
        val arrayAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,noOfPlayers)

        binding.ddlNoOfPlayers.setAdapter(arrayAdapter)

        binding.ddlNoOfPlayers.setOnItemClickListener { parent, view, position, id ->
            players = when(position){
                1-> 3
                2-> 4
                else-> 2
            }
        }
        binding.btnNewGame.setOnClickListener {


            val intent = Intent(this,GameActivity::class.java)
            intent.putExtra("PLAYERS",players)
            startActivity(intent)
        }

    }
}