package com.example.parquerufinotamayo.model

import com.example.parquerufinotamayo.model.entities.JwtToken
import com.example.parquerufinotamayo.model.entities.User
import com.example.parquerufinotamayo.model.repository.RemoteRepository
import com.example.parquerufinotamayo.model.repository.backendinterface.UsersApi
import com.example.parquerufinotamayo.model.repository.responseinterface.ILogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Model(private val token:String) {
    fun login(user: User, callback: ILogin){
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callLogin = retrofit.create(UsersApi::class.java).login(user)

        callLogin.enqueue(object : Callback<JwtToken?> {
            override fun onResponse(call: Call<JwtToken?>, response: Response<JwtToken?>) {
                if(response.isSuccessful) callback.onSuccess(response.body())
                else {
                    val message : String = if (response.errorBody() != null)
                        response.errorBody()!!.string()
                    else
                        response.message()
                    callback.onNoSuccess(response.code(), message)
                }
            }

            override fun onFailure(call: Call<JwtToken?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }
}