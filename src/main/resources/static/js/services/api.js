import axios from 'axios';

// Fonction pour obtenir l'URL de base en fonction de l'environnement
function getBaseURL() {
    if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
        return 'http://localhost:8111/api';
    }
    return '/api'; // En production, utiliser un chemin relatif
}

// Créer une instance d'axios avec la configuration de base
const api = axios.create({
    baseURL: getBaseURL(),
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
});

// Intercepteur pour ajouter le token JWT à chaque requête
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            console.log('🔑 Token ajouté à la requête:', config.url);
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        console.error('❌ Erreur de configuration de la requête:', error);
        return Promise.reject(error);
    }
);

// Intercepteur pour gérer les réponses et les erreurs
api.interceptors.response.use(
    response => {
        // Log de succès pour le débogage
        console.log(`✅ Réponse ${response.status} reçue pour ${response.config.url}`);
        return response;
    },
    error => {
        // Gestion détaillée des erreurs
        if (error.response) {
            // La requête a été faite et le serveur a répondu avec un code d'erreur
            console.error(`❌ Erreur ${error.response.status} pour ${error.config.url}:`, error.message);
            
            // Log détaillé pour les erreurs 500
            if (error.response.status === 500) {
                console.error('Erreur 500 détaillée:', {
                    url: error.config.url,
                    method: error.config.method,
                    headers: error.config.headers,
                    data: error.config.data,
                    responseData: error.response.data
                });
            }
            
            // Redirection vers la page de login pour les erreurs d'authentification
            if ((error.response.status === 401 || error.response.status === 403) && 
                !window.location.pathname.includes('/login')) {
                console.log('⚠️ Session expirée, redirection vers la page de connexion');
                localStorage.removeItem('token');
                localStorage.removeItem('user');
                window.location.href = '/login';
            }
        } else if (error.request) {
            // La requête a été faite mais aucune réponse n'a été reçue
            console.error('❌ Pas de réponse du serveur:', error.request);
        } else {
            // Une erreur s'est produite lors de la configuration de la requête
            console.error('❌ Erreur de configuration:', error.message);
        }
        
        return Promise.reject(error);
    }
);

// Fonction de diagnostic pour tester la connectivité au serveur
api.testConnection = async () => {
    try {
        console.log('🔍 Test de connexion au serveur API...');
        const startTime = Date.now();
        const response = await fetch(`${getBaseURL()}/health`, { 
            method: 'GET',
            cache: 'no-cache',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const endTime = Date.now();
        const latency = endTime - startTime;
        
        if (response.ok) {
            console.log(`✅ Serveur API accessible (latence: ${latency}ms)`);
            return { 
                success: true, 
                latency,
                message: `API accessible avec une latence de ${latency}ms`
            };
        } else {
            console.error(`❌ Le serveur a répondu avec le statut ${response.status}`);
            return { 
                success: false, 
                status: response.status, 
                message: `Erreur ${response.status}: ${response.statusText}`
            };
        }
    } catch (error) {
        console.error('❌ Échec du test de connexion au serveur API:', error);
        return { 
            success: false, 
            error: error.message,
            message: `Impossible de se connecter à l'API: ${error.message}`
        };
    }
};

// Méthode pour vérifier l'état du serveur
api.checkServerStatus = async () => {
    try {
        console.log('🔍 Vérification de l\'état du serveur...');
        
        // Créer plusieurs points de test
        const endpoints = [
            { url: `${getBaseURL()}/health`, name: 'Health Check' },
            { url: `${getBaseURL()}/categories`, name: 'Categories API' },
            { url: `${getBaseURL()}/authors`, name: 'Authors API' },
            { url: `${getBaseURL()}/editors`, name: 'Editors API' },
            { url: `${getBaseURL()}/books`, name: 'Books API' }
        ];
        
        const results = [];
        
        // Tester chaque endpoint
        for (const endpoint of endpoints) {
            try {
                const startTime = Date.now();
                const response = await fetch(endpoint.url, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
                    },
                    credentials: 'include'
                });
                
                const endTime = Date.now();
                const latency = endTime - startTime;
                
                results.push({
                    endpoint: endpoint.name,
                    url: endpoint.url,
                    status: response.status,
                    ok: response.ok,
                    latency: latency,
                    message: response.ok ? 'OK' : `Erreur ${response.status}`
                });
                
                console.log(`${response.ok ? '✅' : '❌'} ${endpoint.name}: ${response.status} (${latency}ms)`);
            } catch (error) {
                results.push({
                    endpoint: endpoint.name,
                    url: endpoint.url,
                    error: error.message,
                    ok: false,
                    message: `Échec: ${error.message}`
                });
                
                console.error(`❌ ${endpoint.name}: ${error.message}`);
            }
        }
        
        return {
            timestamp: new Date().toISOString(),
            results: results,
            allOk: results.every(r => r.ok)
        };
    } catch (error) {
        console.error('Erreur lors du diagnostic:', error);
        return {
            timestamp: new Date().toISOString(),
            error: error.message,
            results: []
        };
    }
};

export default api; 