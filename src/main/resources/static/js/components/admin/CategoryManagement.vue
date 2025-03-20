<template>
  <div class="category-management">
    <div class="toolbar mb-4">
      <button class="btn btn-primary" @click="showAddForm()">
        <i class="fas fa-plus"></i> Ajouter une catégorie
      </button>
      <div class="search-container">
        <input 
          type="text" 
          class="form-control" 
          placeholder="Rechercher une catégorie..."
          v-model="searchTerm"
          @input="searchCategories"
        />
      </div>
    </div>

    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Icône</th>
            <th>Nom</th>
            <th>Description</th>
            <th>Couleur</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="category in categories" :key="category.id">
            <td>{{ category.id }}</td>
            <td>
              <i :class="category.icon" :style="{ color: category.color }"></i>
            </td>
            <td>{{ category.name }}</td>
            <td>{{ category.description }}</td>
            <td>
              <span class="color-box" :style="{ backgroundColor: category.color }"></span>
              {{ category.color }}
            </td>
            <td>
              <button class="btn btn-sm btn-info mr-2" @click="editCategory(category)">
                <i class="fas fa-edit"></i>
              </button>
              <button class="btn btn-sm btn-danger mr-2" @click="showDeleteModal(category)">
                <i class="fas fa-trash"></i>
              </button>
              <button class="btn btn-sm btn-secondary" @click="viewBooks(category)">
                <i class="fas fa-book"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination-container">
      <button 
        class="btn btn-outline-primary" 
        @click="prevPage()" 
        :disabled="currentPage === 0"
      >
        <i class="fas fa-chevron-left"></i> Précédent
      </button>
      <span class="page-info">Page {{ currentPage + 1 }} sur {{ totalPages }}</span>
      <button 
        class="btn btn-outline-primary" 
        @click="nextPage()" 
        :disabled="currentPage >= totalPages - 1"
      >
        Suivant <i class="fas fa-chevron-right"></i>
      </button>
    </div>

    <!-- Modal d'ajout/modification -->
    <div class="modal" :class="{ 'show': showModal }" v-if="showModal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ isEdit ? 'Modifier' : 'Ajouter' }} une catégorie</h5>
            <button type="button" class="close" @click="closeForm()">
              <span>&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveCategory">
              <div class="form-group">
                <label for="categoryName">Nom</label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="categoryName" 
                  v-model="currentCategory.name" 
                  required
                />
              </div>
              <div class="form-group">
                <label for="categoryDescription">Description</label>
                <textarea 
                  class="form-control" 
                  id="categoryDescription" 
                  v-model="currentCategory.description" 
                  rows="3"
                ></textarea>
              </div>
              <div class="form-group">
                <label for="categoryIcon">Icône (class FontAwesome)</label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="categoryIcon" 
                  v-model="currentCategory.icon" 
                  placeholder="fas fa-book"
                />
                <div class="icon-preview mt-2" v-if="currentCategory.icon">
                  <i :class="currentCategory.icon"></i> Aperçu
                </div>
              </div>
              <div class="form-group">
                <label for="categoryColor">Couleur</label>
                <input 
                  type="color" 
                  class="form-control" 
                  id="categoryColor" 
                  v-model="currentCategory.color"
                />
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" @click="closeForm()">Annuler</button>
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
            <button type="button" class="close" @click="cancelDelete()">
              <span>&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>
              Êtes-vous sûr de vouloir supprimer la catégorie <strong>{{ categoryToDelete?.name }}</strong> ?
            </p>
            <p class="text-danger">Cette action est irréversible.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="cancelDelete()">Annuler</button>
            <button type="button" class="btn btn-danger" @click="confirmDelete()">Supprimer</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de visualisation des livres -->
    <div class="modal" :class="{ 'show': showBooksModal }" v-if="showBooksModal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Livres dans la catégorie {{ selectedCategory?.name }}</h5>
            <button type="button" class="close" @click="closeBooksModal()">
              <span>&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <div v-if="loading" class="text-center">
              <div class="spinner-border" role="status">
                <span class="sr-only">Chargement...</span>
              </div>
            </div>
            <div v-else-if="categoryBooks.length === 0" class="text-center">
              <p>Aucun livre trouvé dans cette catégorie.</p>
            </div>
            <div v-else class="row">
              <div class="col-md-6 mb-3" v-for="book in categoryBooks" :key="book.id">
                <div class="card">
                  <div class="card-body">
                    <h5 class="card-title">{{ book.title }}</h5>
                    <h6 class="card-subtitle mb-2 text-muted">{{ book.author ? `${book.author.firstName} ${book.author.lastName}` : 'Auteur inconnu' }}</h6>
                    <p class="card-text">{{ formatPrice(book.price) }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeBooksModal()">Fermer</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CategoryService from '../../services/CategoryService';
import BookService from '../../services/BookService';

export default {
  name: 'CategoryManagement',
  data() {
    return {
      categories: [],
      currentPage: 0,
      pageSize: 10,
      totalCategories: 0,
      totalPages: 0,
      searchTerm: '',
      
      // État du modal d'ajout/modification
      showModal: false,
      isEdit: false,
      currentCategory: {
        name: '',
        description: '',
        icon: 'fas fa-book',
        color: '#3498db'
      },
      
      // État du modal de suppression
      showDeleteConfirmation: false,
      categoryToDelete: null,
      
      // État du modal des livres
      showBooksModal: false,
      selectedCategory: null,
      categoryBooks: [],
      loading: false
    };
  },
  mounted() {
    this.loadCategories();
  },
  methods: {
    // Chargement des données
    async loadCategories() {
      try {
        const response = await CategoryService.getAllCategories(this.currentPage, this.pageSize);
        this.categories = response.content;
        this.totalCategories = response.totalElements;
        this.totalPages = response.totalPages;
      } catch (error) {
        console.error('Erreur lors du chargement des catégories:', error);
        // Afficher une notification d'erreur
      }
    },
    
    // Pagination
    nextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.currentPage++;
        this.loadCategories();
      }
    },
    prevPage() {
      if (this.currentPage > 0) {
        this.currentPage--;
        this.loadCategories();
      }
    },
    
    // Recherche
    searchCategories() {
      // Réinitialiser la pagination
      this.currentPage = 0;
      this.loadCategories();
    },
    
    // Formulaire d'ajout/modification
    showAddForm() {
      this.isEdit = false;
      this.currentCategory = {
        name: '',
        description: '',
        icon: 'fas fa-book',
        color: '#3498db'
      };
      this.showModal = true;
    },
    editCategory(category) {
      this.isEdit = true;
      this.currentCategory = { ...category };
      this.showModal = true;
    },
    closeForm() {
      this.showModal = false;
    },
    async saveCategory() {
      try {
        if (this.isEdit) {
          await CategoryService.updateCategory(this.currentCategory.id, this.currentCategory);
        } else {
          await CategoryService.createCategory(this.currentCategory);
        }
        
        this.showModal = false;
        this.loadCategories();
        // Afficher une notification de succès
      } catch (error) {
        console.error('Erreur lors de l\'enregistrement de la catégorie:', error);
        // Afficher une notification d'erreur
      }
    },
    
    // Suppression
    showDeleteModal(category) {
      this.categoryToDelete = category;
      this.showDeleteConfirmation = true;
    },
    async confirmDelete() {
      if (!this.categoryToDelete) return;
      
      try {
        await CategoryService.deleteCategory(this.categoryToDelete.id);
        this.showDeleteConfirmation = false;
        this.categoryToDelete = null;
        this.loadCategories();
        // Afficher une notification de succès
      } catch (error) {
        console.error('Erreur lors de la suppression de la catégorie:', error);
        // Afficher une notification d'erreur
      }
    },
    cancelDelete() {
      this.showDeleteConfirmation = false;
      this.categoryToDelete = null;
    },
    
    // Visualisation des livres
    async viewBooks(category) {
      this.selectedCategory = category;
      this.showBooksModal = true;
      this.loading = true;
      this.categoryBooks = [];
      
      try {
        this.categoryBooks = await BookService.searchBooksByCategory(category.name);
      } catch (error) {
        console.error(`Erreur lors du chargement des livres de la catégorie ${category.name}:`, error);
      } finally {
        this.loading = false;
      }
    },
    closeBooksModal() {
      this.showBooksModal = false;
      this.selectedCategory = null;
      this.categoryBooks = [];
    },
    
    // Formatage
    formatPrice(price) {
      return new Intl.NumberFormat('fr-FR', { 
        style: 'currency', 
        currency: 'EUR' 
      }).format(price);
    }
  }
};
</script>

<style scoped>
.category-management {
  padding: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.search-container {
  width: 300px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 1rem;
}

.page-info {
  margin: 0 1rem;
}

.modal {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.modal.show {
  display: flex;
  align-items: center;
  justify-content: center;
}

.color-box {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 5px;
  border: 1px solid #ddd;
  vertical-align: middle;
}

.icon-preview {
  font-size: 1.2rem;
}
</style> 