export class AppConstants {

public static get baseServidor(): string { return 'http://localhost:8080/'; }

public static get baseLogin(): string { return  this.baseServidor + 'projeto-restful/login'; }

public static get baseUrl(): string { return  this.baseServidor + 'projeto-restful/usuario/'; }

public static get getBaseUrlPath(): string { return this.baseServidor + 'projeto-restful/'; }

}
