package com.example.parquerufinotamayo.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.R
import com.example.parquerufinotamayo.model.Model
import com.example.parquerufinotamayo.model.entities.JwtToken
import com.example.parquerufinotamayo.model.entities.User
import com.example.parquerufinotamayo.model.repository.RemoteRepository
import com.example.parquerufinotamayo.model.repository.responseinterface.ILogin
import com.example.parquerufinotamayo.model.repository.responseinterface.IRegister

class CrearCuentaActivity : AppCompatActivity() {

    lateinit var btnLogin : ImageButton
    lateinit var btnCrearCuenta : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        btnLogin = findViewById(R.id.btnLogin)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener(crearCuentaClickListener())

        btnLogin.setOnClickListener{
            advanceToLoginActivity()
        }
    }

    private fun crearCuentaClickListener(): View.OnClickListener?{
        return View.OnClickListener {
            val email = findViewById<EditText>(R.id.etEmailCrearCuenta).text.toString()
            val password = findViewById<EditText>(R.id.etPasswordCrearCuenta).text.toString()
            val name = findViewById<EditText>(R.id.editTextTextNombre).text.toString()
            val username = findViewById<EditText>(R.id.editTextNombreUsuario).text.toString()
            val user = User(name, username, email, password)

            Model(LoginUtils.getToken(this)).register(user, object : IRegister {
                override fun onSuccess(user: User?){
                    Toast.makeText(this@CrearCuentaActivity, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                    advanceToLoginActivity()
                }

                override fun onNoSuccess(code: Int, message: String){
                    Toast.makeText(
                        this@CrearCuentaActivity,
                        "Problem detected $code $message",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("errorCrearCuenta", "$code: $message")
                }

                override fun onFailure(t: Throwable){
                    Toast.makeText(
                        this@CrearCuentaActivity,
                        "Network or server error ocurred",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("errorNetwork", t.message.toString())
                }
            })
        }
    }

    private fun advanceToLoginActivity(){
      finish()
    }
}