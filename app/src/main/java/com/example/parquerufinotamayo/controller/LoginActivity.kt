package com.example.parquerufinotamayo.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.parquerufinotamayo.R
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.model.Model
import com.example.parquerufinotamayo.model.entities.JwtToken
import com.example.parquerufinotamayo.model.entities.User
import com.example.parquerufinotamayo.model.repository.RemoteRepository
import com.example.parquerufinotamayo.model.repository.responseinterface.ILogin

class LoginActivity : AppCompatActivity() {

    lateinit var btnLogin : Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("Hole", "Hols")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(loginClickListener())

        if(LoginUtils.isUserLoggedIn(this)) advanceToMainActivity()

        /*
        btnLogin.setOnClickListener {
            var user : String = etUserLogin.text.toString();
            var password : String = etPasswordLogin.text.toString();

            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Uno de los datos está vacío", Toast.LENGTH_SHORT).show();
            } else {
                login(user, password);
            }
         */
    }

    private fun loginClickListener(): View.OnClickListener?{
        return View.OnClickListener {
            val email = findViewById<EditText>(R.id.etUserLogin).text.toString()
            val password = findViewById<EditText>(R.id.etPasswordLogin).text.toString()
            val user = User("anyname", email, password)
            Model(LoginUtils.getToken(this)).login(user, object : ILogin {
                override fun onSuccess(token: JwtToken?){
                    Toast.makeText(this@LoginActivity, "Welcome", Toast.LENGTH_SHORT).show()
                    if(token != null){
                        LoginUtils.saveToken(token, this@LoginActivity.applicationContext)
                        //This updates the HttpClient that at this moment might not have a valid token
                        RemoteRepository.updateRemoteReferences(token.token, this@LoginActivity);
                        advanceToMainActivity()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Something weird happened, login was ok but token was not given...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNoSuccess(code: Int, message: String){
                    Toast.makeText(
                        this@LoginActivity,
                        "Problem detected $code $message",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("errorLogin", "$code: $message")
                }

                override fun onFailure(t: Throwable){
                    Toast.makeText(
                        this@LoginActivity,
                        "Network or server error ocurred",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("errorNetwork", t.message.toString())
                }
            })
        }
    }

    private fun advanceToMainActivity(){

        val mainActivityIntent =
            Intent(applicationContext, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainActivityIntent)
    }

    /*
    private fun login(user : String , password : String) {
        //Set Login Function with API
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }
     */
}