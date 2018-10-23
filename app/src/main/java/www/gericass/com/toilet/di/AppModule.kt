package www.gericass.com.toilet.di

import android.content.Context
import dagger.Module
import dagger.Provides
import www.gericass.com.toilet.App
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: App): Context = app
}