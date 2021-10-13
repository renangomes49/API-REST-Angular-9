import { Profissao } from "./Profissao";
import { Telefone } from "./telefone";

export class Usuario {

    id : Number;
    login : String;
    nome : String;
    senha : String;
    telefones : Array<Telefone>;
    dataNascimento : String;
    salario : DoubleRange;
    profissao : Profissao = new Profissao();
}
