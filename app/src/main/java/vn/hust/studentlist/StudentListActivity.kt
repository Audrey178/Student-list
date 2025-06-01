package vn.hust.studentlist

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
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
    lateinit var db: SQLiteDatabase
    private var studentList = mutableListOf<StudentModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_lists)

        db = SQLiteDatabase.openDatabase("${filesDir.path}/students_db", null, SQLiteDatabase.CREATE_IF_NECESSARY)
        //createTable()

        val columns = arrayOf("id", "name", "mssv", "phone", "email")
        var cursor = db.query("students", columns, null, null, null, null, null)
        cursor.moveToFirst()
        do{
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val mssv = cursor.getString(2)
            val phone = cursor.getString(3)
            val email = cursor.getString(4)
            studentList.add(StudentModel(id, name, mssv, phone, email))
        } while(cursor.moveToNext())
        cursor.close()

        val listItem = findViewById<RecyclerView>(R.id.list_item)

        adapter = StudentAdapter(studentList, object : StudentAdapter.DeleteListener {
            override fun onDelete(position: Int) {
                // Delete from database
                db.beginTransaction()
                try {
                    val sql = "DELETE FROM students WHERE id = ?"
                    db.execSQL(sql, arrayOf(studentList[position].id))
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Log.e("Database", "Error deleting data", e)
                } finally {
                    db.endTransaction()
                }
                // Remove from list and notify adapter
                studentList.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        })

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
                    // Insert into database
                    db.beginTransaction()
                    try{
                        val sql = "INSERT INTO students (name, mssv, phone, email) VALUES (?, ?, ?, ?)"
                        db.execSQL(sql, arrayOf(name, mssv, phone, email))
                        db.setTransactionSuccessful()
                    } catch (e: Exception) {
                        Log.e("Database", "Error inserting data", e)
                    } finally {
                        db.endTransaction()
                    }
                    // Add to list and notify adapter
                   cursor = db.rawQuery("SELECT last_insert_rowid()", null)
                   cursor.moveToFirst()
                   val id = cursor.getInt(0)
                   cursor.close()
                  studentList.add(StudentModel(id, name, mssv, phone, email))
                  adapter.notifyItemInserted(studentList.size - 1)
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
                // Update database
                db.beginTransaction()
                try {
                    val sql = "UPDATE students SET name = ?, mssv = ?, phone = ?, email = ? WHERE id = ?"
                    db.execSQL(sql, arrayOf(name, mssv, phone, email, student.id))
                    db.setTransactionSuccessful()
                } catch (e: Exception) {
                    Log.e("Database", "Error updating data", e)
                } finally {
                    db.endTransaction()
                }
                adapter.notifyItemChanged(position)
            }
        }
    }

    fun createTable(){
        db.beginTransaction()
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS students (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mssv TEXT, phone TEXT, email TEXT)")
            repeat(10) { i ->
                db.execSQL("INSERT INTO students (name, mssv, phone, email) VALUES ('Student $i', 'SV $i', '012345678', 'sv$i@sis.hust.edu.vn')")
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("Database", "Error creating table", e)
        } finally {
            db.endTransaction()
        }
    }

}