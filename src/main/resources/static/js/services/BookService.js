import api from "./api";
import MockDataService from "./MockDataService";

// Option de configuration pour activer les données fictives en cas d'erreur
const USE_MOCK_DATA_ON_ERROR = true;

// Fonction utilitaire pour corriger les URLs d'images
function fixImageUrl(url) {
  if (!url) return "";

  console.log("🔍 URL d'image à corriger:", url);

  // Si l'URL commence par /api/, supprimer ce préfixe
  if (url.startsWith("/api/")) {
    url = url.substring(4);
    console.log("  - Après suppression du préfixe /api/:", url);
  }

  // Corriger le double chemin si présent
  // Cas 1: /uploads/book-covers/uploads/book-covers/
  if (url.includes("/uploads/book-covers/uploads/book-covers/")) {
    url = url.replace(
      "/uploads/book-covers/uploads/book-covers/",
      "/uploads/book-covers/"
    );
    console.log("  - Après correction du doublon (cas 1):", url);
  }

  // Cas 2: uploads/book-covers/uploads/book-covers/
  if (url.includes("uploads/book-covers/uploads/book-covers/")) {
    url = url.replace(
      "uploads/book-covers/uploads/book-covers/",
      "uploads/book-covers/"
    );
    console.log("  - Après correction du doublon (cas 2):", url);
  }

  // S'assurer que l'URL commence par /uploads/book-covers/ si elle contient seulement le nom du fichier
  if (url.match(/^[^\/]+_\d+\.(jpg|jpeg|png|gif)$/i)) {
    url = "/uploads/book-covers/" + url;
    console.log("  - Après ajout du préfixe pour le nom de fichier seul:", url);
  }

  // Commencer par un slash si ce n'est pas le cas et que ce n'est pas une URL complète
  if (!url.startsWith("/") && !url.startsWith("http")) {
    url = "/" + url;
    console.log("  - Après ajout du slash initial:", url);
  }

  console.log("✅ URL d'image finale corrigée:", url);
  return url;
}

