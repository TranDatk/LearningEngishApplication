package com.tnmd.learningenglishapp.model

data class Edit(
    val end: Int,
    val error_type: String,
    val general_error_type: String,
    val id: String,
    val replacement: String,
    val sentence: String,
    val sentence_start: Int,
    val start: Int
)

data class EditResponse(val edits: List<Edit>)
