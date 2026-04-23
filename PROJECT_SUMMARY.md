# 📸 Image Manager - Project Summary

## 🎯 Project Overview

A JavaFX-based image management tool built for the L2 Informatique POO course (2025-2026) at Université de Limoges. The application provides comprehensive image editing, metadata management, and security features.

---

## ✅ Features Implemented

### **1. Basic Application (Reduced App)**
- ✅ Load images via FileChooser
- ✅ Display images in JavaFX ImageView
- ✅ MVC Architecture with FXML UI
- ✅ Status bar showing current operations

### **2. Image Transformations**
- ✅ Rotation (±90°)
- ✅ Symmetry/Mirror (horizontal & vertical)
- ✅ All transformations recorded in metadata

### **3. Image Filters** (with inheritance & interfaces)
- ✅ **Sepia Filter** - Vintage tone effect
- ✅ **Noir & Blanc** - Grayscale (average RGB)
- ✅ **RGB Swap** - Exchanges R, G, B components to G, B, R
- ✅ **Prewitt Filter** - Edge detection
- ✅ All filters extend AbstractFilter → implement Filter interface

### **4. Encryption & Decryption** (Security)
- ✅ **Encryption Filter** - Password-based pixel shuffling
  - Uses SecureRandom seeded with SHA-256 hash of password
  - Password NOT stored (security requirement)
  - Pixels are shuffled in predictable order
- ✅ **Decryption Filter** - Reverses encryption with correct password
  - Uses same SHA-256 seeding for deterministic shuffling

### **5. Metadata System** (Tags & Transformation History)
- ✅ **Tag Management** - Add tags to images for categorization
- ✅ **Transformation Tracking** - Stores filter/transform history with parameters
- ✅ **Metadata Persistence**:
  - **JSON Format** (using Jackson - only authorized library)
    - Professional serialization
    - Stores all metadata in single `metadata.json` file
  - **Text Format** (simple backup format)
    - Human-readable format
    - Easy to edit manually

### **6. Image Library** (Browse & Search)
- ✅ **Library View** - Browse all images with metadata
- ✅ **Image Preview** - View thumbnail with tags
- ✅ **Tag-Based Search** - Find images by tags
  - Partial matching (search "vac" finds "vacation")
  - Shows number of results
- ✅ **Load for Editing** - Open library images in main editor

### **7. User Interface**
- ✅ **Main Panel** - Image display with scrolling
- ✅ **Filter Panel** (right side):
  - Color filters section
  - Transformations section
  - Tags management (add/view)
  - Encryption/Decryption section
  - Metadata save button
- ✅ **Library Panel** (left side):
  - Search bar with tag filtering
  - Image list with preview
  - Quick load button

---

## 📁 Project Structure

```
imageManager/
├── pom.xml                                 # Maven configuration
├── metadata.json                           # Generated metadata file
├── metadata.json.example                   # Example metadata format
│
├── src/main/java/com/imagemanager/
│   ├── Main.java                          # Entry point
│   │
│   ├── controller/
│   │   ├── MainController.java            # Main window controller
│   │   ├── FilterController.java          # Filter & transformation controller
│   │   └── LibraryController.java         # Library browsing & search
│   │
│   ├── model/
│   │   ├── filter/
│   │   │   ├── Filter.java                # Filter interface
│   │   │   ├── AbstractFilter.java        # Base class for color filters
│   │   │   ├── SepiaFilter.java           # Sepia effect
│   │   │   ├── NoireBlanc.java            # Grayscale filter
│   │   │   ├── RGBSwapFilter.java         # RGB component swap
│   │   │   ├── PrewittFilter.java         # Edge detection
│   │   │   ├── EncryptionFilter.java      # Password-based encryption
│   │   │   └── DecryptionFilter.java      # Password-based decryption
│   │   │
│   │   ├── image/
│   │   │   ├── ImageWrapper.java          # Image + metadata wrapper
│   │   │   └── ImageLibrary.java          # Image collection manager
│   │   │
│   │   ├── metadata/
│   │   │   ├── ImageMetadata.java         # Image metadata (tags + transforms)
│   │   │   ├── Transformation.java        # Single transformation record
│   │   │   ├── Tag.java                   # Tag record type
│   │   │   └── MetadataManager.java       # Metadata CRUD operations
│   │   │
│   │   └── persistence/
│   │       ├── MetadataDAO.java           # DAO interface
│   │       ├── JsonMetadataDAO.java       # Jackson-based JSON persistence
│   │       └── TextMetadataDAO.java       # Simple text persistence
│   │
│   └── module-info.java                   # Java module declarations
│
└── src/main/resources/
    ├── fxml/
    │   ├── main.fxml                      # Main window layout
    │   ├── filter.fxml                    # Filter panel layout
    │   └── library.fxml                   # Library panel layout
    │
    └── css/
        └── style.css                      # Application styles
```

---

## 🚀 How to Use

### **Load & Edit Images**
1. Click "📁 Ouvrir image" to load an image
2. Apply filters from the right panel
3. Apply transformations (rotate/mirror)
4. Add tags for organization

