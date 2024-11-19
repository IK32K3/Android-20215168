package com.example.studentman

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText

class StudentDialog(context: Context) {

    private val dialogBuilder = AlertDialog.Builder(context)
    private val dialogView: View = LayoutInflater.from(context).inflate(R.layout.layout_dialog_student, null)
    private val editStudentName: EditText = dialogView.findViewById(R.id.edit_student_name)
    private val editStudentId: EditText = dialogView.findViewById(R.id.edit_student_id)

    init {
        dialogBuilder.setView(dialogView)
    }


    fun showAddDialog(onAdd: (String, String) -> Unit) {
        dialogBuilder.setTitle("Add Student")
            .setPositiveButton("Add") { _, _ ->
                onAdd(editStudentName.text.toString(), editStudentId.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    fun showEditDialog(student: StudentModel, onEdit: (String, String) -> Unit) {
        editStudentName.setText(student.studentName)
        editStudentId.setText(student.studentId)

        dialogBuilder.setTitle("Edit Student")
            .setPositiveButton("Update") { _, _ ->
                onEdit(editStudentName.text.toString(), editStudentId.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}