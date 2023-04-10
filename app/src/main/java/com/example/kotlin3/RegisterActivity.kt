package com.example.kotlin3

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.androidadvance.topsnackbar.TSnackbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 1
    private val GALERIA_REQUEST_CODE = 2
    private lateinit var bitmap: Bitmap
    private lateinit var img: ImageView
    private lateinit var registrar: Button
    private lateinit var switch: SwitchMaterial
    private lateinit var nombreActulizar: String
    private val dbmySQLHelper: mySQLHelper = mySQLHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val textFieldNombre: TextInputLayout = findViewById(R.id.textFieldNombre)
        val textFieldContra: TextInputLayout = findViewById(R.id.textFieldContra)
        val textFieldFecha: TextInputLayout = findViewById(R.id.lytFecha)
        val fotoPerfil: Button = findViewById(R.id.camara)
        switch = findViewById(R.id.switchAdmin)
        registrar = findViewById(R.id.register)
        img = findViewById(R.id.imagen)
        if (intent.hasExtra("nombre")) {
            var usuario: Usuario? = dbmySQLHelper.sacarUnUsuario(intent.extras!!.getString("nombre").toString())
            nombreActulizar = usuario!!.nombre
            textFieldNombre.editText!!.setText(usuario!!.nombre)
            textFieldContra.editText!!.setText(usuario!!.contra)
            textFieldFecha.editText!!.setText(usuario!!.fecha)
            switch.isChecked = usuario.admin
            bitmap = usuario.foto
            img.setImageBitmap(bitmap)
            registrar.text = resources.getString(R.string.actualizar)
            registrar.isEnabled = true
        }
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("title")
        val picker: MaterialDatePicker<*> = builder.build()
        textFieldFecha.editText!!.setOnClickListener {

            if (!picker.isAdded) {
                picker.show(supportFragmentManager, picker.toString())
            }
        }

        picker.addOnPositiveButtonClickListener {
            val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = (picker.selection) as Long
            val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val date: Date = Date(calendar.timeInMillis)
            textFieldFecha.editText!!.setText(simpleDateFormat.format(date))

        }

        fotoPerfil.setOnClickListener {

            MaterialAlertDialogBuilder(
                this,
                R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setTitle(resources.getString(R.string.fotoPerfil))
                .setMessage(resources.getString(R.string.mensajeDialog))
                .setNegativeButton(resources.getString(R.string.galeria)) { dialog, which ->
                    val intent: Intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/"
                    startActivityForResult(intent, GALERIA_REQUEST_CODE)
                }
                .setPositiveButton(resources.getString(R.string.camara)) { dialog, which ->
                    val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                }
                .show()
        }

        registrar.setOnClickListener {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            var arrayBytes: ByteArray = byteArrayOutputStream.toByteArray()
            if (registrar.text.equals(resources.getString(R.string.actualizar))) {
                dbmySQLHelper.actualizarUsuario(
                    textFieldNombre.editText!!.text.toString(),
                    textFieldContra.editText!!.text.toString(),
                    textFieldFecha.editText!!.text.toString(), arrayBytes, switch.isChecked,nombreActulizar
                )
            } else {
                if (dbmySQLHelper.existeUsuario(textFieldNombre.editText!!.text.toString())) {
                    TSnackbar.make(
                        findViewById(R.id.layoutRegister),
                        resources.getString(R.string.mensajeUsuarioExiste),
                        TSnackbar.LENGTH_SHORT
                    ).show()
                } else {
                    dbmySQLHelper.datosUsuario(
                        textFieldNombre.editText!!.text.toString(),
                        textFieldContra.editText!!.text.toString(),
                        textFieldFecha.editText!!.text.toString(), arrayBytes, switch.isChecked
                    )
                }

            }
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            bitmap = data!!.extras!!.get("data") as Bitmap
            img.setImageBitmap(bitmap)
        } else if (requestCode == GALERIA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val uri: Uri? = data?.data
            img.setImageURI(uri)
            bitmap = (img.drawable as BitmapDrawable).bitmap
        }
        registrar.isEnabled = true
    }

    override fun onBackPressed() {
        val vibrator: Vibrator =
            applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)
        AlertDialog.Builder(this).setMessage(resources.getString(R.string.mensajeSalir))
            .setPositiveButton(
                resources.getString(R.string.si)
            ) { dialog, which ->
                salir()
            }.setNegativeButton(
                resources.getString(R.string.no)
            ) { dialog, which ->

            }.show()
    }

    private fun salir() {
        super.onBackPressed()
    }

}