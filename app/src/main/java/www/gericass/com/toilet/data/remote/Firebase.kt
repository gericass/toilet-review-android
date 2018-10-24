package www.gericass.com.toilet.data.remote

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import www.gericass.com.toilet.data.repository.FirebaseRepository

class Firebase : FirebaseRepository {
    override fun getToken(user: FirebaseUser): Observable<String> {
        return Observable.create {
            user.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            task.result?.token?.let { token ->
                                it.onNext(token)
                            } ?: it.onError(NullPointerException("Firebase Token Empty"))

                        } else {
                            task.exception?.let { e ->
                                it.onError(e)
                            } ?: it.onError(Exception("Firebase Token Error"))
                        }
                    }
        }
    }
}