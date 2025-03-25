import api from './api';
import MockDataService from './MockDataService';

// Option de configuration pour activer les données fictives en cas d'erreur
const USE_MOCK_DATA_ON_ERROR = true;

class CategoryService {
    // Récupérer toutes les catégories avec pagination
    async getAllCategories(page = 0, size = 10) {
        try {
            console.log('📂 Tentative de récupération des catégories depuis l\'API...');
            const response = await api.get(`/categories?page=${page}&size=${size}`);
            console.log('✅ Catégories récupérées avec succès:', response.data);
            
            // Vérifier la structure de la réponse
            if (response.data && response.data.content) {
                return response.data;
            } else {
                console.warn('⚠️ Structure de réponse inattendue:', response.data);
                return {
                    content: [],
                    totalElements: 0,
                    totalPages: 0,
                    size: size,
                    number: page
                };
            }
        } catch (error) {
            console.error('❌ Erreur détaillée lors de la récupération des catégories:', error);
            console.error('❌ Erreur serveur 500 - Vérifiez les logs du backend');
            
            // Solution de secours en utilisant fetch
            try {
                console.log('🔄 Tentative de récupération des catégories avec fetch comme solution de secours');
                const response = await fetch(`${api.defaults.baseURL}/categories?page=${page}&size=${size}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'application/json'
                    },
                    credentials: 'include'
                });
                
                if (!response.ok) {
                    console.error(`❌ Échec de la solution de secours fetch pour catégories: ${response.status}`);
                    
                    // Utiliser les données fictives si activé
                    if (USE_MOCK_DATA_ON_ERROR) {
                        console.log('🧪 Utilisation des données fictives pour les catégories');
                        return MockDataService.getCategories();
                    }
                    
                    // Retourner un objet de pagination vide en cas d'échec
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
                console.error('❌ Échec complet de la récupération des catégories:', fetchError);
                
                // Utiliser les données fictives si activé
                if (USE_MOCK_DATA_ON_ERROR) {
                    console.log('🧪 Utilisation des données fictives pour les catégories');
                    return MockDataService.getCategories();
                }
                
                // Retourner un objet de pagination vide
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

    // Récupérer une catégorie par son ID
    async getCategoryById(id) {
        try {
            const response = await api.get(`/categories/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Erreur lors du chargement de la catégorie ${id}:`, error);
            throw error;
        }
    }

    // Créer une nouvelle catégorie
    async createCategory(category) {
        try {
            const response = await api.post('/categories', category);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la création de la catégorie:', error);
            throw error;
        }
    }

    // Mettre à jour une catégorie
    async updateCategory(id, category) {
        try {
            const response = await api.put(`/categories/${id}`, category);
            return response.data;
        } catch (error) {
            console.error(`Erreur lors de la mise à jour de la catégorie ${id}:`, error);
            throw error;
        }
    }

    // Supprimer une catégorie
    async deleteCategory(id) {
        try {
            await api.delete(`/categories/${id}`);
        } catch (error) {
            console.error(`Erreur lors de la suppression de la catégorie ${id}:`, error);
            throw error;
        }
    }

    // Récupérer les livres d'une catégorie
    async getCategoryBooks(id) {
        try {
            const response = await api.get(`/categories/${id}/books`);
            return response.data;
        } catch (error) {
            console.error(`Erreur lors du chargement des livres de la catégorie ${id}:`, error);
            throw error;
        }
    }
}

export default new CategoryService(); 