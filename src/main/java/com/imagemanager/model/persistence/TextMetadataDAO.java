package com.imagemanager.model.persistence;

import com.imagemanager.model.metadata.ImageMetadata;
import com.imagemanager.model.metadata.Tag;
import com.imagemanager.model.metadata.Transformation;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Persistence des metadonnees en texte.
 * Stocke les donnees dans un format simple et lisible.
 */
public class TextMetadataDAO implements MetadataDAO {
    private static final String FICHIER_METADONNEES = "metadata.txt";

    @Override
    public void saveMetadata(Map<String, ImageMetadata> toutesLesMetadonnees) throws IOException {
        try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_METADONNEES))) {
            for (ImageMetadata metadonnees : toutesLesMetadonnees.values()) {
                ecrivain.write("IMAGE: " + metadonnees.getImagePath());
                ecrivain.newLine();

                // Ecrit les tags.
                ecrivain.write("TAGS: ");
                for (Tag etiquette : metadonnees.getTags()) {
                    ecrivain.write(etiquette.valeur());
                    ecrivain.write("|");
                }
                ecrivain.newLine();

                // Ecrit les transforms.
                for (Transformation transformation : metadonnees.getTransformations()) {
                    ecrivain.write("TRANSFORM: " + transformation.nom() + " " + transformation.typeTransformation());
                    ecrivain.newLine();
                }

                ecrivain.write("---");
                ecrivain.newLine();
            }
        }
    }

    @Override
    public Map<String, ImageMetadata> loadMetadata() throws IOException {
        Map<String, ImageMetadata> resultat = new HashMap<>();
        File fichier = new File(FICHIER_METADONNEES);
        if (!fichier.exists()) {
            return resultat;
        }

        try (BufferedReader lecteur = new BufferedReader(new FileReader(FICHIER_METADONNEES))) {
            String ligne;
            ImageMetadata courant = null;

            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.startsWith("IMAGE: ")) {
                    String cheminImage = ligne.substring(7);
                    courant = new ImageMetadata(cheminImage);
                    resultat.put(cheminImage, courant);
                } else if (ligne.startsWith("TAGS: ") && courant != null) {
                    String etiquettes = ligne.substring(6);
                    if (!etiquettes.isEmpty()) {
                        for (String etiquette : etiquettes.split("\\|")) {
                            if (!etiquette.isEmpty()) {
                                courant.getTags().add(new Tag(etiquette));
                            }
                        }
                    }
                } else if (ligne.startsWith("TRANSFORM: ") && courant != null) {
                    String[] parties = ligne.substring(11).split(" ", 2);
                    if (parties.length >= 2) {
                        courant.getTransformations().add(new Transformation(parties[0], parties[1]));
                    }
                }
            }
        }
        return resultat;
    }

    @Override
    public ImageMetadata getMetadata(String cheminImage) throws IOException {
        Map<String, ImageMetadata> toutesLesMetadonnees = loadMetadata();
        return toutesLesMetadonnees.getOrDefault(cheminImage, new ImageMetadata(cheminImage));
    }

    @Override
    public void saveMetadataForImage(String cheminImage, ImageMetadata metadonnees) throws IOException {
        Map<String, ImageMetadata> toutesLesMetadonnees = loadMetadata();
        toutesLesMetadonnees.put(cheminImage, metadonnees);
        saveMetadata(toutesLesMetadonnees);
    }
}

