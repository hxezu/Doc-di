package com.example.doc_di.reminder.util

enum class Department {
    InternalMedicine,
    Surgery,
    Otolaryngology,
    Ophthalmology,
    DentalClinic,
    Orthopedics,
    PlasticSurgery,
    Dermatology,
    ObstetricsAndGynecology,
    Pediatrics,
    Psychiatry,
    OrientalMedicalClinic
}

fun getDepartmentList(): List<Department> {
    val departmentList = mutableListOf<Department>()
    departmentList.add(Department.InternalMedicine)
    departmentList.add(Department.Surgery)
    departmentList.add(Department.Otolaryngology)
    departmentList.add(Department.Ophthalmology)
    departmentList.add(Department.DentalClinic)
    departmentList.add(Department.Orthopedics)
    departmentList.add(Department.PlasticSurgery)
    departmentList.add(Department.Dermatology)
    departmentList.add(Department.ObstetricsAndGynecology)
    departmentList.add(Department.Pediatrics)
    departmentList.add(Department.Psychiatry)
    departmentList.add(Department.OrientalMedicalClinic)

    return departmentList
}