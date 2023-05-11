package br.senai.jandira.sp.testing_firebase

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.jandira.sp.testing_firebase.ui.theme.Testing_FirebaseTheme
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.LoadPainterDefaults
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URL

class ImageInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Testing_FirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    var profilePicture by remember{
        mutableStateOf<Uri?>(null)
    }
    var profilePictureUrl by remember{
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profilePicture = uri
        }
    )

    Column {

        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "Selecione a Imagem")
        }

        Text(text = profilePicture.toString())

        DisplayImageFromUri(uri = profilePicture.toString())
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = {

            val storageRef = Firebase.storage.reference.child("images/${Firebase.}8O19S9321NZXo65DEAfe1vUKOwm1")

            val imageUri = profilePicture

            val uploadTask = storageRef.putFile(imageUri!!)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                val downloadUrlTask = taskSnapshot.metadata?.reference?.downloadUrl
                downloadUrlTask?.addOnSuccessListener { downloadUrl ->
                    val downloadUrlString = downloadUrl.toString()
                    profilePictureUrl = downloadUrlString
                }
            }

        }) {

            Text(text = "Fazer o upload no Firebase")

        }
        
        DisplayImageFromUrl(imageUrl = profilePictureUrl)
        
    }



}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Testing_FirebaseTheme {
        Greeting("Android")
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
            .wrapContentHeight()) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        )
    }
}


