# 🖼️ Image Manager

**A simple, user-friendly image editor with filters, tagging, and encryption.**

---

## ⚡ Quick Start

```bash
mvn clean javafx:run
```

---

## ✨ Features

| Feature | What it does |
|---------|-------------|
| 📸 **Filters** | Sepia, Grayscale, RGB Swap, Edge Detection |
| 🔄 **Transform** | Rotate (-90° / +90°), Flip (H/V) |
| 🏷️ **Tags** | Organize and search images |
| 🔐 **Encryption** | Password-protect your images |
| 📚 **Library** | Browse all images with metadata |

---

## 📖 How to Use

1. **Load an image** → Click "📁 Ouvrir image"
2. **Apply effects** → Choose filter or transformation
3. **Add tags** → Type name, click "+"
4. **Encrypt** (optional) → Enter password, click "Chiffrer"
5. **Save metadata** → Click "💾 Sauvegarder Métadonnées"
6. **Browse library** → View all images and search by tags

---

## ❓ Troubleshooting

| Issue | Solution |
|-------|----------|
| No image appears | Load an image first via "📁 Ouvrir image" |
| Metadata not found | It's created on first save (normal) |
| Decryption fails | Check your password is correct |
| Image missing from library | File was moved/deleted - reload it |

---

## 📁 File Storage

- **Metadata**: `metadata.json` (in project root)
- **Saves**: Tags and transformations only (images not exported)

---

## ℹ️ Info

**Course**: L2 Informatique POO | **University**: Université de Limoges | **Year**: 2025-2026
