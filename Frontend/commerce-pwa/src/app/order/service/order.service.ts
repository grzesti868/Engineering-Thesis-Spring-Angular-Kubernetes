import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { OrderDetails } from 'src/app/order_details/order-details';
import { environment } from 'src/environments/environment';
import { OrderStatus } from '../order-status';
import { Order } from './order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private notification: BehaviorSubject<string> = new BehaviorSubject<string>('');
 
  constructor(private http: HttpClient, private autorization: AuthService) { }

  deleteOrderDetail(orderId: Number, orderDetailsId: Number) {
    return this.http.delete<OrderDetails>( `${environment.APIUrl}`+"/api/orders/" + orderId + "/" + orderDetailsId, {headers: this.addHeader()});
  }
  
  getOrderDetails(orderId: Number) {
    return this.http.get<OrderDetails[]>( `${environment.APIUrl}`+ "/api/order/details/by/order/" + orderId, {headers: this.addHeader()})
    .pipe(
      tap(res => this.notification.next("Pobrano order details."))
    )
  }

  getOrdersByUser(userId: Number): Observable<Order[]> {
    return this.http.get<Order[]>( `${environment.APIUrl}`+"/api/orders/" + userId+ "/all", {headers: this.addHeader()})
    .pipe(
      tap(res => this.notification.next("Pobrano zamówienia użytkownika"))
    )
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>( `${environment.APIUrl}`+"/api/orders/" + "/all", { headers: this.addHeader() })
    .pipe(
      tap(res => this.notification.next("Pobrano wszystkie zamówienia"))
    );
  }

  getOrdersByStatus(status: OrderStatus): Observable<Order[]> {
    return this.http.get<Order[]>( `${environment.APIUrl}`+"/api/orders/status/"+status, {headers: this.addHeader() })
    .pipe(
      tap(res => this.notification.next("Pobrane zamówienia według statusu"))
    );
  }

  getOrderById(orderId: Number): Observable<Order> {
    return this.http.get<Order>( `${environment.APIUrl}`+"/api/orders/id/" + orderId, {headers: this.addHeader()})
    .pipe(
      tap(res => this.notification.next("Pobrano zamówienie"))
    );
  }

  deleteOrderById(orderId: Number){
    return this.http.delete(  `${environment.APIUrl}`+"/api/orders/" + orderId, {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Usunieto zamówienie"))
    );
  }

  deleteOrderDetailsFromOrder(orderId: Number, orderDetailsId: Number){
    return this.http.delete( `${environment.APIUrl}`+"/api/orders/" + orderId + "/" + orderDetailsId, {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Usunieto pordukt z zamówienia"))
    );
  }

  createNewOrder(username: String) {
    return this.http.post( `${environment.APIUrl}`+"/api/orders/" + username + "/new", {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Dodano nowe zamówienie dla użytkownika"))
    );
  }

  updateOrder(orderId: Number, order: Order) {
    return this.http.put( `${environment.APIUrl}`+"/api/orders/update/" + orderId, order, {headers: this.addHeader(), responseType: "text"})
    .pipe(
      tap(res => this.notification.next("Zmodyfikowano zamówienie"))
    );
  }

  addProductToOrder(orderId: Number, orderDetails: OrderDetails) {
    return this.http.put( `${environment.APIUrl}`+"/api/orders/add/" + orderId, orderDetails, {headers: this.addHeader(), responseType: "text"})
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
