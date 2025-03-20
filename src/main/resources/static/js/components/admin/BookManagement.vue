<template>
  <div class="book-management">
    <h2>Gestion des Livres</h2>
    
    <!-- Barre d'outils -->
    <div class="toolbar">
      <button @click="showAddForm" class="btn btn-primary">
        <i class="fas fa-plus"></i> Ajouter un livre
      </button>
      <div class="search-bar">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="Rechercher un livre..." 
          @input="searchBooks"
        >
        <select v-model="searchType">
          <option value="title">Par titre</option>
          <option value="author">Par auteur</option>
          <option value="category">Par catégorie</option>
        </select>
      </div>
    </div>

    <!-- Tableau des livres -->
    <div class="table-responsive">
      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Couverture</th>
            <th scope="col">Titre</th>
            <th scope="col">ISBN</th>
            <th scope="col">Auteur</th>
            <th scope="col">Éditeur</th>
            <th scope="col">Prix</th>
            <th scope="col">Stock</th>
            <th scope="col">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="book in books" :key="book.id">
            <td>{{ book.id }}</td>
            <td>
              <img 
                :src="book.picture || '/img/book-placeholder.png'" 
                alt="Couverture"
                class="thumbnail"
              >
            </td>
            <td>{{ book.title }}</td>
            <td>{{ book.isbn }}</td>
            <td>{{ getAuthorName(book) }}</td>
            <td>{{ book.editor?.name || 'Non spécifié' }}</td>
            <td>{{ book.price ? formatPrice(book.price) : 'Non défini' }}</td>
            <td>{{ book.stock }}</td>
            <td class="action-buttons">
              <button @click="editBook(book)" class="btn btn-sm btn-info">
                <i class="fas fa-edit"></i>
              </button>
              <button @click="showDeleteModal(book)" class="btn btn-sm btn-danger">
                <i class="fas fa-trash"></i>
              </button>
              <button @click="viewDetails(book)" class="btn btn-sm btn-primary">
                <i class="fas fa-eye"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination-controls">
      <button 
        @click="prevPage" 
        :disabled="currentPage === 0" 
        class="btn btn-outline-primary"
      >
        <i class="fas fa-chevron-left"></i> Précédent
      </button>
      <span>Page {{ currentPage + 1 }} sur {{ totalPages }}</span>
      <button 
        @click="nextPage" 
        :disabled="currentPage >= totalPages - 1" 
        class="btn btn-outline-primary"
      >
        Suivant <i class="fas fa-chevron-right"></i>
      </button>
    </div>

    <!-- Modal d'ajout/modification -->
    <div class="modal" :class="{ 'show': showFormModal }" v-if="showFormModal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ isEditMode ? 'Modifier' : 'Ajouter' }} un livre</h5>
            <button @click="closeForm" type="button" class="close">
              <span>&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveBook">
              <div class="form-row">
                <div class="form-group col-md-6">
                  <label for="title">Titre <span class="required">*</span></label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="title" 
                    v-model="currentBook.title" 
                    required
                  >
                </div>
                <div class="form-group col-md-6">
                  <label for="isbn">ISBN</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    id="isbn" 
                    v-model="currentBook.ISBN"
                  >
                </div>
              </div>
              
              <div class="form-row">
                <div class="form-group col-md-6">
                  <label for="price">Prix <span class="required">*</span></label>
                  <input 
                    type="number" 
                    step="0.01" 
                    class="form-control" 
                    id="price" 
                    v-model="currentBook.price" 
                    required
                  >
                </div>
                <div class="form-group col-md-6">
                  <label for="stock">Stock</label>
                  <input 
                    type="number" 
                    class="form-control" 
                    id="stock" 
                    v-model="currentBook.stock"
                  >
                </div>
              </div>
              
              <div class="form-row">
                <div class="form-group col-md-6">
                  <label for="author">Auteur</label>
                  <select 
                    class="form-control" 
                    id="author" 
                    v-model="currentBook.author"
                  >
                    <option :value="null">Sélectionner un auteur</option>
                    <option 
                      v-for="author in authors" 
                      :key="author.id" 
                      :value="{ id: author.id }"
                    >
                      {{ author.firstName }} {{ author.lastName }}
                    </option>
                  </select>
                </div>
                <div class="form-group col-md-6">
                  <label for="editor">Éditeur</label>
                  <select 
                    class="form-control" 
                    id="editor" 
                    v-model="currentBook.editor"
                  >
                    <option :value="null">Sélectionner un éditeur</option>
                    <option 
                      v-for="editor in editors" 
                      :key="editor.id" 
                      :value="{ id: editor.id }"
                    >
                      {{ editor.name }}
                    </option>
                  </select>
                </div>
              </div>
              
              <div class="form-group">
                <label for="detail">Description</label>
                <textarea 
                  class="form-control" 
                  id="detail" 
                  rows="3" 
                  v-model="currentBook.detail"
                ></textarea>
              </div>
              
              <div class="form-group">
                <label for="picture">URL de l'image</label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="picture" 
                  v-model="currentBook.picture"
                >
              </div>
              
              <div class="form-group">
                <label>Catégories</label>
                <div class="categories-selector">
                  <div 
                    v-for="category in categories" 
                    :key="category.id" 
                    class="form-check"
                  >
                    <input 
                      type="checkbox" 
                      class="form-check-input" 
                      :id="'category-' + category.id"
                      :value="category.id"
                      v-model="selectedCategories"
                    >
                    <label 
                      class="form-check-label" 
                      :for="'category-' + category.id"
                    >
                      {{ category.name }}
                    </label>
                  </div>
                </div>
              </div>
              
              <div class="form-row">
                <div class="form-group col-md-6">
                  <label for="publishDate">Date de publication</label>
                  <input 
                    type="date" 
                    class="form-control" 
                    id="publishDate" 
                    v-model="currentBook.publishDate"
                  >
                </div>
                <div class="form-group col-md-6">
                  <label for="rewardDate">Date de récompense</label>
                  <input 
                    type="date" 
                    class="form-control" 
                    id="rewardDate" 
                    v-model="currentBook.rewardDate"
                  >
                </div>
              </div>
              
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" @click="closeForm">Annuler</button>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de confirmation de suppression -->
    <div class="modal" :class="{ 'show': showDeleteConfirmation }" v-if="showDeleteConfirmation">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Confirmer la suppression</h5>
            <button @click="cancelDelete" type="button" class="close">
              <span>&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>Êtes-vous sûr de vouloir supprimer le livre "{{ bookToDelete?.title }}" ?</p>
            <p class="text-danger">Cette action est irréversible.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="cancelDelete">Annuler</button>
            <button type="button" class="btn btn-danger" @click="confirmDelete">Supprimer</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de détails -->
    <div class="modal" :class="{ 'show': showDetailsModal }" v-if="showDetailsModal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Détails du livre</h5>
            <button @click="closeDetails" type="button" class="close">
              <span>&times;</span>
            </button>
          </div>
          <div class="modal-body" v-if="selectedBook">
            <div class="book-details">
              <div class="book-cover">
                <img 
                  :src="selectedBook.picture || '/img/book-placeholder.png'" 
                  alt="Couverture"
                >
              </div>
              <div class="book-info">
                <h3>{{ selectedBook.title }}</h3>
                <p v-if="selectedBook.author">
                  <strong>Auteur:</strong> 
                  {{ selectedBook.author.firstName }} {{ selectedBook.author.lastName }}
                </p>
                <p v-if="selectedBook.editor">
                  <strong>Éditeur:</strong> 
                  {{ selectedBook.editor.name }}
                </p>
                <p>
                  <strong>ISBN:</strong> 
                  {{ selectedBook.ISBN || 'Non spécifié' }}
                </p>
                <p>
                  <strong>Prix:</strong> 
                  {{ selectedBook.price ? formatPrice(selectedBook.price) : 'Non défini' }}
                </p>
                <p>
                  <strong>Stock:</strong> 
                  {{ selectedBook.stock || 0 }}
                </p>
                <p>
                  <strong>Date de publication:</strong> 
                  {{ formatDate(selectedBook.publishDate) }}
                </p>
                <p v-if="selectedBook.rewardDate">
                  <strong>Date de récompense:</strong> 
                  {{ formatDate(selectedBook.rewardDate) }}
                </p>
                <div v-if="selectedBook.categories && selectedBook.categories.length">
                  <strong>Catégories:</strong>
                  <ul class="categories-list">
                    <li v-for="category in selectedBook.categories" :key="category.id">
                      {{ category.name }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="book-description" v-if="selectedBook.detail">
              <h4>Description</h4>
              <p>{{ selectedBook.detail }}</p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeDetails">Fermer</button>
            <button type="button" class="btn btn-primary" @click="editBook(selectedBook)">Modifier</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BookService from '../../services/BookService';
import AuthorService from '../../services/AuthorService';
import EditorService from '../../services/EditorService';
import CategoryService from '../../services/CategoryService';

export default {
  name: 'BookManagement',
  data() {
    return {
      books: [],
      authors: [],
      editors: [],
      categories: [],
      currentPage: 0,
      pageSize: 10,
      totalPages: 0,
      totalElements: 0,
      searchQuery: '',
      searchType: 'title',
      showFormModal: false,
      isEditMode: false,
      currentBook: this.resetBookForm(),
      selectedCategories: [],
      showDeleteConfirmation: false,
      bookToDelete: null,
      showDetailsModal: false,
      selectedBook: null,
      loading: false,
      error: null
    };
  },
  created() {
    this.loadBooks();
    this.loadAuthors();
    this.loadEditors();
    this.loadCategories();
  },
  methods: {
    async loadBooks() {
      try {
        this.loading = true;
        const response = await BookService.getBooks(this.currentPage, this.pageSize);
        this.books = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
      } catch (error) {
        this.error = "Erreur lors du chargement des livres";
        console.error('Erreur lors du chargement des livres', error);
      } finally {
        this.loading = false;
      }
    },
    
    async loadAuthors() {
      try {
        const response = await AuthorService.getAllAuthors();
        this.authors = response;
      } catch (error) {
        console.error('Erreur lors du chargement des auteurs', error);
      }
    },
    
    async loadEditors() {
      try {
        const response = await EditorService.getAllEditors();
        this.editors = response;
      } catch (error) {
        console.error('Erreur lors du chargement des éditeurs', error);
      }
    },
    
    async loadCategories() {
      try {
        const response = await CategoryService.getCategories();
        this.categories = response;
      } catch (error) {
        console.error('Erreur lors du chargement des catégories', error);
      }
    },
    
    nextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.currentPage++;
        this.loadBooks();
      }
    },
    
    prevPage() {
      if (this.currentPage > 0) {
        this.currentPage--;
        this.loadBooks();
      }
    },
    
    async searchBooks() {
      if (!this.searchQuery.trim()) {
        this.loadBooks();
        return;
      }
      
      try {
        let results;
        switch (this.searchType) {
          case 'title':
            results = await BookService.searchBooksByTitle(this.searchQuery);
            break;
          case 'author':
            results = await BookService.searchBooksByAuthor(this.searchQuery);
            break;
          case 'category':
            results = await BookService.searchBooksByCategory(this.searchQuery);
            break;
          default:
            results = await BookService.searchBooksByTitle(this.searchQuery);
        }
        
        this.books = results;
        this.totalPages = 1;
        this.currentPage = 0;
      } catch (error) {
        console.error('Erreur lors de la recherche', error);
      }
    },
    
    showAddForm() {
      this.currentBook = this.resetBookForm();
      this.selectedCategories = [];
      this.isEditMode = false;
      this.showFormModal = true;
    },
    
    editBook(book) {
      this.currentBook = { ...book };
      this.selectedCategories = book.categories ? book.categories.map(c => c.id) : [];
      this.isEditMode = true;
      this.showFormModal = true;
      
      // Si on est dans la vue de détails, fermer la modal
      if (this.showDetailsModal) {
        this.closeDetails();
      }
    },
    
    closeForm() {
      this.showFormModal = false;
      this.currentBook = this.resetBookForm();
      this.selectedCategories = [];
    },
    
    async saveBook() {
      try {
        // Préparer les catégories
        const categoriesForBook = this.selectedCategories.map(id => ({ id }));
        this.currentBook.categories = categoriesForBook;
        
        if (this.isEditMode) {
          await BookService.updateBook(this.currentBook.id, this.currentBook);
        } else {
          await BookService.createBook(this.currentBook);
        }
        
        this.closeForm();
        this.loadBooks();
      } catch (error) {
        console.error('Erreur lors de l\'enregistrement du livre', error);
      }
    },
    
    showDeleteModal(book) {
      this.bookToDelete = book;
      this.showDeleteConfirmation = true;
    },
    
    cancelDelete() {
      this.bookToDelete = null;
      this.showDeleteConfirmation = false;
    },
    
    async confirmDelete() {
      if (!this.bookToDelete) return;
      
      try {
        await BookService.deleteBook(this.bookToDelete.id);
        this.loadBooks();
        this.cancelDelete();
      } catch (error) {
        console.error('Erreur lors de la suppression du livre', error);
      }
    },
    
    viewDetails(book) {
      this.selectedBook = book;
      this.showDetailsModal = true;
    },
    
    closeDetails() {
      this.selectedBook = null;
      this.showDetailsModal = false;
    },
    
    resetBookForm() {
      return {
        title: '',
        ISBN: '',
        detail: '',
        picture: '',
        price: 0,
        stock: 0,
        author: null,
        editor: null,
        publishDate: null,
        rewardDate: null,
        categories: []
      };
    },
    
    getAuthorName(book) {
      if (!book.author) return 'Non spécifié';
      return `${book.author.firstName || ''} ${book.author.lastName || ''}`.trim();
    },
    
    formatPrice(price) {
      return new Intl.NumberFormat('fr-FR', { 
        style: 'currency', 
        currency: 'EUR' 
      }).format(price);
    },
    
    formatDate(dateString) {
      if (!dateString) return 'Non spécifiée';
      
      const date = new Date(dateString);
      return new Intl.DateTimeFormat('fr-FR').format(date);
    }
  }
};
</script>

<style scoped>
.book-management {
  padding: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  gap: 10px;
}

.search-bar input {
  width: 250px;
}

.table-responsive {
  margin-bottom: 20px;
}

.thumbnail {
  width: 50px;
  height: 70px;
  object-fit: cover;
}

.action-buttons {
  display: flex;
  gap: 5px;
}

.pagination-controls {
  display: flex;
  justify-content: center;
  gap: 20px;
  align-items: center;
  margin-top: 20px;
}

.modal.show {
  display: block;
  background-color: rgba(0, 0, 0, 0.5);
}

.categories-selector {
  max-height: 150px;
  overflow-y: auto;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
  padding: 10px;
}

.required {
  color: red;
}

.book-details {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.book-cover img {
  max-width: 200px;
  max-height: 300px;
  object-fit: contain;
}

.book-info {
  flex: 1;
}

.categories-list {
  margin: 0;
  padding-left: 20px;
}
</style> 