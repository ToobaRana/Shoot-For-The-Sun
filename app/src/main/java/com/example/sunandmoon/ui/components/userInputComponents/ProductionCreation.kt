package com.example.sunandmoon.ui.components.userInputComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R

//used to create a new Production. Provides a popup for user
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionCreation(
    modifier: Modifier,
    createProduction: (name: String) -> Unit,
    setProductionName: (name: String?) -> Unit,
    productionName: String
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier
            .fillMaxSize()
            .background(Color(0x88000000))
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()

                })

            },

        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = modifier
                .wrapContentSize()
                .width(320.dp)

                //.size(height = 200.dp, width = 320.dp)
                .clickable(enabled = false) { println("ja") },
            colors = CardDefaults.cardColors(

                containerColor = MaterialTheme.colorScheme.background,

                )

        ) {
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(stringResource(id = R.string.CreateNewProduction), color = MaterialTheme.colorScheme.onSurface)
                TextField(
                    placeholder = { Text(stringResource(id = R.string.defaultProductionName)) },
                    modifier = modifier.clickable {},
                    value = productionName,
                    onValueChange = { newName: String ->
                        if (newName.length <= 70) {
                            setProductionName(newName)
                        }

                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.edit_icon),
                            stringResource(id = R.string.EditTextPencilIcon),
                            Modifier,
                            MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        //cursorColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        placeholderColor = MaterialTheme.colorScheme.secondary
                    ),
                    textStyle = TextStyle(fontSize = 18.sp, fontFamily = FontFamily(Font(R.font.nunito)))
                )

                Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                    Button(
                        onClick = {
                            setProductionName(null)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    {
                        Text(text = stringResource(id = R.string.Cancel), fontFamily = FontFamily(Font(R.font.nunito)))
                    }

                    val defaultProductionName = stringResource(R.string.defaultProductionName)
                    Button(
                        onClick = {
                            if (productionName.isBlank()) {
                                createProduction(defaultProductionName)
                            } else {
                                createProduction(productionName)
                            }
                            //save stuff

                            setProductionName(null)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    {
                        Text(text = stringResource(id = R.string.SaveButton), fontFamily = FontFamily(Font(R.font.nunito)))
                    }
                }
            }

        }

    }


}