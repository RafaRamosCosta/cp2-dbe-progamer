package br.com.fiap.controller;

import br.com.fiap.dao.ProfileDAO;
import br.com.fiap.model.Profile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@Api(value = "Progamer API")
public class ProfileController {

    @Inject
    private ProfileDAO profileDAO;

    @GetMapping()
    @ApiOperation("Obter todos os perfis")
    public ResponseEntity<List<Profile>> index() {
        return ResponseEntity.ok(profileDAO.findAll());
    }

    @GetMapping("{id}")
    @ApiOperation("Obter perfil por ID")
    public ResponseEntity<Profile> show(@PathVariable("id") long id) {
        Profile profile = profileDAO.findById(id);

        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(profile);
    }

    @PostMapping()
    @ApiOperation("Criar novo perfil")
    public ResponseEntity<String> create(@RequestBody Profile profileRequest) {
        Profile profile = new Profile();
        try {
            if (profileRequest.getName() == null || profileRequest.getEmail() == null || profileRequest.getProfile() == null
                    || profileRequest.getPassword() == null) {

                System.out.println("===== ERROR =====");
                System.out.println("Parâmetros insuficientes");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            }

            profile.setName(profileRequest.getName());
            profile.setEmail(profileRequest.getEmail());
            profile.setProfile(profileRequest.getProfile());
            profile.setPassword(profileRequest.getPassword());

            profileDAO.salvar(profile);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualização de Perfil")
    public ResponseEntity<String> update(@PathVariable("id") long id, @RequestBody Profile profileRequest) {

        try {
            Profile profile = profileDAO.findById(id);

            if (profile == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            profile.setName(profileRequest.getName());
            profile.setEmail(profileRequest.getEmail());
            profile.setProfile(profileRequest.getProfile());
            profile.setPassword(profileRequest.getPassword());

            profileDAO.salvar(profile);

            return ResponseEntity.ok("Perfil atualizado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("{id}")
    @ApiOperation("Atualização parcial de Perfil")
    public ResponseEntity<String> patch(@PathVariable("id") long id, @RequestBody Profile profileRequest) {

        try {
            Profile profile = profileDAO.findById(id);

            if (profile == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            if (profileRequest.getName() != null)
                profile.setName(profileRequest.getName());
            if (profileRequest.getEmail() != null)
                profile.setEmail(profileRequest.getEmail());
            if (profileRequest.getProfile() != null)
                profile.setProfile(profileRequest.getProfile());
            if (profileRequest.getPassword() != null)
                profile.setPassword(profileRequest.getPassword());

            profileDAO.salvar(profile);

            return ResponseEntity.ok("Perfil atualizado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir Perfil")
    public ResponseEntity<String> destroy(@PathVariable("id") long id) {
        try {
            Profile profile = profileDAO.findById(id);
            if (profile == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            profileDAO.delete(profile);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
