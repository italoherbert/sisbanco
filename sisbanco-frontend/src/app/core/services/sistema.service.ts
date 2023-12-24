import { Injectable } from '@angular/core';
import { Token } from '../model/login/token';

@Injectable({
  providedIn: 'root'
})
export class SistemaService {

  constructor() { }

  errorMessage( error : any ) : string {
    return error.mensagem;
  }

}
