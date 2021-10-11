package com.example.parquerufinotamayo.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parquerufinotamayo.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val bottomTabListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.action_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.flContainer, HomeFragment()).commit();
                return@OnNavigationItemSelectedListener true;
            }
            R.id.action_user -> {
                supportFragmentManager.beginTransaction().replace(R.id.flContainer, UserFragment()).commit();
                return@OnNavigationItemSelectedListener true;
            }
            R.id.action_new_report -> {
                supportFragmentManager.beginTransaction().replace(R.id.flContainer, NewReportFragment()).commit();
                return@OnNavigationItemSelectedListener true;
            }
        }
        false;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Define bottom tab navigation and default fragment (Home)
        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.selectedItemId = R.id.action_home;
        supportFragmentManager.beginTransaction().replace(R.id.flContainer, HomeFragment()).commit();

        //Navigation item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomTabListener);

    }
}