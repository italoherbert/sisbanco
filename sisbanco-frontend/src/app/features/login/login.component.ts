import { Component } from '@angular/core';
import { Login } from '../../core/model/login/login';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

import { SistemaService } from '../../core/services/sistema.service';
import { LoginService } from '../../core/services/login.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ 
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.sass'
})
export class LoginComponent {

  loginForm : FormGroup = new FormGroup( {
    username : new FormControl( '', [
      Validators.required,
      Validators.minLength( 3 )
    ] ),
    password : new FormControl( '', [
      Validators.required,
      Validators.minLength( 3 )
      //Validators.pattern( '\\d{6,}' )
    ] )
  } );
  
  errorsMsgActived : boolean = false;

  constructor( 
    private router : Router,
    private loginService : LoginService,
    private sistemaService : SistemaService ) {}

  login() {
    this.errorsMsgActived = true;

    if ( this.loginForm.invalid === true )
      return;

    
    this.loginService.login( this.loginForm.value ).subscribe({
      next: ( resp ) => {
        let username = this.loginForm.value.username;
        
        this.loginService.storeUserInfos( resp, username );
        this.router.navigate( [ '/home', { username : username } ] );
      },
      error: ( error ) => {
        let msg = this.sistemaService.errorMessage( error );
        alert( msg );
      }
    })
  }

  hasFieldError( key : any ) {
    return this.loginForm.controls[ key ].errors && this.errorsMsgActived === true;
  }

  hasControlError( key : any, error : any ) {
    return this.loginForm.controls[ key ].hasError( error ) && this.errorsMsgActived === true;
  }
  
  limpaUsername() {
    this.loginForm.reset( 'username' );
  }

  limpaPassword() {
    this.loginForm.reset( 'password' );
  }

}
