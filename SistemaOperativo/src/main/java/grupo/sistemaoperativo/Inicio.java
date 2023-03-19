package grupo.sistemaoperativo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author w
 */
public class Inicio {

    private RandomAccessFile users;

    public Inicio() {
        try {
            File mi = new File("z");
            mi.mkdir();
            File mu = new File("z/users");
            mu.mkdir();
            users = new RandomAccessFile("z/users/Usuarios.usr", "rw");
            Createadmin();

        } catch (IOException e) {

        }
    }

    public void Createadmin() throws IOException {
        if (users.length() == 0) {
            users.writeUTF("PotasticPanda");
            users.writeUTF("123456789");
            users.writeBoolean(true);
            CreateUserFolder("PotasticPanda");

        }
    }

    public void addUser(String user, String password) throws IOException {
        users.seek(users.length());
        users.writeUTF(user);
        users.writeUTF(password);
        CreateUserFolder(user);
        users.writeBoolean(false);

    }

    public String UsersFolder(String user) {
        return "z/users/" + user;
    }

    public void CreateUserFolder(String user) throws IOException {
        File U = new File(UsersFolder(user));
        U.mkdir();
        CrearCarpetas(UsersFolder(user));
    }

    public void CrearCarpetas(String direccion) throws IOException {
        File Imagenes = new File(direccion + "/MisImagenes");
        Imagenes.mkdir();
        File Documentos = new File(direccion + "/MisDocumentos");
        Documentos.mkdir();
        File Musica = new File(direccion + "/Musica");
        Musica.mkdir();
    }

    public boolean existe(String nombre) throws IOException {
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            String user = users.readUTF();
            if (user.equals(nombre)) {
                return true;
            }
            users.readUTF();
        }
        return false;
    }

    public boolean login(String nombre, String password) throws IOException {
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            String user = users.readUTF();
            String pass = users.readUTF();
            users.readBoolean();
            if (user.equals(nombre) && pass.equals(password)) {
                return true;
            }
            users.readBoolean();
        }
        return false;
    }
}
