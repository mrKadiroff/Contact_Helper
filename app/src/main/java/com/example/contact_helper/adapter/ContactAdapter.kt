package com.example.contact_helper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contact_helper.R
import com.example.contact_helper.adapter.model.Kontakt
import com.example.contact_helper.databinding.ContactChildBinding

class ContactAdapter(var list : List<Kontakt>, val ctx:Context)
    : RecyclerView.Adapter<ContactAdapter.Vh>() {

        inner class Vh(var contactChildBinding: ContactChildBinding): RecyclerView.ViewHolder(contactChildBinding.root){
            fun onBind(kontakt: Kontakt){
                contactChildBinding.name.text = kontakt.name
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ContactChildBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

}