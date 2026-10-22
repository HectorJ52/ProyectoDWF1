// ESTO ES SOLAMENTE PARA GENERAR CONTRASEÑAS ENCRIPTADAS
// PARA INICIARLO DEBEN HACER CLICK DERECHO ENCIMA DE LA CLASE Y DARLE
// A "RUN FILE"
package sv.edu.udb;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Contraseña en textoplano que quieres encriptar
        String password = "admin";

        // Generar el hash BCrypt
        String hash = encoder.encode(password);

        // Mostrar ambos valores
        System.out.println("================================");
        System.out.println("Texto plano: " + password);
        System.out.println("Hash generado: " + hash);
        System.out.println("================================");

        // Comprobar que el hash corresponde al texto original
        boolean coincide = encoder.matches(password, hash);
        System.out.println("¿El hash corresponde al original? " + coincide);
    }
}
