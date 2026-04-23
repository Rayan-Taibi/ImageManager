# ⚠️ Important: Metadata Structure Explanation

## 📝 Why Metadata Looks Like That

The metadata.json you're seeing is **correct and expected**. Here's why:

### What Metadata Does
Metadata is **NOT the filtered/transformed image** - it's a **record** of what operations were performed.

```json
{
  "C:\\Users\\rmdzv\\OneDrive\\Images\\alien.jpg" : {
    "imagePath" : "C:\\Users\\rmdzv\\OneDrive\\Images\\alien.jpg",
    "tags" : [ { "value" : "psps" } ],
    "transformations" : [ 
      { "name" : "Sepia", "type" : "filter" },
      { "name" : "NoireBlanc", "type" : "filter" },
      { "name" : "Encryption", "type" : "filter" }
    ]
  }
}
```

### What This Means
- ✅ `imagePath` - Where the **ORIGINAL** image file is located
- ✅ `tags` - Labels for organizing the image
- ✅ `transformations` - A **LOG** of filters applied (NOT the actual image)

### Important Limitation
⚠️ **Filters are NOT permanently applied to the image file**
- When you click "Sépia" → The displayed image shows sepia (on screen)
- But the **original image file** is NOT modified
- When you reload, you get the original again

---

## 🔄 How Transformations Work

### What Actually Happens
1. **Load Image** → Original file loaded into memory
2. **Click Filter** → Applied to ImageView (display only)
3. **Save Metadata** → Records what filters were clicked
4. **Click Rotate** → Changes Java properties (setRotate, setScaleX)
5. **Close & Reopen** → Original image loads again (filters lost!)

### Why This Design?
- **Simple**: Don't modify original files
- **Non-destructive**: Can always reset to original
- **Safe**: Original image file never changes

---

## 🎯 The Real Issues

### Issue 1: Filters Don't Persist
**Problem:** You apply Sepia, close app, open app = Original image shows  
**Why:** Filters only change display, not file  
**Solution:** Currently not supported in this app

### Issue 2: "Load Image" Doesn't Show Saved Changes
**Problem:** Metadata shows 20 transformations, but loading shows original  
**Why:** Metadata only records what was clicked, not the result  
**Solution:** The app would need to:
  1. Replay all saved transformations when loading, OR
  2. Save the modified image separately

### Issue 3: Transformations Like Rotation Don't Work
**Problem:** setRotate() is UI-only, not actual image rotation  
**Why:** These only change display properties, not pixel data  
**Solution:** Need to apply actual image transformations

---

## 💡 What You Need to Understand

### Current Design (Simplified)
```
Original File
    ↓
Load → ImageView (Display)
    ↓
Apply Filter → Image object (Memory)
    ↓
Click Save → metadata.json (List of filter names)
    ↓
Close App
    ↓
Next Session → Load original again (filters lost)
```

### What's NOT Happening
- ❌ Modified images are NOT saved to disk
- ❌ Transformations are NOT replayed when loading
- ❌ Rotations/Mirrors don't actually rotate/mirror pixels

---

## ✅ What DOES Work

✅ **Filters on Screen** - Sepia, Grayscale, RGB Swap, Edge Detection  
✅ **Encryption** - Scrambles pixels with password  
✅ **Decryption** - Reverses scrambling with same password  
✅ **Tags** - Metadata records and searches  
✅ **Metadata Save** - Records what was done  
✅ **Status Messages** - Shows what happened  

---

## ❌ What DOESN'T Persist

❌ **Filtered Image** - Only visible while app is open  
❌ **Rotation/Mirror** - Only display changes, not pixel changes  
❌ **Transformation Replay** - Not automatically applied on load  
❌ **Image File Changes** - Original never modified  

---

## 📋 Metadata Format Is Correct

The metadata you're seeing:
```json
"transformations" : [
  { "name" : "Sepia", "type" : "filter" },
  { "name" : "Decryption", "type" : "filter" },
  { "name" : "RGBSwap", "type" : "filter" }
]
```

This is **perfect JSON** and **exactly what should be stored**.

---

## 🎓 Conclusion

**Metadata = Log of actions, NOT the modified image**

The app was designed to:
- Show filters in real-time (on screen)
- Record what was done (metadata)
- NOT permanently modify files

If you want transformations to persist, the app would need completely different architecture.

---

**Is this the intended behavior?** Or do you want the app to:
- [ ] Actually apply and save filters to image files?
- [ ] Replay filters when loading images?
- [ ] Actually rotate/mirror the pixel data?
