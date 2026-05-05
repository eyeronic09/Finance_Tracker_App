package com.example.financetracker.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.core.domain.model.Goal
import com.example.financetracker.core.domain.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GoalUiState(
    val goals: List<Goal> = emptyList(),
    val isAddGoalDialogVisible: Boolean = false,
    val isAddFundsDialogVisible: Boolean = false,
    val selectedGoal: Goal? = null,
    val newGoalName: String = "",
    val newGoalTarget: Double = 0.0,
    val fundAmount: Double = 0.0
)

class GoalViewModel(
    private val repository: GoalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalUiState())
    val uiState: StateFlow<GoalUiState> = combine(
        repository.getAllGoals(),
        _uiState
    ) { goals, state ->
        state.copy(goals = goals)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GoalUiState()
    )

    fun onEvent(event: GoalEvent) {
        when (event) {
            is GoalEvent.OnNameChange -> _uiState.update { it.copy(newGoalName = event.name) }
            is GoalEvent.OnTargetChange -> _uiState.update { it.copy(newGoalTarget = event.target) }
            is GoalEvent.OnFundAmountChange -> _uiState.update { it.copy(fundAmount = event.amount) }
            GoalEvent.ShowAddGoalDialog -> _uiState.update { it.copy(isAddGoalDialogVisible = true) }
            GoalEvent.DismissAddGoalDialog -> _uiState.update { it.copy(isAddGoalDialogVisible = false, newGoalName = "", newGoalTarget = 0.0) }
            is GoalEvent.ShowAddFundsDialog -> _uiState.update { it.copy(isAddFundsDialogVisible = true, selectedGoal = event.goal) }
            GoalEvent.DismissAddFundsDialog -> _uiState.update { it.copy(isAddFundsDialogVisible = false, selectedGoal = null, fundAmount = 0.0) }
            GoalEvent.SaveGoal -> saveGoal()
            GoalEvent.AddFunds -> addFunds()
            is GoalEvent.DeleteGoal -> deleteGoal(event.goal)
        }
    }

    private fun saveGoal() {
        val state = _uiState.value
        if (state.newGoalName.isBlank() || state.newGoalTarget <= 0) return

        viewModelScope.launch {
            repository.insertGoal(
                Goal(
                    name = state.newGoalName,
                    targetAmount = state.newGoalTarget
                )
            )
            onEvent(GoalEvent.DismissAddGoalDialog)
        }
    }

    private fun addFunds() {
        val state = _uiState.value
        val goalId = state.selectedGoal?.id ?: return
        if (state.fundAmount <= 0) return

        viewModelScope.launch {
            repository.addFundsToGoal(goalId, state.fundAmount)
            onEvent(GoalEvent.DismissAddFundsDialog)
        }
    }

    private fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }
}

sealed interface GoalEvent {
    data class OnNameChange(val name: String) : GoalEvent
    data class OnTargetChange(val target: Double) : GoalEvent
    data class OnFundAmountChange(val amount: Double) : GoalEvent
    object ShowAddGoalDialog : GoalEvent
    object DismissAddGoalDialog : GoalEvent
    data class ShowAddFundsDialog(val goal: Goal) : GoalEvent
    object DismissAddFundsDialog : GoalEvent
    object SaveGoal : GoalEvent
    object AddFunds : GoalEvent
    data class DeleteGoal(val goal: Goal) : GoalEvent
}
