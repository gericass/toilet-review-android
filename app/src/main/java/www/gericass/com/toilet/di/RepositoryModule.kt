package www.gericass.com.toilet.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import www.gericass.com.toilet.data.remote.Firebase
import www.gericass.com.toilet.data.remote.Toilet
import www.gericass.com.toilet.data.repository.FirebaseRepository
import www.gericass.com.toilet.data.repository.ToiletRepository
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideToiletRepository(retrofit: Retrofit): ToiletRepository = Toilet(retrofit)

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository = Firebase()
}