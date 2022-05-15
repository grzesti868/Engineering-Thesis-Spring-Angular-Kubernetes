import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrderDetails } from 'src/app/order_details/order-details';
import { OrderService } from '../../service/order.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cart: OrderDetails[] =[];
  quantity!: number;

  constructor(private orderService: OrderService, public router: Router) { }

  ngOnInit(): void {
    this.refresh();
    this.orderService.trackChanges().subscribe(res => {
      this.refresh();
    })
  }

  onDelete() {
    this.orderService.deleteOrderDetail(1,1).subscribe(res => 
      this.quantity = this.cart.length
      );
  }

  refresh() {
    this.orderService.getOrderDetails(1).subscribe(res => {
      this.cart = res;
      this.quantity = this.cart.length;
    });
  }

}
