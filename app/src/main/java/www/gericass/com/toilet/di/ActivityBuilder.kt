package www.gericass.com.toilet.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import www.gericass.com.toilet.di.activity.MainActivityModule
import www.gericass.com.toilet.ui.MainActivity
import www.gericass.com.toilet.ui.login.LoginActivity

@Module
interface ActivityBuilder {
    @ContributesAndroidInjector(modules = [
        MainActivityModule::class,
        ViewModelModule::class,
        UseCaseModule::class,
        RepositoryModule::class
    ])
    fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [
        ViewModelModule::class,
        UseCaseModule::class,
        RepositoryModule::class
    ])
    fun contributeLoginActivity(): LoginActivity

}