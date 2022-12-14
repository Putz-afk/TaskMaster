package com.D121201030.task

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.D121201030.task.databinding.ActivityAddEditBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEditActivity : AppCompatActivity() {
    private lateinit var noteTitleEdit: EditText
    private lateinit var noteDescEdit: EditText
    private lateinit var button: Button
    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: ActivityAddEditBinding
    private lateinit var itemAdapter: ArrayAdapter<CharSequence>
    private var noteID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteViewModel::class.java]


        noteTitleEdit = findViewById(R.id.idNoteTitle)
        noteDescEdit = findViewById(R.id.idNoteDesc)
        button = findViewById(R.id.idButton)

        val noteType = intent.getStringExtra("noteType")


        if (noteType.equals("Edit")) {
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteID = intent.getIntExtra("noteId", -1)

            binding.idATV.setText(intent.getStringExtra("notePriority"))

            button.text = resources.getString(R.string.update_btn_text)
            noteTitleEdit.setText(noteTitle)
            noteDescEdit.setText(noteDescription)
        } else {
            button.text = resources.getString(R.string.save_btn_text)
        }

        button.setOnClickListener {
            binding.apply {
                val noteTitle = noteTitleEdit.text.toString()
                val noteDescription = noteDescEdit.text.toString()

                if (noteType.equals("Edit")) {
                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                        val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.US)
                        val currentDateAndTime: String = sdf.format(Date())
                        val notePriority = idATV.text.toString()
                        val updatedNote =
                            Note(noteTitle, noteDescription, notePriority, currentDateAndTime)
                        updatedNote.id = noteID
                        viewModel.updateNote(updatedNote)
                        Toast.makeText(this@AddEditActivity, "Note Updated..", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                        val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.US)
                        val currentDateAndTime: String = sdf.format(Date())
                        val notePriority = idATV.text.toString()

                        viewModel.addNote(
                            Note(noteTitle, noteDescription, notePriority, currentDateAndTime)
                        )
                        Toast.makeText(this@AddEditActivity, "$noteTitle Added", Toast.LENGTH_LONG).show()
                    }
                }
                startActivity(Intent(applicationContext, MainActivity::class.java))
                this@AddEditActivity.finish()
            }
        }
        prioSetter()
    }

    fun prioSetter() {
        itemAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.priority_list,
            android.R.layout.simple_list_item_1
        )
        binding.idATV.setAdapter(itemAdapter)
    }
}