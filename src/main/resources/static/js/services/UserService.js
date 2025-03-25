import api from './api';
import MockDataService from './MockDataService';

// Option de configuration pour activer les données fictives en cas d'erreur
const USE_MOCK_DATA_ON_ERROR = true;

class UserService {
    // Récupérer tous les utilisateurs avec pagination
    async getUsers(page = 0, size = 10) {
        try {
            console.log('🔍 Tentative de récupération des utilisateurs avec params:', { page, size });
            const response = await api.get(`/users?page=${page}&size=${size}`);
            console.log('✅ Données des utilisateurs récupérées avec succès:', response.data);
            
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
            console.error('❌ Erreur lors de la récupération des utilisateurs:', error);
            
            // Solution de secours en utilisant fetch
            try {
                const response = await fetch(`${api.defaults.baseURL}/users?page=${page}&size=${size}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'application/json'
                    },
                    credentials: 'include'
                });
                
                if (!response.ok) {
                    console.error(`❌ Échec de la solution de secours fetch pour utilisateurs: ${response.status}`);
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
                console.error('❌ Échec complet de la récupération des utilisateurs:', fetchError);
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

    // Récupérer un utilisateur par ID
    async getUserById(id) {
        try {
            const response = await api.get(`/users/${id}`);
            return response.data;
        } catch (error) {
            console.error(`UserService: Erreur lors de la récupération de l'utilisateur ${id}:`, error);
            throw error;
        }
    }

    // Créer un nouvel utilisateur
    async createUser(user) {
        try {
            const response = await api.post('/users', user);
            return response.data;
        } catch (error) {
            console.error('UserService: Erreur lors de la création de l\'utilisateur:', error);
            throw error;
        }
    }

    // Mettre à jour un utilisateur
    async updateUser(id, user) {
        try {
            const response = await api.put(`/users/${id}`, user);
            return response.data;
        } catch (error) {
            console.error(`UserService: Erreur lors de la mise à jour de l'utilisateur ${id}:`, error);
            throw error;
        }
    }

    // Supprimer un utilisateur
    async deleteUser(id) {
        try {
            await api.delete(`/users/${id}`);
        } catch (error) {
            console.error(`UserService: Erreur lors de la suppression de l'utilisateur ${id}:`, error);
            throw error;
        }
    }

    // Récupérer l'utilisateur courant
    async getCurrentUser() {
        try {
            const response = await api.get('/users/me');
            return response.data;
        } catch (error) {
            console.error('UserService: Erreur lors de la récupération de l\'utilisateur courant:', error);
            throw error;
        }
    }
}

export default new UserService(); 