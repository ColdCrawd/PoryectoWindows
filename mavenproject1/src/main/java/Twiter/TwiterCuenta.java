/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Twiter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

/**
 *
 * @author w
 */
public class TwiterCuenta {
    private RandomAccessFile Userdoc;
    private RandomAccessFile following, followers, twits;
    
    public TwiterCuenta() {
        try {
            File mi = new File("z");
            mi.mkdir();
            File mu=new File("z/Twiter");
            mu.mkdir();
            Userdoc=new RandomAccessFile("z/Twiter/users.twe","rw");
            
            
        }catch(IOException e){
            
        }
    }
    
    public boolean addUser(String nombre, char genero, String Username, String password, int edad) throws IOException{
        if(searchUser(Username)){
            return false;
        }else{
            Userdoc.seek(Userdoc.length());
            Userdoc.writeUTF(nombre);
            Userdoc.writeChar(genero);
            Userdoc.writeUTF(Username);
            Userdoc.writeUTF(password);
            Userdoc.writeLong(Calendar.getInstance().getTimeInMillis());
            Userdoc.writeInt(edad);
            Userdoc.writeBoolean(true);
            return true;
        }
    }
    public boolean searchUser(String Username) throws IOException{
        Userdoc.seek(0);
        while(Userdoc.getFilePointer()<Userdoc.length()){
            long pos=Userdoc.getFilePointer();
            Userdoc.readUTF();
            Userdoc.readChar();
            String username=Userdoc.readUTF();
            Userdoc.readUTF();
            Userdoc.readLong();
            Userdoc.readInt();
            Userdoc.readBoolean();
            
            if(Username.equalsIgnoreCase(username)){
                Userdoc.seek(pos);
                return true;
            }
        }
        return false;
    }
    
    private String UserFolder(String username){
        return "z/Twiter/"+username;
    }
    
    public void CreateUserFolder(String Username) throws FileNotFoundException{
        File U=new File(UserFolder(Username));
        U.mkdir();
        
        following=new RandomAccessFile(UserFolder(Username)+"/following.twc","rw");
        followers=new RandomAccessFile(UserFolder(Username)+"/followers.twc","rw");
        twits=new RandomAccessFile(UserFolder(Username)+"/twits.twc","rw");        
    }
    
    public boolean Login(String Username, String password) throws IOException{
        if(searchUser(Username)){
            Userdoc.readUTF();
            Userdoc.readChar();
            Userdoc.readUTF();
            String Password=Userdoc.readUTF();
            Userdoc.readLong();
            Userdoc.readInt();
            Userdoc.readBoolean();
            if(password.equals(Password)){
                return true;
            }
        }
        return false;
    }

}
