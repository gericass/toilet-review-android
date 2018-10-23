package www.gericass.com.toilet.di

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import www.gericass.com.toilet.ui.MainActivity

@Module
interface MainActivityModule {
    @Binds
    fun providesAppCompatActivity(activity: MainActivity): AppCompatActivity
}