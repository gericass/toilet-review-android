package www.gericass.com.toilet.usecase

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import www.gericass.com.toilet.R
import www.gericass.com.toilet.data.remote.request.User
import www.gericass.com.toilet.data.repository.FirebaseRepository
import www.gericass.com.toilet.data.repository.ToiletRepository
import www.gericass.com.toilet.util.observeOnMainThread
import javax.inject.Inject


class LoginUseCase @Inject constructor(
        private val toiletRepository: ToiletRepository,
        private val firebaseRepository: FirebaseRepository
) : LifecycleObserver {

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private val _signInResult = PublishSubject.create<FirebaseUser>()
    val signInResult = _signInResult

    private val _toiletLoginResult = PublishSubject.create<String>()
    val toiletLoginResult = _toiletLoginResult

    private var currentUser: FirebaseUser? = null

    private val disposable = CompositeDisposable()

    private var activity: Activity? = null

    companion object {
        const val RC_SIGN_IN = 9001
    }

    fun create(activity: Activity) {
        this.activity = activity
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.server_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun start() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        activity = null
        disposable.clear()
    }

    fun loginState(): Boolean {
        if (currentUser == null) {
            return false
        }
        return true
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        activity?.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun loginToilet(firebaseUser: FirebaseUser) {
        val user = User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName,
                icon = firebaseUser.photoUrl.toString()
        )
        firebaseRepository.getToken(firebaseUser)
                .observeOnMainThread()
                .flatMap {
                    toiletRepository.login(it, user)
                            .observeOnMainThread()
                            .andThen(Observable.just(it))
                }
                .subscribe({
                    _toiletLoginResult.onNext(it)
                }, {
                    _toiletLoginResult.onError(it)
                }).addTo(disposable)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        activity?.let {
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(it) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let { u ->
                                _signInResult.onNext(u)
                            } ?: _signInResult.onError(NullPointerException("FirebaseUser"))

                        } else {
                            Timber.e(task.exception)
                            task.exception?.let { e ->
                                _signInResult.onError(e)
                            }
                        }
                    }
        }
    }
}