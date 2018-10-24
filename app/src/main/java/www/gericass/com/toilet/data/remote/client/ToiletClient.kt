package www.gericass.com.toilet.data.remote.client

import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import www.gericass.com.toilet.data.HEADER_TOKEN
import www.gericass.com.toilet.data.LOGIN
import www.gericass.com.toilet.data.remote.request.User

interface ToiletClient {
    @POST(LOGIN)
    fun login(@Header(HEADER_TOKEN) token: String, @Body user: User): Completable
}