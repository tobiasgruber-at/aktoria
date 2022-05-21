package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidTokenException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SecureTokenRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SecureTokenService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class SecureTokenServiceImpl implements SecureTokenService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    final SecureTokenRepository secureTokenRepository;

    @Autowired
    SecureTokenServiceImpl(SecureTokenRepository secureTokenRepository) {
        this.secureTokenRepository = secureTokenRepository;
    }

    @Override
    public SecureToken createSecureToken(TokenType type, int expirationTime) {
        String tokenValue = Base64.encodeBase64URLSafeString(DEFAULT_TOKEN_GENERATOR.generateKey());
        SecureToken secureToken = new SecureToken();
        secureToken.setToken(tokenValue);
        secureToken.setType(type);
        secureToken.setExpireAt(LocalDateTime.now().plusMinutes(expirationTime));
        return secureToken;
    }

    @Override
    public void saveSecureToken(SecureToken token) {
        secureTokenRepository.deleteAllExpired(LocalDateTime.now());
        secureTokenRepository.saveAndFlush(token);
    }

    @Override
    public SecureToken findByToken(String token) {
        Optional<SecureToken> secureTokenOptional = secureTokenRepository.findByToken(token);
        if (secureTokenOptional.isPresent()) {
            return secureTokenOptional.get();
        } else {
            throw new InvalidTokenException();
        }
    }

    @Override
    public void removeToken(String token) {
        secureTokenRepository.removeByToken(token);
    }
}
