package com.libertosforever.note.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.libertosforever.note.R
import com.libertosforever.note.databinding.ActivityShopListBinding
import com.libertosforever.note.db.MainViewModel
import com.libertosforever.note.db.ShopListItemAdapter
import com.libertosforever.note.dialogs.EditItemListDialog
import com.libertosforever.note.entities.ShopListItem
import com.libertosforever.note.entities.ShopListNameItem

class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener{
    private lateinit var binding: ActivityShopListBinding
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initRcView()
        listItemObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shop_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item_shop)
        edItem = newItem.actionView.findViewById(R.id.edNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {
                addNewShopItem()
            }
            R.id.delete_list -> {
                mainViewModel.deleteShopList(shopListNameItem?.id!!, true)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteShopList(shopListNameItem?.id!!, false)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addNewShopItem() {
        if (edItem?.text.toString().isEmpty()) return
        val item = ShopListItem(
            null,
            edItem?.text.toString(),
            "",
            false,
            shopListNameItem?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertShopItem(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).observe(this, {
        adapter?.submitList(it)
        binding.tvEmpty.visibility = if (it.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        })
    }

    private fun initRcView() = with(binding){
        adapter = ShopListItemAdapter(this@ShopListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ShopListActivity)
        rcView.adapter = adapter
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                saveItem.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()
                return true
            }

        }
    }

    private fun init() {
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
    }

    companion object {
        const val SHOP_LIST_NAME = "shop_list_name"
    }

    override fun onClickItem(shopListItem: ShopListItem, state: Int) {
        when (state) {
            ShopListItemAdapter.CHECK_BOX -> mainViewModel.updateListItem(shopListItem)
            ShopListItemAdapter.EDIT -> editListItem(shopListItem)
        }
    }

    private fun editListItem(item: ShopListItem) {
        EditItemListDialog.showDialog(this, item, object : EditItemListDialog.Listener {

            override fun onClick(item: ShopListItem) {
                mainViewModel.updateListItem(item)
            }

        })
    }
}