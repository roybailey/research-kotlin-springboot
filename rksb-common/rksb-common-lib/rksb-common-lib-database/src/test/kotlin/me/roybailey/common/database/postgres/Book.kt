package me.roybailey.common.database.postgres

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "books")
data class Book(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0L,

  @NaturalId
  var isbn: String = "",

  @Column(nullable = false)
  var title: String = ""
)
