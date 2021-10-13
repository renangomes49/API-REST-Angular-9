import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from '../app-constants';


@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http : HttpClient) {

   }

   // Carregar todos os usuários - somente para primeira página
   getUsuariosService() : Observable<any>{
     return this.http.get<any>(AppConstants.baseUrl);
   }

   // Carregar todos os usuários - página 2 em diante
   getUsuariosPorPaginaService(pagina) : Observable<any>{
      return this.http.get<any>(AppConstants.baseUrl + 'pagina/' + pagina);
    }

    // Carregar usuários pelo nome informado - página 2 em diante
    consultarUsuariosPorNomePaginaService(nome : String, pagina : Number) : Observable<any> {
      return this.http.get(AppConstants.baseUrl + 'usuarios/' + nome + '/pagina/' + pagina);
   }

    // Carregar usuários pelo nome informado - Somente para primeira página
    consultarUsuariosPorNomeService(nome : String) : Observable<any> {
      return this.http.get(AppConstants.baseUrl + 'usuarios/' + nome);
   }

   deletarUsuarioService(id : Number) : Observable<any>{
      return this.http.delete(AppConstants.baseUrl + id, {responseType : 'text'});
   }

   getUsuarioIdService(id) : Observable<any>{
      return this.http.get(AppConstants.baseUrl + id);
   }

   salvarUsuario(usuario) : Observable<any>{
      return this.http.post<any>(AppConstants.baseUrl, usuario);
   }
   
   atualizarUsuario(usuario) : Observable<any>{
      return this.http.put<any>(AppConstants.baseUrl, usuario);
   }

   usuarioAutenticado(){
      if(localStorage.getItem('token') !== null){

            return true;
      }else{
            return false;
      }
   }

   //excluirTelefone
   excluirTelefoneService(id) : Observable<any>{
      return this.http.delete(AppConstants.baseUrl + 'excluir-telefone/' + id, {responseType:'text'});
   }
   
   // carregar todas as profissões cadastradas no banco de dados
   getProfissoes(): Observable<any>{
      return this.http.get<any>(AppConstants.getBaseUrlPath + 'profissao/');
   }
   
   // chamando o relatório para download
   downloadPdfRelatorioService(){
      return this.http.get(AppConstants.baseUrl + 'download-relatorio',{responseType : 'text'}).subscribe(data =>{

         document.querySelector('iframe').src = data;

      });
   }

    // chamando o relatório para download com parametros de dataInical e dataFinal
    downloadRelatorioPdfComParametroService(dataIni : String, dataFin : String){
      return this.http.get(AppConstants.baseUrl + 'download-relatorio-intervalo-datas/data-inicio/' + dataIni + '/data-final/' + dataFin
                              , {responseType : 'text'}); //.subscribe(data =>{

        // document.querySelector('iframe').src = data;

      //});
   }

   //carregar Dados para o gráfico
   carregarDadosGraficoService() : Observable<any> {
      return this.http.get(AppConstants.baseUrl + 'grafico');
   }

}






