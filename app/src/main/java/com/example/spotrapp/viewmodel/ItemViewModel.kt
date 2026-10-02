package com.example.spotrapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotrapp.data.local.ItemEntity
import com.example.spotrapp.data.repository.ItemRepository
import com.example.spotrapp.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// handle state for items
@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _itemState = MutableStateFlow<Resource<List<ItemEntity>>>(Resource.Loading())
    val itemState: StateFlow<Resource<List<ItemEntity>>> = _itemState

    // most recent action (delete, add etc)
    private val _actionState = MutableStateFlow<Resource<Unit>?>(null)
    val actionState: StateFlow<Resource<Unit>?> = _actionState

    init {
        viewModelScope.launch {
            repository.items.collect { _itemState.value = it }
        }
    }

    fun addItem(item: ItemEntity) {
        viewModelScope.launch {
            val duplicateCheck = async { repository.isDuplicate(item.name, item.zone) }
            launch {}

            if (duplicateCheck.await()) {
                _actionState.value = Resource.Error(
                    "\"${item.name}\" already exists in ${item.zone}."
                )
            } else {
                repository.addItem(item).collect { _actionState.value = it }
            }
        }
    }

    fun updateItem(item: ItemEntity) {
        viewModelScope.launch {
            repository.updateItem(item).collect { _actionState.value = it }
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(item).collect { _actionState.value = it }
        }
    }

    // logs a retrieve event, item stays in the list
    fun retrieveItem(item: ItemEntity) {
        viewModelScope.launch {
            repository.retrieveItem(item).collect { _actionState.value = it }
        }
    }

    fun clearActionState() {
        _actionState.value = null
    }
}