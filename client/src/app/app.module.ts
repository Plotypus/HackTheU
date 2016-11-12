import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {RouterModule}   from '@angular/router';

import {AppComponent} from './app.component';
import {FindPetComponent} from './findpet.component';
import {PostPetComponent} from './postpet.component';
import {RegisterComponent} from './register.component';
import {LoginComponent} from './login.component';

@NgModule({
  declarations: [
    AppComponent,
    FindPetComponent,
    PostPetComponent,
    RegisterComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot([
      {path: '', redirectTo: '/findpet', pathMatch: 'full'},
      {path: 'findpet', component: FindPetComponent},
      {path: 'postpet', component: PostPetComponent},
      {path: 'register', component: RegisterComponent},
      {path: 'login', component: LoginComponent}
    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
