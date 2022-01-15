package com.example.contact_helper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.contact_helper.R
import com.example.contact_helper.adapter.model.Kontakt
import com.example.contact_helper.databinding.ContactChildBinding

class ContactAdapter(var list : List<Kontakt>, var onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<ContactAdapter.Vh>() {

        inner class Vh(var contactChildBinding: ContactChildBinding): RecyclerView.ViewHolder(contactChildBinding.root){
            fun onBind(kontakt: Kontakt){
                contactChildBinding.name.text = kontakt.name
               contactChildBinding.phoneText.text = kontakt.number

                contactChildBinding.kattatelefon.setOnClickListener {
                    onItemClickListener.swipePhoneClick(kontakt,position,contactChildBinding.kattatelefon)
                }
                contactChildBinding.sms.setOnClickListener {
                    onItemClickListener.smsClick(kontakt,position,contactChildBinding.sms)
                }
                contactChildBinding.swipetelefon.setOnClickListener {
                    onItemClickListener.swipePhoneClick(kontakt,position,contactChildBinding.swipetelefon)
                }

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ContactChildBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun phoneClick(kontakt: Kontakt, position: Int, imageView: ImageView)
        fun smsClick(kontakt: Kontakt, position: Int, imageView: ImageView)
        fun swipePhoneClick(kontakt: Kontakt, position: Int, imageView: ImageView)
    }

}