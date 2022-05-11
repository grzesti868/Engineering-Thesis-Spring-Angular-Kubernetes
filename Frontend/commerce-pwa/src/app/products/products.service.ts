import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Product } from './product';
import { tap, map } from 'rxjs/operators';
import { AuthService } from '../auth/auth.service';


@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  
  private notification: BehaviorSubject<string> =  new BehaviorSubject<string>('');

  constructor(private http: HttpClient, private authorization: AuthService) {
  }

  getProdcuts(): Observable<Product[]> {
    return this.http.get<Product[]>("http://localhost:8080/api/products/all", {headers: this.addHeader()});
  }
  
  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>("http://localhost:8080/api/products/" + id, {headers: this.addHeader()});
  }

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>("http://localhost:8080/api/products/add", product, {headers: this.addHeader()})
    .pipe(
      tap(res => this.notification.next("Dodano nowy produkt."))
    );
  }

  editProduct(name: string, product: Product): Observable<Product> { 
    return this.http.put<Product>("http://localhost:8080/api/products/" + name, product, {headers: this.addHeader()})
    .pipe(
      tap(res => this.notification.next('Zmodyfikowano produkt'))
    );
  }
  
  track() : Observable<string> {
    return this.notification.asObservable();
  }

  addHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', 'Bearer '+this.authorization.getLoggedUser()?.accessToken)
  }
}
