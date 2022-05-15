export interface Order {
    id: number;
    status: string;
    created: string;
    updated: string;
    fk_address: number;
    fk_user: number;
}
