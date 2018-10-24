package www.gericass.com.toilet.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import www.gericass.com.toilet.usecase.LoginUseCase
import www.gericass.com.toilet.util.observeOnMainThread
import www.gericass.com.toilet.util.vo.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _authResult = MutableLiveData<Resource<FirebaseUser>>()
    val authResult: LiveData<Resource<FirebaseUser>> = _authResult

    private val _toiletLoginResult = MutableLiveData<Resource<String>>()
    val toiletLoginResult: LiveData<Resource<String>> = _toiletLoginResult

    @Inject
    lateinit var loginUseCase: LoginUseCase

    private var disposable = CompositeDisposable()

    fun init(activity: AppCompatActivity) {
        activity.lifecycle.addObserver(loginUseCase)
        loginUseCase.create(activity)
        subscribeLoginUseCase()
    }

    fun signIn() {
        _isLogin.value = loginUseCase.loginState()
    }

    fun auth(acct: GoogleSignInAccount?) {
        loginUseCase.firebaseAuthWithGoogle(acct)
    }

    fun onButtonClick() {
        loginUseCase.signIn()
    }

    fun loginToilet(firebaseUser: FirebaseUser) {
        loginUseCase.loginToilet(firebaseUser)
    }

    private fun subscribeLoginUseCase() {
        loginUseCase.signInResult
                .observeOnMainThread()
                .subscribe({
                    _authResult.value = Resource.success(it)
                }, {
                    _authResult.value = Resource.error(it, null)
                    Timber.e(it)
                })
                .addTo(disposable)
        loginUseCase.toiletLoginResult
                .observeOnMainThread()
                .subscribe({
                    _toiletLoginResult.value = Resource.success(it)
                }, {
                    _toiletLoginResult.value = Resource.error(it, null)
                    Timber.e(it)
                })
                .addTo(disposable)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}