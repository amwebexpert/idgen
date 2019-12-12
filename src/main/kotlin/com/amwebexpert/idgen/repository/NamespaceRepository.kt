package com.amwebexpert.idgen.repository

import com.amwebexpert.idgen.domain.Namespace
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NamespaceRepository : JpaRepository<Namespace, String> {

    fun findByName(name: String): Optional<Namespace>

}
