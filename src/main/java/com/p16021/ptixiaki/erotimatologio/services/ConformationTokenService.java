package com.p16021.ptixiaki.erotimatologio.services;

import com.p16021.ptixiaki.erotimatologio.models.entities.user.ConfirmationToken;
import com.p16021.ptixiaki.erotimatologio.repos.ConformationTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConformationTokenService{

    private final ConformationTokenRepo conformationTokenRepo;

    public void saveConformationToken(ConfirmationToken token) {
        conformationTokenRepo.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return conformationTokenRepo.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return conformationTokenRepo.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
