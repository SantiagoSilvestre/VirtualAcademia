package fitflex.com.fitflex.model;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;

import fitflex.com.fitflex.config.ConfiguracaoFirebase;

public class Contato {

    private String identificadorUsuario;
    private String nome;
    private String email;
    private String status;
    private String nivel_acesso;

    public Contato() {
        //Constructor here code all
    }

    public void salvar() {
        DatabaseReference refereciaFirebase = ConfiguracaoFirebase.getFirebase();
        DatabaseReference referencedatabase = ConfiguracaoFirebase.getFirebase();
        refereciaFirebase.child("contatos").child("c2FuQGdtYWlsLmNvbQ==").child( getIdentificadorUsuario() ).setValue( this );
        refereciaFirebase.child("usuarios").child( getIdentificadorUsuario() ).setValue( this );

    }

    public String getIdentificadorUsuario() {
        return identificadorUsuario;
    }

    public void setIdentificadorUsuario(String identificadorUsuario) {
        this.identificadorUsuario = identificadorUsuario;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNivel_acesso() {
        return nivel_acesso;
    }

    public void setNivel_acesso(String nivel_acesso) {
        this.nivel_acesso = nivel_acesso;
    }
}
