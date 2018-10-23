package www.gericass.com.toilet.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import www.gericass.com.toilet.ui.MainActivity

@Module
interface ActivityBuilder {
    @ContributesAndroidInjector(modules = [
        MainActivityModule::class
    ])
    fun contributeMainActivity(): MainActivity

}