package www.gericass.com.toilet

import android.support.v4.app.Fragment
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import www.gericass.com.toilet.di.DaggerAppComponent
import javax.inject.Inject

class App : DaggerApplication(), HasSupportFragmentInjector {

    override fun applicationInjector() = DaggerAppComponent.builder()
            .application(this)
            .build()

    // Fragment Injection
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

    override fun supportFragmentInjector() = androidInjector

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        FirebaseAnalytics.getInstance(this)
        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
    }
}