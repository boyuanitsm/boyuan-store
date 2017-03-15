package com.boyuanitsm.store.repository;

import com.boyuanitsm.store.domain.WishList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WishList entity.
 */
@SuppressWarnings("unused")
public interface WishListRepository extends JpaRepository<WishList,Long> {

    @Query("select wishList from WishList wishList where wishList.user.login = ?#{principal.username}")
    List<WishList> findByUserIsCurrentUser();

}
