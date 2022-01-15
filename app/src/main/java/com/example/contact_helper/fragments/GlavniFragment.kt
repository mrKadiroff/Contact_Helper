package com.example.contact_helper.fragments

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.contact_helper.MainActivity
import com.example.contact_helper.R
import com.example.contact_helper.adapter.ContactAdapter
import com.example.contact_helper.adapter.model.Kontakt
import com.example.contact_helper.databinding.FragmentGlavniBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GlavniFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GlavniFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentGlavniBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = FragmentGlavniBinding.inflate(layoutInflater,container,false)


        myMethod()


        return binding.root
    }
    fun myMethod() {
        askPermission(Manifest.permission.READ_CONTACTS) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()



            val na = activity?.application!!.contentResolver


            val contactList : MutableList<Kontakt> = ArrayList()
//            val contentResolver: ContentResolver = MainActivity().getContentResolver()
            val contacts = na.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
            while (contacts!!.moveToNext()){
                val name = contacts.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = contacts.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val obj = Kontakt()
                obj.name = name
                obj.number = number


                contactList.add(obj)

            }
            binding.contactList.adapter = ContactAdapter(contactList, object : ContactAdapter.OnItemClickListener{
                override fun phoneClick(kontakt: Kontakt, position: Int, imageView: ImageView) {

                }

                override fun smsClick(kontakt: Kontakt, position: Int, imageView: ImageView) {
                    var bundle = Bundle()
                    bundle.putSerializable("kontakt",kontakt)
                    findNavController().navigate(R.id.smsFragment, bundle)
                }

                override fun swipePhoneClick(
                    kontakt: Kontakt,
                    position: Int,
                    imageView: ImageView
                ) {
                    askPermission(Manifest.permission.CALL_PHONE){
                        //all permissions already granted or just granted
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:" + kontakt.number)

                        startActivity(callIntent)

                    }.onDeclined { e ->
                        if (e.hasDenied()) {


                            android.app.AlertDialog.Builder(binding.root.context)
                                .setMessage("Please accept our permissions")
                                .setPositiveButton("yes") { dialog, which ->
                                    e.askAgain();
                                } //ask again
                                .setNegativeButton("no") { dialog, which ->
                                    dialog.dismiss();
                                }
                                .show();
                        }

                        if(e.hasForeverDenied()) {

                            // you need to open setting manually if you really need it
                            e.goToSettings();
                        }
                    }







//                    Toast.makeText(binding.root.context, "swipe phne clicked", Toast.LENGTH_SHORT).show()
                }

            })

                contacts.close()




        }.onDeclined { e ->
            if (e.hasDenied()) {
                Toast.makeText(binding.root.context, "Denied", Toast.LENGTH_SHORT).show()

                AlertDialog.Builder(binding.root.context)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if(e.hasForeverDenied()) {
                Toast.makeText(binding.root.context, "Denied", Toast.LENGTH_SHORT).show()

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GlavniFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GlavniFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}