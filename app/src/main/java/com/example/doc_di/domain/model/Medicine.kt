package com.example.doc_di.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Medicine(
    val id: Long,
    val itemSeq: String,
    val itemName: String,
    val entpSeq: String,
    val entpName: String,
    val chart: String,
    val itemImage: String,
    val printFront: String,
    val printBack: String,
    val drugShape: String,
    val colorClass1: String,
    val colorClass2: String,
    val lineFront: String,
    val lineBack: String,
    val lengLong: String,
    val lengShort: String,
    val thick: String,
    val imgRegistTs: String,
    val classNo: String,
    val className: String,
    val etcOtcName: String,
    val itemPermitDate: String,
    val formCodeName: String,
    val markCodeFrontAnal: String,
    val markCodeBackAnal: String,
    val markCodeFrontImg: String,
    val markCodeBackImg: String,
    val itemEngName: String,
    val changeDate: String,
    val markCodeFront: String,
    val markCodeBack: String,
    val ediCode: String,
    val bizrno: String,
    val rateTotal: String,
    val rateAmount: String,
) : Parcelable{

}