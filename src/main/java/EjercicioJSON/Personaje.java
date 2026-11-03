package EjercicioJSON;

import java.util.List;

public class Personaje {
    private String nombre;
    private List<String> pistas;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<String> getPistas() {
        return pistas;
    }
    public void setPistas(List<String> pistas) {
        this.pistas = pistas;
    }
}