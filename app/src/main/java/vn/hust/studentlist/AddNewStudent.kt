package vn.hust.studentlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddNewStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_student)
        val name = findViewById<EditText>(R.id.add_name)
        val mssv = findViewById<EditText>(R.id.add_mssv)
        val phone = findViewById<EditText>(R.id.add_phone)
        val email = findViewById<EditText>(R.id.add_email)


        val position = intent.getIntExtra("position", -1)
        if(position != -1) {
                name.setText(intent.getStringExtra("name"))
                mssv.setText(intent.getStringExtra("mssv"))
                phone.setText(intent.getStringExtra("phone"))
                email.setText(intent.getStringExtra("email"))
            }


        findViewById<Button>(R.id.ok).setOnClickListener{
            if(name.text.toString() != "" && mssv.text.toString() != "" && phone.text.toString() != "" && email.text.toString() != ""){
                val resultIntent = intent.apply {
                                putExtra("position", position)
                                putExtra("name", name.text.toString())
                                putExtra("mssv", mssv.text.toString())
                                putExtra("phone", phone.text.toString())
                                putExtra("email", email.text.toString())
                            }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

        findViewById<Button>(R.id.cancel).setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}