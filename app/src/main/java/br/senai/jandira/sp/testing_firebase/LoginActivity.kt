package br.senai.jandira.sp.testing_firebase

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.jandira.sp.testing_firebase.ui.theme.Testing_FirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Testing_FirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.DarkGray
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {

    val context = LocalContext.current

    var emailState by remember {
        mutableStateOf("")
    }
    var emailError by remember {
        mutableStateOf(false)
    }

    var passwordState by remember {
        mutableStateOf("")
    }
    var passwordError by remember {
        mutableStateOf(false)
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            ){

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Entrar no ",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "FireBase",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(
                    id = R.color.dark_orange
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = emailState,
            onValueChange = { emailState = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Email")
            },
            isError = emailError,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id = R.color.light_orange),
                focusedLabelColor = colorResource(
                    id = R.color.dark_orange
                ),
                cursorColor = colorResource(id = R.color.light_orange)
            )
        )

        if (emailError) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(text = "Campo obrigatório!", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(6.dp))
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }

        OutlinedTextField(
            value = passwordState,
            onValueChange = { passwordState = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Senha")
            },
            isError = passwordError,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id = R.color.light_orange),
                focusedLabelColor = colorResource(
                    id = R.color.dark_orange
                ),
                cursorColor = colorResource(id = R.color.light_orange)
            )
        )

        if (passwordError) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(text = "Campo obrigatório!", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(6.dp))
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailState, passwordState)
                        .addOnSuccessListener {
                            val toImageInput = Intent(context, ImageInputActivity::class.java)
                            context.startActivity(toImageInput)
                        }
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entrar")
            }
        }

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    val intent = Intent(context, SignInActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange))
            ) {
                Text("Registrar Conta")
            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview3() {
    Testing_FirebaseTheme {
        LoginScreen()
    }
}