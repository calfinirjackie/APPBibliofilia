package com.example.appbibliofilia.ui.BookCRUDScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.appbibliofilia.data.repository.LibrosRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksCrudScreen(
    librosRepo: LibrosRepository? = null,
    viewModelParam: BooksViewModel? = null,
    onBack: () -> Unit = {}
) {
    val booksViewModel: BooksViewModel = viewModelParam ?: viewModel<BooksViewModel>(factory = BooksViewModelFactory(librosRepo))
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var author by remember { mutableStateOf(TextFieldValue("")) }
    var pagesText by remember { mutableStateOf(TextFieldValue("")) }
    var isbn by remember { mutableStateOf(TextFieldValue("")) }
    var selectedFormat by remember { mutableStateOf<BookFormat?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val editingId = booksViewModel.editingId

    LaunchedEffect(editingId) {
        if (editingId != null) {
            booksViewModel.getBook(editingId)?.let { b ->
                name = TextFieldValue(b.name)
                author = TextFieldValue(b.author)
                selectedFormat = b.format
                pagesText = TextFieldValue(if (b.pages > 0) b.pages.toString() else "")
                isbn = TextFieldValue(b.isbn)
            }
        } else {
            name = TextFieldValue("")
            author = TextFieldValue("")
            selectedFormat = null
            dropdownExpanded = false
            pagesText = TextFieldValue("")
            isbn = TextFieldValue("")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BiblioGestión") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color(0xFFFFF7F0)),
                verticalArrangement = Arrangement.Top
            ) {
                Text("Integra tu libro", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del Libro") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                // Dropdown para formato: usar Box + menu anclado para garantizar que flecha y campo respondan
                Box {
                    OutlinedTextField(
                        value = selectedFormat?.let { if (it == BookFormat.FISICO) "Físico" else "Digital" } ?: "",
                        onValueChange = {},
                        label = { Text("Formato") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = if (dropdownExpanded) "Cerrar formato" else "Abrir formato"
                                )
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DropdownMenuItem(text = { Text("Físico") }, onClick = {
                            selectedFormat = BookFormat.FISICO
                            dropdownExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Digital") }, onClick = {
                            selectedFormat = BookFormat.DIGITAL
                            dropdownExpanded = false
                        })
                    }

                    // También permitir abrir el menú al pulsar el campo completo (no solo la flecha)
                    Spacer(modifier = Modifier
                        .matchParentSize()
                        .clickable(onClick = { dropdownExpanded = !dropdownExpanded }))
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = pagesText,
                    onValueChange = { pagesText = it },
                    label = { Text("Páginas") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text("ISBN") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        val fmt = selectedFormat ?: BookFormat.FISICO
                        // parsear páginas, usar 0 si inválido
                        val pagesInt = pagesText.text.trim().toIntOrNull() ?: 0
                        if (editingId != null) {
                            booksViewModel.updateBook(editingId, name.text.trim(), author.text.trim(), fmt, pagesInt, isbn.text.trim())
                            booksViewModel.stopEditing()
                        } else {
                            booksViewModel.addBook(name.text.trim(), author.text.trim(), fmt, pagesInt, isbn.text.trim())
                        }
                        // limpiar
                        name = TextFieldValue("")
                        author = TextFieldValue("")
                        selectedFormat = null
                        pagesText = TextFieldValue("")
                        isbn = TextFieldValue("")
                    }, enabled = name.text.isNotBlank() && author.text.isNotBlank()) {
                        Text(if (editingId != null) "Actualizar" else "Agregar")
                    }

                    OutlinedButton(onClick = {
                        name = TextFieldValue("")
                        author = TextFieldValue("")
                        selectedFormat = null
                        booksViewModel.stopEditing()
                        pagesText = TextFieldValue("")
                        isbn = TextFieldValue("")
                    }) {
                        Text("Limpiar")
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text("Libros", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(booksViewModel.books, key = { it.id }) { book ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F6F8))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(book.name, fontWeight = FontWeight.Bold)
                                    Text("Autor: ${book.author}", color = Color(0xFF5A5A5A))
                                    Text("Formato: ${if (book.format == BookFormat.FISICO) "Físico" else "Digital"}",
                                        color = Color(0xFF5A5A5A))
                                    if (book.pages > 0) {
                                        Text("Páginas: ${book.pages}", color = Color(0xFF5A5A5A))
                                    }
                                    if (book.isbn.isNotBlank()) {
                                        Text("ISBN: ${book.isbn}", color = Color(0xFF5A5A5A))
                                    }
                                }
                                Row {
                                    IconButton(onClick = { booksViewModel.startEditing(book.id) }) {
                                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(onClick = { booksViewModel.deleteBook(book.id) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BooksCrudScreenPreview() {
    // Llamar sin pasar ViewModel - preview usará viewModel() internamente
    BooksCrudScreen()
}