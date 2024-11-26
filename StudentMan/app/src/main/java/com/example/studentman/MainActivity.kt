package com.example.studentman

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val students = mutableListOf<StudentModel>()
    private lateinit var studentAdapter: ArrayAdapter<StudentModel>
    private var deletedStudent: StudentModel? = null
    private var deletedStudentPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo danh sách sinh viên (Ví dụ)
        students.addAll(listOf(
            StudentModel("Nguyễn Văn An", "SV001"),
            StudentModel("Trần Thị Bảo", "SV002"),
            StudentModel("Lê Hoàng Cường", "SV003")
        ))

        // Tạo adapter và liên kết với ListView
        studentAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, students)
        val listView: ListView = findViewById(R.id.list_view_students)
        listView.adapter = studentAdapter

        // Thiết lập sự kiện khi chọn mục trong ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedStudent = students[position]
            val intent = Intent(this, EditStudent::class.java)
            intent.putExtra("student_name", selectedStudent.studentName)
            intent.putExtra("student_id", selectedStudent.studentId)
            intent.putExtra("position", position)
            startActivityForResult(intent, 1)
        }

        // Thiết lập Context Menu cho ListView
        registerForContextMenu(listView)
    }

    // Thiết lập Option Menu (Add New)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_new -> {
                val intent = Intent(this, AddStudent::class.java)
                startActivityForResult(intent, 2)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // Xử lý Context Menu (Edit & Remove)
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position
        val selectedStudent = students[position]

        return when (item.itemId) {
            R.id.menu_edit -> {
                val intent = Intent(this, EditStudent::class.java)
                intent.putExtra("student_name", selectedStudent.studentName)
                intent.putExtra("student_id", selectedStudent.studentId)
                intent.putExtra("position", position)
                startActivityForResult(intent, 1)
                true
            }
            R.id.menu_remove -> {
                // Hiển thị hộp thoại xác nhận trước khi xóa
                showDeleteConfirmationDialog(selectedStudent, position)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Hiển thị hộp thoại xác nhận xóa
    private fun showDeleteConfirmationDialog(student: StudentModel, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${student.studentName}?")
            .setPositiveButton("Có") { _, _ ->
                // Tiến hành xóa sinh viên và lưu lại thông tin để hoàn tác
                deleteStudent(student, position)
            }
            .setNegativeButton("Không", null)
            .show()
    }

    // Xóa sinh viên khỏi danh sách
    private fun deleteStudent(student: StudentModel, position: Int) {
        deletedStudent = student
        deletedStudentPosition = position
        students.removeAt(position)
        studentAdapter.notifyDataSetChanged()

        // Hiển thị Snackbar cho phép hoàn tác hành động xóa
        showUndoSnackbar()
    }

    // Hiển thị Snackbar với tùy chọn hoàn tác
    private fun showUndoSnackbar() {
        val snackbar = Snackbar.make(
            findViewById(R.id.list_view_students),
            "${deletedStudent?.studentName} đã bị xóa",
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction("Hoàn tác") {
            // Khôi phục lại sinh viên đã bị xóa
            deletedStudent?.let { student ->
                students.add(deletedStudentPosition!!, student)
                studentAdapter.notifyDataSetChanged()
                Snackbar.make(
                    findViewById(R.id.list_view_students),
                    "${student.studentName} đã được khôi phục",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        snackbar.show()
    }

    // Nhận kết quả từ các Activity (Add hoặc Edit)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> { // Chỉnh sửa sinh viên
                    val name = data?.getStringExtra("student_name") ?: ""
                    val id = data?.getStringExtra("student_id") ?: ""
                    val position = data?.getIntExtra("position", -1) ?: -1
                    if (position != -1) {
                        students[position] = StudentModel(name, id)
                        studentAdapter.notifyDataSetChanged()
                    }
                }
                2 -> { // Thêm sinh viên mới
                    val name = data?.getStringExtra("student_name") ?: ""
                    val id = data?.getStringExtra("student_id") ?: ""
                    students.add(StudentModel(name, id))
                    studentAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}