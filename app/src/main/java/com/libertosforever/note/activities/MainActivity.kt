package com.libertosforever.note.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.libertosforever.note.R
import com.libertosforever.note.databinding.ActivityMainBinding
import com.libertosforever.note.dialogs.NewListDialog
import com.libertosforever.note.fragments.FragmentManager
import com.libertosforever.note.fragments.NoteFragment
import com.libertosforever.note.fragments.ShopListNamesFragment
import com.libertosforever.note.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private lateinit var defPreferences: SharedPreferences
    private var currentMenuItemId = R.id.shop_list
    private var currentTheme = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        defPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPreferences.getString("theme_key", "orange").toString()
        setTheme(getSelectedTheme())

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemReselectedListener {
            when(it.itemId){

                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity :: class.java ))
                }

                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }

                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }

                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
        if (defPreferences.getString("theme_key", "orange") != currentTheme) recreate()

    }

    private fun getSelectedTheme(): Int {
        return if (defPreferences.getString("theme_key", "orange") == "orange") {
            R.style.Theme_NoteOrange
        } else {
            R.style.Theme_NoteBlue
        }
    }

    override fun onClick(name: String) {
        Log.d("MyLog", "Name: $name")
    }
}