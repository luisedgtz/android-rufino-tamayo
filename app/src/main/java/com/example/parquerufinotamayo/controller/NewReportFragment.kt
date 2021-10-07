package com.example.parquerufinotamayo.controller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.parquerufinotamayo.LoginUtils
import com.example.parquerufinotamayo.R
import android.content.SharedPreferences
import com.example.parquerufinotamayo.model.Model


class NewReportFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_new_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = Model(LoginUtils.getToken(requireContext()))
    }
}