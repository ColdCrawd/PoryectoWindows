package miniwindows;

import java.io.FileNotFoundException;
import java.io.IOException;

import usuarios.Usuarios;
import ventanaprincipal.VentanaPrincipal;

public class MiniWindows {

    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        VentanaPrincipal ventana = new VentanaPrincipal();
        Usuarios usuarios = new Usuarios();
        usuarios.almacenar("admin", "admin");
        ventana.crearCarpetaZ();
        ventana.setVisible(true);
         
    }
}
        