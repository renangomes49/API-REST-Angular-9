import { Component, OnInit } from '@angular/core';
import { ChartDataSets } from 'chart.js';
import { Label } from 'ng2-charts';
import { UserChart } from 'src/app/model/UserChart';
import { UsuarioService } from 'src/app/service/usuario.service';

@Component({
  selector: 'app-my-bar-chart',
  templateUrl: './my-bar-chart.component.html',
  styleUrls: ['./my-bar-chart.component.css']
})
export class MyBarChartComponent implements OnInit {

  constructor(private usuarioService : UsuarioService) { }

  userChart = new UserChart();

  ngOnInit(): void {
     // Carregando os dados do gráfico assim que o componente é aberto 
     this.usuarioService.carregarDadosGraficoService().subscribe(data =>{
      this.userChart = data;

      // carregando nome dos usuários para o chartLabels
      this.chartLabels = this.userChart.nome.split(',');
      
      // Carregando os salarios dos usuarios
      var arraySalario = JSON.parse('[' + this.userChart.salario + ']');
      this.chartData = [
        { data: arraySalario, label: 'Salários dos Usuários' }
      ];

    });
  }

  chartData : ChartDataSets[] = [
    { data: [], label: 'Salários dos Usuários' }
  ];

  chartLabels : Label[];

  chartOptions = {
    responsive: true
  };
}
