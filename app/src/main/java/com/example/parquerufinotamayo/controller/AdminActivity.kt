package com.example.parquerufinotamayo.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parquerufinotamayo.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {
    private val bottomTabListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.action_homeAdmin -> {
                supportFragmentManager.beginTransaction().replace(R.id.flContainerAdmin, HomeFragment()).commit();
                return@OnNavigationItemSelectedListener true;
            }
            R.id.action_solve_report -> {
                supportFragmentManager.beginTransaction().replace(R.id.flContainerAdmin, SolveReportFragment()).commit();
                return@OnNavigationItemSelectedListener true;
            }
        }
        false;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //Define bottom tab navigation and default fragment (Home)
        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_navigationAdmin);
        bottomNavigationView.selectedItemId = R.id.action_homeAdmin;
        supportFragmentManager.beginTransaction().replace(R.id.flContainerAdmin, HomeFragment()).commit();

        //Navigation item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomTabListener);

    }
}