package com.hkm.emptyactivity.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.hkm.emptyactivity.data.Priority
import com.hkm.emptyactivity.data.Todo

@Composable
fun EditTodoDialog(
    todo: Todo,
    onDismiss: () -> Unit,
    onSave: (String, Priority) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var text by remember(todo) { mutableStateOf(todo.title) }
    var priority by remember(todo) { mutableStateOf(todo.priority) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        title = {
            Text(
                "Chỉnh sửa công việc",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nội dung") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    maxLines = 3
                )

                Text(
                    "Mức độ ưu tiên",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Priority.entries.forEach { p ->
                        val isSelected = priority == p
                        val label = when (p) {
                            Priority.HIGH -> "Cao"
                            Priority.MEDIUM -> "TB"
                            Priority.LOW -> "Thấp"
                        }
                        val color = when (p) {
                            Priority.HIGH -> MaterialTheme.colorScheme.error
                            Priority.MEDIUM -> MaterialTheme.colorScheme.tertiary
                            Priority.LOW -> MaterialTheme.colorScheme.primary
                        }

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                priority = p
                            },
                            label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = color.copy(alpha = 0.2f),
                                selectedLabelColor = color,
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = color.copy(alpha = 0.3f),
                                selectedBorderColor = color.copy(alpha = 0.5f),
                                enabled = true,
                                selected = isSelected
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSave(text, priority)
                },
                enabled = text.isNotBlank(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Hủy")
            }
        }
    )
}
