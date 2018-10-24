package www.gericass.com.toilet.data.remote.request

import java.io.Serializable

data class User(
        private val uid: String,
        private val name: String?,
        private val icon: String?
) : Serializable