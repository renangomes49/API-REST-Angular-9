import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Usuario } from 'src/app/model/usuario';
import { UsuarioService } from 'src/app/service/usuario.service';

@Component({
  selector: 'app-usuario',
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.css']
})
export class UsuarioComponent implements OnInit {

  usuarios : Array<Usuario[]>;
  nome : String;
  total : number;
  public paginaAtual = 1;

  constructor(private usuarioService : UsuarioService) { }

  ngOnInit() {

      this.usuarioService.getUsuariosService().subscribe(data => {

        this.usuarios = data.content;
        this.total = data.totalElements;

      });

  }

  deletarUsuario(id : Number, index){

      if(confirm('Deseja excluiro usuário ?')){
          this.usuarioService.deletarUsuarioService(id).subscribe(data => {
              //console.log(data);
              // atualizando lista de usuarios
              // this.usuarioService.getUsuariosService().subscribe(data => {
              // this.usuarios = data;
              // });
              
              this.usuarios.splice(index,1); // depois que o usuário é excluído do Banco, excluímos ele da tela

          });
     }
  }

  consultarUsuariosPornome(){

    if(this.nome === ''){
      // se não passou nenhum nome, então faz uma pesquisa para retorna a primeira página
      this.usuarioService.getUsuariosService().subscribe(data => {

        this.usuarios = data.content;
        this.total = data.totalElements;

      });
    }else{

      this.usuarioService.consultarUsuariosPorNomeService(this.nome).subscribe(data => {
        alert('ok');  
        this.usuarios = data.content;
        this.total = data.totalElements;  
      });  
    }    
        
  }

  carregarPagina(pagina){

    if(this.nome !== ''){
        this.usuarioService.consultarUsuariosPorNomePaginaService(this.nome,pagina - 1).subscribe(data =>{
            this.usuarios = data.content;
            this.total = data.totalElements;
        });      
    }else{

      this.usuarioService.getUsuariosPorPaginaService(pagina-1).subscribe(data => {
        
          this.usuarios = data.content;
          this.total = data.totalElements;
      })
    }
  }  

  // download Relatório PDF
  downloadPdfRelatorio(){
    return this.usuarioService.downloadPdfRelatorioService();
  }


}
