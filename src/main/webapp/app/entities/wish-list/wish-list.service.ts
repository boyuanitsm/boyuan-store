import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { WishList } from './wish-list.model';
import { DateUtils } from 'ng-jhipster';
@Injectable()
export class WishListService {

    private resourceUrl = 'api/wish-lists';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(wishList: WishList): Observable<WishList> {
        let copy: WishList = Object.assign({}, wishList);
        copy.createDate = this.dateUtils
            .convertLocalDateToServer(wishList.createDate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(wishList: WishList): Observable<WishList> {
        let copy: WishList = Object.assign({}, wishList);
        copy.createDate = this.dateUtils
            .convertLocalDateToServer(wishList.createDate);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<WishList> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.createDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.createDate);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }


    private convertResponse(res: any): any {
        let jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].createDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse[i].createDate);
        }
        res._body = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}
