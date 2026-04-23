# 🧪 TESTING GUIDE - Complete Workflow

**Last Updated**: April 23, 2026  
**Status**: Ready for Testing After Fixes  
**Critical Fixes Applied**: UI buttons, transformation replay, encryption/decryption

---

## 📋 Quick Start

### Prerequisites
- Java 21+
- Maven 3.6+
- Any JPG or PNG image

### Run Application
```bash
cd c:\Users\rmdzv\OneDrive\Bureau\imageManager
mvn clean javafx:run
```

The application should start in 5-10 seconds.

---

## ✅ CRITICAL TEST SEQUENCE

### Phase 1: Verify UI Elements Are Present

**Expected Result**: Application launches and shows all buttons

1. **Check Filter Buttons**
   - [ ] "Sépia" button visible
   - [ ] "Noir & Blanc" button visible
   - [ ] "RGB Swap" button visible
   - [ ] "Contours (Prewitt)" button visible

2. **Check Transformation Buttons**
   - [ ] "↺ -90°" button visible
   - [ ] "↻ +90°" button visible
   - [ ] "↔ Miroir H" button visible
   - [ ] "↕ Miroir V" button visible

3. **Check Security Buttons**
   - [ ] Password input field visible
   - [ ] "Chiffrer" button visible
   - [ ] **"Déchiffrer" button visible** ← THIS WAS MISSING
   - [ ] Status shows ready message

4. **Check Tag Buttons**
   - [ ] Tag input field visible
   - [ ] "+" button visible
   - [ ] "Tags: (none)" label visible

5. **Check Metadata Buttons**
   - [ ] "💾 Sauvegarder Métadonnées" button visible
   - [ ] "Réinitialiser" button visible

---

### Phase 2: Test Transformation Replay (NEW FEATURE)

**This is the MOST IMPORTANT test** - verifies spec compliance.

#### Setup
1. Click "📁 Ouvrir image" button
2. Select any image file (e.g., `alien.jpg`)
3. Image should appear in center panel

#### Test Steps

**Step 1: Apply Filter**
```
1. Click "Sépia" button
   Expected: Image turns brownish/sepia tone
   Status bar shows: "✓ Sepia filter applied"
```

**Step 2: Add Tag**
```
1. Type "test-image" in tag input field
2. Click "+" button
   Expected: Tags label shows "Tags: test-image"
   Status bar shows: "✓ Tag added: test-image"
```

**Step 3: Save Metadata**
```
1. Click "💾 Sauvegarder Métadonnées"
   Expected: Status bar shows "✓ Metadata saved"
   File created/updated: metadata.json
```

**Step 4: Close Application**
```
1. Close the window completely
   Expected: All in-memory data lost
```

**Step 5: Reopen Application** ← THIS IS THE CRITICAL TEST
```
1. Run: mvn clean javafx:run
   Application starts
```

**Step 6: Load Same Image Again**
```
1. Click "📁 Ouvrir image"
2. Select the SAME image file
3. DON'T CLICK ANY BUTTONS
   
   Expected Result:
   - [ ] Image displays
   - [ ] Image is ALREADY SEPIA (filter auto-applied!)
   - [ ] Tags show "Tags: test-image"
   - [ ] Status bar shows "✓ Transformations loaded and applied"
   
   THIS PROVES TRANSFORMATION REPLAY WORKS!
```

**If you see the original image WITHOUT sepia, then transformation replay failed.**

---

### Phase 3: Test Encryption/Decryption

#### Setup
1. Load fresh image (not encrypted)
2. Visually note the original colors

#### Test Sequence

**Step 1: Encrypt Image**
```
1. Type password: "secret123" in password field
2. Click "Chiffrer" button
   Expected: Image becomes scrambled/random pixels
   Status bar shows: "✓ Image encrypted"
   Password field clears
```

