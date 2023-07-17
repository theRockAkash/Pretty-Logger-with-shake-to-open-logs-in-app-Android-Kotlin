package com.zapp.app.retrofit

import com.zapp.app.utils.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class Interceptor @Inject constructor(private val preferenceHelper: PreferenceHelper) :
    Interceptor {

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {

            if (preferenceHelper.getString(PreferenceHelper.TOKEN).isNullOrEmpty()) {
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                return chain.proceed(request.build())
            } else {
                val request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer ${preferenceHelper.getString(PreferenceHelper.TOKEN)}"
                    )
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                return chain.proceed(request.build())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            var msg = ""
            when (e) {
                is SocketTimeoutException -> {
                    msg = "Timeout - Please check your internet connection"
                }

                is UnknownHostException -> {
                    msg = "Unable to make a connection. Please check your internet"
                }

                is ConnectionShutdownException -> {
                    msg = "Connection shutdown. Please check your internet"
                }

                is IOException -> {
                    msg = "Server is unreachable, please try again later."
                }

                is IllegalStateException -> {
                    msg = "${e.message}"
                }
            }
            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message(msg)
                .body(ResponseBody.create(null, "{${e}}")).build()
        }
    }

}