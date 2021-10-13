import { Component, OnInit, Injectable } from '@angular/core';
import { UsuarioService } from 'src/app/service/usuario.service';
import { NgbDateParserFormatter, NgbDateStruct, NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap'

@Injectable()
export class FormataDataAdapter extends NgbDateAdapter<string> {
  
  readonly DELIMITER = '-';

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
    return date ? validarData(date.day) + this.DELIMITER + validarData(date.month) + this.DELIMITER + date.year : null;
  }

}

// classe para formatar a data, pois o padrão não está de acordo com o do Brasil
@Injectable()
export class FormataData extends NgbDateParserFormatter{
  
  readonly DELIMITER = '-'; // Ex.: 11-11-1111

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
    return date ? validarData(date.day) + this.DELIMITER + validarData(date.month) + this.DELIMITER + date.year : null;

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
  selector: 'app-usuario-relatorio-report',
  templateUrl: './usuario-relatorio-report.component.html',
  styles: [ './usuario-relatorio-report.component.css'],
  providers : [
    {provide: NgbDateParserFormatter, useClass : FormataData},
    {provide : NgbDateAdapter, useClass : FormataDataAdapter}
  ]
})
export class UsuarioRelatorioReportComponent implements OnInit {


  dataIni : String;
  dataFin : String;

  constructor(private usuarioService : UsuarioService) { }

  ngOnInit(): void {
  }

  downloadRelatorioPdfComParametro(){
  
   this.usuarioService.downloadRelatorioPdfComParametroService(this.dataIni, this.dataFin).subscribe(data =>{
    document.querySelector('iframe').src = data;
   });
  
  }


}
