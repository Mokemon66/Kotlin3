package com.example.kotlin3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.androidadvance.topsnackbar.TSnackbar
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity() {

    private val dbmySQLHelper: mySQLHelper = mySQLHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val register: Button = findViewById(R.id.register);
        val textFieldNombre: TextInputLayout= findViewById(R.id.textFieldNombre)
        val textFieldContra: TextInputLayout= findViewById(R.id.textFieldContra)
        val layout: ConstraintLayout= findViewById(R.id.layoutLogin)
        val login: Button = findViewById(R.id.login)
        register.setOnClickListener(View.OnClickListener {

            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        })

        login.setOnClickListener {
           if(dbmySQLHelper.comprobarUsuario(textFieldNombre.editText!!.text.toString(),textFieldContra.editText!!.text.toString())){
               var intent: Intent= Intent(this,Cards::class.java)
               intent.putExtra("nombre", textFieldNombre.editText?.text.toString())
               startActivity(intent)
           }else{
               val tSnackbar: TSnackbar= TSnackbar.make(layout,resources.getText(R.string.mensajeUsuarioNoExiste),TSnackbar.LENGTH_SHORT)
               tSnackbar.view.setBackgroundColor(R.drawable.ic_launcher_background)
               tSnackbar.show()
           }
        }
    }
}