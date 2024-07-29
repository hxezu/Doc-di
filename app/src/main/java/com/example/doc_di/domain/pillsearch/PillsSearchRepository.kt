package com.example.doc_di.domain.pillsearch

import com.example.practice.data.model.Pill
import kotlinx.coroutines.flow.Flow

interface PillsSearchRepository {
    suspend fun getPillSearchListByName(name: String): Flow<Result<List<Pill>>>
}