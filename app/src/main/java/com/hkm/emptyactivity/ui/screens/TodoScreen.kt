package com.hkm.emptyactivity.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hkm.emptyactivity.data.Priority
import com.hkm.emptyactivity.ui.components.AddTodoBar
import com.hkm.emptyactivity.ui.components.EditTodoDialog
import com.hkm.emptyactivity.ui.components.EmptyState
import com.hkm.emptyactivity.ui.components.TodoItem
import com.hkm.emptyactivity.viewmodel.FilterType
import com.hkm.emptyactivity.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = viewModel()
) {
    val haptic = LocalHapticFeedback.current
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val editingTodo by viewModel.editingTodo.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val filteredTodos = viewModel.filteredTodos

    EditDialogHandler(
        editingTodo = editingTodo,
        onDismiss = { viewModel.setEditing(null) },
        onSave = { title, priority ->
            editingTodo?.let { viewModel.updateTodo(it.id, title, priority) }
        }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "My Tasks",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        AnimatedContent(
                            targetState = "${viewModel.activeCount} đang chờ · ${viewModel.completedCount} đã xong",
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith
                                        fadeOut(animationSpec = tween(300))
                            },
                            label = "counter"
                        ) { text ->
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    AnimatedVisibility(
                        visible = viewModel.completedCount > 0,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.clearCompleted()
                        }) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Xóa đã hoàn thành",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Add todo bar
            AddTodoBar(
                onAdd = { title, priority -> viewModel.addTodo(title, priority) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filter chips
            FilterChips(
                currentFilter = filter,
                onFilterChanged = { viewModel.setFilter(it) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // Stats bar
            AnimatedVisibility(
                visible = todos.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                ProgressBar(
                    total = todos.size,
                    completed = viewModel.completedCount,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Todo list or empty state
            if (filteredTodos.isEmpty()) {
                EmptyState(
                    filter = filter.name.lowercase(),
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(
                        items = filteredTodos,
                        key = { it.id }
                    ) { todo ->
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) + fadeIn(),
                            exit = slideOutHorizontally(
                                targetOffsetX = { -it }
                            ) + fadeOut()
                        ) {
                            TodoItem(
                                todo = todo,
                                onToggle = { viewModel.toggleTodo(todo.id) },
                                onDelete = { viewModel.deleteTodo(todo.id) },
                                onEdit = { viewModel.setEditing(todo) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditDialogHandler(
    editingTodo: com.hkm.emptyactivity.data.Todo?,
    onDismiss: () -> Unit,
    onSave: (String, Priority) -> Unit
) {
    AnimatedVisibility(
        visible = editingTodo != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        editingTodo?.let {
            EditTodoDialog(
                todo = it,
                onDismiss = onDismiss,
                onSave = onSave
            )
        }
    }
}

@Composable
private fun FilterChips(
    currentFilter: FilterType,
    onFilterChanged: (FilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterType.entries.forEach { type ->
            val isSelected = currentFilter == type
            val label = when (type) {
                FilterType.ALL -> "Tất cả"
                FilterType.ACTIVE -> "Đang chờ"
                FilterType.COMPLETED -> "Đã xong"
            }

            FilterChip(
                selected = isSelected,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onFilterChanged(type)
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.primary,
                    enabled = true,
                    selected = isSelected
                )
            )
        }
    }
}

@Composable
private fun ProgressBar(
    total: Int,
    completed: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (total > 0) completed.toFloat() / total else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "progress"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tiến độ",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
    }
}
