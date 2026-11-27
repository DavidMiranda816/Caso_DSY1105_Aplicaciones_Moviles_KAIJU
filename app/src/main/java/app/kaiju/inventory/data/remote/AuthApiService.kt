package app.kaiju.inventory.data.remote

import app.kaiju.inventory.data.model.LoginRequest
import app.kaiju.inventory.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
