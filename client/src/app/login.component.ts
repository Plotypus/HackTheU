import {Component} from '@angular/core';
import {Router} from '@angular/router';

import {LoginService} from './model/login.service';

@Component({
  templateUrl: './view/login.component.html',
  styleUrls: ['./app.component.css']
})
export class LoginComponent {
  user: string;
  username: string;
  password: string;

  constructor(private loginService: LoginService, private router: Router) {
    this.user = this.loginService.user;
  }

  login(): void {
    this.loginService.login(this.username, this.password)
      .subscribe(response => this.loginService.setUser(response)
        .subscribe(response => this.router.navigate(['/findpet'])));
  }

  logout(): void {
    this.loginService.logout().subscribe(response => this.router.navigate(['/findpet']));
  }
}
