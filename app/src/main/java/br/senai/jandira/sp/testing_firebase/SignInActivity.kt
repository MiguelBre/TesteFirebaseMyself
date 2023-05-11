package br.senai.jandira.sp.testing_firebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.jandira.sp.testing_firebase.ui.theme.Testing_FirebaseTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SignInActivity : ComponentActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            Log.i("success?", "Usuário já cadastrado")
        }

        setContent {
            Testing_FirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignInActivityContent(auth)
                }
            }
        }
    }
}

@Composable
fun SignInActivityContent(auth: FirebaseAuth) {

    val context = LocalContext.current



    val storage = Firebase.storage("gs://teste---zerowaste.appspot.com")
    val storageRef = storage.reference.child("images")


    var emailState by remember {
        mutableStateOf("")
    }
    var emailError by remember{
        mutableStateOf(false)
    }

    var passwordState by remember {
        mutableStateOf("")
    }
    var passwordError by remember{
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Registro no ",
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

        if (emailError){
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

        if (passwordError){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(text = "Campo obrigatório!", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(6.dp))
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {

                    emailError = emailState.isEmpty()
                    passwordError = passwordState.isEmpty()

                    if (!emailError && !passwordError){
                        accountCreate(context, emailState, passwordState, auth)
                    } else {
                        Toast.makeText(context, "Campos Vazios", Toast.LENGTH_SHORT).show()
                    }


                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange))
            ) {
                Text(text = "Registrar", color = Color.White)
            }
        }

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    Testing_FirebaseTheme {
        SignInActivityContent(FirebaseAuth.getInstance())
    }
}

fun accountCreate(context: Context, email: String, password: String, auth: FirebaseAuth) {

//    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener {
            Log.i(
                "ds3m", it.user!!.uid
            )
        }
        .addOnFailureListener { error ->
            try {
                throw error
            } catch (e: FirebaseAuthUserCollisionException) {
                Log.i("ds3m", "Esse email já existe!")
                Log.i("ds3m", "${e.message}")
            } catch (e: FirebaseAuthWeakPasswordException) {
                Log.i("ds3m", "Senha fraca!")
                Log.i("ds3m", "${e.message}")
            } catch (e: FirebaseAuthInvalidUserException) {
                Log.i("ds3m", "Usuário inválido!")
                Log.i("ds3m", "${e.message}")
            } catch (e: Exception) {
                Log.i("ds3m", "Algo deu errado ;-;!")
                Log.i("ds3m", "${e.message}")
            }
        }
    
}