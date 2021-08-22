package com.example.fantasydraft

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class FixturesViewModel: ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    init {
        _title.value = "Hello Beach"
        parse()
    }

    private fun parse(){

        _title.value = "Hey"
        viewModelScope.launch(Dispatchers.IO){
            try {
                val doc = Jsoup.connect("https://sport.ua/football/results/ukraine/1/calendar").get()

                val metaElements = doc.select("meta")

                val name: String = metaElements.attr("name")
                Log.i("MainActivity","$name")
                if(name.isNotEmpty())
                    withContext(Dispatchers.Main){
                        _title.value = name
                    }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}