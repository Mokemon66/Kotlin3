package com.example.kotlin3

import android.graphics.Bitmap

class Usuario {

    var nombre: String
    var contra: String
    var fecha: String
    var foto: Bitmap
    var admin: Boolean

    constructor( nombre: String, contra: String, fecha: String, foto: Bitmap, admin: Boolean) {
        this.nombre=nombre
        this.contra=contra
        this.foto=foto
        this.fecha=fecha
        this.admin=admin
    }

}