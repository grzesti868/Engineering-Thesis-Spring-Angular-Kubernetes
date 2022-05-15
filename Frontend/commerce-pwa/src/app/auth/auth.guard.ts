import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }


  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const user = this.authService.getLoggedUser() || null;
      let roles;
      let result = false;
      if(user!=null){
        roles = JSON.parse(atob(user.accessToken.split('.')[1])).roles;
      }
      console.log(typeof(roles))
      if (user == null) {
        this.router.navigateByUrl('logowanie');
      } else if(route.data?.accessRule != null) {
        roles.forEach((role: string) => {
          console.log("rola: "+role);
          if(role ==  route.data.accessRule)
          result = true;
        });
        return result;
      }
      return user != null;
  }
  
}
