package com.hkm.emptyactivity.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class TodoRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TODOS = "todos"
    }

    fun loadTodos(): List<Todo> {
        val json = prefs.getString(KEY_TODOS, "[]") ?: "[]"
        val array = JSONArray(json)
        val todos = mutableListOf<Todo>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            todos.add(
                Todo(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    isCompleted = obj.getBoolean("isCompleted"),
                    createdAt = obj.getLong("createdAt"),
                    completedAt = if (obj.isNull("completedAt")) null else obj.getLong("completedAt"),
                    priority = try {
                        Priority.valueOf(obj.getString("priority"))
                    } catch (_: Exception) {
                        Priority.MEDIUM
                    }
                )
            )
        }
        return todos
    }

    fun saveTodos(todos: List<Todo>) {
        val array = JSONArray()
        for (todo in todos) {
            val obj = JSONObject().apply {
                put("id", todo.id)
                put("title", todo.title)
                put("isCompleted", todo.isCompleted)
                put("createdAt", todo.createdAt)
                put("completedAt", todo.completedAt ?: JSONObject.NULL)
                put("priority", todo.priority.name)
            }
            array.put(obj)
        }
        prefs.edit().putString(KEY_TODOS, array.toString()).apply()
    }
}
