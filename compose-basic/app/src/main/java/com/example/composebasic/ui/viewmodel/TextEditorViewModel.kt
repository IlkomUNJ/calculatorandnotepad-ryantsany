package com.example.composebasic.ui.viewmodel

import android.util.Log
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID


data class TextEditorUiState (
    val text: TextFieldValue = TextFieldValue(
        annotatedString = buildAnnotatedString { append("") }
    ),
    var fontSize: Int = 16,
    var isBoldActive: Boolean = false,
    var isItalicActive: Boolean = false
)

data class Note (
    val id: UUID = UUID.randomUUID(),
    val note: TextEditorUiState = TextEditorUiState()
)

class TextEditorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TextEditorUiState())
    val uiState: StateFlow<TextEditorUiState> = _uiState.asStateFlow()

    //    Test
    private var _noteList: List<Note> = emptyList()
    private var currentEditingNoteId: UUID? = null


    fun applyStyle(text: TextFieldValue, style: SpanStyle): TextFieldValue {
        val annotatedString = text.annotatedString
        val selected = text.selection

        if (selected.collapsed){
            return text
        }

        val newText = buildAnnotatedString {
            // Don't just append and add - you need to preserve existing styles
            append(annotatedString.text)

            // Copy over existing span styles
            annotatedString.spanStyles.forEach { spanStyle ->
                addStyle(spanStyle.item, spanStyle.start, spanStyle.end)
            }

            // Add the new style
            addStyle(style, selected.start, selected.end)
        }

        return text.copy(
            annotatedString = newText,
            selection = TextRange(selected.end)
        )
    }

    fun toggleBold() {
        _uiState.update {
            it.copy(
                isBoldActive = !it.isBoldActive
            )
        }
    }

    fun toggleItalic() {
        _uiState.update {
            it.copy(
                isItalicActive = !it.isItalicActive
            )
        }
    }

    fun enableBold(){
        _uiState.update {
            it.copy(
                isBoldActive = true
            )
        }
    }

    fun enableItalic(){
        _uiState.update {
            it.copy(
                isItalicActive = true
            )
        }
    }

    fun increaseFontSize() {
        _uiState.update {
            it.copy(
                fontSize = it.fontSize + 1
            )
        }
    }

    fun decreaseFontSize(){
        if (_uiState.value.fontSize > 10){
            _uiState.update {
                it.copy(
                    fontSize = it.fontSize - 1
                )
            }
        }
    }

    fun onClickBold(){
        if (!_uiState.value.text.selection.collapsed) {
            val style = if (!_uiState.value.isBoldActive) {
                SpanStyle(fontWeight = FontWeight.Bold)
            } else {
                SpanStyle(fontWeight = FontWeight.Normal)
            }

            _uiState.update {
                it.copy(text = applyStyle(it.text, style))
            }
        }

        toggleBold()

    }

    fun onClickItalic(){
        if (!_uiState.value.text.selection.collapsed) {
            val style = if (!_uiState.value.isItalicActive) {
                SpanStyle(fontStyle = FontStyle.Italic)
            } else {
                SpanStyle(fontStyle = FontStyle.Normal)
            }

            _uiState.update {
                it.copy(text = applyStyle(it.text, style))
            }
        }

        toggleItalic()

    }

    fun clearTextField(){
        _uiState.update {
            it.copy(
                text = TextFieldValue(annotatedString = buildAnnotatedString { append("") }),
                fontSize = 16,
                isBoldActive = false,
                isItalicActive = false
            )
        }
    }

    fun onTextFieldChange(newValue: TextFieldValue){
        val oldLength = _uiState.value.text.text.length
        val newLength = newValue.text.length

        val newAnnotatedString = buildAnnotatedString {
            append(newValue.text)

            // Preserve existing styles
            _uiState.value.text.annotatedString.spanStyles.forEach { spanStyle ->
                val start = minOf(spanStyle.start, newValue.text.length)
                val end = minOf(spanStyle.end, newValue.text.length)
                if (start < end) {
                    addStyle(spanStyle.item, start, end)
                }
            }

            // Apply active styles to newly typed text
            if (newLength > oldLength) {
                val insertPosition = newValue.selection.start - (newLength - oldLength)
                val insertedLength = newLength - oldLength

                if (_uiState.value.isBoldActive) {
                    addStyle(
                        SpanStyle(fontWeight = FontWeight.Bold),
                        insertPosition,
                        insertPosition + insertedLength
                    )
                }

                if (_uiState.value.isItalicActive) {
                    addStyle(
                        SpanStyle(fontStyle = FontStyle.Italic),
                        insertPosition,
                        insertPosition + insertedLength
                    )
                }
            }
        }

        _uiState.update {
            it.copy(
                text = newValue.copy(annotatedString = newAnnotatedString)
            )
        }
    }

    fun onSave(text: TextFieldValue, fontSize: Int, isBoldActive: Boolean, isItalicActive: Boolean){
        val noteToSave = TextEditorUiState(text, fontSize, isBoldActive, isItalicActive)

        if (currentEditingNoteId != null) {
            val updatedNoteList = _noteList.map { note ->
                if (note.id == currentEditingNoteId) {
                    note.copy(note = noteToSave)
                } else {
                    note
                }
            }
            _noteList = updatedNoteList

        } else {
            val newNote = Note(note = noteToSave)
            _noteList = _noteList + newNote
        }
        currentEditingNoteId = null
    }

    fun getNoteList(): List<Note> {
        return _noteList
    }

    fun loadNoteById(noteId: UUID) {
        val note = _noteList.find { it.id == noteId }
        if (note != null) {
            _uiState.update {
                it.copy(
                    text = note.note.text,
                    fontSize = note.note.fontSize,
                    isBoldActive = note.note.isBoldActive,
                    isItalicActive = note.note.isItalicActive
                )
            }
        }
    }

}