package com.folksdev.blog.model

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
data class Users @JvmOverloads constructor(
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    val id: String? = "",
    val username: String,
    val gender: Gender,

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY,  cascade = [CascadeType.ALL])
    val entries: List<Entry>? = ArrayList(),

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY,  cascade = [CascadeType.ALL])
    val comments: List<Comment>? = ArrayList(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Users

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}

enum class Gender {
    MALE, FEMALE, UNKNOWN
}