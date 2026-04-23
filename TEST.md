# ✅ Image Manager - Testing Guide

**Last Updated**: April 23, 2026  
**Status**: ✅ Ready for Testing  
**Project**: L2 Informatique POO - Université de Limoges

---

## 📋 Quick Start

### Prerequisites
- Java 21+
- Maven 3.6+
- Any JPG or PNG image file

### Run the Application
```bash
cd C:\Users\rmdzv\OneDrive\Bureau\imageManager
mvn clean javafx:run
```

The application should start in ~5-10 seconds.

---

## ✅ Testing Checklist

### Phase 1: Build Verification
- [ ] **Compile without errors**: `mvn clean compile`
- [ ] **No syntax errors** in Java files
- [ ] **FXML files are valid** (check in IDE)
- [ ] **Dependencies installed** (Jackson 2.17.0)

### Phase 2: Application Startup
- [ ] **Application launches** without crashing
- [ ] **Main window appears** with title "Gestionnaire d'image"
- [ ] **Three panels visible**: Library (left), Image display (center), Filters (right)
- [ ] **Status bar** shows "Prêt" (Ready)
- [ ] **No console errors** in startup logs

### Phase 3: Image Loading
Test with any JPG or PNG file from your computer.

**Steps:**
1. Click "📁 Ouvrir image" button
2. Select any image file
3. Click "Open"

**Expected Results:**
- [ ] Image appears in center panel
- [ ] Image is properly scaled (fits in window)
- [ ] Status bar updates (no errors)
- [ ] Can scroll if image is larger than window
- [ ] No "No image loaded" errors

---

## 🎨 Feature Testing

### Feature 1: Color Filters

#### Test 1a: Sepia Filter
**Steps:**
1. Load an image (see Phase 3)
2. Click "Sépia" button in filters panel
3. Observe the image

**Expected:**
- [ ] Image takes on vintage/brownish tone
- [ ] Colors shift to sepia tones
- [ ] Status bar shows success message
- [ ] Can apply again (filter stacks)

#### Test 1b: Noir & Blanc (Grayscale)
**Steps:**
1. Load an image
2. Click "Noir & Blanc" button

**Expected:**
- [ ] Image becomes grayscale
- [ ] All colors removed
- [ ] Still recognizable

#### Test 1c: RGB Swap
**Steps:**
1. Load an image with distinct colors (colorful photo)
2. Click "RGB Swap" button

**Expected:**
- [ ] Colors shift dramatically
- [ ] Red areas become green
- [ ] Green areas become blue
- [ ] Blue areas become red

#### Test 1d: Contours (Prewitt)
**Steps:**
1. Load an image with clear edges/objects
2. Click "Contours (Prewitt)" button

**Expected:**
- [ ] Image shows edge detection
- [ ] Outlines of objects visible
- [ ] Background becomes dark
- [ ] Takes 1-2 seconds to process

#### Test 1e: Multiple Filters
**Steps:**
1. Load an image
2. Click "Sépia"
3. Click "Contours (Prewitt)"
4. Click "RGB Swap"

**Expected:**
- [ ] Each filter applies on top of previous
- [ ] Final result shows all 3 effects
- [ ] Order matters (result is unique to order applied)

### Feature 2: Image Transformations

#### Test 2a: Rotate Right (↻ +90°)
**Steps:**
1. Load an image (non-square for visibility)
2. Click "↻ +90°" button

**Expected:**
- [ ] Image rotates 90° clockwise
- [ ] Dimensions swap (width ↔ height)
- [ ] Can apply multiple times (4 rotations = original)

#### Test 2b: Rotate Left (↺ -90°)
**Steps:**
1. Load an image
2. Click "↺ -90°" button

**Expected:**
- [ ] Image rotates 90° counter-clockwise
- [ ] Can apply 4 times to return to original

#### Test 2c: Mirror Horizontal (↔)
**Steps:**
1. Load an image with text or asymmetric features
2. Click "↔ Miroir H" button

**Expected:**
- [ ] Image flips left-to-right
- [ ] Text reads backwards
- [ ] Applying twice = original

#### Test 2d: Mirror Vertical (↕)
**Steps:**
1. Load an image
2. Click "↕ Miroir V" button

**Expected:**
- [ ] Image flips top-to-bottom
- [ ] Upside down appearance
- [ ] Applying twice = original

### Feature 3: Tags Management

#### Test 3a: Add Single Tag
**Steps:**
1. Load an image
2. Type "vacation" in tag input field
3. Click "+" button

**Expected:**
- [ ] Tag appears in "Tags: vacation" label
- [ ] Input field clears
- [ ] Tag stored in memory
- [ ] No errors

