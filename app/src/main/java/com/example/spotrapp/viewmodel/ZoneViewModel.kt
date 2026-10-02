package com.example.spotrapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotrapp.data.local.ZoneEntity
import com.example.spotrapp.data.repository.Resource
import com.example.spotrapp.data.repository.ZoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// state for zones
@HiltViewModel
class ZoneViewModel @Inject constructor(
    private val repository: ZoneRepository
) : ViewModel() {

    private val _zoneState = MutableStateFlow<Resource<List<ZoneEntity>>>(Resource.Loading())
    val zoneState: StateFlow<Resource<List<ZoneEntity>>> = _zoneState

    private val _actionState = MutableStateFlow<Resource<Unit>?>(null)
    val actionState: StateFlow<Resource<Unit>?> = _actionState

    init {
        viewModelScope.launch {
            repository.zones.collect { _zoneState.value = it }
        }
    }

    fun addZone(name: String) {
        viewModelScope.launch {
            if (repository.isDuplicate(name)) {
                _actionState.value = Resource.Error("\"$name\" already exists.")
            } else {
                repository.addZone(ZoneEntity(name = name)).collect { _actionState.value = it }
            }
        }
    }

    fun deleteZone(zone: ZoneEntity) {
        viewModelScope.launch {
            repository.deleteZone(zone).collect { _actionState.value = it }
        }
    }

    fun clearActionState() {
        _actionState.value = null
    }
}