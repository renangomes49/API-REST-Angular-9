import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppConstants } from '../app-constants';
import { error } from '@angular/compiler/src/util';
import { Router } from '@angular/router';
import { Usuario } from '../model/usuario';

@Injectable({
  providedIn: 'root'
})
export class LoginServiceService {

  constructor(private http: HttpClient, private router: Router) { }

  recuperarAcessoServ(login) {

    let user = new Usuario();
    user.login = login;

    return this.http.post(AppConstants.getBaseUrlPath + 'recuperar/', user).subscribe(data => {

      alert( JSON.parse(JSON.stringify(data)).descricaoError )

    }, error => {

        console.error('Erro ao tentar recuperar o login!');
        alert('Erro ao tentar recuperar o login!');

    });

  }



  loginServ(usuario: { login: string; senha: string; }) {

    return this.http.post(AppConstants.baseLogin, JSON.stringify(usuario)).subscribe(data => {

      // retorno Http
      const token = JSON.parse(JSON.stringify(data)).Authorization.split(' ')[1];

      localStorage.setItem('token', token);
      console.info('Token: ' + localStorage.getItem('token'));

      // redirecionando
      this.router.navigate(['home']);

    }, error => {

        console.error('Erro ao tentar fazer o login');
        alert('Acesso Negado!');

    });

  }

}
