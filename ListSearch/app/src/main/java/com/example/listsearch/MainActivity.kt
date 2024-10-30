package com.example.listsearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Student(
    val hoTen: String,
    val mssv: String
)

class StudentAdapter(private var studentList: List<Student>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val studentIdTextView: TextView = itemView.findViewById(R.id.studentIdTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.nameTextView.text = student.hoTen
        holder.studentIdTextView.text = student.mssv
    }

    override fun getItemCount(): Int = studentList.size

    fun updateList(newList: List<Student>) {
        studentList = newList
        notifyDataSetChanged()
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var studentAdapter: StudentAdapter
    private val studentList = listOf(
        Student("Nguyễn Văn A", "20216996"),
        Student("Trần Thị B", "20226969"),
        Student("Lê Văn C", "20196969"),
        Student("Nguyễn H.V", "20215169")
        // Thêm các sinh viên khác vào danh sách
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val studentRecyclerView = findViewById<RecyclerView>(R.id.studentRecyclerView)

        studentAdapter = StudentAdapter(studentList)
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        studentRecyclerView.adapter = studentAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString().trim()
                if (keyword.length > 2) {
                    val filteredList = studentList.filter { student ->
                        student.hoTen.contains(keyword, ignoreCase = true) ||
                                student.mssv.contains(keyword, ignoreCase = true)
                    }
                    studentAdapter.updateList(filteredList)
                } else {
                    studentAdapter.updateList(studentList)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}