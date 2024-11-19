package com.example.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
  private val students: MutableList<StudentModel>,
  private val onEditClick: (student: StudentModel, position: Int) -> Unit,
  private val onDeleteClick: (student: StudentModel, position: Int) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textName: TextView = itemView.findViewById(R.id.text_student_name)
    val textId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
    return StudentViewHolder(view)
  }

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]
    holder.textName.text = student.studentName
    holder.textId.text = student.studentId

    // Sự kiện khi nhấn nút Edit
    holder.imageEdit.setOnClickListener {
      onEditClick(student, position)
    }

    // Sự kiện khi nhấn nút Delete
    holder.imageRemove.setOnClickListener {
      onDeleteClick(student, position)
    }
  }

  override fun getItemCount(): Int = students.size
}