package com.example.doc_di.domain.pillsearch

import com.example.doc_di.domain.model.Pill
import kotlinx.coroutines.flow.Flow

interface PillsSearchRepository {
    suspend fun getPillSearchList(
        queryParams: Map<String, String>
    ): Flow<Result<List<Pill>>>
}