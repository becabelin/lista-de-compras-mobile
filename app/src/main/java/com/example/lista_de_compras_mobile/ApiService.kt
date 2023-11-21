import com.example.lista_de_compras_mobile.Item
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("items")
    fun addItem(@Body item: Item): Call<Void>
}

