import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable, ɵɵqueryRefresh } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { Login } from './login';
import { LoginRes } from './login-res';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  changeUserStatus : EventEmitter<void> = new EventEmitter<void>(); 

  constructor(private http: HttpClient) { }

  headers: HttpHeaders = new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Headers': 'Content-Type',
    'Access-Control-Allow-Methods': 'GET,POST,OPTIONS,DELETE,PUT',
    
  });


  getLoggedUser(): LoginRes | null {
    let loginRes = JSON.parse(sessionStorage.getItem('user') || '{}') as LoginRes;
    
    if(this.isTokenOutdated(loginRes.accessToken) && !this.isTokenOutdated(loginRes.refreshToken)) {
      console.log("TOKEN AUTDATED, trying to refresh!")
      this.refreshLogin(loginRes)
    } else if(this.isTokenOutdated(loginRes.accessToken) && this.isTokenOutdated(loginRes.refreshToken)){
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
    if(token!= null){
      let expirationDate = (JSON.parse(atob(token.split('.')[1]))).exp;
    //  console.log(expirationDate);
      let actualDate = Math.round(Date.now() / 1000);
      console.log(Math.round(Date.now() / 1000));
      return actualDate >= expirationDate;
    }
    return true;
  }
  
  

  login(login: Login): Observable<boolean> {
    return this.http.post<LoginRes>("http://localhost:8080/api/login", login, {headers: this.headers })
    .pipe(
      map(res => {
      //  console.log(JSON.stringify(res));
        sessionStorage.setItem('user', JSON.stringify(res));
        this.changeUserStatus.emit();
        return true;
      }), catchError(err => {
        return of(false);
      })
    );
  }

  refreshLogin(loginRes: LoginRes): Observable<boolean> {
    return this.http.get<LoginRes>("http://localhost:8080/api/users/token/refresh", { headers:
    new HttpHeaders().set('Authorization', 'Bearer ' + loginRes.refreshToken)
  })
    .pipe(
      map(res => { 
      //  console.log(JSON.stringify(res));
        sessionStorage.setItem('user', JSON.stringify(res));
        this.changeUserStatus.emit();
        return true;
      }), catchError(err => {
        return of(false);
      })
    );
  }

}
