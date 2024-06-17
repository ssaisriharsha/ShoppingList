package com.example.shoppinglist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShoppingList()
                }
            }
        }
    }
}

@Composable
fun ShoppingList() {
    // State Variables
    val sItems = remember { mutableStateListOf<ShoppingItem>() }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    val context = LocalContext.current
    // Composable functions
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Shopping List",
                fontSize = 32.sp
            )
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(625.dp)
        ) {
            items(sItems) { item ->
//                Row {
//                    Spacer(modifier = Modifier.width(8.dp))
//                    OutlinedTextField(value = "$item", onValueChange = {}, readOnly = true)
//                    //Spacer(modifier = Modifier.width(128.dp))
//                }
                ShoppingListItem(item = item, onEditClick = { /*TODO*/ }, onDeleteClick = {sItems.remove(item)})
                Spacer(Modifier.height(16.dp))
            }
        }
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Item")
                    Text(text = "Add Item")

                }


            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = !showDialog },
                    confirmButton = {
                        Row {
                            TextButton(onClick = { showDialog = !showDialog }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cancel"
                                )
                                Text(text = "Cancel")
                            }
                            Spacer(modifier = Modifier.width(105.dp))
                            TextButton(onClick = {
                                try {
                                    if (itemName.isNotBlank() && quantity.toInt() >= 0) {
                                        sItems.add(ShoppingItem(id = sItems.size + 1, name = itemName, quantity = quantity.toInt(), isEditing = false))
                                        Toast.makeText(context, "${sItems.size}", Toast.LENGTH_SHORT).show()
                                        showDialog = !showDialog
                                        quantity = ""
                                        itemName = ""
                                    }
                                } catch (e: NumberFormatException) {
                                    Toast.makeText(
                                        context,
                                        "Quantity shouldn't be empty.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                                Text(text = "Add")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = ""
                        )
                    },
                    title = { Text(text = "Add Item") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = itemName,
                                onValueChange = { name -> itemName = name },
                                label = {
                                    Text(text = "Item name*")
                                },
                                keyboardOptions = KeyboardOptions(
                                    autoCorrect = false,
                                    keyboardType = KeyboardType.Email
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedTextField(
                                value = quantity,
                                onValueChange = { value -> quantity = value },
                                label = {
                                    Text(text = "Quantity*")
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    })
            }
            }
        }

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    var itemState by remember {mutableStateOf(false)}
    var itemName by remember {mutableStateOf(item.name)}
    var itemQuantity by remember {mutableStateOf(item.quantity.toString())}
    val context  = LocalContext.current
    Row(modifier = Modifier
        .padding(start = 8.dp, end = 8.dp)
        .fillMaxWidth()
        .height(64.dp)
        .border(
            border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.secondary),
            shape = RoundedCornerShape(8.dp)
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Item Name", modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 3.dp, bottom = 3.dp))
            Text(text = item.name, modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 3.dp, bottom = 3.dp)
                .width(64.dp), maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
        }
        Divider(modifier = Modifier
            .height(32.dp)
            .width(1.dp), thickness = 1.dp)
//        Spacer(modifier = Modifier.width(8.dp))
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Qty", modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 3.dp, bottom = 3.dp))
            Text(text = "${item.quantity}", modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 3.dp, bottom = 3.dp), maxLines = 1)
        }
        Divider(modifier = Modifier
            .height(32.dp)
            .width(1.dp), thickness = 1.dp)
        TextButton(onClick = {itemState = true}) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit item")
        }
        Divider(modifier = Modifier
            .height(32.dp)
            .width(1.dp), thickness = 1.dp)
        TextButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete item")
        }
    }
    if (itemState) {
        AlertDialog(
            onDismissRequest = {
                itemState = false
            },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = {itemState = !itemState}) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
                        Text(text = "Cancel")
                    }
                    TextButton(onClick = {
                        try {
                            if (itemName.isNotBlank() && itemQuantity.toInt() >= 0) {
                                item.name = itemName
                                item.quantity = itemQuantity.toInt()
                                itemState = !itemState
                            }
                        } catch (e: NumberFormatException) {
                            Toast.makeText(
                                context,
                                "Quantity shouldn't be empty.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Done")
                        Text(text = "Done")
                    }
                }
            },
            icon = {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
            },
            title = {
                Text(text = "Edit item")
            },
            text = {
                Column (modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(value = itemName, onValueChange = {itemName = it}, label = {Text(text = "Item Name*")}, keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Email))
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(value = itemQuantity, onValueChange = {itemQuantity = it}, label = {Text(text = "Quantity*")}, keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Number))
                }
            }
        )
    }
}

@Preview(showBackground = false, showSystemUi = true, uiMode = 1)
@Composable
fun GreetingPreview() {
    ShoppingListTheme {
        ShoppingList()
    }
}

