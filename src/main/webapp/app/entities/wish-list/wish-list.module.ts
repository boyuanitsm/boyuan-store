import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoyuanStoreSharedModule } from '../../shared';
import { BoyuanStoreAdminModule } from '../../admin/admin.module';

import {
    WishListService,
    WishListPopupService,
    WishListComponent,
    WishListDetailComponent,
    WishListDialogComponent,
    WishListPopupComponent,
    WishListDeletePopupComponent,
    WishListDeleteDialogComponent,
    wishListRoute,
    wishListPopupRoute,
    WishListResolvePagingParams,
} from './';

let ENTITY_STATES = [
    ...wishListRoute,
    ...wishListPopupRoute,
];

@NgModule({
    imports: [
        BoyuanStoreSharedModule,
        BoyuanStoreAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WishListComponent,
        WishListDetailComponent,
        WishListDialogComponent,
        WishListDeleteDialogComponent,
        WishListPopupComponent,
        WishListDeletePopupComponent,
    ],
    entryComponents: [
        WishListComponent,
        WishListDialogComponent,
        WishListPopupComponent,
        WishListDeleteDialogComponent,
        WishListDeletePopupComponent,
    ],
    providers: [
        WishListService,
        WishListPopupService,
        WishListResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BoyuanStoreWishListModule {}
