package com.example.kotlin3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin3.AdapterRecycler.ViewHolder
import com.makeramen.roundedimageview.RoundedImageView

class AdapterRecycler(var listaUsuarios: List<Usuario>, cards: Cards): RecyclerView.Adapter<ViewHolder>()  {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: onItemClickListener){

        mListener =listener
    }

    fun  setData(listaUsuarios: List<Usuario>) {
        this.listaUsuarios = listaUsuarios
        notifyDataSetChanged()
    }

    class ViewHolder(inflate: View, listener: onItemClickListener): RecyclerView.ViewHolder(inflate) {

        val nombre:TextView = inflate.findViewById(R.id.nombre)
        val fecha:TextView = inflate.findViewById(R.id.fecha)
        val imagen:RoundedImageView = inflate.findViewById(R.id.imagenUsuario)

        fun render(usuario: Usuario){
            nombre.text =usuario.nombre
            fecha.text = usuario.fecha
            imagen.setImageBitmap(usuario.foto)
        }

        init {
            itemView.setOnClickListener{

                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater: LayoutInflater= LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.tarjetas, parent,false), mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item= listaUsuarios[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

}
