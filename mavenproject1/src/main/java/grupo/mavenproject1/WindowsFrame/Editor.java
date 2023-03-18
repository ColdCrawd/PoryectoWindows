/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupo.mavenproject1.WindowsFrame;

/**
 *
 * @author balto
 */
import java.io.*;

public class Editor {
    
    private static final String FOLDER_NAME = "text_files"; // nombre de la carpeta donde se guardaran los archivos
    
    public static void main(String[] args) throws IOException {
        BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Bienvenido al editor de archivos de texto");
        System.out.println("¿Qué acción desea realizar?");
        System.out.println("1. Crear un nuevo archivo");
        System.out.println("2. Editar un archivo existente");
        int choice = Integer.parseInt(leer.readLine());
        
        switch (choice) {
            case 1:
                crearArchivo();
                break;
            case 2:
                editarArchivo();
                break;
            default:
                System.out.println("Opción no válida");
                break;
        }
    }
    
    private static void crearArchivo() throws IOException {
        BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese el nombre del archivo: ");
        String fileName = leer.readLine();
        System.out.println("Ingrese el contenido del archivo: ");
        String fileContent = leer.readLine();
        
        File folder = new File(FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        File file = new File(FOLDER_NAME + "/" + fileName);
        if (file.exists()) {
            System.out.println("El archivo ya existe");
        } else {
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.close();
            System.out.println("El archivo ha sido creado");
        }
    }
    
    private static void editarArchivo() throws IOException {
        BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese el nombre del archivo que desea editar: ");
        String fileName = leer.readLine();
        
        File file = new File(FOLDER_NAME + "/" + fileName);
        if (!file.exists()) {
            System.out.println("El archivo no existe");
        } else {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            bufferedReader.close();
            
            System.out.println("Contenido actual del archivo:");
            System.out.println(fileContent.toString());
            
            System.out.println("Ingrese el nuevo contenido del archivo:");
            String newContent = leer.readLine();
            
            FileWriter writer = new FileWriter(file);
            writer.write(newContent);
            writer.close();
            
            System.out.println("El archivo ha sido modificado");
        }
    }
}

