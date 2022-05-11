export interface Product {
    id: number;
    name: string;
    imgFile: string;
    quantity: number;
    basePricePerUnit: Price;
    
    //created: string; //? 
    //updated: string;

   
}
export interface Price {
    basePricePerUnit: string;
}


