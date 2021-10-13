import { Component, Injectable, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'; // import para pegar o Id que vai ser enviado do componente de usuario
import { Telefone } from 'src/app/model/telefone';
import { Usuario } from 'src/app/model/usuario';
import { UsuarioService } from 'src/app/service/usuario.service';
import { NgbDateParserFormatter, NgbDateStruct, NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap'
import { Profissao } from 'src/app/model/Profissao';

@Injectable()
export class FormataDataAdapter extends NgbDateAdapter<string> {
  
  readonly DELIMITER = '/';

  fromModel(value: string | null ): NgbDateStruct | null {
    
    if(value){
      let date = value.split(this.DELIMITER);
      return{
        day: parseInt(date[0], 10),
        month: parseInt(date[1], 10),
        year: parseInt(date[2], 10),
      };
    }
    return null;
  }
  
  toModel(date: NgbDateStruct | null) : string  | null {
    return date ? date.day + this.DELIMITER + date.month + this.DELIMITER + date.year : null;
  }

}

// classe para formatar a data, pois o padrão não está de acordo com o do Brasil
@Injectable()
export class FormataData extends NgbDateParserFormatter{
  
  readonly DELIMITER = '/'; // Ex.: 11/11/1111

  parse(value: string): NgbDateStruct | null {
    
    if(value){
      let date = value.split(this.DELIMITER);
      return{
        day: parseInt(date[0], 10),
        month: parseInt(date[1], 10),
        year: parseInt(date[2], 10),
      };
    }
    return null;

  }
  
  format(date: NgbDateStruct): string | null{
    
    return date ? validarData(date.day) + this.DELIMITER + validarData(date.month) + this.DELIMITER + date.year : '';
  }

  toModel(date : NgbDateStruct | null) : string | null {
    return date ? date.day + this.DELIMITER + date.month + this.DELIMITER + date.year : null;

  }

}

// Padrão do formator de data é 1/1/2021, com o método validarData() vamos acrescentar o '0';
// para ficar 01/01/2021
function validarData(data: any){
  if(data.toString !== '' && parseInt(data) <= 9)
    return '0' + data;
  else 
    return data;
}

@Component({
  selector: 'app-root',
  templateUrl: './usuario-add.component.html',
  styleUrls: ['./usuario-add.component.css'],
  providers : [
                {provide: NgbDateParserFormatter, useClass : FormataData},
                {provide : NgbDateAdapter, useClass : FormataDataAdapter}
              ]
})
export class UsuarioAddComponent implements OnInit {

  user = new Usuario();
  
  telefone = new Telefone();

  profissoes = Array<Profissao>();

  constructor(private routeAcive : ActivatedRoute, private usuarioService : UsuarioService) { }

  ngOnInit() {

    // carregando a lista de profissoes para a tela, para que o usuário possa cadastrar sua profissãp
    this.usuarioService.getProfissoes().subscribe(data =>{
      this.profissoes = data;
    });

    // pegando o id que vem da tela de listagem de usuarios (componente usuário)
    let id = this.routeAcive.snapshot.paramMap.get('id');
    if(id != null){
      this.usuarioService.getUsuarioIdService(id).subscribe( data => {
        this.user = data;
      });
    }

  }

  salvarUsuario(){

      if(this.user.id != null){ // atualizando
          this.usuarioService.atualizarUsuario(this.user).subscribe(data =>{
             
          });
          this.novo();
      }else{
          this.usuarioService.salvarUsuario(this.user).subscribe(data =>{

          });
          this.novo();
          
      }
  }

  novo (){
    this.user = new Usuario();
    this.telefone = new Telefone();
  }

  excluirTelefone(idTelefone, i){
  
      if(idTelefone == null){
          this.user.telefones.splice(i);
          return;
      }

      if(confirm("Deseja realmente excluir o telefone ?") && idTelefone !== null){
       
        this.usuarioService.excluirTelefoneService(idTelefone).subscribe(data =>{

            this.user.telefones.splice(i,1);  

        });
      }
  }

  addFone(){

    // lista nula, então instanciar a lista
    if(this.user.telefones === undefined){
        this.user.telefones = new Array<Telefone>();
    }

    this.user.telefones.push(this.telefone);
    this.telefone = new Telefone();

  }

}


