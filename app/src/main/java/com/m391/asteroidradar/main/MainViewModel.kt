package com.m391.asteroidradar.main


import android.app.Application
import androidx.lifecycle.*
import com.m391.asteroidradar.Asteroid
import com.m391.asteroidradar.PictureOfDay
import com.m391.asteroidradar.api.getPictureOfDay
import com.m391.asteroidradar.database.getDatabase
import com.m391.asteroidradar.repository.AsteroidRepository
import com.m391.asteroidradar.repository.seventhDay
import com.m391.asteroidradar.repository.today
import kotlinx.coroutines.launch



class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay
    private val _navigateToDetailAsteroid = MutableLiveData<Asteroid?>()
    val navigateToDetailAsteroid: LiveData<Asteroid?>
        get() = _navigateToDetailAsteroid
    fun doneNavigated() {
        _navigateToDetailAsteroid.value = null
    }
    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids
    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            refreshPictureOfDay()
        }
        savedClicked()
    }
    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailAsteroid.value = asteroid
    }

    private suspend fun refreshPictureOfDay()  {

        _pictureOfDay.value = getPictureOfDay()

    }
    fun todayClicked() {
       viewModelScope.launch {
           database.asteroidDao.getAsteroids(today(), today()).collect{
               _asteroids.value = it
           }
       }
    }
    fun nextWeekClicked() {
        viewModelScope.launch {
            database.asteroidDao.getAsteroids(today(), seventhDay()).collect{
                _asteroids.value = it
            }
        }
    }
    fun savedClicked() {
        viewModelScope.launch {
            database.asteroidDao.getAllAsteroids().collect{
                _asteroids.value = it
            }
        }
        }
    }