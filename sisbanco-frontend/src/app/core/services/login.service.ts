import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Login } from '../model/login/login';
import { Observable } from 'rxjs';

import * as Rotas from '../rotas';
import { Token } from '../model/login/token';

@Injectable({
  providedIn: 'root',
})
export class LoginService {

  constructor(private http: HttpClient) { }

  login( lgn : Login ) : Observable<any> {
    return this.http.post( Rotas.AUTH_API_PATH + "/login", lgn );
  }

  logout() {
    this.clearUserInfos();
  }

  storeUserInfos( token : Token, username : string ) {
    localStorage.setItem( 'access-token', token.accessToken );
    localStorage.setItem( 'refresh-token', token.refreshToken );
    localStorage.setItem( 'username', username );
  }

  clearUserInfos() {
    localStorage.setItem( 'access-token', '' );
    localStorage.setItem( 'refresh-token', '' );
    localStorage.setItem( 'username', '' );
  }

}
