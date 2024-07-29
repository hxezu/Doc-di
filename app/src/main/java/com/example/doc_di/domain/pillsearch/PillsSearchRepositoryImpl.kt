package com.example.doc_di.domain.pillsearch

import android.util.Log
import com.example.practice.data.model.Pill
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class PillsSearchRepositoryImpl(
    private val api : Api
): PillsSearchRepository {
    override suspend fun getPillSearchListByName(name: String): Flow<Result<List<Pill>>> {
        return flow{
            val pillsFromApi = try{
                Log.d("pillsFromApi", "Result.Success: ${api.getPillSearchListByName(name)}")
                api.getPillSearchListByName(name)
            }
            catch (e: IOException){
                e.printStackTrace()
                emit(Result.Error(message = "IO error loading pills by name"))
                return@flow
            }
            catch (e: HttpException){
                e.printStackTrace()
                emit(Result.Error(message = "HTTP error loading pills by name "))
                return@flow
            }
            catch (e: Exception){
                e.printStackTrace()
                emit(Result.Error(message = "Error loading pills by name"))
                return@flow
            }
            Log.d("pillsFromApi", "Result.Success: ${pillsFromApi.data}")
            emit(Result.Success(pillsFromApi.data))
        }
    }
}