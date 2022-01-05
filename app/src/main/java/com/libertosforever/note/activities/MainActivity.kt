package com.libertosforever.note.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.libertosforever.note.R
import com.libertosforever.note.databinding.ActivityMainBinding
import com.libertosforever.note.fragments.FragmentManager
import com.libertosforever.note.fragments.NoteFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemReselectedListener {
            when(it.itemId){

                R.id.settings -> {

                }

                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }

                R.id.shop_list -> {

                }

                R.id.new_item -> {

                }
            }
            true
        }
    }
}