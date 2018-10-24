package www.gericass.com.toilet.data.repository

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable

interface FirebaseRepository {
    fun getToken(user: FirebaseUser): Observable<String>
}