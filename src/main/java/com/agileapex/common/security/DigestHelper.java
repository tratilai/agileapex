package com.agileapex.common.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.AgileApexException;

public class DigestHelper {
    private static final Logger logger = LoggerFactory.getLogger(DigestHelper.class);
    private static final int DERIVED_KEY_LENGTH = 160;
    private static final int HASH_GENERATION_ITERATIONS = 34985;

    public synchronized boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt) {
        byte[] encryptedAttemptedPassword = getDigest(attemptedPassword, salt);
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }

    public synchronized byte[] getDigest(String data, byte[] passwordSalt) {
        String algorithmName = "PBKDF2WithHmacSHA1";
        KeySpec specification = new PBEKeySpec(data.toCharArray(), passwordSalt, HASH_GENERATION_ITERATIONS, DERIVED_KEY_LENGTH);
        SecretKeyFactory secretKeyFactory = null;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            String message = "Could not get security key factory instance.";
            logger.error(message, e);
            throw new AgileApexException(message, e);
        }
        try {
            return secretKeyFactory.generateSecret(specification).getEncoded();
        } catch (InvalidKeySpecException e) {
            String message = "Could not generate digest from the data.";
            logger.error(message, e);
            throw new AgileApexException(message, e);
        }
    }

    public synchronized byte[] getNewSalt() {
        SecureRandom randomNumberGenerator = null;
        try {
            randomNumberGenerator = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            String message = "Could not get security salt.";
            logger.error(message, e);
            throw new AgileApexException(message, e);
        }
        byte[] passwordSalt = new byte[8];
        randomNumberGenerator.nextBytes(passwordSalt);
        return passwordSalt;
    }
}
