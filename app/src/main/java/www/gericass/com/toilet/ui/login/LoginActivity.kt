package www.gericass.com.toilet.ui.login

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import dagger.android.AndroidInjection
import timber.log.Timber
import www.gericass.com.toilet.R
import www.gericass.com.toilet.databinding.ActivityLoginBinding
import www.gericass.com.toilet.ui.MainActivity
import www.gericass.com.toilet.usecase.LoginUseCase.Companion.RC_SIGN_IN
import www.gericass.com.toilet.util.observe
import www.gericass.com.toilet.util.toast
import www.gericass.com.toilet.util.vo.Resource
import www.gericass.com.toilet.util.vo.Status.SUCCESS
import www.gericass.com.toilet.util.withViewModel
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.setLifecycleOwner(this)
        setUpViewModel()
        setUpUI()
    }

    override fun onStart() {
        super.onStart()
        viewModel.signIn()
    }

    private fun setUpUI() {
        binding.viewModel = viewModel
        binding.loginButton.setOnClickListener {
            viewModel.onButtonClick()
        }
    }

    private fun setUpViewModel() {
        viewModel = withViewModel(viewModelFactory) {
            observe(authResult, ::observeAuthResult)
            observe(toiletLoginResult, ::observeToiletLoginResult)
            observe(isLogin, ::observeIsLogin)
        }
        viewModel.init(this)
    }

    private fun observeAuthResult(authResult: Resource<FirebaseUser>?) {
        authResult?.let {
            if (it.status == SUCCESS) {
                it.data?.let { user ->
                    viewModel.loginToilet(user)
                }
                return
            }
            toast(getString(R.string.authentication_error))
            finish()
        }
    }

    private fun observeToiletLoginResult(loginResult: Resource<String>?) {
        loginResult?.let {
            if (it.status == SUCCESS) {
                transitMain()
                return
            }
            toast(getString(R.string.authentication_error))
            finish()
        }
    }

    private fun observeIsLogin(isLogin: Boolean?) {
        isLogin?.let { login ->
            if (login) {
                transitMain()
            }
        }
    }

    private fun transitMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.auth(account)
            } catch (e: ApiException) {
                Timber.e(e)
            }
        }
    }
}
