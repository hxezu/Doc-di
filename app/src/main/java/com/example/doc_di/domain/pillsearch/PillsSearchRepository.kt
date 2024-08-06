package com.example.doc_di.domain.pillsearch

import com.example.doc_di.domain.model.PillInfo
import com.example.doc_di.domain.model.Pill
import kotlinx.coroutines.flow.Flow

interface PillsSearchRepository {
    suspend fun getPillSearchList(
        queryParams: Map<String, String>
    ): Flow<Result<List<Pill>>>

    suspend fun getPillInfo(
        name: String
    ): Flow<Result<PillInfo>>
}