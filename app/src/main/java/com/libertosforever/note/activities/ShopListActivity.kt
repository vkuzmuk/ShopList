package com.libertosforever.note.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.libertosforever.note.databinding.ActivityShopListBinding
import com.libertosforever.note.db.MainViewModel

class ShopListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopListBinding
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}