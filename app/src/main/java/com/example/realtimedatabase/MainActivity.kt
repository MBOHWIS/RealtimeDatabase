package com.example.realtimedatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ref = FirebaseDatabase.getInstance().getReference("USERS")
        btn_submit.setOnClickListener{saveData()}
        btn_showdata.setOnClickListener{
            val i=Intent(this,ShowActivity::class.java)
            startActivity(i)
        }
    }
    private fun saveData(){

        val nama = input_name.text.toString()
        val email= input_email.text.toString()
        val userId= ref.push().key.toString()
        val user= Users(userId,nama,email)
        ref.child(userId).setValue(user).addOnCompleteListener{
            Toast.makeText(this,"success",Toast.LENGTH_SHORT).show()
            input_name.setText("")
            input_email.setText("")
            val i= Intent(this,ShowActivity::class.java)
            startActivity(i)
        }
    }
}