#### Test 3b: Add Multiple Tags
**Steps:**
1. Load an image
2. Add tags: "beach", "summer", "2025"

**Expected:**
- [ ] Each tag appears as comma-separated: "Tags: beach, summer, 2025"
- [ ] All tags visible in label
- [ ] Can add unlimited tags

#### Test 3c: Empty Tag (Edge Case)
**Steps:**
1. Load an image
2. Don't type anything
3. Click "+" button

**Expected:**
- [ ] Nothing happens (no error)
- [ ] Tag list unchanged
- [ ] No crash

#### Test 3d: Duplicate Tags (Edge Case)
**Steps:**
1. Load an image
2. Add tag "photo"
3. Add tag "photo" again

**Expected:**
- [ ] Either: Shows both, or avoids duplicates
- [ ] No crash
- [ ] Behavior is consistent

### Feature 4: Reset Functionality

#### Test 4: Reset Button
**Steps:**
1. Load an image
2. Apply filter: "Sépia"
3. Apply transform: "↻ +90°"
4. Add tag: "test"
5. Click "Réinitialiser" button

**Expected:**
- [ ] Image returns to original (undoes Sepia + rotation)
- [ ] Visual appearance reset
- [ ] Tags are STILL there (metadata not cleared)
- [ ] Status bar shows reset message

---

## 🔐 Encryption & Decryption Testing

### Test 5a: Encryption
**Steps:**
1. Load an image
2. Type password: "secret123" in password field
3. Click "Chiffrer" button

**Expected:**
- [ ] Image becomes scrambled/unreadable
- [ ] Pixels shuffled randomly
- [ ] Status bar shows success
- [ ] Original image lost (only encrypted version visible)
- [ ] Password NOT visible in UI

### Test 5b: Decryption with Correct Password
**Steps:**
1. (Continue from 5a - image is encrypted)
2. Type password: "secret123" in password field
3. Click "Déchiffrer" button

**Expected:**
- [ ] Image returns to original state
- [ ] Pixels unscrambled correctly
- [ ] Exact replica of original
- [ ] Status bar shows success
- [ ] Can now apply new filters

### Test 5c: Decryption with Wrong Password
**Steps:**
1. Load image
2. Encrypt with password: "correct123"
3. Type password: "wrong123"
4. Click "Déchiffrer"

**Expected:**
- [ ] Image unscrambles but shows garbage/random pixels
- [ ] NOT the original image
- [ ] No error message (password not validated)
- [ ] Shows that wrong password = wrong result

### Test 5d: Encryption Twice (Edge Case)
**Steps:**
1. Load image
2. Type "pass1" → Click "Chiffrer"
3. Type "pass2" → Click "Chiffrer" again

**Expected:**
- [ ] First encryption applies
- [ ] Second encryption applies to already-encrypted image
- [ ] Double-encrypted (very scrambled)
- [ ] Decrypt with "pass2" → still encrypted
- [ ] Decrypt with "pass1" → original

---

## 💾 Metadata & Save Testing

### Test 6a: Save Metadata
**Steps:**
1. Load an image
2. Apply filters: "Sépia" + "RGB Swap"
3. Apply transform: "↻ +90°"
4. Add tags: "test", "demo"
5. Click "💾 Sauvegarder Métadonnées" button

**Expected:**
- [ ] Status bar shows "✓ Metadata saved" (or similar)
- [ ] `metadata.json` file created in project root
- [ ] File is valid JSON (can open in text editor)
- [ ] Contains image path, tags, transformations

### Test 6b: Verify Metadata Content
**Steps:**
1. (Continue from 6a)
2. Open `metadata.json` in text editor

**Expected:**
- [ ] File structure is valid JSON: `{ ... }`
- [ ] Contains image path as key
- [ ] Contains tags: ["test", "demo"]
- [ ] Contains transformations: [{"name": "Sepia", ...}, ...]
- [ ] Password is NOT stored (security ✓)

### Test 6c: Multiple Images Metadata
**Steps:**
1. Load image #1 → add tags → save
2. Load image #2 → add tags → save
3. Load image #3 → add tags → save

**Expected:**
- [ ] All 3 images in metadata.json
- [ ] Each has own entry in JSON
- [ ] No data loss from previous saves
- [ ] File grows with more images

---

## 📁 Library & Search Testing

### Test 7a: Library Panel Population
**Steps:**
1. (Ensure metadata.json exists with 2+ images)
2. Look at left panel "📁 Bibliothèque d'Images"

**Expected:**
- [ ] Image list shows all images from metadata.json
- [ ] Shows filename + path
- [ ] Clear, readable format
- [ ] No duplicates

### Test 7b: Image Preview
**Steps:**
1. Ensure library has images
2. Click on first image in list

