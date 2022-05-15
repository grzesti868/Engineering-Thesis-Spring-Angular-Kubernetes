import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { OrderDetails } from 'src/app/order_details/order-details';
import { OrderStatus } from '../order-status';
import { Order } from './order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private notification: BehaviorSubject<string> = new BehaviorSubject<string>('');
  private orderHttpUrl: string = "http://localhost:8080/api/orders/";
  private orderDetailsHttpUrl: string = "http://localhost:8080/api/order/details/";
 
  constructor(private http: HttpClient, private autorization: AuthService) { }

  deleteOrderDetail(orderId: Number, orderDetailsId: Number) {
    return this.http.delete<OrderDetails>( this.orderHttpUrl + orderId + "/" + orderDetailsId, {headers: this.addHeader()});
  }
  
  getOrderDetails(orderId: Number) {
    return this.http.get<OrderDetails[]>( this.orderDetailsHttpUrl + "by/order/" + orderId, {headers: this.addHeader()})
    .pipe(
      tap(res => this.notification.next("Pobrano order details."))
    )
  }

  getOrdersByUser(userId: Number): Observable<Order[]> {
    return this.http.get<Order[]>( this.orderHttpUrl + userId+ "/all", {headers: this.addHeader()})
    .pipe(
      tap(res => this.notification.next("Pobrano zamówienia użytkownika"))
    )
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.orderHttpUrl + "/all", { headers: this.addHeader() })
    .pipe(
      tap(res => this.notification.next("Pobrano wszystkie zamówienia"))
    );
  }

  getOrdersByStatus(status: OrderStatus): Observable<Order[]> {
    return this.http.get<Order[]>(this.orderHttpUrl+"status/"+status, {headers: this.addHeader() })
    .pipe(
      tap(res => this.notification.next("Pobrane zamówienia według statusu"))
    );
  }

  getOrderById(orderId: Number): Observable<Order> {
    return this.http.get<Order>( this.orderHttpUrl + "id/" + orderId, {headers: this.addHeader()})
    .pipe(
      tap(res => this.notification.next("Pobrano zamówienie"))
    );
  }

  deleteOrderById(orderId: Number){
    return this.http.delete( this.orderHttpUrl + orderId, {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Usunieto zamówienie"))
    );
  }

  deleteOrderDetailsFromOrder(orderId: Number, orderDetailsId: Number){
    return this.http.delete( this.orderHttpUrl + orderId + "/" + orderDetailsId, {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Usunieto pordukt z zamówienia"))
    );
  }

  createNewOrder(username: String) {
    return this.http.post( this.orderHttpUrl + username + "/new", {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Dodano nowe zamówienie dla użytkownika"))
    );
  }

  updateOrder(orderId: Number, order: Order) {
    return this.http.put( this.orderHttpUrl + "update/" + orderId, order, {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Zmodyfikowano zamówienie"))
    );
  }

  addProductToOrder(orderId: Number, orderDetails: OrderDetails) {
    return this.http.put( this.orderHttpUrl + "add/" + orderId, orderDetails, {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Dodano nowy produkt do zamówienia"))
    );
  }


  addHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', 'Bearer ' + this.autorization.getLoggedUser()?.accessToken)
  }

  trackChanges() : Observable<string> {
    return this.notification.asObservable();
  }

}
