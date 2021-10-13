import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'projeto-front-restful-m35';


  constructor(private router : Router) { }

  

  // método executado toda vez que o AppComponent é chamado
  ngOnInit(): void {
    
    if(localStorage.getItem('token') == null){
        this.router.navigate(['login']);
    }
  }

  public sair(){
    localStorage.clear();
    this.router.navigate(['login']);
  }

  public esconderMenu(){
    if(localStorage.getItem('token') !== null){
      return false;
   }else{
     return true;
   }
  }

}
