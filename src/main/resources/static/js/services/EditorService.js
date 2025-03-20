import api from './api';
import MockDataService from './MockDataService';

// Option de configuration pour activer les données fictives en cas d'erreur
const USE_MOCK_DATA_ON_ERROR = true;

class EditorService {
    // Récupérer tous les éditeurs
    async getEditors() {
        try {
            console.log('📚 Tentative de récupération des éditeurs depuis l\'API...');
            const response = await api.get('/editors');
            console.log('✅ Éditeurs récupérés avec succès');
            return response.data;
        } catch (error) {
            console.error('❌ EditorService: Erreur lors de la récupération des éditeurs:', error);
            console.error('❌ Erreur détaillée lors du chargement des éditeurs:', error);
            
            // Solution de secours en utilisant fetch
            try {
                console.log('🔄 Tentative de récupération des éditeurs avec fetch comme solution de secours');
                const response = await fetch(`${api.defaults.baseURL}/editors`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'application/json'
                    },
                    credentials: 'include'
                });
                
                if (!response.ok) {
                    console.error(`❌ Échec de la solution de secours fetch pour éditeurs: ${response.status}`);
                    
                    // Utiliser les données fictives si activé
                    if (USE_MOCK_DATA_ON_ERROR) {
                        console.log('🧪 Utilisation des données fictives pour les éditeurs');
                        return MockDataService.getEditors();
                    }
                    
                    return [];
                }
                
                const data = await response.json();
                return data;
            } catch (fetchError) {
                console.error('❌ Échec complet de la récupération des éditeurs:', fetchError);
                
                // Utiliser les données fictives si activé
                if (USE_MOCK_DATA_ON_ERROR) {
                    console.log('🧪 Utilisation des données fictives pour les éditeurs');
                    return MockDataService.getEditors();
                }
                
                // Retourner un tableau vide en cas d'erreur pour éviter les erreurs en cascade
                return [];
            }
        }
    }

    // Récupérer un éditeur par ID
    async getEditorById(id) {
        try {
            const response = await api.get(`/editors/${id}`);
            return response.data;
        } catch (error) {
            console.error(`EditorService: Erreur lors de la récupération de l'éditeur ${id}:`, error);
            throw error;
        }
    }

    // Créer un nouvel éditeur
    async createEditor(editor) {
        try {
            const response = await api.post('/editors', editor);
            return response.data;
        } catch (error) {
            console.error('EditorService: Erreur lors de la création de l\'éditeur:', error);
            throw error;
        }
    }

    // Mettre à jour un éditeur
    async updateEditor(id, editor) {
        try {
            const response = await api.put(`/editors/${id}`, editor);
            return response.data;
        } catch (error) {
            console.error(`EditorService: Erreur lors de la mise à jour de l'éditeur ${id}:`, error);
            throw error;
        }
    }

    // Supprimer un éditeur
    async deleteEditor(id) {
        try {
            await api.delete(`/editors/${id}`);
        } catch (error) {
            console.error(`EditorService: Erreur lors de la suppression de l'éditeur ${id}:`, error);
            throw error;
        }
    }

    // Récupérer les livres d'un éditeur
    async getEditorBooks(id) {
        try {
            const response = await api.get(`/editors/${id}/books`);
            return response.data;
        } catch (error) {
            console.error(`EditorService: Erreur lors de la récupération des livres de l'éditeur ${id}:`, error);
            // Retourner un tableau vide en cas d'erreur
            return [];
        }
    }
}

export default new EditorService(); 