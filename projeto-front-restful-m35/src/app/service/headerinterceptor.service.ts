import { HttpErrorResponse, HttpEvent, HttpResponse, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable, NgModule } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, tap} from 'rxjs/operators';

@Injectable()

// toda requisição e resposta vai ser interceptada por esse serviço 
export class HeaderinterceptorService implements HttpInterceptor{

  constructor() { }
  
  
  //colocando o token no cabeçalho da requisição para consumir a API
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
   
      if(localStorage.getItem('token') !== null){

          const token = 'Bearer ' + localStorage.getItem('token');

          const tokenRequest = req.clone({
              headers : req.headers.set('Authorization',token)
          });

          // continua a requisição
          return next.handle(tokenRequest).pipe(
            
            tap((event : HttpEvent<any>) => {

                if(event instanceof HttpResponse && (event.status === 200 || event.status === 201)){
                  console.info('Operação realizada com sucesso!');
                }
            })
            ,catchError(this.processarError));

      }else{
        // continua a requisição sem o token
        return next.handle(req).pipe(catchError(this.processarError));
      }

  }

  // capturando as exceções do back-end para mostar para o usuário
  processarError(error : HttpErrorResponse){
      
      let errorMessage = 'Error desconhecido';

      if(error.error instanceof ErrorEvent){

        console.error(error.error);
        errorMessage = 'Error: ' + error.error.error;
      }else{
        //alert('Token expirado');  
        
        if(error.status == 403){
          errorMessage = 'Acesso Negado: Tente o login novamente';  
        }else{
          errorMessage = 'Código: ' + error.error.codigoError + '\nMensagem: ' + error.error.descricaoError;
        }  
      }
      window.alert(errorMessage);
      return throwError(errorMessage);  
  }

}

@NgModule({

    providers : [{
      provide : HTTP_INTERCEPTORS,
      useClass: HeaderinterceptorService,
      multi : true,

    },],
})

export class HttpInterceptorModule {

}
