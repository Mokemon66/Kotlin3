package com.example.kotlin3

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream

class mySQLHelper(context: Context) : SQLiteOpenHelper(context, "Usuarios.db", null, 2) {


    override fun onCreate(db: SQLiteDatabase?) {
        val crearTabla =
            "CREATE TABLE Usuarios (nombre TEXT PRIMARY KEY , password TEXT, fecha TEXT, img BLOB, admin NUMBER)"
        db!!.execSQL(crearTabla)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val borrarTabla = "DROP TABLE IF EXISTS Usuarios"
        db!!.execSQL(borrarTabla)
        onCreate(db)
    }

    fun datosUsuario(
        nombre: String,
        contra: String,
        fecha: String,
        foto: ByteArray,
        admin: Boolean
    ) {
        val datos = ContentValues()
        datos.put("nombre", nombre)
        datos.put("password", contra)
        datos.put("fecha", fecha)
        datos.put("img", foto)
        datos.put("admin", admin)
        val db = this.writableDatabase
        db.insert("Usuarios", null, datos)
    }

    fun actualizarUsuario(
        nombre: String,
        contra: String,
        fecha: String,
        foto: ByteArray,
        admin: Boolean,
        nombreUsuario: String
    ) {
        val datos = ContentValues()
        datos.put("nombre", nombre)
        datos.put("password", contra)
        datos.put("fecha", fecha)
        datos.put("img", foto)
        datos.put("admin", admin)
        val db = this.writableDatabase
        db.update("Usuarios", datos, "nombre='" + nombreUsuario + "'", null)
    }

    fun comprobarUsuario(nombre: String, contra: String): Boolean {
        val db = this.writableDatabase
        val cursor: Cursor =
            db.rawQuery(
                "SELECT * FROM Usuarios WHERE nombre='" + nombre + "' AND password ='" + contra + "'",
                null
            )

        return cursor.count != 0
    }

    fun sacarUsuarios(): List<Usuario> {
        var listaUsuarios: ArrayList<Usuario> = ArrayList<Usuario>()
        val db = this.writableDatabase
        val cursor: Cursor =
            db.rawQuery("SELECT * FROM Usuarios", null)
        if (cursor.moveToFirst()) {
            do {
                val blob: ByteArray = cursor.getBlob(3)
                val bais = ByteArrayInputStream(blob)
                val bitmap = BitmapFactory.decodeStream(bais)
                var admin: Boolean = false
                if (cursor.getInt(4) == 1) {
                    admin = true
                }
                listaUsuarios.add(
                    Usuario(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        bitmap,
                        admin
                    )
                )

            } while (cursor.moveToNext())
        }
        return listaUsuarios.toList()
    }

    fun borrarUsuario(nombre: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM Usuarios WHERE nombre='" + nombre + "'");
    }

    fun existeUsuario(nombre: String): Boolean {
        val db = this.writableDatabase
        val cursor: Cursor =
            db.rawQuery(
                "SELECT * FROM Usuarios WHERE nombre='" + nombre + "'",
                null
            )

        return cursor.count != 0
    }

    fun sacarUnUsuario(string: String): Usuario? {
        val db = this.writableDatabase
        var usuario: Usuario? = null
        val cursor: Cursor =
            db.rawQuery("SELECT * FROM Usuarios Where nombre='"+string+"'", null)
        if (cursor.moveToFirst()) {
            do {
                val blob: ByteArray = cursor.getBlob(3)
                val bais = ByteArrayInputStream(blob)
                val bitmap = BitmapFactory.decodeStream(bais)
                var admin: Boolean = false
                if (cursor.getInt(4) == 1) {
                    admin = true
                }
                usuario = Usuario(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    bitmap,
                    admin
                )


            } while (cursor.moveToNext())
        }
        return usuario
    }
}