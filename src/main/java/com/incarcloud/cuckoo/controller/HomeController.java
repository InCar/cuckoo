package com.incarcloud.cuckoo.controller;

import com.incarcloud.cuckoo.GitVer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class HomeController {

    @GetMapping("version")
    public ResponseEntity<GitVer> getGitVersion() {
        return ResponseEntity.ok(new GitVer());
    }
}
