package com.example.rngesus

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import java.util.regex.Matcher
import java.util.regex.Pattern
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var tv_reference: TextView
    private lateinit var tv_text: TextView
    private lateinit var bt_generate: Button
    private lateinit var bt_select: Button
    private val url = "https://bible-api.com/?random=verse&translation=" //API link for the random verse
    private val url2 = "https://bible-api.com/" //API link for searching for a specific verse
    private val manualInVersion = "?translation=kjv"
    private var version = "King James Version"
    private var version_id = "kjv" //For choosing which version with the menu selector
    private var apiResponseData: String? = ""
    private lateinit var editText: EditText
    private var enteredText = ""

    //mapping the menu string with version ID for API
    private val versionMap = mutableMapOf ("Cherokee New Testament" to "cherokee",
        "Bible in Basic English" to "bbe",
        "King James Version" to "kjv",
        "World English Bible" to "web",
        "Open EnglishBible,Commonwealth Ed." to "oeb-cw",
        "World English Bible,British Edition" to "webbe",
        "Open English Bible,US Edition" to "oeb-us",
        "Clementine Latin Vulgate" to "clementine",
        "João Ferreira de Almeida" to "almeida",
        "Protestant Romanian Corrected" to "rccv")

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
        editText = findViewById<EditText>(R.id.editText)
    }
    // basic format checking for the manual input
    private fun isBibleVerseFormat(input: String): Boolean {
        val bibleVersePattern = "^[A-Za-z]+\\s\\d+:\\d+$"
        val pattern: Pattern = Pattern.compile(bibleVersePattern)
        val matcher: Matcher = pattern.matcher(input)
        val bibleVersePattern2 = "^[A-Za-z]+\\d+:\\d+$"
        val pattern2: Pattern = Pattern.compile(bibleVersePattern2)
        val matcher2: Matcher = pattern2.matcher(input)
        val bibleVersePattern3 = "^[A-Za-z]+\\d+$"
        val pattern3: Pattern = Pattern.compile(bibleVersePattern3)
        val matcher3: Matcher = pattern3.matcher(input)
        return (matcher.matches() || matcher2.matches() || matcher3.matches())
    }

    override fun onClick(view: View) {
        version_id = versionMap[version]!! // getting the version ID
        when (view.id) {
            // request a verse on click
            R.id.bt_generate -> {
                enteredText = editText.text.toString()
                enteredText = enteredText.replace("\\s", "")
                // if there's input in the input text box then we switch to requesting that verse
                if (enteredText.isNotEmpty()) {
                    if (isBibleVerseFormat(enteredText)) {
                        getData(url2 + enteredText + manualInVersion)
                    }
                    else {
                        Toast.makeText(this@MainActivity, R.string.incorrect_format, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    getData(url + version_id)
                }
            }
            // change the version via menu selection
            R.id.bt_select -> {
                val popupMenu = PopupMenu(this@MainActivity, bt_select)
                popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                    version = item.toString().trim { it <= ' ' }
                    version_id = versionMap[version]!!
                    bt_select.text = version
                    getData(url + version_id)
                    true
                }
                popupMenu.show()
            }
        }
    }

    private fun getData(str: String?) {
        // run the API request with threads
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
                // failed to get a response
                if (apiResponseData == null) {
                    Toast.makeText(this@MainActivity, R.string.connection_fail, Toast.LENGTH_SHORT)
                        .show()
                    return@runOnUiThread
                }
                 // manually requested verse doesn't exist
                 else if (apiResponseData!!.contains("error")){
                    Toast.makeText(this@MainActivity, R.string.verse_not_exit, Toast.LENGTH_SHORT)
                        .show()
                    return@runOnUiThread
                }
                // using Gson to parse the JSON response
                val gson = Gson()
                val gsonRes = gson.fromJson(apiResponseData, VersesJSON::class.java)
                if (gsonRes.reference != null && gsonRes.reference != "") {
                    tv_reference.text = gsonRes.reference
                }
                // remove the random new line symbol that sometimes exist
                if (gsonRes.verses?.get(0)?.text != null && gsonRes.verses?.get(0)?.text != "") {
                    tv_text.text = gsonRes.verses!![0].text!!.replace("\n", "")
                }
            }
        }.start()
    }
    // the sign out button for signing out
    fun signOut(view: View) {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
