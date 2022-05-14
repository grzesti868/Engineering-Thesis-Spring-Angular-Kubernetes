import { Component, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { AuthService } from './auth/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'commerce-pwa';

  constructor(private authService: AuthService) {
    
  }

  isLogged(): boolean {
    return this.authService.getLoggedUser() !=null;
  }

  logout() {
    this.authService.logout();
  }
}
