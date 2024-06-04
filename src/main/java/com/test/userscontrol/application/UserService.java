package com.test.userscontrol.application;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UserService {
    private final IUserRepository iUserRepository;
    private final UploadFileService uploadFileService;

    public UserService(IUserRepository iUserRepository, UploadFileService uploadFileService) {
        this.iUserRepository = iUserRepository;
        this.uploadFileService = uploadFileService;
    }

    public User save(User user, MultipartFile multipartFile) throws IOException {
        // Asignamos la imagen del usuario utilizando el servicio de carga de archivos (uploadFileService)
        user.setImage(uploadFileService.upload(multipartFile));
        // Guardamos el usuario actualizado en el repositorio de usuarios (iUserRepository)
        return iUserRepository.save(user);
    }


    public Iterable<User> findAll() {
        // Devuelve una lista de usuarios en el repositorio de usuarios (iUserRepository)
        return iUserRepository.findAll();
    }

    public User findById(Integer id) {
        // Devuelve un usuario mediante su en el repositorio de usuarios (iUserRepository)
        return iUserRepository.findById(id);
    }

    public void deleteById(Integer id) {
        // Buscamos el usuario por su ID utilizando el método findById del servicio
        User user = findById(id);
        // Extraemos el nombre del archivo de imagen del usuario
        String nameFile = user.getImage().substring(29);
        // Verificamos si la imagen del usuario no es la imagen predeterminada ("default.jpg")
        if (!nameFile.equals("default.jpg")) {
            // Si la imagen no es la predeterminada, eliminamos la imagen utilizando el servicio de carga de archivos (uploadFileService)
            uploadFileService.delete(nameFile);
        }
        // Eliminamos el usuario de la base de datos utilizando el repositorio de usuarios (iUserRepository)
        iUserRepository.deleteById(id);
    }

    public User updateUser(User user, MultipartFile multipartFile) throws IOException {
        // Verificamos si se proporcionó un nuevo archivo de imagen
        if (multipartFile != null) {
            // Extraemos el nombre del archivo de imagen actual del usuario
            String nameFile = user.getImage().substring(29);
            // Verificamos si la imagen actual no es la imagen predeterminada ("default.png")
            if (!nameFile.equals("default.png")) {
                // Si la imagen no es la predeterminada, la eliminamos utilizando el servicio de carga de archivos (uploadFileService)
                uploadFileService.delete(nameFile);
            }
            // Actualizamos la imagen del usuario con la nueva imagen utilizando el servicio de carga de archivos (uploadFileService)
            user.setImage(uploadFileService.upload(multipartFile));
        }
        // Actualizamos el usuario en la base de datos utilizando el repositorio de usuarios (iUserRepository) y devolvemos el usuario actualizado
        return iUserRepository.updateUser(user);
    }

    public User findByEmail(String email) {
        // Utilizamos el repositorio de usuarios (iUserRepository) para buscar un usuario por su correo electrónico
        return iUserRepository.findByEmail(email);
    }
}