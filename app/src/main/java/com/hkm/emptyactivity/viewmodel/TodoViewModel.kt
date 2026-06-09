package com.hkm.emptyactivity.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hkm.emptyactivity.data.Priority
import com.hkm.emptyactivity.data.Todo
import com.hkm.emptyactivity.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class FilterType { ALL, ACTIVE, COMPLETED }

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TodoRepository(application)

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    private val _filter = MutableStateFlow(FilterType.ALL)
    val filter: StateFlow<FilterType> = _filter.asStateFlow()

    private val _editingTodo = MutableStateFlow<Todo?>(null)
    val editingTodo: StateFlow<Todo?> = _editingTodo.asStateFlow()

    init {
        _todos.value = repository.loadTodos()
    }

    val filteredTodos: List<Todo>
        get() = when (_filter.value) {
            FilterType.ALL -> _todos.value
            FilterType.ACTIVE -> _todos.value.filter { !it.isCompleted }
            FilterType.COMPLETED -> _todos.value.filter { it.isCompleted }
        }

    val activeCount: Int get() = _todos.value.count { !it.isCompleted }
    val completedCount: Int get() = _todos.value.count { it.isCompleted }

    fun addTodo(title: String, priority: Priority = Priority.MEDIUM) {
        if (title.isBlank()) return
        val todo = Todo(title = title.trim(), priority = priority)
        _todos.value = listOf(todo) + _todos.value
        save()
    }

    fun toggleTodo(id: String) {
        _todos.value = _todos.value.map {
            if (it.id == id) {
                it.copy(
                    isCompleted = !it.isCompleted,
                    completedAt = if (!it.isCompleted) System.currentTimeMillis() else null
                )
            } else it
        }
        save()
    }

    fun deleteTodo(id: String) {
        _todos.value = _todos.value.filter { it.id != id }
        save()
    }

    fun updateTodo(id: String, newTitle: String, newPriority: Priority) {
        _todos.value = _todos.value.map {
            if (it.id == id) it.copy(title = newTitle.trim(), priority = newPriority)
            else it
        }
        _editingTodo.value = null
        save()
    }

    fun setEditing(todo: Todo?) {
        _editingTodo.value = todo
    }

    fun setFilter(type: FilterType) {
        _filter.value = type
    }

    fun clearCompleted() {
        _todos.value = _todos.value.filter { !it.isCompleted }
        save()
    }

    private fun save() {
        repository.saveTodos(_todos.value)
    }
}
