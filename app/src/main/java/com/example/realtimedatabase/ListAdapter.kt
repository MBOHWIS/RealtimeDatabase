package com.example.realtimedatabase

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.layout_update.view.*

class ListAdapter (val con:Context,val layoutResId:Int,val list:List<Users>)
    :ArrayAdapter<Users>(con,layoutResId,list){
    override fun getView(position: Int, convertView: View?, parent:ViewGroup):View{

        val layoutInflater=LayoutInflater.from(con)
        val view=layoutInflater.inflate(layoutResId,null)
        val textName=view.findViewById<TextView>(R.id.show_name)
        val textEmail=view.findViewById<TextView>(R.id.show_email)
        val btnUpdate= view.findViewById<Button>(R.id.btn_update)
        val btnDelete= view.findViewById<Button>(R.id.btn_delete)
        val user=list[position]
        textName.text=user.nama
        textEmail.text=user.email
        btnUpdate.setOnClickListener{
            showUpdateDialog(user)
        }
        btnDelete.setOnClickListener{
            deleteInfo(user)
        }
        return view
    }

    private fun deleteInfo(user: Users){
        val progressDialog= ProgressDialog(con)
        progressDialog.isIndeterminate=  true
        progressDialog.setMessage("deleting........")
        progressDialog.show()
        val db = FirebaseDatabase.getInstance().getReference("USERS")
        db.child(user.id).removeValue().addOnCompleteListener{
            Toast.makeText(con,"deleted",Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }

    }

    private fun showUpdateDialog(user: Users){
        val builder=AlertDialog.Builder(con)
        builder.setTitle("update")
        var inflater=LayoutInflater.from(con)
        val view =inflater.inflate(R.layout.layout_update,null)

        view.input_update_name.setText(user.nama)
        view.input_update_email.setText(user.email)
        builder.setView(view)
        builder.setPositiveButton("update"){dialogInterface, i ->
            val dbUser= FirebaseDatabase.getInstance().getReference("USERS")
            val nama=view.input_update_name.text.toString().trim()
            val email=view.input_update_email.text.toString().trim()

            when{
                nama.isEmpty()->{view.input_update_name.error="nama jangan kosong lah"
                return@setPositiveButton
                }
                email.isEmpty()->{view.input_update_email.error="email jangan kosong lah"
                return@setPositiveButton
                }
                else->{
                    val user=Users(user.id,nama,email)
                    dbUser.child(user.id).setValue(user).addOnCompleteListener {
                        Toast.makeText(con, "updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        builder.setNegativeButton("cancel"){dialogInterface, i ->

        }
        val alert=builder.create()
        alert.show()
    }
}

