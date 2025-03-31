package vn.hust.studentlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StudentListActivity : AppCompatActivity() {
    private lateinit var adapter: StudentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_lists)
        val studentList = mutableListOf<StudentModel>()
        repeat(20){studentList.add(StudentModel("Student $it", "SV $it"))}

        val listItem = findViewById<ListView>(R.id.list_item)
        val nameSV = findViewById<EditText>(R.id.ten)
        val msSV = findViewById<EditText>(R.id.maso)
        val buttonadd = findViewById<Button>(R.id.add)


        val adapter = StudentAdapter(studentList)


        buttonadd.setOnClickListener {
            studentList.add(StudentModel(nameSV.text.toString(),msSV.text.toString()))
            adapter.notifyDataSetChanged()
        }
        listItem.adapter = adapter
    }
}