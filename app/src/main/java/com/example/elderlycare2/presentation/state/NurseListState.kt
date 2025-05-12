package com.example.elderlycare2.presentation.state

import com.example.elderlycare2.data.remote.response.NurseListResponse

data class NurseListState(
    val elderList: List<NurseListResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed class NurseListEvent {
    object FetchElderList : NurseListEvent()
    data class OnElderClick(val elder: NurseListResponse) : NurseListEvent()
    data class NavigateToElderDetail(val elderId: Int) : NurseListEvent()
}