package vn.hust.studentlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(val students: MutableList<StudentModel>,val listener: DeleteListener): RecyclerView.Adapter<StudentAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hoten.text = students[position].hoten
        holder.mssv.text = students[position].mssv
        holder.popupMenu.setOnClickListener {
            val popupMenu = android.widget.PopupMenu(holder.itemView.context, holder.popupMenu)
            popupMenu.menuInflater.inflate(R.menu.student_item_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menu_update -> {
                        val intent = android.content.Intent(holder.itemView.context, AddNewStudent::class.java)
                        intent.putExtra("position", position)
                        intent.putExtra("name", students[position].hoten)
                        intent.putExtra("mssv", students[position].mssv)
                        intent.putExtra("phone", students[position].phone)
                        intent.putExtra("email", students[position].email)
                        (holder.itemView.context as androidx.appcompat.app.AppCompatActivity).startActivityForResult(intent, 2)
                        true
                    }
                    R.id.menu_delete -> {
                        listener.onDelete(position)
                        true
                    }
                    R.id.menu_call -> {
                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL)
                        intent.data = android.net.Uri.parse("tel:${students[position].phone}")
                        holder.itemView.context.startActivity(intent)
                        true
                    }
                    R.id.menu_email -> {
                        val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO)
                        intent.data = android.net.Uri.parse("mailto:${students[position].email}")
                        holder.itemView.context.startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val hoten = itemView.findViewById<TextView>(R.id.name)
        val mssv = itemView.findViewById<TextView>(R.id.id)
        val popupMenu = itemView.findViewById<ImageView>(R.id.popup_menu)
    }

    interface DeleteListener {
        fun onDelete(position: Int)
    }
}