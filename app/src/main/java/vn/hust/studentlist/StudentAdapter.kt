package vn.hust.studentlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(val students: MutableList<StudentModel>, val listener: ItemClickListener? = null): RecyclerView.Adapter<StudentAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return MyViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hoten.text = students[position].hoten
        holder.mssv.text = students[position].mssv
        holder.del.setOnClickListener {
            students.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
    }
    class MyViewHolder(itemView: View, listener: ItemClickListener?): RecyclerView.ViewHolder(itemView) {
        val hoten = itemView.findViewById<TextView>(R.id.name)
        val mssv = itemView.findViewById<TextView>(R.id.id)
        val del  = itemView.findViewById<Button>(R.id.delete)
    }

    interface ItemClickListener {
        fun onItemClicked(position: Int)
    }
}