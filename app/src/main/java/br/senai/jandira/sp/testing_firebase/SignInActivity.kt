package br.senai.jandira.sp.testing_firebase

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

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        setContent {
            Testing_FirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignInActivityContent()
                }
            }
        }
    }
}

@Composable
fun SignInActivityContent() {

    val context = LocalContext.current

    var emailState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
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


        OutlinedTextField(
            value = emailState,
            onValueChange = { emailState = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Email")
            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id = R.color.light_orange),
                focusedLabelColor = colorResource(
                    id = R.color.dark_orange
                ),
                cursorColor = colorResource(id = R.color.light_orange)
            )
        )
        OutlinedTextField(
            value = passwordState,
            onValueChange = { passwordState = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Senha")
            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id = R.color.light_orange),
                focusedLabelColor = colorResource(
                    id = R.color.dark_orange
                ),
                cursorColor = colorResource(id = R.color.light_orange)
            )
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {
                    accountCreate(emailState, passwordState)

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
        SignInActivityContent()
    }
}

fun accountCreate(email: String, password: String) {

    val auth = FirebaseAuth.getInstance()

    var authTry = auth.createUserWithEmailAndPassword(email, password)
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