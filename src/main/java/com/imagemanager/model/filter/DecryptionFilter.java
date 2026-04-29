package com.imagemanager.model.filter;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DecryptionFilter implements Filter {
    private final String motDePasse;

    public DecryptionFilter(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    @Override
    public Image apply(Image imageSource) {
        int largeur = (int) imageSource.getWidth();
        int hauteur = (int) imageSource.getHeight();
        int nombreTotalPixels = largeur * hauteur;

        List<Integer> indicesPixels = new ArrayList<>();
        for (int indice = 0; indice < nombreTotalPixels; indice++) {
            indicesPixels.add(indice);
        }

        // Meme graine que le chiffrement, sinon ca casse.
        SecureRandom generateurAleatoireSecurise = getSecureRandomDepuisMotDePasse();
        Collections.shuffle(indicesPixels, generateurAleatoireSecurise);

        PixelReader lecteurPixels = imageSource.getPixelReader();
        Color[] pixelsChiffres = new Color[nombreTotalPixels];
        for (int indice = 0; indice < nombreTotalPixels; indice++) {
            pixelsChiffres[indice] = lecteurPixels.getColor(indice % largeur, indice / largeur);
        }

        WritableImage imageResultante = new WritableImage(largeur, hauteur);
        PixelWriter ecrivainPixels = imageResultante.getPixelWriter();

        for (int positionMelangee = 0; positionMelangee < nombreTotalPixels; positionMelangee++) {
            int indiceOriginal = indicesPixels.get(positionMelangee);
            ecrivainPixels.setColor(indiceOriginal % largeur, indiceOriginal / largeur, pixelsChiffres[positionMelangee]);
        }

        return imageResultante;
    }

    @Override
    public String getName() {
        return "Decryption";
    }

    private SecureRandom getSecureRandomDepuisMotDePasse() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] graine = digest.digest(motDePasse.getBytes());
            
            SecureRandom generateur = SecureRandom.getInstance("SHA1PRNG");
            generateur.setSeed(graine);
            return generateur;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not available", e);
        }
    }
}