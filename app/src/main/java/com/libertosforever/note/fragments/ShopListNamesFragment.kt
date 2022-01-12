package com.libertosforever.note.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.libertosforever.note.activities.MainApp
import com.libertosforever.note.databinding.FragmentShopListNamesBinding
import com.libertosforever.note.db.MainViewModel
import com.libertosforever.note.db.ShopListNameAdapter
import com.libertosforever.note.dialogs.DeleteDialog
import com.libertosforever.note.dialogs.NewListDialog
import com.libertosforever.note.entities.NoteItem
import com.libertosforever.note.entities.ShoppingListName
import com.libertosforever.note.utils.TimeManager

class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {
    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter: ShopListNameAdapter
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database) }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val shopListName = ShoppingListName(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }
        }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rcView.adapter = adapter

    }

    private fun observer() {
        mainViewModel.allShopListNames.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener {

            override fun onClick() {
                mainViewModel.deleteShopListName(id)
            }

        })
    }

    override fun editItem(shopListName: ShoppingListName) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {

                mainViewModel.updateListName(shopListName.copy(name = name))
            }
        }, shopListName.name)
    }

    override fun onClickItem(shopListName: ShoppingListName) {

    }

}