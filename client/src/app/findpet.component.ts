import {Component} from '@angular/core';
import {LoginService} from "./model/login.service";
import {PetService} from "./model/pet.service";
import {Router} from '@angular/router';

@Component({
  templateUrl: './view/findpet.component.html',
  styleUrls: ['./app.component.css'],
  providers: [PetService]
})

export class FindPetComponent {

  user: string;
  species: string;

  listings: string[];
  interested: string[];
  totalListings: string[];
  currentIndex: number = 0;

  constructor(private loginService: LoginService, private petService: PetService, private router: Router) {
    this.user = loginService.user;
    this.petService.getInterested(this.user).subscribe(response => this.interested = response);
  }

  find() {
    this.petService.find(this.user).subscribe(response => this.totalListings = this.listings = response);
  }

  sendInterest() {
    this.petService.interested(this.user, this.listings[this.currentIndex])
      .subscribe(response => this.interested.push(this.listings[this.currentIndex]));
  }
}
