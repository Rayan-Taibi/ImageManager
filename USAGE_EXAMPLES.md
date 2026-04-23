# 📖 Image Manager - Usage Examples

## Example 1: Basic Image Editing

### Scenario: Edit a vacation photo

**Step 1**: Start application
```
mvn clean javafx:run
```

**Step 2**: Load image
- Click "📁 Ouvrir image"
- Select `vacation_photo.jpg`
- Image appears in center

**Step 3**: Apply filters
- Click "Sépia" → Photo gets vintage look
- Click "RGB Swap" → Colors shift
- Result: Two filters applied and recorded

**Step 4**: Save metadata
- Click "💾 Sauvegarder Métadonnées"
- Status: "✓ Metadata saved"
- File created: `metadata.json`

**Result**: Metadata file contains:
```json
{
  "/path/to/vacation_photo.jpg": {
    "imagePath": "/path/to/vacation_photo.jpg",
    "tags": [],
    "transformations": [
      {"name": "Sepia", "type": "filter"},
      {"name": "RGBSwap", "type": "filter"}
    ]
  }
}
```

---

## Example 2: Organizing with Tags

### Scenario: Tag photos for later search

**Step 1**: Load first image
- Click "📁 Ouvrir image"
- Select `beach_sunset.jpg`

**Step 2**: Add tags
- Type "beach" in tag input field
- Click "+"
- Tags label shows: "Tags: beach"
- Type "vacation"
- Click "+"
- Tags label shows: "Tags: beach, vacation"

**Step 3**: Apply filter and save
- Click "Noir & Blanc"
- Click "💾 Sauvegarder Métadonnées"

**Step 4**: Load another image
- Click "📁 Ouvrir image"
- Select `family_photo.jpg`

**Step 5**: Add different tags
- Add tags: "family", "important"
- Click "💾 Sauvegarder Métadonnées"

**Result**: Metadata file now has:
```json
{
  "/path/beach_sunset.jpg": {
    "tags": ["beach", "vacation"],
    "transformations": [{"name": "NoireBlanc", "type": "filter"}]
  },
  "/path/family_photo.jpg": {
    "tags": ["family", "important"],
    "transformations": []
  }
}
```

---

## Example 3: Encrypting Sensitive Images

### Scenario: Protect a confidential document photo

**Step 1**: Load image
- Click "📁 Ouvrir image"
- Select `secret_document.jpg`

**Step 2**: Encrypt with password
- Type password: `MySecret123` in password field
- Click "Chiffrer"
- Status: "✓ Image encrypted"
- Image pixels are scrambled
- Photo now unreadable without password

**Step 3**: Save (password NOT stored)
- Click "💾 Sauvegarder Métadonnées"
- Metadata shows: `{"name": "Encryption", "type": "filter"}`
- Password is NOT in the file (security ✅)

**Later**: Decrypt the image
- Reload encrypted image
- Type password: `MySecret123` 
- Click "Déchiffrer"
- Status: "✓ Image decrypted"
- Photo is readable again

**Wrong password?**
- Type wrong password: `WrongPass`
- Click "Déchiffrer"
- Result: Random garbage image (wrong shuffle)
- Status: Still shows success (password not validated)

---

## Example 4: Using the Library

### Scenario: Find all "vacation" photos

**Step 1**: Open left panel
- Left side shows "📁 Bibliothèque d'Images"
- All your previously edited images listed:
  ```
  beach_sunset.jpg (/path/to/)
  vacation_photo.jpg (/path/to/)
  family_photo.jpg (/path/to/)
  ```

**Step 2**: Search by tag
- Type "vacation" in search field
- Click "🔍 Chercher"
- Results: Shows 2 images
  ```
  beach_sunset.jpg
  vacation_photo.jpg
  ```
- Status: "Found 2 image(s) with tag: 'vacation'"

**Step 3**: View image details
- Click on `beach_sunset.jpg`
- Preview appears on right
- Tags displayed: "Tags: beach, vacation"
- Title shows: "beach_sunset.jpg"

**Step 4**: Load for editing
- Click "📂 Charger pour éditer"
- Image loads in main editor
- You can now apply new filters
- Status: "✓ Image loaded for editing: beach_sunset.jpg"

