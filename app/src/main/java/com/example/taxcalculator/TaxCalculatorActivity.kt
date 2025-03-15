package com.example.taxcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taxcalculator.ui.theme.TaxCalculatorTheme


// Variables for calc to hold the "state" of our app
var subTotal = 0f;
var total = 0f;
var totalDisplay = mutableStateOf("");

// The calculator
fun calc() {
    total = subTotal + (0.08875f * subTotal)
    // https://www.baeldung.com/kotlin/string-interpolation
    // and
    // https://www.programiz.com/kotlin-programming/examples/round-number-decimal
    totalDisplay.value = "$${"%.2f".format(total)}"
    println("[INFO] total is $total")
}

class TaxCalculatorActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaxCalculatorTheme {
                Scaffold(
                    // https://developer.android.com/develop/ui/compose/components/app-bars#center
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.hsv(9.1f, 0.72f, 1f),
                                titleContentColor = Color.White,
                            ),
                            title = {
                                Text(
                                    "NYC Sales Tax Calculator",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },

                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // https://stackoverflow.com/questions/63719072/jetpack-compose-centering-text
                    // Both Column and TotalInput Modifiers ^
                    Column(
                        modifier = Modifier.padding(innerPadding)
                            .padding(20.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            modifier= Modifier.padding(top = 50.dp)
                                .fillMaxWidth(),
                            // https://developer.android.com/reference/kotlin/androidx/compose/ui/text/style/TextAlign
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            text = "NYC Sales Tax is 8.875% 😮"
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TotalInput(
                                Modifier.padding(top=50.dp, end=20.dp)
                                    .width(180.dp)
                                    .height(60.dp)
                            )
                            CalculateButton(
                                modifier = Modifier.padding(top = 58.5.dp)
                            ) {
                                calc()
                            }
                        }


                        TotalDisplay(modifier = Modifier.padding(top = 80.dp))

                    }
                }
            }
        }
    }
}

// https://developer.android.com/develop/ui/compose/components/button#filled_button
@Composable
fun CalculateButton(modifier: Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier.height(50.dp),
        // shape from here https://www.geeksforgeeks.org/button-in-android-using-jetpack-compose/
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 10.dp),
        colors = ButtonColors(
            containerColor = Color.hsv(9.1f, 0.72f, 1f),
            contentColor = Color.White,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.White,
        ),
        onClick = { onClick() },
    ) {
        Text(
            text="CALCULATE",
            // https://developer.android.com/develop/ui/compose/text/style-text
            fontSize = 15.sp,
            letterSpacing = 1.5.sp
        )
    }
}


//https://developer.android.com/develop/ui/compose/text/user-input
@Composable
fun TotalInput(modifier: Modifier) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,

        onValueChange = {
            text = it
            try {
                // Hover your mouse over toFloat() to see the exception type it throws
                subTotal = it.toFloat()
                totalDisplay.value = ""
            } catch (error : NumberFormatException) {
                // some kinda popup or whatever
                println("[ERROR] parsing input to float 👨‍💻")
                totalDisplay.value = "Invalid Input"
            }
                        },
        label = { Text("Enter Subtotal") },
        modifier = modifier
    )
}

@Composable
fun TotalDisplay(modifier: Modifier) {
    // How to bind state
    // see: https://stackoverflow.com/questions/66494520/is-there-a-way-to-dynamically-change-the-string-in-text-of-compose
    val myText by totalDisplay
    // Formatting Text:
    // https://shorturl.at/3OVuB
    Text(
        modifier= modifier.fillMaxWidth(),
        // https://developer.android.com/reference/kotlin/androidx/compose/ui/text/style/TextAlign
        textAlign = TextAlign.Center,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        text = myText
    )
}