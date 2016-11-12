import {Component} from '@angular/core';

import {Router} from '@angular/router';
import {LoginService} from "./model/login.service";
import {User} from "./model/user";

@Component({
  templateUrl: './view/register.component.html',
  styleUrls: ['./app.component.css']
})
export class RegisterComponent {
  user: string;
  newUser: User = new User('', '', '');
  message: string = '';

  constructor(private loginService: LoginService, private router: Router) {
    this.user = loginService.user;
  }

  register(): void {
    if (!this.newUser.username || !this.newUser.password || !this.newUser.location)
      this.message = 'All fields required';
    else
      this.loginService.register(this.newUser)
        .subscribe(response => this.router.navigate(['/findpet']));
  }
}
