package fitflex.com.fitflex.model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import fitflex.com.fitflex.config.ConfiguracaoFirebase;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String nivel_acesso;
    private String status;
    private String telefone;

    public Usuario(){

    }

    public void salvar() {
        DatabaseReference refereciaFirebase = ConfiguracaoFirebase.getFirebase();
        refereciaFirebase.child("usuarios").child( getId() ).setValue( this );
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNivel_acesso() {
        return nivel_acesso;
    }

    public void setNivel_acesso(String status) {

        this.nivel_acesso = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus() {
        this.status = "false";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
