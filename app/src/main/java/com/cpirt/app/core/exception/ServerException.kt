package com.cpirt.app.core.exception

class ServerException(val code: Int) : Exception("Ошибка сервера: $code")