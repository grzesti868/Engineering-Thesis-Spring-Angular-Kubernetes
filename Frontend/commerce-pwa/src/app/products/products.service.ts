import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Product } from './product';
import { tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  
  private notification: BehaviorSubject<string> =  new BehaviorSubject<string>('');

  constructor(private http: HttpClient) {
  }

  getProdcuts(): Observable<Product[]> {
    return this.http.get<Product[]>("http://localhost:8080/api/products/all");
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>("http://localhost:8080/api/products/" + id);
  }

  addProdcut(product: Product): Observable<Product> {
    return this.http.post<Product>("http://localhost:8080/api/products/add", product)
    .pipe(
      tap(res => this.notification.next("Dodano nowy produkt."))
    );
  }

  editProduct(id: number, product: Product): void { //todo: impl
  }
  
  track() : Observable<string> {
    return this.notification.asObservable();
  }
}
