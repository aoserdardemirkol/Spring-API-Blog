package com.folksdev.blog.model

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Entry @JvmOverloads constructor(
    @Id
    @Column(name = "entry_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: String? = "",
    val title: String,
    val content: String,
    val createDate: LocalDateTime = LocalDateTime.now(),
    val updateDate: LocalDateTime?,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "entry_tags",
        joinColumns = [JoinColumn(name = "entry_id", referencedColumnName = "entry_id" )],
        inverseJoinColumns = [JoinColumn(name = "tag_id", referencedColumnName = "tag_id")]
    )
    val tags: List<Tag>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "user_id") // Entry = Owner(fk)
    val users: Users,

    @OneToMany(mappedBy = "entry", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val comments: List<Comment>? = ArrayList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Entry

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}