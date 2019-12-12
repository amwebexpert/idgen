package com.amwebexpert.idgen.domain

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "namespace")
data class Namespace(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", updatable = false, unique = true)
        var id: Long? = null,

        @Column(name = "name", unique = true, updatable = false, nullable = false)
        @get:NotBlank(message = "The [name] field cannot be blank")
        var name: String?,

        @OneToMany(mappedBy = "namespace", fetch = FetchType.LAZY)
        val identifiers: MutableSet<NamespaceIdentifier> = mutableSetOf()
) {
    override fun toString(): String = "$id - $name"

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as Namespace

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
    }

}
