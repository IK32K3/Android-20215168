package com.example.studentman

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddStudent : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_add_student)

        val editName: EditText = findViewById(R.id.edit_student_name)
        val editId: EditText = findViewById(R.id.edit_student_id)
        val btnAdd: Button = findViewById(R.id.btn_add)

        btnAdd.setOnClickListener {
            val name = editName.text.toString()
            val id = editId.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("student_name", name)
            resultIntent.putExtra("student_id", id)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}