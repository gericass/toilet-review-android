package www.gericass.com.toilet.data.remote

import io.reactivex.Completable
import retrofit2.Retrofit
import www.gericass.com.toilet.data.remote.client.ToiletClient
import www.gericass.com.toilet.data.remote.request.User
import www.gericass.com.toilet.data.repository.ToiletRepository
import javax.inject.Inject

class Toilet @Inject constructor(
        private val retrofit: Retrofit
) : ToiletRepository {

    override fun login(token: String, user: User): Completable {
        return retrofit.create(ToiletClient::class.java)
                .login(token, user)
    }
}