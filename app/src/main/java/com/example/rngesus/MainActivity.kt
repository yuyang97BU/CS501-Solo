package com.example.rngesus

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tv_reference: TextView
    private lateinit var tv_text: TextView
    private lateinit var bt_generate: Button
    private lateinit var bt_select: Button
    private val url = "https://bible-api.com/?random=verse&translation="
    private var version = "King James Version"
    private var version_id = "kjv"
    private var apiResponseData: String? = ""

    val versionID = mutableMapOf ("Cherokee New Testament" to "cherokee",
        "Bible in Basic English" to "bbe",
        "King James Version" to "kjv",
        "World English Bible" to "web",
        "Open EnglishBible,Commonwealth Edition" to "oeb-cw",
        "World English Bible,British Edition" to "webbe",
        "Open English Bible,US Edition" to "oeb-us",
        "Clementine Latin Vulgate" to "clementine",
        "João Ferreira de Almeida" to "almeida",
        "Protestant Romanian Corrected Cornilescu Version" to "rccv")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_select = findViewById(R.id.bt_select)
        bt_generate = findViewById(R.id.bt_generate)
        tv_reference = findViewById(R.id.tv_reference)
        tv_text = findViewById(R.id.tv_text)
        bt_select.setOnClickListener(this)
        bt_generate.setOnClickListener(this)
        bt_select.text = version
    }

    override fun onClick(view: View) {
        version_id = versionID[version]!!
        when (view.id) {
            R.id.bt_generate -> getData(url + version_id)
            R.id.bt_select -> {
                val popupMenu = PopupMenu(this@MainActivity, bt_select)
                popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                    version = item.toString().trim { it <= ' ' }
                    version_id = versionID[version]!!
                    bt_select.text = version
                    getData(url + version_id)
                    true
                }
                popupMenu.show()
            }
        }
    }

    private fun getData(str: String?) {
        Thread {
            try {
                val url = URL(str)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 3000
                connection.readTimeout = 3000
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val responseData = StringBuilder()
                while (reader.readLine().also { line = it } != null) {
                    responseData.append(line)
                }
                reader.close()
                apiResponseData = responseData.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            runOnUiThread {
                if (apiResponseData == null || !apiResponseData!!.contains("reference")) {
                    Toast.makeText(this@MainActivity, "Connection Failed", Toast.LENGTH_SHORT)
                        .show()
                    return@runOnUiThread
                }
                val gson = Gson()
                val newsBean = gson.fromJson(apiResponseData, NewsBean::class.java)
                if (newsBean.reference != null && newsBean.reference != "") {
                    tv_reference.text = newsBean.reference
                }
                if (newsBean.verses?.get(0)?.text != null && newsBean.verses?.get(0)?.text != "") {
                    tv_text.text = newsBean.verses!![0].text!!.replace("\n", "")
                }
            }
        }.start()
    }
}
