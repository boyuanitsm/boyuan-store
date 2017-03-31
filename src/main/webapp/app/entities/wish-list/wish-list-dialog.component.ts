import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { WishList } from './wish-list.model';
import { WishListPopupService } from './wish-list-popup.service';
import { WishListService } from './wish-list.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-wish-list-dialog',
    templateUrl: './wish-list-dialog.component.html'
})
export class WishListDialogComponent implements OnInit {

    wishList: WishList;
    authorities: any[];
    isSaving: boolean;

    users: User[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private wishListService: WishListService,
        private userService: UserService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['wishList']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query().subscribe(
            (res: Response) => { this.users = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.wishList.id !== undefined) {
            this.wishListService.update(this.wishList)
                .subscribe((res: WishList) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.wishListService.create(this.wishList)
                .subscribe((res: WishList) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: WishList) {
        this.eventManager.broadcast({ name: 'wishListListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-wish-list-popup',
    template: ''
})
export class WishListPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private wishListPopupService: WishListPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.wishListPopupService
                    .open(WishListDialogComponent, params['id']);
            } else {
                this.modalRef = this.wishListPopupService
                    .open(WishListDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
