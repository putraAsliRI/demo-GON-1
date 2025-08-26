package com.asliri.samplegon

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(private val items: List<String>) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    inner class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 16
            }
            textSize = 16f
            setPadding(24, 12, 24, 12)
            background = ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].substring(0, 1) // inisial
    }

    override fun getItemCount() = items.size
}
