package com.grupoess.grupoess.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Texto Fragment Home OG"
    }
    val text: LiveData<String> = _text
}