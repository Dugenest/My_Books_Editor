import api from './api';
import MockDataService from './MockDataService';

// Option de configuration pour activer les données fictives en cas d'erreur
const USE_MOCK_DATA_ON_ERROR = true;

class BookService {
    // Récupérer tous les livres avec pagination
    async getBooks(page = 0, size = 10, sort = 'title,asc') {
        try {
            console.log('📚 Tentative de récupération des livres depuis l\'API...');
            const response = await api.get(`/books?page=${page}&size=${size}&sort=${sort}`);
            console.log('✅ Livres récupérés avec succès depuis l\'API');
            return response.data;
        } catch (error) {
            console.error('❌ Erreur lors de la récupération des livres:', error);
            
            // Solution de secours en utilisant fetch
            try {
                console.log('🔄 Tentative de récupération des livres avec fetch comme solution de secours');
                const response = await fetch(`${api.defaults.baseURL}/books?page=${page}&size=${size}&sort=${sort}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'application/json'
                    },
                    credentials: 'include'
                });
                
                if (!response.ok) {
                    console.error(`❌ Échec de la solution de secours fetch pour livres: ${response.status}`);
                    
                    // Utiliser les données fictives si activé
                    if (USE_MOCK_DATA_ON_ERROR) {
                        console.log('🧪 Utilisation des données fictives pour les livres');
                        return MockDataService.getBooks(page, size);
                    }
                    
                    return {
                        content: [],
                        totalElements: 0,
                        totalPages: 0,
                        size: size,
                        number: page
                    };
                }
                
                const data = await response.json();
                return data;
            } catch (fetchError) {
                console.error('❌ Échec complet de la récupération des livres:', fetchError);
                
                // Utiliser les données fictives si activé
                if (USE_MOCK_DATA_ON_ERROR) {
                    console.log('🧪 Utilisation des données fictives pour les livres');
                    return MockDataService.getBooks(page, size);
                }
                
                // Retourner une structure par défaut en cas d'erreur pour éviter les erreurs en cascade
                return {
                    content: [],
                    totalElements: 0,
                    totalPages: 0,
                    size: size,
                    number: page
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
            console.error('Erreur lors du chargement des nouvelles parutions:', error);
            throw error;
        }
    }

    // Récupérer les livres populaires
    async getPopularBooks(limit = 5) {
        try {
            const response = await api.get(`/books/popular?limit=${limit}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors du chargement des livres populaires:', error);
            throw error;
        }
    }

    // Récupérer un livre par son ID
    async getBookById(id) {
        try {
            const response = await api.get(`/books/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Erreur lors du chargement du livre ID=${id}:`, error);
            throw error;
        }
    }

    // Créer un nouveau livre
    async createBook(book) {
        try {
            const response = await api.post('/books', book);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la création du livre:', error);
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
            const response = await api.get(`/books/search/title?title=${encodeURIComponent(title)}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la recherche de livres par titre:', error);
            throw error;
        }
    }

    // Rechercher des livres par auteur
    async searchBooksByAuthor(lastName, firstName = '') {
        try {
            const response = await api.get(
                `/books/search/author?lastName=${encodeURIComponent(lastName)}&firstName=${encodeURIComponent(firstName)}`
            );
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la recherche de livres par auteur:', error);
            throw error;
        }
    }

    // Rechercher des livres par catégorie
    async searchBooksByCategory(categoryName) {
        try {
            const response = await api.get(`/books/search/category?categoryName=${encodeURIComponent(categoryName)}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la recherche de livres par catégorie:', error);
            throw error;
        }
    }

    // Ajouter une catégorie à un livre
    async addCategoryToBook(bookId, categoryId) {
        try {
            const response = await api.post(`/books/${bookId}/categories/${categoryId}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de l\'ajout d\'une catégorie au livre:', error);
            throw error;
        }
    }

    // Supprimer une catégorie d'un livre
    async removeCategoryFromBook(bookId, categoryId) {
        try {
            const response = await api.delete(`/books/${bookId}/categories/${categoryId}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la suppression d\'une catégorie du livre:', error);
            throw error;
        }
    }
}

export default new BookService(); 