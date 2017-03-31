import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { WishListComponent } from './wish-list.component';
import { WishListDetailComponent } from './wish-list-detail.component';
import { WishListPopupComponent } from './wish-list-dialog.component';
import { WishListDeletePopupComponent } from './wish-list-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class WishListResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const wishListRoute: Routes = [
  {
    path: 'wish-list',
    component: WishListComponent,
    resolve: {
      'pagingParams': WishListResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'boyuanStoreApp.wishList.home.title'
    }
  }, {
    path: 'wish-list/:id',
    component: WishListDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'boyuanStoreApp.wishList.home.title'
    }
  }
];

export const wishListPopupRoute: Routes = [
  {
    path: 'wish-list-new',
    component: WishListPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'boyuanStoreApp.wishList.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'wish-list/:id/edit',
    component: WishListPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'boyuanStoreApp.wishList.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'wish-list/:id/delete',
    component: WishListDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'boyuanStoreApp.wishList.home.title'
    },
    outlet: 'popup'
  }
];
