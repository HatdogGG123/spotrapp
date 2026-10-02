package com.example.spotrapp.data.repository

import com.example.spotrapp.data.local.ZoneDao
import com.example.spotrapp.data.local.ZoneEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

// zonedao - zoneviewmodel
class ZoneRepository @Inject constructor(
    private val zoneDao: ZoneDao
) {
    val zones: Flow<Resource<List<ZoneEntity>>> = zoneDao.getAllZones()
        .map<List<ZoneEntity>, Resource<List<ZoneEntity>>> { Resource.Success(it) }
        .onStart { emit(Resource.Loading()) }
        .catch { e -> emit(Resource.Error(e.message ?: "Failed to load zones")) }

    fun addZone(zone: ZoneEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            zoneDao.insert(zone)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to add zone"))
        }
    }

    fun deleteZone(zone: ZoneEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            zoneDao.delete(zone)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to delete zone"))
        }
    }

    suspend fun isDuplicate(name: String): Boolean =
        zoneDao.findByName(name) != null
}