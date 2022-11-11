package com.m391.asteroidradar.repository



import com.m391.asteroidradar.Asteroid
import com.m391.asteroidradar.Constants
import com.m391.asteroidradar.api.API
import com.m391.asteroidradar.api.parseAsteroidsJsonResult
import com.m391.asteroidradar.database.AsteroidDatabase
import com.m391.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AsteroidRepository(private val database: AsteroidDatabase) {

    suspend fun  refreshAsteroids(
        startDate: String = today(),
        endDate: String = seventhDay()
    ){
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = API.service.getAsteroidsAsync(
                startDate, endDate,
                Constants.API_KEY
            )
                .await()
            asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))
            database.asteroidDao.insertAll(*asteroidList.asDomainModel())
        }
    }
}


private fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(date)
}

fun today(): String {
    val calendar = Calendar.getInstance()
    return formatDate(calendar.time)
}

fun seventhDay(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    return formatDate(calendar.time)
}