**Step 2: Attempt Decrypt with WRONG Password**
```
1. Type wrong password: "wrong" in password field
2. Click "Déchiffrer" button
   Expected: Image stays scrambled (not restored)
   This verifies password is checked
```

**Step 3: Decrypt with CORRECT Password**
```
1. Type correct password: "secret123" in password field
2. Click "Déchiffrer" button
   
   Expected: Image returns to ORIGINAL (or very close)
   Status bar shows: "✓ Image decrypted"
   Password field clears
   
   THIS PROVES ENCRYPTION/DECRYPTION NOW WORKS!
```

**If decryption doesn't restore the original, encryption is still broken.**

---

### Phase 4: Test Combined Features

#### Scenario: Apply multiple transformations

```
1. Load fresh image

2. Click "Sépia"
   → Image becomes sepia

3. Click "RGB Swap"
   → Image colors change further

4. Type tag "modified"
   Click "+"
   → Tag added

5. Click "💾 Sauvegarder Métadonnées"
   → Metadata saved with 2 filters + 1 tag

6. Close application

7. Reopen and load same image
   
   Expected:
   - Sepia applied
   - RGB Swap applied on top
   - Tags show "Tags: modified"
   - Status shows "✓ Transformations loaded and applied"
```

---

### Phase 5: Verify metadata.json Structure

Open `metadata.json` file and verify structure:

```json
{
  "C:\\Users\\...\\image.jpg" : {
    "imagePath" : "C:\\Users\\...\\image.jpg",
    "tags" : [ { "value" : "test-image" } ],
    "transformations" : [
      { "name" : "Sepia", "type" : "filter" },
      { "name" : "RGBSwap", "type" : "filter" }
    ]
  }
}
```

**Verify**:
- [ ] Path is REAL file path (not timestamp)
- [ ] Tags array contains correct values
- [ ] Transformations are in order applied
- [ ] No password stored anywhere
- [ ] Valid JSON format

---

## 🔧 Troubleshooting

### Problem: "Déchiffrer" button not visible
**Solution**: Run `mvn clean compile` to rebuild with updated FXML

### Problem: Transformation replay not working
**Cause**: Check that `metadata.json` was saved correctly
**Solution**: 
1. Verify metadata.json exists and has transformations
2. Check status bar for "✓ Metadata saved"

### Problem: Decryption doesn't restore image
**Cause**: Still using wrong random seed mechanism
**Solution**: Rebuild with updated EncryptionFilter/DecryptionFilter

### Problem: Compilation fails
**Run this**:
```bash
mvn clean compile -X
```
This shows detailed error messages.

---

## ✅ Final Checklist

- [ ] Application launches without FXML errors
- [ ] All UI buttons visible
- [ ] Filters apply and show visually
- [ ] Tags can be added and saved
- [ ] Metadata saves to JSON
- [ ] Metadata structure is valid
- [ ] **Transformation replay works** (critical!)
- [ ] Encryption scrambles image
- [ ] Decryption with correct password restores original
- [ ] Tags load when reopening image
- [ ] No errors in console output

---

## 📊 Success Criteria

**✅ PASS if**:
1. Application launches
2. UI buttons all visible
3. Transformation replay works (Sepia applied auto on reload)
4. Encryption/Decryption works (correct password restores original)
5. Tags persist and load
6. metadata.json has valid structure

**❌ FAIL if**:
1. Application crashes on startup
2. Buttons missing (especially Déchiffrer)
3. Transformation replay doesn't work
4. Decryption can't restore original image
5. metadata.json malformed

---

## 📝 How to Report Issues

If tests fail, provide:
1. **Which test failed** (phase + step number)
2. **What you expected** (from checklist)
3. **What actually happened** (with screenshot/description)
4. **Console error messages** (copy full error)
5. **metadata.json content** (if relevant)

Then I can debug and fix quickly!

---

**Created**: Session Fix Applied  
**For**: Comprehensive Testing  
**Status**: Ready to Execute