### **Encrypt/Decrypt Images**
1. Enter a password in the password field
2. Click "Chiffrer" to encrypt (pixels shuffled)
3. Click "Déchiffrer" to decrypt (with same password)
4. Note: Password is NOT stored for security

### **Save Metadata**
1. After editing, click "💾 Sauvegarder Métadonnées"
2. All tags and transformations saved to `metadata.json`
3. Metadata file is in project root directory

### **Browse & Search Library**
1. Open the left panel (Library)
2. All previously edited images appear in the list
3. Type a tag in search box (e.g., "vacation")
4. Click "🔍 Chercher" to filter images
5. Click image preview to load for editing

---

## 🏗️ Architecture Decisions

### **Design Pattern: MVC**
- **Model**: ImageMetadata, Transformation, Tag classes
- **View**: FXML files (main.fxml, filter.fxml, library.fxml)
- **Controller**: MainController, FilterController, LibraryController

### **Design Pattern: DAO**
- MetadataDAO interface abstracts persistence
- JsonMetadataDAO uses Jackson (authorized library)
- TextMetadataDAO provides fallback format
- Easy to swap implementations

### **Design Pattern: Strategy**
- Filter interface allows different filter implementations
- All filters extend AbstractFilter for code reuse
- Easy to add new filters without modifying existing code

### **Security Approach**
- Password NOT stored (only transformation name)
- SecureRandom for cryptographically sound shuffling
- SHA-256 hashing for deterministic seeding
- Same password always produces same shuffle sequence

---

## 📊 Requirements Checklist

### **Specification Requirements (PDF)**

#### Application Réduite ✅
- ✅ Read and display images
- ✅ Create "resources" directory (structure ready)
- ✅ FileChooser for image selection
- ✅ MVC pattern with FXML
- ✅ Controllers manage UI interactions

#### Les transformations d'image ✅
- ✅ Rotation & Symmetry system
- ✅ Multiple filters with inheritance/interfaces
- ✅ RGB component exchange (RGB → GBR)
- ✅ Grayscale filter (average)
- ✅ Sepia filter
- ✅ Prewitt edge detection
- ✅ Filters integrated in UI

#### Support des tags et sauvegarde ✅
- ✅ Tag system for image categorization
- ✅ Transformation history storage
- ✅ File-based persistence (JSON & Text)
- ✅ Single metadata file
- ✅ Jackson library (authorized)
- ✅ Can replay transformations on image load

#### Un peu de sécurité ✅
- ✅ SecureRandom-based encryption
- ✅ SHA-256 password hashing
- ✅ Password NOT stored in metadata
- ✅ Deterministic shuffle (same password = same shuffle)
- ✅ Decryption support

#### Les fonctions avancées (Bonus) ✅
- ✅ Library browsing
- ✅ Tag-based search
- ✅ Image preview with metadata display

---

## 📚 Libraries Used

| Library | Version | Purpose | Status |
|---------|---------|---------|--------|
| **JavaFX** | 21 | UI Framework (FXML) | ✅ Authorized |
| **Jackson** | 2.17.0 | JSON Serialization | ✅ Authorized (spec requirement) |
| **JUnit 5** | 5.10.2 | Testing | ✅ For tests only |
| **Java SE** | 21 | Core (Image, Security, Collections) | ✅ Standard Library |

**Note**: NO unauthorized libraries used ✅

---

## 🔧 Build & Run

### **Prerequisites**
- Java 21+
- Maven 3.6+
- JavaFX SDK 21

### **Commands**
```bash
# Compile
mvn clean compile

# Run
mvn clean javafx:run

# Package
mvn clean package
```

---

## 💾 Metadata File Format (JSON)

```json
{
  "/path/to/image1.jpg": {
    "imagePath": "/path/to/image1.jpg",
    "tags": [
      {"value": "vacation"},
      {"value": "beach"}
    ],
    "transformations": [
      {
        "name": "RGBSwap",
        "type": "filter",
        "parameter": null
      },
      {
        "name": "RotationDroite",
        "type": "transform",
        "parameter": null
      },
      {
        "name": "Encryption",
        "type": "filter",
        "parameter": null
      }
    ]
  }
}
```

---

## 🎓 Learning Outcomes

This project demonstrates:
- **OOP Principles**: Inheritance, Interfaces, Encapsulation
- **Design Patterns**: MVC, DAO, Strategy
- **JavaFX**: FXML layouts, Controllers, Image manipulation
- **Security**: Password hashing, SecureRandom, encryption fundamentals
- **File I/O**: JSON serialization with Jackson
- **Metadata Management**: Structured data persistence

---

## 📝 Notes

- **Not Professional Level**: Code is simple and functional, focused on learning
- **UI Design**: Straightforward layout with clear sections
- **Error Handling**: Basic error messages to user
- **Performance**: Suitable for small to medium image libraries
- **Extensible**: Easy to add new filters or transformations

---

**Project Status**: ✅ Complete

**Team**: Individual project for POO Course  
**Date**: April 2026  
**University**: Université de Limoges - L2 Informatique
