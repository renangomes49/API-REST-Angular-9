import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UsuarioService } from './usuario.service';

@Injectable({
  providedIn: 'root'
})
export class GuardiaoGuard implements CanActivate {

  constructor(private usuarioService : UsuarioService){

  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      
      // chamando o metodo usuarioAutenticado() para verificar se o usuário está autenticado, e com isso
      // liberar as rotas caso retorne true;
      return this.usuarioService.usuarioAutenticado();
  }
  
}
