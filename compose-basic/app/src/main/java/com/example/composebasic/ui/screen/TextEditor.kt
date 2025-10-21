package com.example.composebasic.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composebasic.R
import com.example.composebasic.ui.theme.Navy
import com.example.composebasic.ui.viewmodel.TextEditorUiState
import com.example.composebasic.ui.viewmodel.TextEditorViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notepad(modifier: Modifier = Modifier, navController: NavController, viewModel: TextEditorViewModel) {
    val noteList = viewModel.getNoteList()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text("Notepad")},
                colors = TopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Navy,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    scrolledContainerColor = Navy
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("main_screen") }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "back",
                            tint = Color.Black
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("text_editor_screen")
                },
                containerColor = Navy,
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add note",
                        tint = Color.White
                    )
                }
            )
        },
        content = { paddingValues ->
            Column (
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.White),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black)
                )
                if (noteList.isEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "You have no notes yet",
                            fontSize = 20.sp,
                            color = Color.Gray,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        items(
                            items = noteList,
                            key = { it.id }
                        ) { note ->
                            NoteItem(
                                note = note.note,
                                onClick = {
                                    viewModel.loadNoteById(note.id)
                                    navController.navigate("text_editor_screen")
                                },
                            )
                        }

                    }

                }
            }
        }
    )
}

@Composable
fun NoteItem(
    note: TextEditorUiState,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(2.dp, Navy, RoundedCornerShape(10.dp))
            ,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.text.text,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview
@Composable
fun NotepadPreview()
{
    Notepad(navController = rememberNavController(), viewModel = viewModel())
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditor(navController: NavController, viewModel: TextEditorViewModel)
{
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Text Editor",
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Navy,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    scrolledContainerColor = Navy
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("notepad_screen") }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.decreaseFontSize() },
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.minus_circle),
                                        contentDescription = "decrease font size",
                                        tint = Color.Black
                                    )
                                }
                            )
                            Text(
                                text = uiState.fontSize.toString(),
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            IconButton(
                                onClick = { viewModel.increaseFontSize() },
                                content = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.add_circle),
                                        contentDescription = "increase font size",
                                        tint = Color.Black
                                    )
                                }
                            )
                        }
                        Row (
                            verticalAlignment = Alignment.CenterVertically,

                        ) {
                            Button(
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .width(40.dp),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.isBoldActive) Navy else Color.Gray
                                ),
                                onClick = { viewModel.onClickBold() }
                            ) {
                                Text(
                                    text = "B",
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )
                            }

                            Button(
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .width(40.dp),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.isItalicActive) Navy else Color.Gray
                                ),
                                onClick = { viewModel.onClickItalic() }
                            ) {
                                Text(
                                    text = "\uD835\uDC3C"
                                )
                            }
                        }

                    }
                }
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = {
                                    viewModel.onSave(uiState.text, uiState.fontSize, uiState.isBoldActive, uiState.isItalicActive)
                                    viewModel.clearTextField()
                                    navController.navigate("notepad_screen")
                              },
                    containerColor = Navy,
                    content = {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "save",
                            tint = Color.White
                        )
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                FloatingActionButton(
                    onClick = { viewModel.clearTextField() },
                    containerColor = Navy,
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NoteAdd,
                            contentDescription = "create new",
                            tint = Color.White
                        )
                    }
                )
            }

        },
        content = { paddingValues ->
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black)
                        .padding(paddingValues)
                )
                BasicTextField(
                    value = uiState.text,
                    onValueChange = { newValue ->
                        viewModel.onTextFieldChange(newValue)
                    },
                    textStyle = TextStyle(fontSize = uiState.fontSize.sp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.White),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 10.dp, horizontal = 15.dp)
                        ) {
                            if(uiState.text.text.isEmpty()){
                                Text(
                                    text = "Type here...",
                                    color = Color.Gray,
                                    fontSize = uiState.fontSize.sp
                                )
                            }
                            innerTextField()
                        }
                    },
                    cursorBrush = SolidColor(Color.Black)
                )
            }
        }
    )
}

@Preview
@Composable
fun TextEditorPreview()
{
    TextEditor(navController = rememberNavController(), viewModel = viewModel())
}