**Expected:**
- [ ] Image preview appears on right side of library panel
- [ ] Shows actual image thumbnail
- [ ] Title label shows image name
- [ ] Tags label shows image tags
- [ ] Info is accurate

### Test 7c: Tag Search - Exact Match
**Steps:**
1. Library has images with tags: "vacation", "beach", "family"
2. Type "vacation" in search field
3. Click "🔍 Chercher" button

**Expected:**
- [ ] List filters to show only images with "vacation" tag
- [ ] Status shows "Found 1 image(s)" (or actual count)
- [ ] Other images hidden
- [ ] Correct images shown

### Test 7d: Tag Search - Partial Match
**Steps:**
1. Library has images with tags: "vacation", "vacations"
2. Type "vac" in search field
3. Click "🔍 Chercher"

**Expected:**
- [ ] Both "vacation" and "vacations" images appear
- [ ] Partial matching works
- [ ] Status shows "Found 2 image(s)"

### Test 7e: Tag Search - Case Insensitive
**Steps:**
1. Library has "BEACH" tag
2. Search "beach" (lowercase)
3. Click "🔍 Chercher"

**Expected:**
- [ ] Images found despite case difference
- [ ] Case-insensitive matching works

### Test 7f: Tag Search - No Results
**Steps:**
1. Type "nonexistent_tag_xyz"
2. Click "🔍 Chercher"

**Expected:**
- [ ] List becomes empty
- [ ] Status shows "Found 0 image(s)"
- [ ] No crash

### Test 7g: Clear Search
**Steps:**
1. (Search is active, showing filtered results)
2. Click "✕ Effacer" button

**Expected:**
- [ ] All images appear again
- [ ] List shows complete library
- [ ] Status shows "Showing all images"

### Test 7h: Load Image for Editing
**Steps:**
1. Library panel shows images
2. Click on an image to preview
3. Click "📂 Charger pour éditer" button

**Expected:**
- [ ] Image loads in main editor (center panel)
- [ ] Tags from that image are populated
- [ ] Ready to apply new filters
- [ ] Previous filters NOT applied (original image)

---

## 🏗️ Build & Compilation Testing

### Test 8a: Clean Compile
```bash
mvn clean compile
```

**Expected:**
- [ ] No errors
- [ ] No warnings (or acceptable warnings)
- [ ] BUILD SUCCESS
- [ ] target/classes created

### Test 8b: Package
```bash
mvn clean package
```

**Expected:**
- [ ] Builds successfully
- [ ] JAR file created in target/
- [ ] All tests pass (if any exist)

### Test 8c: Run from IDE
- [ ] Run button in IntelliJ/Eclipse works
- [ ] Application launches from IDE
- [ ] No differences from Maven run

---

## 🐛 Edge Cases & Error Handling

### Test 9a: No Image Loaded
**Steps:**
1. Start application
2. Click "Sépia" button (without loading image)

**Expected:**
- [ ] Error message shown: "No image loaded" (or similar)
- [ ] No crash
- [ ] App remains responsive

### Test 9b: Invalid Image File
**Steps:**
1. Try to open non-image file (text file, video)
2. Select file

**Expected:**
- [ ] Either: Opens only if valid image, or shows error
- [ ] No crash
- [ ] App handles gracefully

### Test 9c: Missing metadata.json
**Steps:**
1. Delete metadata.json
2. Open library panel

**Expected:**
- [ ] Library shows empty (or "No images found")
- [ ] No crash
- [ ] Can still use editor for current image

### Test 9d: Corrupted metadata.json
**Steps:**
1. Open metadata.json
2. Delete closing brace "}"
3. Save file
4. Try to load from library

**Expected:**
- [ ] Errors handled gracefully
- [ ] Either: Shows error message, or creates new file
- [ ] Application doesn't crash

### Test 9e: Very Large Image
**Steps:**
1. Find large image (4000x3000 pixels)
2. Load and apply filters

**Expected:**
- [ ] Image loads
- [ ] Filters apply (might take 2-5 seconds)
- [ ] UI remains responsive
- [ ] No out-of-memory errors

### Test 9f: Rapid Clicking
**Steps:**
1. Load image
2. Rapidly click "Sépia" button 10+ times

**Expected:**
- [ ] Each click queues filter
- [ ] All filters apply
- [ ] No crash or lag
- [ ] UI responsive

---

## 📊 Performance Testing

### Test 10a: Filter Performance
**Benchmark**: How long does each filter take?

| Filter | Expected Time |
|--------|---|
| Sepia | < 1 second |
| Noir & Blanc | < 1 second |
| RGB Swap | < 1 second |
| Prewitt | 1-2 seconds |
| Encryption | 1-5 seconds |
| Decryption | 1-5 seconds |

