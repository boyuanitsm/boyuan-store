import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { WishList } from './wish-list.model';
import { WishListService } from './wish-list.service';

@Component({
    selector: 'jhi-wish-list-detail',
    templateUrl: './wish-list-detail.component.html'
})
export class WishListDetailComponent implements OnInit, OnDestroy {

    wishList: WishList;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private wishListService: WishListService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['wishList']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.wishListService.find(id).subscribe(wishList => {
            this.wishList = wishList;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
