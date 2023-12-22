import { Component } from '@angular/core';
import { Login } from '../../core/model/login/login';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

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
      Validators.minLength( 6 ),
      Validators.pattern( '\\d{6,}' )
    ] )
  } );
  
  errorsMsgActived : boolean = false;

  hasFieldError( key : any ) {
    return this.loginForm.controls[ key ].errors && this.errorsMsgActived === true;
  }

  hasControlError( key : any, error : any ) {
    return this.loginForm.controls[ key ].hasError( error ) && this.errorsMsgActived === true;
  }

  login() {
    this.errorsMsgActived = true;

    if ( this.loginForm.invalid === true )
      return;
    
    alert( this.loginForm.value.username );    
  }

  limpaUsername() {
    this.loginForm.reset( 'username' );
  }

  limpaPassword() {
    this.loginForm.reset( 'password' );
  }

}
