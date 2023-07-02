package com.example.projekt

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var noteListView: ListView
    private lateinit var noteEditText: EditText
    private lateinit var createButton: Button
    private lateinit var deleteButton: Button

    private val noteList = mutableListOf<String>()
    private lateinit var noteAdapter: NoteAdapter
    private var selectedNoteIndex: Int = -1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteListView = findViewById(R.id.noteListView)
        noteEditText = findViewById(R.id.noteEditText)
        createButton = findViewById(R.id.createButton)
        deleteButton = findViewById(R.id.deleteButton)

        sharedPreferences = getSharedPreferences("NoteApp", MODE_PRIVATE)
        loadNoteList()

        noteAdapter = NoteAdapter(this, noteList)
        noteListView.adapter = noteAdapter

        noteListView.setOnItemClickListener { _, _, position, _ ->
            selectedNoteIndex = position
        }

        createButton.setOnClickListener {
            val noteText = noteEditText.text.toString()
            if (noteText.isNotEmpty()) {
                addNoteToList(noteText)
                showToast("Utworzono notatkę: $noteText")
                noteEditText.text.clear()
                saveNoteList()
            } else {
                showToast("Wprowadź treść notatki")
            }
        }

        deleteButton.setOnClickListener {
            if (selectedNoteIndex != -1) {
                val selectedNote = noteList[selectedNoteIndex]
                removeNoteFromList(selectedNote)
                showToast("Usunięto notatkę: $selectedNote")
                selectedNoteIndex = -1
                saveNoteList()
            } else {
                showToast("Nie zaznaczono żadnej notatki do usunięcia")
            }
        }
    }

    private fun addNoteToList(note: String) {
        noteList.add(note)
        noteAdapter.notifyDataSetChanged()
    }

    private fun removeNoteFromList(note: String) {
        noteList.remove(note)
        noteAdapter.notifyDataSetChanged()
    }

    private fun saveNoteList() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("NoteList", noteList.toSet())
        editor.apply()
    }

    private fun loadNoteList() {
        val noteSet = sharedPreferences.getStringSet("NoteList", setOf())
        noteList.addAll(noteSet ?: emptySet())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}