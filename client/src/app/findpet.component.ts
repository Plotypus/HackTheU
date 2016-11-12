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

  title = 'Find A Pet Here';
  private list = [
    { id: 1, name: 'Cat' },
    { id: 2, name: 'Dog' },
    { id: 3, name: 'Bird' },
    { id: 4, name: 'Reptiles'}
  ];
  private current: number = 2;
  private log: string ='';

  private logDropdown(id: number): void {
    const NAME = this.list.find( (item: any) => item.id == id ).name;
    this.log += `Value ${NAME} was selected\n`
  }

  click(message): void {
    alert(message);
  }

}
