package br.senai.jandira.sp.testing_firebase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.jandira.sp.testing_firebase.ui.theme.Testing_FirebaseTheme
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import com.google.accompanist.imageloading.LoadPainterDefaults
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.net.URL
import java.util.UUID

class ImageInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
        }

        setContent {
            Testing_FirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.DarkGray
                ) {
                    ImageInputActivityContent()
                }
            }
        }
    }
}

@Composable
fun ImageInputActivityContent() {

    val context = LocalContext.current

//    val storage = Firebase.storage("gs://teste---zerowaste.appspot.com")

    var profilePicture by remember {
        mutableStateOf<Uri?>(null)
    }
    var profilePictureUrl by remember {
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profilePicture = uri
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {
                val backToLoginActivity = Intent(context, LoginActivity::class.java)
                context.startActivity(backToLoginActivity)
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_orange))
        ) {
            Text(text = "Voltar", color = Color.White)
        }

        Button(
            onClick = {
                launcher.launch("image/*")
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_orange))
        ) {
            Text(text = "Selecione a Imagem", color = Color.White)
        }

        Text(text = profilePicture.toString(), color = Color.White)

        DisplayImageFromUri(uri = profilePicture.toString())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                val storageRef = Firebase.storage.reference
                val imageRef = storageRef.child("images/imagem.jpg")
                val firebaseAuth = FirebaseAuth.getInstance()

                val uploadTask = imageRef.putFile(profilePicture!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Get the download URL for the uploaded file
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        // Use the download URL as a URL to access the image
                        profilePictureUrl = uri.toString()

                        Log.i("YESSIR!", profilePictureUrl)
                    }?.addOnFailureListener { exception ->
                        // Handle any errors that occur during the downloadUrl task
                        Log.i("error", "$exception")
                    }
                }.addOnFailureListener { exception ->
                    // Handle any errors that occur during the upload process
                    Log.i("error", "$exception")
                }

            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_orange))
        ) {

            Text(text = "Fazer o upload no Firebase", color = Color.White)

        }

        Text(text = profilePictureUrl, color = Color.White)

        DisplayImageFromUrl(imageUrl = profilePictureUrl)

    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Testing_FirebaseTheme {
        ImageInputActivityContent()
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DisplayImageFromUri(uri: String) {
    val painter = rememberImagePainter(
        data = uri,
        imageLoader = LocalImageLoader.current,
        builder = {
            if (false == true) this.crossfade(LoadPainterDefaults.FadeInTransitionDuration)
            placeholder(0)
        }
    )
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.FillHeight
    )
}

@Composable
fun DisplayImageFromUrl(imageUrl: String) {
    val painter = rememberImagePainter(imageUrl)
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        )
    }
}


