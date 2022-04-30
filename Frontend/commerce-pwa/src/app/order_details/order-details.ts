export interface OrderDetails {
    id: number;
    product_quantity: number;
    created: string;
    updated: string;
    fk_product: number;
    fk_order: number;
}
