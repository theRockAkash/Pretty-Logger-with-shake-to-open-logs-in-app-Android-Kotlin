# Pretty-Logger-with-shake-to-open-logs-in-app-Android-Kotlin

#add your app's package name in Printet.kt file line:24
```
 val logFile = File("/data/user/0/{add your app package name}/cache/", "log.file")  //add your app package name 
```

#add logger to your Retrofit Object builder
```
 fun getRetrofitApi(interceptor: Interceptor): Api {
   val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor) 
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)

        if (com.app.app.BuildConfig.DEBUG) {
            val prettyInterceptor = LoggingInterceptor.Builder()
                .setLevel(Level.BASIC)
                .log(VERBOSE)
            httpClient.addNetworkInterceptor(prettyInterceptor.build())
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build().create(Api::class.java)
}
```
