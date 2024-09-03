package com.example.doc_di.domain

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class RefreshCookieJar: CookieJar {
    private val cookieStore: MutableMap<String, MutableList<Cookie>> = mutableMapOf()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host]?.toList() ?: emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val urlHost = url.host
        if (cookieStore[urlHost] == null){
            cookieStore[urlHost] = mutableListOf()
        }
        cookieStore[urlHost]?.addAll(cookies)
    }
}