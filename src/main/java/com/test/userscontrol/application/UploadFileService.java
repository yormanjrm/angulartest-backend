package com.test.userscontrol.application;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileService {
    // Definición de rutas y URL
    public final String FOLDER = "src//main//resources//static//images//"; // Ruta donde se guardarán los archivos
    public final String IMG_DEFAULT = "default.png"; // Nombre del archivo predeterminado
    public final String URL = "http://localhost:8080/images/"; // URL base para acceder a los archivos cargados

    // Método para cargar un archivo
    public String upload(MultipartFile multipartFile) throws IOException {
        // Verifica si el archivo es válido
        if (multipartFile != null) {
            // Obtiene los bytes del archivo
            byte[] bytes = multipartFile.getBytes();
            // Obtiene el nombre original del archivo
            String originalFilename = multipartFile.getOriginalFilename();
            // Crea la ruta del archivo
            Path path = Paths.get(FOLDER + originalFilename);
            // Escribe los bytes del archivo en la ruta especificada
            Files.write(path, bytes);
            // Retorna la URL completa del archivo cargado
            return URL + originalFilename;
        }
        // Si el archivo es nulo, retorna la URL del archivo predeterminado
        return URL + IMG_DEFAULT;
    }

    // Método para eliminar un archivo
    public void delete(String nameFile) {
        // Crea un objeto File con la ruta del archivo a eliminar
        File file = new File(FOLDER + nameFile);
        // Elimina el archivo
        file.delete();
    }
}

