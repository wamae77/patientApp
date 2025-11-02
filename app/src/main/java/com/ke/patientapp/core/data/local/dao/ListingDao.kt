package com.ke.patientapp.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ke.patientapp.core.data.local.entities.PatientFullRecord
import com.ke.patientapp.core.data.models.PatientListItem

@Dao
interface ListingDao {


    @Query(
        """
    SELECT p.id          AS patientDbId,
           p.firstName   AS firstName,
           p.lastName    AS lastName,
           p.dateOfBirth AS dateOfBirth,
           v.visitDate   AS lastVisitDate,
           v.bmi         AS lastBmi
    FROM patients p
    INNER JOIN vitals v
      ON v.patientDbId = p.id
    WHERE v.visitDate = :visitDate
    ORDER BY p.id ASC
    """
    )
    fun pagingByDate(visitDate: String): PagingSource<Int, PatientListItem>

    @Query(
        """
    SELECT p.id AS patientDbId,
           p.firstName AS firstName,
           p.lastName  AS lastName,
           p.dateOfBirth AS dateOfBirth,
           v.visitDate AS lastVisitDate,
           v.bmi       AS lastBmi
    FROM patients p
    LEFT JOIN vitals v
      ON v.id = (
         SELECT vv.id
         FROM vitals vv
         WHERE vv.patientDbId = p.id
         ORDER BY vv.visitDate DESC
         LIMIT 1
      )
    ORDER BY p.id ASC
    """
    )
    fun pagingLatest(): PagingSource<Int, PatientListItem>

    @Transaction
    @Query("SELECT * FROM patients WHERE id = :id LIMIT 1")
    suspend fun getPatientFullRecord(id: Long): PatientFullRecord?


}