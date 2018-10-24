package www.gericass.com.toilet.di

import dagger.Module
import dagger.Provides
import www.gericass.com.toilet.data.repository.FirebaseRepository
import www.gericass.com.toilet.data.repository.ToiletRepository
import www.gericass.com.toilet.usecase.LoginUseCase

@Module
class UseCaseModule {
    @Provides
    fun provideLoginUseCase(
            firebaseRepository: FirebaseRepository,
            toiletRepository: ToiletRepository
    ): LoginUseCase = LoginUseCase(toiletRepository, firebaseRepository)
}