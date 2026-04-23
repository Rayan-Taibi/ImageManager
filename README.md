# 🖼️ Image Manager - Quick Start Guide

## What Does This Do?

This is a simple image editor with:
- 📸 Load & edit images
- 🎨 Apply color filters (Sepia, Grayscale, RGB Swap, Edge Detection)
- 🔄 Rotate & mirror images
- 🏷️ Add tags to organize images
- 🔐 Encrypt/decrypt images with passwords
- 📁 Browse your image library
- 🔍 Search images by tags

---

## Quick Start

### 1️⃣ Open the App
```bash
mvn clean javafx:run
```

### 2️⃣ Load an Image
- Click "📁 Ouvrir image" button
- Select any JPG or PNG file

### 3️⃣ Apply Filters
- **Sépia** → Vintage sepia tone
- **Noir & Blanc** → Grayscale
- **RGB Swap** → Shuffle colors
- **Contours** → Show edges

### 4️⃣ Transform Image
- **↺ -90°** → Rotate left
- **↻ +90°** → Rotate right
- **↔ Miroir H** → Flip horizontal
- **↕ Miroir V** → Flip vertical

### 5️⃣ Add Tags
- Type tag name (e.g., "vacation", "beach")
- Click "+" button
- Tags saved in metadata

### 6️⃣ Encrypt Your Images
- Enter a password
- Click "Chiffrer" → Image shuffled
- Click "Déchiffrer" → Unscramble with same password
- Password NOT stored for security ✅

### 7️⃣ Save Everything
- Click "💾 Sauvegarder Métadonnées"
- Tags & transformations saved to `metadata.json`

### 8️⃣ Browse Library
- Click left panel
- See all your images with tags
- Search by tag in search box
- Click image to preview

---

## File Locations

| Item | Location |
|------|----------|
| Metadata | `metadata.json` (in project root) |
| Example | `metadata.json.example` |
| Summary | `PROJECT_SUMMARY.md` |

---

## Keyboard Tips

- No keyboard shortcuts (simple UI)
- Use buttons and text fields only

---

## What Happens When I...

### ...load an image?
1. Image appears in center
2. Existing tags load (if any)
3. You can now apply filters/transforms

### ...apply a filter?
1. Image updates immediately
2. Filter recorded in metadata
3. You can undo with "Réinitialiser"

### ...encrypt an image?
1. Image pixels get scrambled
2. Only YOU can unscramble (with password)
3. Password NOT saved anywhere

### ...save metadata?
1. All tags saved
2. All filters/transforms saved
3. File created: `metadata.json`

### ...search for images?
1. Type tag name
2. Click search
3. Only matching images appear
4. Clear button shows all again

---

## Common Issues

### "Error: No image loaded"
- Click "📁 Ouvrir image" first
- Then try applying filters

### "Metadata file not found"
- This is OK - will be created on first save
- Search in library will show empty

### "Image not found in library"
- File was moved or deleted
- Remove old path and reload

### "Decryption didn't work"
- You used the wrong password
- Try again with correct password

---

## Project Structure (Simple)

```
├── Main                    ← Starts the app
├── MainController          ← Opens images
├── FilterController        ← Applies filters & saves metadata
├── LibraryController       ← Browsing & search
│
├── Image Filters           ← Sepia, Grayscale, RGB, Edges
├── Encryption Filters      ← Scramble & unscramble pixels
├── Metadata System         ← Tags & transformation history
└── FXML UI Files           ← main.fxml, filter.fxml, library.fxml
```

---

## What's NOT Included

- ❌ Undo/Redo (use Reset button instead)
- ❌ Save edited images (only metadata saved)
- ❌ Batch processing
- ❌ Complex effects
- ❌ Image comparison
- ❌ Advanced security

---

## Class Diagram (Simple)

```
Filter (interface)
├── AbstractFilter (base class)
│   ├── SepiaFilter
│   ├── NoireBlanc
│   ├── RGBSwapFilter
│   └── PrewittFilter
├── EncryptionFilter
└── DecryptionFilter

MetadataDAO (interface)
├── JsonMetadataDAO (uses Jackson)
└── TextMetadataDAO

ImageMetadata
├── tags: List<Tag>
└── transformations: List<Transformation>
```

---

## Next Steps to Improve (Optional)

- [ ] Batch encrypt multiple images
- [ ] Preview before save
- [ ] Tag suggestions
- [ ] Favorite images
- [ ] Category folders
- [ ] Export filtered image
- [ ] Watermark support

---

**Made for**: L2 Informatique POO Course  
**University**: Université de Limoges  
**Date**: 2025-2026
