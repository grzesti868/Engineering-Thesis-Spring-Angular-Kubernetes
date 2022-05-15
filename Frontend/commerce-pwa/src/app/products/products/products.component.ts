import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../product';
import { ProductsService } from '../products.service';
import {MatTable} from '@angular/material/table';
import { OrderService } from 'src/app/order/service/order.service';
import { LoginRes } from 'src/app/auth/login-res';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  @ViewChild(MatTable) table: MatTable<Product> | undefined;
  products: Product[] =[];
  col: string[] = ['id', 'name', 'imgFile', 'quantity', 'basePricePerUnit', 'actions'];


  constructor(private allProducts: ProductsService, private route: ActivatedRoute, private router: Router, private orderService: OrderService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {
    this.allProducts.getProdcuts().subscribe(res => this.products = res); 
    
  }

  deleteProduct(productId: string) {
    let some = this.allProducts.deleteProduct(productId).subscribe(res => {console.log(res); this.refresh()});
  }

  addToCart(id: Number) {
    //this.orderService.addProductToOrder()
    let token = (JSON.parse(sessionStorage.getItem('user') || '{}') as LoginRes).accessToken;
    let username = (JSON.parse(atob(token.split('.')[1]))).sub;
    console.log(username);
    
    this.snackBar.open("Produkt o id: "+id+ " dodano do koszyka użytkownika: "+username,'', {
      duration: 4000,
      panelClass: ['added-snack-bar-container']
    })

  }

  editProduct(id: Number) {
    this.router.navigateByUrl('produkty/' +id)
  }
  

}