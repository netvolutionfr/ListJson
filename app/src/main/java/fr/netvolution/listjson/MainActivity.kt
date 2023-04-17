package fr.netvolution.listjson

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import fr.netvolution.listjson.ui.theme.ListJsonTheme
import org.json.JSONArray


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListJsonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val context = LocalContext.current
    val response = remember { mutableStateOf<JSONArray?>(null) }

    fetchPosts(context, response)

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Posts") },
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding
        ) {
            items(response.value?.length() ?: 0) { index ->
                val post = response.value?.getJSONObject(index)
                ListItem(
                    leadingContent = { Text((index + 1).toString()) },
                    headlineText = { Text(post?.getString("title") ?: "") },
                    trailingContent = {
                        Icon(
                            Icons.Filled.ArrowRight,
                            contentDescription = "Go to post"
                        )
                    },
                    modifier = Modifier.selectable(
                        selected = false,
                        onClick = {
                            Log.d("Post", post.toString())
                        })
                )
                Divider()
            }
        }
    }
}

fun fetchPosts(context: Context, response: MutableState<JSONArray?>) {
    val url = "https://jsonplaceholder.typicode.com/posts"
    val queue = Volley.newRequestQueue(context)

    val jsonArrayRequest = JsonArrayRequest(
        Request.Method.GET, url, null,
        { resp ->
            response.value = resp
        },
        { error ->
            Log.e("Volley", error.toString())
        }
    )
    queue.add(jsonArrayRequest)
}
