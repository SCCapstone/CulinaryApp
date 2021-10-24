package com.github.lablonczy.remilestone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numInputField: TextView = numInput
        val passInputField: TextView = passInput

        val correctPhone: String = "8438675309"
        val correctPass: String = "capstone"

        val login: Button = loginButton
        loginButton.setOnClickListener {
            val numRight = numInputField.text.toString() == correctPhone
            val passRight = passInputField.text.toString() == correctPass

            if (numRight && passRight)
                output("Login Success", "Welcome, Chef Cocky!")
            else
                output("Login Failed", "Check Phone Number or Password. (Phone: 8438675309, Pass: capstone)")

        }



    }

    fun output(title: String, msg: String){
        val alertBox = AlertDialog.Builder(this)
        alertBox.setTitle(title)
        alertBox.setMessage(msg)

        alertBox.setPositiveButton("Ok") { dialog, which ->

        }

        alertBox.show()
    }
}