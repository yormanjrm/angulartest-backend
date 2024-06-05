package com.test.userscontrol.infrastructure.config;

import com.test.userscontrol.infrastructure.dto.ApiResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"security.enabled=true"}, locations = "classpath:application.properties")
public class SecurityConfigurationTests {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port;
    }

    private String loginAndGetAdminToken() {
        return loginAndGetToken("john.doe@example.com", "password123");
    }

    private String loginAndGetRecepToken() {
        return loginAndGetToken("jane.doe@example.com", "password123");
    }

    private String loginAndGetToken(String email, String password) {
        String loginURL = url + "/api/login";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", email);
        params.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<ApiResponseDTO> response = testRestTemplate.postForEntity(loginURL, request, ApiResponseDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ApiResponseDTO<?> body = response.getBody();
            if (body.getData() != null) {
                Map<String, Object> data = (Map<String, Object>) body.getData();
                String token = (String) data.get("token");
                return token.replace("Bearer ", "");
            }
        }
        return null;
    }

    @Test
    void testLoginEndpoint() {
        // Construye la URL del endpoint de login usando la base de la URL del servidor de pruebas
        String loginURL = url + "/api/login";
        // Crea un objeto MultiValueMap para almacenar los parámetros de la solicitud
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        // Agrega el parámetro 'email' con el valor 'john.doe@example.com'
        params.add("email", "john.doe@example.com");
        // Agrega el parámetro 'password' con el valor 'password123'
        params.add("password", "password123");
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el tipo de contenido del encabezado a 'application/x-www-form-urlencoded'
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Crea una HttpEntity con los parámetros y los encabezados configurados
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        // Realiza la solicitud POST al endpoint de login utilizando TestRestTemplate con autenticación básica
        ResponseEntity<String> res = testRestTemplate
                .withBasicAuth("abc", "def")  // Proporciona credenciales de autenticación básica
                .postForEntity(loginURL, request, String.class);  // Envía la solicitud POST y recibe una respuesta
        // Verifica que el estado de la respuesta sea 200 OK
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode().value());
    }

    @Test
    void testLoginEndpoint_InvalidUser() {
        // Construye la URL del endpoint de login usando la base de la URL del servidor de pruebas
        String loginURL = url + "/api/login";
        // Crea un objeto MultiValueMap para almacenar los parámetros de la solicitud
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        // Agrega el parámetro 'email' con un valor que no corresponde a un usuario existente
        params.add("email", "john.doe3@example.com");
        // Agrega el parámetro 'password' con el valor 'password123'
        params.add("password", "password123");
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el tipo de contenido del encabezado a 'application/x-www-form-urlencoded'
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Crea una HttpEntity con los parámetros y los encabezados configurados
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        // Realiza la solicitud POST al endpoint de login utilizando TestRestTemplate con autenticación básica
        ResponseEntity<String> res = testRestTemplate
                .withBasicAuth("abc", "def")  // Proporciona credenciales de autenticación básica
                .postForEntity(loginURL, request, String.class);  // Envía la solicitud POST y recibe una respuesta
        // Verifica que el estado de la respuesta sea 404 NOT FOUND, indicando que el usuario no existe
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), res.getStatusCode().value());
    }

    @Test
    void testLoginEndpoint_InvalidPassword() {
        // Construye la URL del endpoint de login usando la base de la URL del servidor de pruebas
        String loginURL = url + "/api/login";
        // Crea un objeto MultiValueMap para almacenar los parámetros de la solicitud
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        // Agrega el parámetro 'email' con un valor que corresponde a un usuario existente
        params.add("email", "john.doe@example.com");
        // Agrega el parámetro 'password' con un valor incorrecto
        params.add("password", "password1234");
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el tipo de contenido del encabezado a 'application/x-www-form-urlencoded'
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Crea una HttpEntity con los parámetros y los encabezados configurados
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        // Realiza la solicitud POST al endpoint de login utilizando TestRestTemplate con autenticación básica
        ResponseEntity<String> res = testRestTemplate
                .withBasicAuth("abc", "def")  // Proporciona credenciales de autenticación básica
                .postForEntity(loginURL, request, String.class);  // Envía la solicitud POST y recibe una respuesta
        // Verifica que el estado de la respuesta sea 403 FORBIDDEN, indicando que la contraseña es incorrecta
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), res.getStatusCode().value());
    }

    @Test
    void testRegister_Unauthenticated() {
        // Construye la URL del endpoint de registro usando la base de la URL del servidor de pruebas
        String registerURL = url + "/api/users/register?id=0&name=Mark&email=mark.doe@example.com&password=password123&role=RECEP&image=null";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud POST al endpoint de registro utilizando TestRestTemplate sin autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(registerURL, HttpMethod.POST, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que la solicitud requiere autenticación
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }

    @Test
    void testRegister_Authenticated() {
        // Obtiene un token de autenticación para un usuario administrador
        String token = loginAndGetAdminToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint de registro usando la base de la URL del servidor de pruebas
        String registerURL = url + "/api/users/register?id=0&name=Mark&email=mark.doe@example.com&password=password123&role=RECEP&image=null";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Establece el tipo de contenido del encabezado a 'application/x-www-form-urlencoded'
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud POST al endpoint de registro utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(registerURL, HttpMethod.POST, request, String.class);
        // Verifica que el estado de la respuesta sea 201 CREATED, indicando que el registro fue exitoso
        Assertions.assertEquals(HttpStatus.CREATED.value(), res.getStatusCode().value());
    }

    @Test
    void testRegister_WrongRole() {
        // Obtiene un token de autenticación para un usuario con rol de recepcionista
        String token = loginAndGetRecepToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint de registro usando la base de la URL del servidor de pruebas
        String registerURL = url + "/api/users/register?id=0&name=Mark&email=mark.doe@example.com&password=password123&role=RECEP&image=null";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Establece el tipo de contenido del encabezado a 'application/x-www-form-urlencoded'
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud POST al endpoint de registro utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(registerURL, HttpMethod.POST, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que el usuario no tiene permisos suficientes
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }

    @Test
    void testGetAllUsers_Unauthenticated() {
        // Construye la URL del endpoint para obtener todos los usuarios usando la base de la URL del servidor de pruebas
        String getAllUsersURL = url + "/api/users/get/all";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud GET al endpoint utilizando TestRestTemplate sin autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(getAllUsersURL, HttpMethod.GET, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que la solicitud requiere autenticación
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }

    @Test
    void testGetAllUsers_Authenticated() {
        // Obtiene un token de autenticación para un usuario administrador
        String token = loginAndGetAdminToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint para obtener todos los usuarios usando la base de la URL del servidor de pruebas
        String getAllUsersURL = url + "/api/users/get/all";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud GET al endpoint utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(getAllUsersURL, HttpMethod.GET, request, String.class);
        // Verifica que el estado de la respuesta sea 200 OK, indicando que la solicitud fue exitosa
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode().value());
    }


    @Test
    void testGetUserById_Unauthenticated() {
        // Construye la URL del endpoint para obtener un usuario por ID usando la base de la URL del servidor de pruebas
        String getUserByIdURL = url + "/api/users/get/byId?id=3";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud GET al endpoint utilizando TestRestTemplate sin autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(getUserByIdURL, HttpMethod.GET, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que la solicitud requiere autenticación
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }

    @Test
    void testGetUserById_Authenticated() {
        // Obtiene un token de autenticación para un usuario administrador
        String token = loginAndGetAdminToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint para obtener un usuario por ID usando la base de la URL del servidor de pruebas
        String getUserByIdURL = url + "/api/users/get/byId?id=3";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud GET al endpoint utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(getUserByIdURL, HttpMethod.GET, request, String.class);
        // Verifica que el estado de la respuesta sea 200 OK, indicando que la solicitud fue exitosa
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode().value());
    }

    @Test
    void testGetUserById_WrongRole() {
        // Obtiene un token de autenticación para un usuario con rol de recepcionista
        String token = loginAndGetRecepToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint para obtener un usuario por ID usando la base de la URL del servidor de pruebas
        String getUserByIdURL = url + "/api/users/get/byId?id=3";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud GET al endpoint utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(getUserByIdURL, HttpMethod.GET, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que el usuario no tiene permisos suficientes
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }


    @Test
    void testUpdate_Unauthenticated() {
        // Construye la URL del endpoint para actualizar un usuario usando la base de la URL del servidor de pruebas
        String updateURL = url + "/api/users/update?id=3&name=Mark&email=mark@example.com&password=password123&role=RECEP&image=http://localhost:8080/images/default2.png";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud PUT al endpoint utilizando TestRestTemplate sin autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(updateURL, HttpMethod.PUT, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que la solicitud requiere autenticación
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }

    @Test
    void testUpdate_Authenticated() {
        // Obtiene un token de autenticación para un usuario administrador
        String token = loginAndGetAdminToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint para actualizar un usuario usando la base de la URL del servidor de pruebas
        String updateURL = url + "/api/users/update?id=3&name=Mark&email=mark@example.com&password=password123&role=RECEP&image=http://localhost:8080/images/default3.png";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Establece el tipo de contenido del encabezado a 'application/x-www-form-urlencoded'
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud PUT al endpoint utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(updateURL, HttpMethod.PUT, request, String.class);
        // Verifica que el estado de la respuesta sea 200 OK, indicando que la actualización fue exitosa
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode().value());
    }

    @Test
    void testUpdate_WrongRole() {
        // Obtiene un token de autenticación para un usuario con rol de recepcionista
        String token = loginAndGetRecepToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint para actualizar un usuario usando la base de la URL del servidor de pruebas
        String updateURL = url + "/api/users/update?id=3&name=Mark&email=mark@example.com&password=password123&role=RECEP&image=http://localhost:8080/images/default3.png";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Establece el tipo de contenido del encabezado a 'application/x-www-form-urlencoded'
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud PUT al endpoint utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(updateURL, HttpMethod.PUT, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que el usuario no tiene permisos suficientes
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }

    @Test
    void testDelete_Unauthenticated() {
        // Construye la URL del endpoint para eliminar un usuario usando la base de la URL del servidor de pruebas
        String deleteURL = url + "/api/users/delete?id=3";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud DELETE al endpoint utilizando TestRestTemplate sin autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(deleteURL, HttpMethod.DELETE, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que la solicitud requiere autenticación
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }

    @Test
    void testDelete_Authenticated() {
        // Obtiene un token de autenticación para un usuario administrador
        String token = loginAndGetAdminToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint para eliminar un usuario usando la base de la URL del servidor de pruebas
        String deleteURL = url + "/api/users/delete?id=3";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud DELETE al endpoint utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(deleteURL, HttpMethod.DELETE, request, String.class);
        // Verifica que el estado de la respuesta sea 200 OK, indicando que la eliminación fue exitosa
        Assertions.assertEquals(HttpStatus.OK.value(), res.getStatusCode().value());
    }

    @Test
    void testDelete_WrongRole() {
        // Obtiene un token de autenticación para un usuario con rol de recepcionista
        String token = loginAndGetRecepToken();
        // Verifica que el token no sea nulo
        Assertions.assertNotNull(token, "Token should not be null");
        // Construye la URL del endpoint para eliminar un usuario usando la base de la URL del servidor de pruebas
        String deleteURL = url + "/api/users/delete?id=3";
        // Crea un objeto HttpHeaders para configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        // Establece el encabezado de autenticación Bearer con el token obtenido
        headers.setBearerAuth(token);
        // Crea una HttpEntity con los encabezados configurados
        HttpEntity<String> request = new HttpEntity<>(headers);
        // Realiza la solicitud DELETE al endpoint utilizando TestRestTemplate con autenticación
        ResponseEntity<String> res = testRestTemplate.exchange(deleteURL, HttpMethod.DELETE, request, String.class);
        // Verifica que el estado de la respuesta sea 401 UNAUTHORIZED, indicando que el usuario no tiene permisos suficientes
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), res.getStatusCode().value());
    }

}
