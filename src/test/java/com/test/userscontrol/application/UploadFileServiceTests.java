package com.test.userscontrol.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UploadFileServiceTests {

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UploadFileService uploadFileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpload() throws IOException {
        // Mock del MultipartFile
        byte[] content = "file content".getBytes(); // Creamos contenido de archivo de prueba
        when(multipartFile.getBytes()).thenReturn(content); // Configuramos el mock para devolver el contenido de archivo cuando se llame a getBytes()
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg"); // Configuramos el mock para devolver "test.jpg" cuando se llame a getOriginalFilename()
        // Realizamos la prueba
        String url = uploadFileService.upload(multipartFile); // Invocamos el método upload con el mock MultipartFile
        // Verificamos que se haya llamado al método correctamente
        verify(multipartFile).getBytes(); // Verificamos que se haya llamado a getBytes() en el mock MultipartFile
        verify(multipartFile).getOriginalFilename(); // Verificamos que se haya llamado a getOriginalFilename() en el mock MultipartFile
        // Verificamos el resultado
        assertEquals("http://localhost:8080/images/test.jpg", url); // Verificamos que la URL devuelta sea la esperada
    }

    @Test
    public void testUploadWithNullMultipartFile() throws IOException {
        // Realizamos la prueba
        String url = uploadFileService.upload(null);
        // Verificamos el resultado
        assertEquals("http://localhost:8080/images/default.png", url);
    }

    @Test
    public void testDelete(@TempDir Path tempDir) throws IOException {
        // Mock del MultipartFile
        byte[] content = "file content".getBytes(); // Creamos contenido simulado para el MultipartFile
        when(multipartFile.getBytes()).thenReturn(content); // Configuramos el mock para devolver el contenido simulado cuando se llame a getBytes()
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg"); // Configuramos el mock para devolver "test.jpg" como el nombre original del archivo
        // Realizamos la carga del archivo
        String url = uploadFileService.upload(multipartFile); // Invocamos el método upload del servicio UploadFile para guardar el archivo
        // Verificamos que se haya llamado al método correctamente
        verify(multipartFile).getBytes(); // Verificamos que se haya llamado a getBytes() en el mock MultipartFile
        verify(multipartFile).getOriginalFilename(); // Verificamos que se haya llamado a getOriginalFilename() en el mock MultipartFile
        // Verificamos que el archivo se haya guardado correctamente
        assertNotNull(url); // Verificamos que la URL de retorno no sea nula
        // Obtenemos el nombre completo del archivo (incluyendo la ruta)
        String nameFile = url.replace(uploadFileService.URL, ""); // Extraemos el nombre del archivo de la URL de retorno
        // Realizamos la eliminación del archivo
        uploadFileService.delete(nameFile); // Llamamos al método delete del servicio UploadFile para eliminar el archivo
        // Verificamos que el archivo haya sido eliminado correctamente
        assertFalse(Files.exists(tempDir.resolve(nameFile))); // Verificamos que el archivo no exista en el directorio temporal
    }
}