package com.amwebexpert.idgen.domain

import javax.persistence.*

@Entity
@Table(name = "namespace_identifier")
data class NamespaceIdentifier(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", updatable = false, unique = true)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "namespace", nullable = false)
        var namespace: Namespace
) {

    fun fullyQualifiedIdentifier() = "${namespace.name}-$id"
    override fun toString(): String = "$id - ${fullyQualifiedIdentifier()}"

}