package vn.hust.studentlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class StudentAdapter(val students: MutableList<StudentModel>): BaseAdapter() {
    override fun getCount(): Int {
        return students.size
    }

    override fun getItem(position: Int): Any {
        return students[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: MyViewHolder
        var itemView: View
        if (convertView == null) {
            itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.student_item, parent, false)
            viewHolder = MyViewHolder()
            viewHolder.hoten = itemView.findViewById<TextView>(R.id.name)
            viewHolder.mssv = itemView.findViewById<TextView>(R.id.id)
            viewHolder.del = itemView.findViewById<Button>(R.id.delete)
            itemView.tag = viewHolder
        } else {
            itemView = convertView
            viewHolder = itemView.tag as MyViewHolder
        }
        val student = students[position]
        viewHolder.hoten.text = student.hoten
        viewHolder.mssv.text = student.mssv
        viewHolder.del.setOnClickListener{
            students.removeAt(position)
            notifyDataSetChanged()
        }
        return itemView
    }
    class MyViewHolder {
        lateinit var hoten: TextView
        lateinit var mssv: TextView
        lateinit var del : Button
    }
}