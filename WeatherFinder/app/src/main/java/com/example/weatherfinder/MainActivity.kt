package com.example.weatherfinder

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weatherfinder.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener{
            val downloadJSONTask = DownloadJSONTask()
            downloadJSONTask.execute()
        }

    }

    private inner class DownloadJSONTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            var result: String = ""
            var connection: HttpURLConnection? = null
            var location = binding.input.text.toString()
            var url =
                URL("https://api.openweathermap.org/data/2.5/weather?q=$location&appid=d91dd6be53461fabe640501f0f95e7be")
            connection = url.openConnection() as HttpURLConnection
            val httpResult: Int = connection.responseCode

            if (httpResult == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var data: Int = reader.read()
                while (data != -1) {
                    var current = data.toChar()
                    stringBuilder.append(current)
                    data = reader.read()
                }
                result = stringBuilder.toString()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result != ""){
                var jSONRes = JSONObject(result)
                var weather = jSONRes.optJSONArray("weather")

                for(x in 0 until weather.length()) {
                    var weatherInfo = weather.optJSONObject(x)
                    var description = weatherInfo.get("description")
                    binding.result.text = description.toString().toUpperCase()
                    Log.i("JSONresult", description.toString() )
                }
            }
        }
    }
}