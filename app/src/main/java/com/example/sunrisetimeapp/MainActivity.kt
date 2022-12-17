package com.example.sunrisetimeapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.*
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getSunrise(view: View?){
        val API_KEY = "704d79dc6f5e4712ac082007220212"
        var city = txtCityName.text.toString()
        val currentTF = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var currentDay = currentTF.format(formatter).toString()
        var url =
            "https://api.weatherapi.com/v1/astronomy.json?key=$API_KEY&q=$city&dt=$currentDay"
        MyAsyncTask().execute(url)

    }

    inner class MyAsyncTask : AsyncTask<String,String,String>() {

        override fun onPreExecute() {
            //before task started
        }

        override fun doInBackground(vararg p0: String?): String {

                var url = URL(p0[0])
                val urlConnect = url.openConnection() as HttpsURLConnection
                urlConnect.connectTimeout = 1000
                var inStream = convertStreamToString(urlConnect.inputStream)

                publishProgress(inStream)

            return "null"
            // cannot access UI as this happens in background
        }


        @SuppressLint("SetTextI18n")
        override fun onProgressUpdate(vararg values: String?) {

            var jsonObj = JSONObject(values[0])
            val astronomy = jsonObj.getJSONObject("astronomy")
            val astro = astronomy.getJSONObject("astro")
            var sunrise = astro.getString("sunrise")

            tvSunriseTIme.text = "Sunrise time is $sunrise"

        }

        override fun onPostExecute(result: String?) {

        }

    }

    fun convertStreamToString(inputStream:InputStream):String{

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String?
        var allString =""
        try {
            do{
                line  = bufferReader.readLine()
                if(line!=null){
                    allString += line
                }
            }while(line!=null)
            inputStream.close()
        }catch (ex:Exception){}

        return allString
    }

}