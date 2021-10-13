import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './home/home.component'; /* Requisi��es Ajax */
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders} from '@angular/compiler/src/core';
import { LoginComponent } from './login/login.component'
import { HttpInterceptorModule } from './service/headerinterceptor.service';
import { UsuarioComponent } from './componente/usuario/usuario/usuario.component';
import { UsuarioAddComponent } from './componente/usuario/usuario-add/usuario-add.component';
import { GuardiaoGuard } from './service/guardiao.guard';
//import { NgxMaskModule, IConfig } from 'ngx-mask';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxCurrencyModule } from 'ngx-currency';
import { UsuarioRelatorioReportComponent } from './componente/usuario/usuario-relatorio-report/usuario-relatorio-report.component';
import { MyBarChartComponent } from './componente/my-bar-chart/my-bar-chart.component';
//import { ChartsModule } from 'ng2-charts';
import { ChartsModule } from 'ng2-charts'
export const appRouters: Routes = [

  {path : 'home', component : HomeComponent, canActivate : [GuardiaoGuard] },
  {path : 'login', component : LoginComponent},
  {path : '', component : LoginComponent},
  {path : 'usuarioList', component : UsuarioComponent, canActivate : [GuardiaoGuard]},
  {path : 'usuarioAdd', component : UsuarioAddComponent, canActivate : [GuardiaoGuard]},
  {path : 'usuarioAdd/:id', component : UsuarioAddComponent, canActivate : [GuardiaoGuard]},
  {path : 'usuarioRelatorio', component : UsuarioRelatorioReportComponent, canActivate : [GuardiaoGuard]},
  {path : 'graficoChart', component : MyBarChartComponent, canActivate : [GuardiaoGuard]}
];

export const routes : ModuleWithProviders = RouterModule.forRoot(appRouters);

//export const options: Partial<IConfig> | (() => Partial<IConfig>) = null;



@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    UsuarioComponent,
    UsuarioAddComponent,
    UsuarioRelatorioReportComponent,
    MyBarChartComponent,  
],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routes,
    HttpInterceptorModule,
   // NgxMaskModule.forRoot(),
    NgxPaginationModule,
   NgbModule,
   NgxCurrencyModule,
  ChartsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
