import {Component} from '@angular/core';
import {LoginService} from "./model/login.service";
import {PetService} from "./model/pet.service";
import {Pet} from "./model/pet";
import {Router} from '@angular/router';

@Component({
  templateUrl: './view/findpet.component.html',
  styleUrls: ['./app.component.css'],
  providers: [PetService]
})

export class FindPetComponent {

  user: string;
  species: string;

  listings: Pet[];
  interested: Pet[];
  totalListings: Pet[];
  currentIndex: number = 0;

  constructor(private loginService: LoginService, private petService: PetService, private router: Router) {
    this.user = loginService.user;
    this.petService.getInterested(this.user).subscribe(response => this.interested = response);
  }

  find() {
    this.petService.find(this.user).subscribe(response => this.totalListings = this.listings = response);
  }

  changeSpecies(species) {
    this.currentIndex = 0;
    this.listings = this.totalListings.filter(pet => pet.species == species);
  }

  sendInterest() {
    this.petService.interested(this.user, this.listings[this.currentIndex].id.toString())
      .subscribe(response => this.interested.push(this.listings[this.currentIndex]));
  }
}
