package com.example.parquerufinotamayo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    lateinit var btnLogin : Button;
    lateinit var etUserLogin : EditText;
    lateinit var etPasswordLogin : EditText;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin);
        etUserLogin = findViewById(R.id.etUserLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);

        btnLogin.setOnClickListener {
            var user : String = etUserLogin.text.toString();
            var password : String = etPasswordLogin.text.toString();

            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Uno de los datos está vacío", Toast.LENGTH_SHORT).show();
            } else {
                login(user, password);
            }
        }
    }

    private fun login(user : String , password : String) {
        //ToDo
        //Set Login Function with API
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }
}