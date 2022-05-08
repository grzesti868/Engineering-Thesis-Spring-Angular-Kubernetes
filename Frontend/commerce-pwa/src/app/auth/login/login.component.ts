import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Login } from '../login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  hide =true;

  login: Login = {
    password: null as any,
    login: null as any
  }

  err: string = "";

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.authService.login(this.login).subscribe(res => {
      if(res) {
        this.router.navigateByUrl('');
      } else {
        this.err = "Niepoprawny login lub hasło!";
      }
    })
  }

}
