import {Component} from '@angular/core';
import {LoginService} from "./model/login.service";

@Component({
  templateUrl: './view/findpet.component.html',
  styleUrls: ['./app.component.css']
})
export class FindPetComponent {
  user: string;

  constructor(private loginService: LoginService) {
    this.user = loginService.user;
  }
}
