package www.gericass.com.toilet.util.vo

import www.gericass.com.toilet.util.vo.Status.ERROR
import www.gericass.com.toilet.util.vo.Status.SUCCESS

data class Resource<out T>(val status: Status, val data: T?, val e: Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(e: Throwable, data: T?): Resource<T> {
            return Resource(ERROR, null, e)
        }
    }
}