**Step 5**: Clear search
- Click "✕ Effacer"
- All images appear again
- Status: "Showing all images"

---

## Example 5: Complex Workflow

### Scenario: Complete photo editing & archiving

**Step 1**: Load vacation album photo
```
1. Click "📁 Ouvrir image"
2. Select "summer_2025.jpg"
```

**Step 2**: Edit the photo
```
3. Click "↻ +90°" → Rotate right
4. Click "Sépia" → Add vintage filter
5. Click "Contours (Prewitt)" → Show edges
```

**Step 3**: Tag for organization
```
6. Type "summer" + click "+"
7. Type "2025" + click "+"
8. Type "family" + click "+"
```

**Step 4**: Encrypt important moments
```
9. Type password: "SummerMemories2025"
10. Click "Chiffrer"
```

**Step 5**: Save everything
```
11. Click "💾 Sauvegarder Métadonnées"
```

**Step 6**: Later - find and view
```
12. Open Library (left panel)
13. Type "summer" in search
14. Find "summer_2025.jpg"
15. Click to preview
16. See tags & transformations
17. Click "📂 Charger pour éditer"
18. Enter password "SummerMemories2025"
19. Click "Déchiffrer"
20. Photo is readable, continue editing
```

---

## Example 6: Search Features

### Scenario: Different search use cases

**Case 1**: Exact tag match
- Search: "family"
- Result: All images with "family" tag

**Case 2**: Partial match
- Search: "fam"
- Result: Images with "family" (partial match works!)

**Case 3**: Case insensitive
- Search: "FAMILY"
- Search: "family"
- Result: Same images (both work)

**Case 4**: No results
- Search: "nonexistent"
- Result: "Found 0 image(s)"

**Case 5**: Multiple tags (search one)
- Search: "beach"
- Result: Shows all images with "beach"
  - Even if they also have "vacation"

---

## Troubleshooting Examples

### Issue: "Error: No image loaded"
**Cause**: Haven't clicked "📁 Ouvrir image" yet
**Fix**: Click button first, then apply filters

### Issue: Decryption shows garbage
**Cause**: Wrong password used
**Fix**: Try again with correct password from when you encrypted

### Issue: Tags not appearing
**Cause**: Didn't save metadata
**Fix**: Click "💾 Sauvegarder Métadonnées" after adding tags

### Issue: Library is empty
**Cause**: No metadata.json file created yet
**Fix**: Load an image and click save, then library will populate

### Issue: Search shows no results
**Cause**: Wrong tag spelling
**Fix**: Check spelling, try partial match (e.g., "vac" for "vacation")

---

## Pro Tips

### Tip 1: Password strategies
- Use same password for all personal images
- Different password for confidential docs
- Write it down! (You can't recover it)

### Tip 2: Tagging strategy
- Use consistent tags: "2025", "family", "work"
- Avoid: "good", "nice" (not searchable)
- Use: "location", "people", "year"

### Tip 3: Transformation order matters
- Rotation before sepia → different result
- Sepia before rotation → different result
- Metadata records order → can replay exactly

### Tip 4: Use library before editing
- Search for image
- Preview with tags
- Then load for editing
- Saves time finding right photo

### Tip 5: Reset button
- Click "Réinitialiser" to undo ALL changes
- Goes back to original image
- Transformations not lost in metadata
- Only visual undo

---

## File System After Usage

```
imageManager/
├── metadata.json              ← Created after first save
│   (contains all tags & transforms)
│
├── README.md
├── PROJECT_SUMMARY.md
├── COMPLETION_CHECKLIST.md
└── src/...
```

**Size**: metadata.json grows as you add images (typically 1-10 KB per 10 images)

---

## Performance Tips

- ✅ Metadata loads instantly (JSON is small)
- ✅ Filters apply in real-time (<1 second for typical images)
- ✅ Encryption/Decryption: 1-5 seconds (depends on image size)
- ✅ Search: Instant (<100ms)
- ✅ Library loading: <500ms

---

**Tips Version**: 1.0  
**Last Updated**: April 2026
