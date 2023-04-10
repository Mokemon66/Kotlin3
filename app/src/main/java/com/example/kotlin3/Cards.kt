package com.example.kotlin3

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Cards : AppCompatActivity() {
    private val dbmySQLHelper: mySQLHelper = mySQLHelper(this)
    private lateinit var adapterRecycler: AdapterRecycler
    private lateinit var listaUsuarios: List<Usuario>
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)
        listaUsuarios = dbmySQLHelper.sacarUsuarios()
        val layout: ConstraintLayout = findViewById(R.id.layoutUsuarios)
        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapterRecycler = AdapterRecycler(listaUsuarios, this)
        var context: Context = this
        recyclerView.adapter = adapterRecycler
        var usuarioIniciado: String = intent.extras!!.get("nombre").toString()
        TSnackbar.make(layout, resources.getString(R.string.entrada)+" "+usuarioIniciado, TSnackbar.LENGTH_SHORT).show()
        adapterRecycler.setOnClickListener(object : AdapterRecycler.onItemClickListener {
            override fun onItemClick(position: Int) {
                val usuarioSeleccionado: String = listaUsuarios.get(position).nombre
                if (listaUsuarios.get(posicionIniciado(usuarioIniciado)).admin) {
                    MaterialAlertDialogBuilder(
                        context, R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
                    ).setTitle(resources.getString(R.string.usuario) + " " + usuarioSeleccionado)
                        .setMessage(resources.getString(R.string.mensajeDialog)).setNegativeButton(
                            resources.getString(R.string.modificar),
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                val intent: Intent =
                                    Intent(applicationContext, RegisterActivity::class.java)
                                intent.putExtra("nombre", listaUsuarios.get(position).nombre)
                                startActivityForResult(intent, 1)
                            })
                        .setPositiveButton(resources.getString(R.string.elimnar)) { dialogInterface, i ->
                            dbmySQLHelper.borrarUsuario(listaUsuarios.get(position).nombre)
                            listaUsuarios = dbmySQLHelper.sacarUsuarios()
                            adapterRecycler.setData(listaUsuarios)
                        }.show()
                }
            }
        })
        recyclerView.adapter = adapterRecycler
    }

    private fun posicionIniciado(string: String): Int {
        var int: Int = 0
        for(x in 0..listaUsuarios.size-1){
            if(listaUsuarios.get(x).nombre.equals(string)){
                int = x
            }
        }
        return int
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            listaUsuarios = dbmySQLHelper.sacarUsuarios()
            adapterRecycler.setData(listaUsuarios)
        }
    }

}