**Steps:**
1. Load medium image (2000x1500)
2. Click filter and note time until complete

**Expected:**
- [ ] Times match table above (approximate)
- [ ] Larger images take proportionally longer

### Test 10b: Search Performance
**Steps:**
1. metadata.json has 20+ images
2. Type search term
3. Click search

**Expected:**
- [ ] Results appear instantly (< 100ms)
- [ ] No lag
- [ ] Smooth experience

### Test 10c: Memory Usage
**Steps:**
1. Open app → Task Manager (Windows) or Activity Monitor (Mac)
2. Note memory usage
3. Load image → Apply filters
4. Note memory usage again

**Expected:**
- [ ] Starting memory: ~100-200 MB
- [ ] After operations: ~200-400 MB (depends on image size)
- [ ] No continuous growth (memory leaks)
- [ ] Reasonable for Java application

---

## 🎯 Requirements Verification

### From Course PDF Specification

#### Application Réduite (Reduced App)
- [ ] ✅ Load images via FileChooser
- [ ] ✅ Display in JavaFX ImageView
- [ ] ✅ MVC architecture with FXML
- [ ] ✅ Status updates

#### Les transformations d'image (Image Transformations)
- [ ] ✅ Rotation (±90°)
- [ ] ✅ Symmetry/Mirror (H & V)
- [ ] ✅ Sepia filter
- [ ] ✅ Grayscale filter
- [ ] ✅ RGB Swap
- [ ] ✅ Prewitt edge detection
- [ ] ✅ Filters use inheritance & interfaces

#### Support des tags et sauvegarde (Tags & Saving)
- [ ] ✅ Tag system
- [ ] ✅ Transformation tracking
- [ ] ✅ Single metadata.json file
- [ ] ✅ Jackson library (authorized)
- [ ] ✅ Can replay transformations

#### Un peu de sécurité (Security)
- [ ] ✅ Encryption with SHA-256
- [ ] ✅ SecureRandom usage
- [ ] ✅ Password NOT stored
- [ ] ✅ Decryption support

#### Les fonctions avancées (Bonus Features)
- [ ] ✅ Image library browsing
- [ ] ✅ Tag-based search
- [ ] ✅ Image preview

---

## 📝 Test Report Template

Copy this template when documenting test results:

```
TEST REPORT - [Date]
=====================

Tester: [Name]
Test Environment: Windows/Mac/Linux, Java 21+
Build Status: PASS / FAIL

Feature Tests:
- [ ] Filters: PASS / FAIL
- [ ] Transformations: PASS / FAIL
- [ ] Tags: PASS / FAIL
- [ ] Encryption: PASS / FAIL
- [ ] Search: PASS / FAIL
- [ ] Library: PASS / FAIL

Issues Found:
1. [Issue description]
   Fix: [How to fix]

Performance:
- Average filter time: X seconds
- Memory usage: X MB

Recommendation: [APPROVE / NEEDS FIXES / REJECT]
```

---

## ✅ Final Checklist Before Submission

- [ ] All tests pass
- [ ] No compile errors
- [ ] No runtime errors
- [ ] metadata.json valid JSON
- [ ] All 4 features working (Encryption, Library, Search, Docs)
- [ ] Documentation complete
- [ ] Code compiles on command line
- [ ] Application launches without crashes
- [ ] Performance acceptable
- [ ] No unauthorized libraries

---

## 🆘 Troubleshooting

### Issue: "Unexpected end-of-input" error
**Cause**: metadata.json corrupted or incomplete  
**Fix**: Delete metadata.json and save new one

### Issue: "No image loaded" error
**Cause**: Didn't click "Ouvrir image" first  
**Fix**: Load image before applying filters

### Issue: Decryption shows garbage
**Cause**: Wrong password used  
**Fix**: Use same password as encryption

### Issue: Library is empty
**Cause**: metadata.json doesn't exist  
**Fix**: Load image and save metadata first

### Issue: Search finds no results
**Cause**: Wrong tag spelling or case issue  
**Fix**: Use partial match (e.g., "vac" for "vacation")

### Issue: FXML load exception
**Cause**: Invalid FXML or missing controller  
**Fix**: Check filter.fxml and library.fxml for errors

### Issue: Out of memory
**Cause**: Very large image  
**Fix**: Use smaller image or increase heap size

---

## 📞 Support

For issues:
1. Check the troubleshooting section above
2. Check README.md for quick reference
3. Check DOCUMENTATION_INDEX.md for architecture details

---

**Testing Version**: 1.0  
**Last Updated**: April 23, 2026  
**Project Status**: ✅ Ready for Full Testing  
**University**: Université de Limoges - L2 Informatique
