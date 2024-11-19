package com.example.studentman

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.Dialog
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val students = mutableListOf<StudentModel>()
    private lateinit var studentAdapter: StudentAdapter
    private var deletedStudent: StudentModel? = null
    private var deletedStudentPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        students.addAll(listOf(
            StudentModel("Nguyễn Văn An", "SV001"),
            StudentModel("Trần Thị Bảo", "SV002"),
            StudentModel("Lê Hoàng Cường", "SV003"),
            StudentModel("Phạm Thị Dung", "SV004"),
            StudentModel("Đỗ Minh Đức", "SV005"),
            StudentModel("Vũ Thị Hoa", "SV006"),
            StudentModel("Hoàng Văn Hải", "SV007"),
            StudentModel("Bùi Thị Hạnh", "SV008"),
            StudentModel("Đinh Văn Hùng", "SV009"),
            StudentModel("Nguyễn Thị Linh", "SV010"),
            StudentModel("Phạm Văn Long", "SV011"),
            StudentModel("Trần Thị Mai", "SV012"),
            StudentModel("Lê Thị Ngọc", "SV013"),
            StudentModel("Vũ Văn Nam", "SV014"),
            StudentModel("Hoàng Thị Phương", "SV015"),
            StudentModel("Đỗ Văn Quân", "SV016"),
            StudentModel("Nguyễn Thị Thu", "SV017"),
            StudentModel("Trần Văn Tài", "SV018"),
            StudentModel("Phạm Thị Tuyết", "SV019"),
            StudentModel("Lê Văn Vũ", "SV020")
        ))

        studentAdapter = StudentAdapter(students, ::onEditStudent, ::onDeleteStudent)

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            StudentDialog(this).showAddDialog { name, id ->
                val newStudent = StudentModel(name, id)
                students.add(newStudent)
                studentAdapter.notifyItemInserted(students.size - 1)
            }
        }
    }

    private fun onEditStudent(student: StudentModel, position: Int) {
        StudentDialog(this).showEditDialog(student) { name, id ->
            student.studentName = name
            student.studentId = id
            studentAdapter.notifyItemChanged(position)
        }
    }

    private fun onDeleteStudent(student: StudentModel, position: Int) {
        deletedStudent = student
        deletedStudentPosition = position

        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.studentName}?")
            .setPositiveButton("Yes") { _, _ ->
                students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)
                // Show Snackbar with Undo option
                showUndoSnackbar()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showUndoSnackbar() {
        val snackbar = Snackbar.make(
            findViewById(R.id.recycler_view_students),
            "${deletedStudent?.studentName} deleted",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("UNDO") {
            deletedStudent?.let { student ->
                // Undo deleted item
                students.add(deletedStudentPosition!!, student)
                studentAdapter.notifyItemInserted(deletedStudentPosition!!)
                // Show another Snackbar to indicate that the undo was successful
                Snackbar.make(
                    findViewById(R.id.recycler_view_students),
                    "${student.studentName} restored",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        snackbar.show()
    }
}