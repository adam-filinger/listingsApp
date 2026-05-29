package cz.vse.java.listingsapp.model;

public class PO {
    private String ico;
    private int uzivatelId;
    private String name;
    private String email;
    private String tel;

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public int getUzivatelId() {
        return uzivatelId;
    }

    public void setUzivatelId(int uzivatelId) {
        this.uzivatelId = uzivatelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
