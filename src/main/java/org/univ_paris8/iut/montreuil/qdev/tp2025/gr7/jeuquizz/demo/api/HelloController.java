package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/helloWorld")
    public ResponseEntity<Map<String, String>> helloWorld() {
        return ResponseEntity.ok(Map.of("message", "Hello World !"));
    }

    @GetMapping("/params")
    public ResponseEntity<?> paramsQuery(@RequestParam(name = "name", required = false) String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le paramètre 'name' est obligatoire"));
        }
        return ResponseEntity.ok(Map.of("QueryParam_name", name));
    }

    @GetMapping("/params/{value}")
    public ResponseEntity<Map<String, String>> paramsPath(@PathVariable("value") String value) {
        return ResponseEntity.ok(Map.of("PathParam_value", value));
    }
}
