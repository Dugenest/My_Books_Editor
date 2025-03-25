import api from './api';
import MockDataService from './MockDataService';

// Option de configuration pour activer les données fictives en cas d'erreur
const USE_MOCK_DATA_ON_ERROR = true;

class AuthorService {
    // Récupérer tous les auteurs
    async getAllAuthors(page = 0, size = 10) {
        try {
            console.log('👤 Tentative de récupération des auteurs depuis l\'API...');
            const response = await api.get(`/authors?page=${page}&size=${size}`);
            console.log('✅ Auteurs récupérés avec succès');
            return response.data;
        } catch (error) {
            console.error('❌ AuthorService: Erreur lors de la récupération des auteurs:', error);
            
            // Solution de secours en utilisant fetch
            try {
                console.log('🔄 Tentative de récupération avec fetch comme solution de secours');
                const response = await fetch(`${api.defaults.baseURL}/authors?page=${page}&size=${size}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'application/json'
                    },
                    credentials: 'include'
                });
                
                if (!response.ok) {
                    console.error(`❌ Échec de la solution de secours fetch: ${response.status}`);
                    
                    // Utiliser les données fictives si activé
                    if (USE_MOCK_DATA_ON_ERROR) {
                        console.log('🧪 Utilisation des données fictives pour les auteurs');
                        return MockDataService.getAuthors();
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
                console.error('❌ Échec complet de la récupération des auteurs:', fetchError);
                
                // Utiliser les données fictives si activé
                if (USE_MOCK_DATA_ON_ERROR) {
                    console.log('🧪 Utilisation des données fictives pour les auteurs');
                    return MockDataService.getAuthors();
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

    // Récupérer un auteur par ID
    async getAuthorById(id) {
        try {
            const response = await api.get(`/authors/${id}`);
            return response.data;
        } catch (error) {
            console.error(`AuthorService: Erreur lors de la récupération de l'auteur ${id}:`, error);
            throw error;
        }
    }

    // Créer un nouvel auteur
    async createAuthor(author) {
        try {
            const response = await api.post('/authors', author);
            return response.data;
        } catch (error) {
            console.error('AuthorService: Erreur lors de la création de l\'auteur:', error);
            throw error;
        }
    }

    // Mettre à jour un auteur
    async updateAuthor(id, author) {
        try {
            const response = await api.put(`/authors/${id}`, author);
            return response.data;
        } catch (error) {
            console.error(`AuthorService: Erreur lors de la mise à jour de l'auteur ${id}:`, error);
            throw error;
        }
    }

    // Supprimer un auteur
    async deleteAuthor(id) {
        try {
            await api.delete(`/authors/${id}`);
        } catch (error) {
            console.error(`AuthorService: Erreur lors de la suppression de l'auteur ${id}:`, error);
            throw error;
        }
    }

    // Récupérer les livres d'un auteur
    async getAuthorBooks(id) {
        try {
            const response = await api.get(`/authors/${id}/books`);
            return response.data;
        } catch (error) {
            console.error(`AuthorService: Erreur lors de la récupération des livres de l'auteur ${id}:`, error);
            // Retourner un tableau vide en cas d'erreur
            return [];
        }
    }
}

export default new AuthorService(); 