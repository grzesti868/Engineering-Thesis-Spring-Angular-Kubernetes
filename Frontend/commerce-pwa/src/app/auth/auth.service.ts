import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { catchError, ConnectableObservable, map, Observable, of } from 'rxjs';
import { Login } from './login';
import { LoginRes } from './login-res';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  changeUserStatus : EventEmitter<void> = new EventEmitter<void>(); 

  constructor(private http: HttpClient) { }

  getLoggedUser(): LoginRes | null {
    let loginRes = JSON.parse(sessionStorage.getItem('user') || '{}') as LoginRes;
    if(loginRes!= null && this.isTokenOutdated(loginRes.token)){
      this.logout();
      loginRes = null as any;
    }
    return loginRes
  }
  logout() {
    sessionStorage.removeItem('user');
    this.changeUserStatus.emit();
  }
  isTokenOutdated(token: string): boolean {
    token.split('.').forEach( c => {
      try{
        console.log(atob(c));
      }
      catch{
        console.log("Orginal: "+c)
      }
    });
    let expirationDate = (JSON.parse(atob(token.split('.')[1]))).exp;
    let actualDate = Math.floor((new Date).getDate() / 1000);
    return actualDate >= expirationDate;
  }

  login(login: Login): Observable<boolean> {
    return this.http.post<LoginRes>("http://localhost:8080/api/login", login)
    .pipe(
      map(res => {
        sessionStorage.setItem('user', JSON.stringify(res));
        this.changeUserStatus.emit();
        return true;
      }), catchError(err => {
        return of(false);
      })
    );
  }

}
