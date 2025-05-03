package vn.hust.studentlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentListActivity : AppCompatActivity() {
    private lateinit var adapter: StudentAdapter
    private lateinit var launcher1: ActivityResultLauncher<Intent>
    private var studentList = mutableListOf<StudentModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_lists)
        repeat(20){studentList.add(StudentModel("Student $it", "SV $it","it012345678","sv$it@sis.hust.edu.vn" ))}

        val listItem = findViewById<RecyclerView>(R.id.list_item)

        adapter = StudentAdapter(studentList)

        listItem.adapter = adapter
        listItem.layoutManager = LinearLayoutManager(this)
        listItem.itemAnimator = DefaultItemAnimator()

        launcher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK){
                val name = it.data?.getStringExtra("name")
                val mssv = it.data?.getStringExtra("mssv")
                val phone = it.data?.getStringExtra("phone")
                val email = it.data?.getStringExtra("email")
                if (name != null && mssv != null && phone != null && email != null) {
                  studentList.add(0, StudentModel(name, mssv, phone, email))
                  adapter.notifyItemInserted(0)
                }
            }
            else if(it.resultCode == RESULT_CANCELED){
                // Do nothing
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_student -> {
                val intent = Intent(this,AddNewStudent::class.java)
                launcher1.launch(intent)
            }
        }
        return true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            val position = data?.getIntExtra("position", -1) ?: return
            val name = data.getStringExtra("name")
            val mssv = data.getStringExtra("mssv")
            val phone = data.getStringExtra("phone")
            val email = data.getStringExtra("email")
            Log.d("TAG", "onActivityResult: $position $name $mssv $phone $email")
            if (position != -1 && name != null && mssv != null && phone != null && email != null) {
                val student = studentList[position]
                student.hoten = name
                student.mssv = mssv
                student.phone = phone
                student.email = email
                adapter.notifyItemChanged(position)
            }
        }
    }

}