class BookService {
  // Récupérer tous les livres avec pagination
  async getBooks(page = 0, size = 10, sort = "title,asc") {
    try {
      console.log("📚 Tentative de récupération des livres depuis l'API...");
      const response = await api.get(
        `/books?page=${page}&size=${size}&sort=${sort}`
      );
      console.log("✅ Livres récupérés avec succès depuis l'API");

      // Corriger les URLs d'images
      if (response.data && response.data.content) {
        response.data.content.forEach((book) => {
          if (book.picture) {
            book.picture = fixImageUrl(book.picture);
          }
        });
      }

      return response.data;
    } catch (error) {
      console.error("❌ Erreur lors de la récupération des livres:", error);

      // Solution de secours en utilisant fetch
      try {
        console.log(
          "🔄 Tentative de récupération des livres avec fetch comme solution de secours"
        );
        const response = await fetch(
          `${api.defaults.baseURL}/books?page=${page}&size=${size}&sort=${sort}`,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
              "Content-Type": "application/json",
            },
            credentials: "include",
          }
        );

        if (!response.ok) {
          console.error(
            `❌ Échec de la solution de secours fetch pour livres: ${response.status}`
          );

          // Utiliser les données fictives si activé
          if (USE_MOCK_DATA_ON_ERROR) {
            console.log("🧪 Utilisation des données fictives pour les livres");
            return MockDataService.getBooks(page, size);
          }

          return {
            content: [],
            totalElements: 0,
            totalPages: 0,
            size: size,
            number: page,
          };
        }

        const data = await response.json();

        // Corriger les URLs d'images
        if (data && data.content) {
          data.content.forEach((book) => {
            if (book.picture) {
              book.picture = fixImageUrl(book.picture);
            }
          });
        }

        return data;
      } catch (fetchError) {
        console.error(
          "❌ Échec complet de la récupération des livres:",
          fetchError
        );

        // Utiliser les données fictives si activé
        if (USE_MOCK_DATA_ON_ERROR) {
          console.log("🧪 Utilisation des données fictives pour les livres");
          return MockDataService.getBooks(page, size);
        }

        // Retourner une structure par défaut en cas d'erreur pour éviter les erreurs en cascade
        return {
          content: [],
          totalElements: 0,
          totalPages: 0,
          size: size,
          number: page,
        };
      }
    }
  }

  // Récupérer les nouvelles parutions
  async getNewReleases(limit = 5) {
    try {
      const response = await api.get(`/books/new-releases?limit=${limit}`);
      return response.data;
    } catch (error) {
      console.error(
        "Erreur lors du chargement des nouvelles parutions:",
        error
      );
      throw error;
    }
  }

  // Récupérer les livres populaires
  async getPopularBooks(limit = 5) {
    try {
      const response = await api.get(`/books/popular?limit=${limit}`);
      return response.data;
    } catch (error) {
      console.error("Erreur lors du chargement des livres populaires:", error);
      throw error;
    }
  }

  // Récupérer un livre par son ID
  async getBookById(id) {
    try {
      const response = await api.get(`/books/${id}`);

      // Corriger l'URL de l'image
      if (response.data && response.data.picture) {
        response.data.picture = fixImageUrl(response.data.picture);
      }

      return response.data;
    } catch (error) {
      console.error(`Erreur lors de la récupération du livre ${id}:`, error);
      throw error;
    }
  }

  // Créer un nouveau livre
  async createBook(book) {
    try {
      const response = await api.post("/books", book);
      return response.data;
    } catch (error) {
      console.error("Erreur lors de la création du livre:", error);
      throw error;
    }
  }

  // Mettre à jour un livre
  async updateBook(id, book) {
    try {
      const response = await api.put(`/books/${id}`, book);
      return response.data;
    } catch (error) {
      console.error(`Erreur lors de la mise à jour du livre ID=${id}:`, error);
      throw error;
    }
  }

  // Supprimer un livre
  async deleteBook(id) {
    try {
      await api.delete(`/books/${id}`);
      return true;
    } catch (error) {
      console.error(`Erreur lors de la suppression du livre ID=${id}:`, error);
      throw error;
    }
  }

  // Rechercher des livres par titre
  async searchBooksByTitle(title) {
    try {
      const response = await api.get(
        `/books/search/title?title=${encodeURIComponent(title)}`
      );
      return response.data;
    } catch (error) {
      console.error("Erreur lors de la recherche de livres par titre:", error);
      throw error;
    }
  }

  // Rechercher des livres par auteur
  async searchBooksByAuthor(lastName, firstName = "") {
    try {
      const response = await api.get(
        `/books/search/author?lastName=${encodeURIComponent(
          lastName
        )}&firstName=${encodeURIComponent(firstName)}`
      );
      return response.data;
    } catch (error) {
      console.error("Erreur lors de la recherche de livres par auteur:", error);
      throw error;
    }
  }

  // Rechercher des livres par catégorie
  async searchBooksByCategory(categoryName) {
    try {
      const response = await api.get(
        `/books/search/category?categoryName=${encodeURIComponent(
          categoryName
        )}`
      );
      return response.data;
    } catch (error) {
      console.error(
        "Erreur lors de la recherche de livres par catégorie:",
        error
      );
      throw error;
    }
  }

  // Ajouter une catégorie à un livre
  async addCategoryToBook(bookId, categoryId) {
    try {
      const response = await api.post(
        `/books/${bookId}/categories/${categoryId}`
      );
      return response.data;
    } catch (error) {
      console.error("Erreur lors de l'ajout d'une catégorie au livre:", error);
      throw error;
    }
  }

  // Supprimer une catégorie d'un livre
  async removeCategoryFromBook(bookId, categoryId) {
    try {
      const response = await api.delete(
        `/books/${bookId}/categories/${categoryId}`
      );
      return response.data;
    } catch (error) {
      console.error(
        "Erreur lors de la suppression d'une catégorie du livre:",
        error
      );
      throw error;
    }
  }

  // Upload d'une image pour un livre
  async uploadBookImage(bookId, file) {
    try {
      console.log("📸 Tentative d'upload d'image pour le livre", bookId);
      console.log("📄 Détails du fichier:", {
        type: file.type,
        size: file.size,
        name: file.name,
      });

      // Vérifier si le fichier est valide
      if (!file) {
        throw new Error("Aucun fichier sélectionné");
      }

      // Vérifier le type de fichier
      const allowedTypes = ["image/png", "image/jpeg", "image/jpg"];
      if (!allowedTypes.includes(file.type.toLowerCase())) {
        throw new Error("Seuls les fichiers PNG, JPEG et JPG sont acceptés");
      }

      // Vérifier la taille minimale (1 Ko)
      if (file.size < 1024) {
        throw new Error("L'image doit faire au moins 1 Ko");
      }

      // Vérifier la taille maximale (10 Mo)
      if (file.size > 10 * 1024 * 1024) {
        throw new Error("L'image ne doit pas dépasser 10 Mo");
      }

      const formData = new FormData();
      formData.append("file", file);

      // Obtenir l'URL de base de l'API
      const baseURL = api.defaults.baseURL;
      const uploadURL = `${baseURL}/books/${bookId}/image`;
      console.log("📤 Envoi de la requête d'upload à:", uploadURL);

      const response = await fetch(uploadURL, {
        method: "POST",
        body: formData,
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        credentials: "include",
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error("❌ Réponse d'erreur:", response.status, errorText);
        throw new Error(
          `Erreur lors de l'upload: ${response.status} ${response.statusText}`
        );
      }

      const imageUrl = await response.text();
      console.log("✅ URL retournée par le serveur:", imageUrl);

      // S'assurer que l'URL ne contient pas le préfixe /api/ pour les images
      const fixedImageUrl = fixImageUrl(imageUrl);
      console.log("🖼️ URL d'image finale après correction:", fixedImageUrl);

      return fixedImageUrl;
    } catch (error) {
      console.error("❌ Erreur lors de l'upload de l'image:", error);
      throw error;
    }
  }

  // Obtenir l'URL correcte de l'image d'un livre
  getBookCoverUrl(imagePath) {
    // Si aucune image n'est définie
    if (!imagePath || typeof imagePath !== "string") {
      try {
        return require("@/assets/images/default-cover.jpg");
      } catch (error) {
        console.error(
          "Erreur lors du chargement de l'image par défaut:",
          error
        );
        return ""; // Retourner une chaîne vide en cas d'erreur
      }
    }

    // L'image a été trouvée dans la BDD
    return `/api/uploads/book-covers/${imagePath.split("/").pop()}`;
  }
}

export default new BookService();
