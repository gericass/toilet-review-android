package www.gericass.com.toilet.data.repository

import io.reactivex.Completable
import www.gericass.com.toilet.data.remote.request.User

interface ToiletRepository {
    fun login(token: String, user: User): Completable
}