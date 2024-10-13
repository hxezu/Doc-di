package com.example.doc_di.domain.pill

import android.util.Log
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.domain.model.PillInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class PillsSearchRepositoryImpl(
    private val pillApi : PillApi
): PillsSearchRepository {
    override suspend fun getPillSearchList(queryParams: Map<String,String>): Flow<Result<List<Pill>>> {
        return flow{
            val pillsFromApi = try{
                Log.d("pillsFromApi", "Result.Success: ${pillApi.getPillSearchList(queryParams)}")
                pillApi.getPillSearchList(queryParams)
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

    override suspend fun getPillInfo(searchHistoryDto: SearchHistoryDto): Flow<Result<PillInfo>> {
        return flow{
            val pillsFromApi = try{
                pillApi.getPillInfo(searchHistoryDto)
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
            emit(Result.Success(pillsFromApi.data))
        }
    }
}