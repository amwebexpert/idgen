package com.amwebexpert.idgen.repository

import com.amwebexpert.idgen.domain.NamespaceIdentifier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NamespaceIdRepository : JpaRepository<NamespaceIdentifier, Long> {

    // Optional, for testing purpose
    fun findAllByNamespace(namespace: String): NamespaceIdentifier

}
