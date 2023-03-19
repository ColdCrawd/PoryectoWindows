/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Twiter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author w
 */
public class TwiterCuenta {

    private RandomAccessFile Userdoc;
    private RandomAccessFile following, followers, twits;
    private String usuarioActual, direccion;

    public TwiterCuenta() {
        try {
            File mi = new File("z");
            mi.mkdir();
            File mu = new File("z/Twiter");
            mu.mkdir();
            Userdoc = new RandomAccessFile("z/Twiter/users.twe", "rw");

        } catch (IOException e) {

        }
    }

    public boolean addUser(String nombre, char genero, String Username, String password, int edad) throws IOException {
        if (searchUser(Username)) {
            return false;
        } else {
            Userdoc.seek(Userdoc.length());
            Userdoc.writeUTF(nombre);
            Userdoc.writeChar(genero);
            Userdoc.writeUTF(Username);
            Userdoc.writeUTF(password);
            Userdoc.writeLong(Calendar.getInstance().getTimeInMillis());
            Userdoc.writeInt(edad);
            Userdoc.writeBoolean(true);
            usuarioActual = Username;
            direccion = UserFolder(Username);
            CreateUserFolder(Username);
            ArchivosTwiter();
            return true;
        }
    }

    private String UserFolder(String username) {
        return "z/Twiter/" + username;
    }

    public boolean searchUser(String Username) throws IOException {
        Userdoc.seek(0);
        while (Userdoc.getFilePointer() < Userdoc.length()) {
            long pos = Userdoc.getFilePointer();
            Userdoc.readUTF();
            Userdoc.readChar();
            String username = Userdoc.readUTF();
            Userdoc.readUTF();
            Userdoc.readLong();
            Userdoc.readInt();
            Userdoc.readBoolean();

            if (Username.equalsIgnoreCase(username)) {
                Userdoc.seek(pos);
                return true;
            }
        }
        return false;
    }

    private void CreateUserFolder(String Username) throws FileNotFoundException {
        File U = new File(UserFolder(Username));
        U.mkdir();
    }

    public void ArchivosTwiter() throws FileNotFoundException {
        following = new RandomAccessFile(direccion + "/following.twc", "rw");
        followers = new RandomAccessFile(direccion + "/followers.twc", "rw");
        twits = new RandomAccessFile(direccion + "/twits.twc", "rw");
    }

    public boolean Login(String Username, String password) throws IOException {
        if (searchUser(Username)) {
            Userdoc.readUTF();
            Userdoc.readChar();
            Userdoc.readUTF();
            String Password = Userdoc.readUTF();
            Userdoc.readLong();
            Userdoc.readInt();
            Userdoc.readBoolean();
            if (password.equals(Password)) {
                usuarioActual = Username;
                direccion = UserFolder(Username);
                ArchivosTwiter();
                return true;
            }
        }
        return false;
    }
    
    public void addSeguir(String usuario) throws IOException {
        if (searchSeguidos(usuario)) {
            following.readUTF();
            long pos=following.getFilePointer();
            if(following.readBoolean()){
                
            }else{
                following.seek(pos);
                following.writeBoolean(true);
                addSeguidores(usuario);
            }
            
        } else {
            following.seek(following.length());
            following.writeUTF(usuario);
            following.writeBoolean(true);
            addSeguidores(usuario);
        }
    }
    public void dejarSeguir(String usuario) throws IOException{
        if(searchSeguidos(usuario)){
            following.readUTF();
            following.writeBoolean(false);
            dejarSeguidores(usuario);
        }
    }

    public boolean searchSeguidos(String usuario) throws IOException {
        following.seek(0);
        while (following.getFilePointer() < following.length()) {
            long pos=following.getFilePointer();
            if (following.readUTF().equals(usuario)) {
                following.seek(pos);
                return true;
            }
            following.readBoolean();
            
        }
        return false;
    }

    public void addSeguidores(String usuario) throws FileNotFoundException, IOException {
        followers = new RandomAccessFile(UserFolder(usuario) + "/followers.twc", "rw");
        if(searchSeguidores(usuarioActual)){
            followers.readUTF();
            followers.writeBoolean(true);
        }else{
            followers.seek(followers.length());
            followers.writeUTF(usuario);
            followers.writeBoolean(true);
        }
    }
    
    public void dejarSeguidores(String usuario) throws FileNotFoundException, IOException{
        followers = new RandomAccessFile(UserFolder(usuario) + "/followers.twc", "rw");
        if(searchSeguidores(usuarioActual)){
            followers.readUTF();
            followers.writeBoolean(false);
        }
    }
    
    public boolean searchSeguidores(String usuario) throws IOException{
        followers.seek(0);
        while(followers.getFilePointer()<followers.length()){
            long pos=followers.getFilePointer();
            if(followers.readUTF().equals(usuario)){
                followers.seek(pos);
                return true;
            }
            followers.readBoolean();
        }
        return false;
    }
    
    public void addTweet(String texto) throws IOException{
        twits = new RandomAccessFile(direccion + "/twits.twc", "rw");
        twits.seek(twits.length());
        twits.writeLong(Calendar.getInstance().getTimeInMillis());
        twits.writeUTF(texto);
    }
    
    public String seguidosUsers() throws IOException{
        following.seek(0);
        while(following.getFilePointer()<following.length()){
            String User=following.readUTF();
            if(following.readBoolean()){
                return User;
            }            
        }
        return null;
    }
    public int ContarSeguidos() throws IOException{
        following.seek(0);
        int NumSeguidos=0;
        while(following.getFilePointer()<following.length()){
            following.readUTF();
            if(following.readBoolean()){
                NumSeguidos++;
            }
        }
        return NumSeguidos;
    }
    public void TweetsSeguidos() throws IOException{
        int twets=0, total=0;
        int n=0;
        String[] seguidos=new String[ContarSeguidos()];
        following.seek(0);
        while(following.getFilePointer()<following.length()){
            String usuario=following.readUTF();
            if(following.readBoolean()){
                total=total+ContarTweets(usuario);
                seguidos[n]=usuario;
                if(n+1!=ContarSeguidos()){
                    n++;
                }
            }
            following.readBoolean();
        }
        String[] tweets=new String[total];
        n=0;
        while(twets<total){
            twits = new RandomAccessFile(UserFolder(seguidos[n]) + "/twits.twc", "rw");
            twits.seek(0);
            while(twits.getFilePointer()<twits.length()){
                twits.readLong();
                tweets[twets]=twits.readUTF();
                twets++;
            }
            n++;
        }      
        
    }
    
    public int ContarTweets(String usuario) throws FileNotFoundException, IOException{
        twits = new RandomAccessFile(UserFolder(usuario) + "/twits.twc", "rw");
        twits.seek(0);
        int n=0;
        while(twits.getFilePointer()<twits.length()){
            twits.readLong();
            twits.readUTF();
            n++;
        }
        return n;
    }
    
    
}
