import { Component, OnInit } from '@angular/core';
import { LoginServiceService } from '../service/login-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  usuario = { login : '', senha : '' };

  constructor(private loginService: LoginServiceService, private router: Router) {

  }

  ngOnInit() {

      if(localStorage.getItem('token') !== null){
         this.router.navigate(['/home']); 
      }

  }

  public loginUsuario() {
    this.loginService.loginServ(this.usuario);
  }

  public recuperarAcesso(){
    this.loginService.recuperarAcessoServ(this.usuario.login);
  }

}
