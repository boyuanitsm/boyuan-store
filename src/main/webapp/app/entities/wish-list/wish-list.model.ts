import { User } from '../../shared';
export class WishList {
    constructor(
        public id?: number,
        public name?: string,
        public createDate?: any,
        public body?: string,
        public user?: User,
    ) {
    }
}
