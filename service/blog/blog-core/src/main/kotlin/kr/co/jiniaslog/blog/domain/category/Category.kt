package kr.co.jiniaslog.blog.domain.category

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import kr.co.jiniaslog.shared.core.domain.AggregateRoot

@Entity
class Category(
    categoryId: CategoryId,
    categoryTitle: CategoryTitle,
    depth: Int,
    sortingPoint: Int,
) : AggregateRoot<CategoryId>() {
    @EmbeddedId
    @AttributeOverride(
        column = Column(name = "category_id"),
        name = "value",
    )
    override val id: CategoryId = categoryId

    @AttributeOverride(
        column = Column(name = "category_title"),
        name = "value",
    )
    var categoryTitle: CategoryTitle = categoryTitle
        private set

    @ManyToOne
    @JoinColumn(name = "parent_id")
    var parent: Category? = null
        private set

    @OneToMany(mappedBy = "parent")
    var children: MutableList<Category> = mutableListOf()
        private set

    var depth: Int = depth
        private set

    var sortingPoint: Int = sortingPoint
        private set

    fun edit(
        categoryTitle: CategoryTitle,
        depth: Int,
        sortingPoint: Int,
    ) {
        require(sortingPoint >= 0) { "sortingPoint must be positive" }
        require(depth >= 0) { "dept must be positive" }
        this.categoryTitle = categoryTitle
        this.sortingPoint = sortingPoint
        this.depth = depth
    }

    fun setParent(parent: Category) {
        require(parent.id != this.id) { "parent must be different from self" }
        require(parent.parent == null) { "parent must be root" }
        this.parent = parent
        parent.addChild(this)
    }

    private fun addChild(child: Category) {
        this.children.add(child)
    }

    @PreUpdate
    @PrePersist
    fun validate() {
        require(depth >= 0) { "dept must be positive" }
        require(sortingPoint >= 0) { "sortingPoint must be positive" }
    }
}
