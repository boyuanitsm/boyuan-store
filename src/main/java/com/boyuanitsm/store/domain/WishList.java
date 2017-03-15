package com.boyuanitsm.store.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A WishList.
 */
@Entity
@Table(name = "wish_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WishList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "body")
    private String body;

    @Min(value = 0)
    @Column(name = "new_field")
    private Integer newField;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public WishList name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public WishList createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getBody() {
        return body;
    }

    public WishList body(String body) {
        this.body = body;
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getNewField() {
        return newField;
    }

    public WishList newField(Integer newField) {
        this.newField = newField;
        return this;
    }

    public void setNewField(Integer newField) {
        this.newField = newField;
    }

    public User getUser() {
        return user;
    }

    public WishList user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WishList wishList = (WishList) o;
        if (wishList.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wishList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WishList{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", createDate='" + createDate + "'" +
            ", body='" + body + "'" +
            ", newField='" + newField + "'" +
            '}';
    }
}
