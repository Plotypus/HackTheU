import {Component} from '@angular/core';
import {LoginService} from "./model/login.service";

@Component({
  templateUrl: './view/register.component.html',
  styleUrls: ['./app.component.css']
})
export class RegisterComponent {
  user: string;

  constructor(private loginService: LoginService) {
    this.user = loginService.user;
  }
}
