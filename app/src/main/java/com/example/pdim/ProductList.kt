package com.example.pdim

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class ProductList(private val context: Activity, internal var products: List<Products>) : ArrayAdapter<Products>(context, R.layout.item_view, products) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.item_view, null, true)

        val textViewName = listViewItem.findViewById(R.id.productName) as TextView
        val textViewDes = listViewItem.findViewById(R.id.productDsc) as TextView

        val textViewPrice = listViewItem.findViewById(R.id.price) as TextView
        val textViewSiUnit = listViewItem.findViewById(R.id.siUnit) as TextView

        val textViewExDate = listViewItem.findViewById(R.id.exDate) as TextView
        val textViewManuDate = listViewItem.findViewById(R.id.manuDate) as TextView



        val products = products[position]
        textViewName.text = products.productName
        textViewDes.text = products.productDescription

        textViewPrice.text = products.price
        textViewSiUnit.text = products.siUnit
        textViewExDate.text = products.expiryDate
        textViewManuDate.text = products.manufactureDate

        return listViewItem
    }
}