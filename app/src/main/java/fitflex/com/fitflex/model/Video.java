package fitflex.com.fitflex.model;

import com.google.firebase.database.DatabaseReference;

import fitflex.com.fitflex.config.ConfiguracaoFirebase;

public class Video {

    private String categoria;
    private String nome;
    private String descricao;
    private String dicas;
    private String uri;
    private String filename;

    public Video() {

        //All code here

    }

    public void salvar(String string) {
        this.setFilename(string);
        DatabaseReference refereciaFirebase = ConfiguracaoFirebase.getFirebase();
        refereciaFirebase.child("Video").child( getFilename()).setValue( this );
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDicas() {
        return dicas;
    }

    public void setDicas(String dicas) {
        this.dicas = dicas;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
