package com.example.doc_di.domain.pill

import com.example.doc_di.domain.model.Pill
import com.example.doc_di.domain.model.PillInfo
import kotlinx.coroutines.flow.Flow

interface PillsSearchRepository {
    suspend fun getPillSearchList(
        queryParams: Map<String, String>
    ): Flow<Result<List<Pill>>>

    suspend fun getPillInfo(
        name: String
    ): Flow<Result<PillInfo>>
}