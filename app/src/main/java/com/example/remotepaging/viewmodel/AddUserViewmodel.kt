package com.example.remotepaging.viewmodel

import android.text.TextUtils
import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotepaging.db.dao.AppDb
import com.example.remotepaging.model.User
import kotlinx.coroutines.launch

class AddUserViewmodel @ViewModelInject constructor(private val db: AppDb) : ViewModel(){

    fun saveData(imgPath : String,email : String,fname : String,lname : String) {
        viewModelScope.launch {
            val user = User(imgPath,email,fname,lname,0)
            db.userDao().insert(user)
        }

    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}