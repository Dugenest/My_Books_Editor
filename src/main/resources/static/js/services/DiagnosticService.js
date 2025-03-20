import api from './api';

/**
 * Service de diagnostic pour identifier les problèmes dans l'application
 */
class DiagnosticService {
    /**
     * Vérifie l'état du serveur en testant différents endpoints
     */
    async checkServerStatus() {
        return await api.checkServerStatus();
    }
    
    /**
     * Affiche les informations système pour le débogage
     */
    getSystemInfo() {
        return {
            userAgent: navigator.userAgent,
            platform: navigator.platform,
            language: navigator.language,
            cookiesEnabled: navigator.cookieEnabled,
            localStorage: typeof localStorage !== 'undefined',
            sessionStorage: typeof sessionStorage !== 'undefined',
            online: navigator.onLine,
            screenSize: {
                width: window.screen.width,
                height: window.screen.height
            },
            viewport: {
                width: window.innerWidth,
                height: window.innerHeight
            },
            timestamp: new Date().toISOString(),
            token: localStorage.getItem('token') ? 'Présent' : 'Absent',
            user: localStorage.getItem('user') ? 'Présent' : 'Absent'
        };
    }
    
    /**
     * Vérifie la connectivité au backend
     */
    async testBackendConnection() {
        const results = [];
        const start = Date.now();
        
        try {
            const response = await fetch(`${api.defaults.baseURL}/health`, {
                method: 'HEAD',
                cache: 'no-cache'
            });
            
            results.push({
                test: 'Connexion au backend',
                success: response.ok,
                status: response.status,
                message: response.ok ? 'Connexion établie' : `Erreur ${response.status}`,
                latency: Date.now() - start
            });
        } catch (error) {
            results.push({
                test: 'Connexion au backend',
                success: false,
                error: error.message,
                message: `Échec: ${error.message}`,
                latency: Date.now() - start
            });
        }
        
        return {
            timestamp: new Date().toISOString(),
            results: results,
            allTests: results.every(r => r.success)
        };
    }
    
    /**
     * Vérifie l'authentification de l'utilisateur
     */
    async checkAuthentication() {
        const token = localStorage.getItem('token');
        const user = localStorage.getItem('user');
        
        const results = {
            hasToken: !!token,
            hasUser: !!user,
            tokenDetails: token ? this.parseJwt(token) : null,
            isTokenExpired: token ? this.isTokenExpired(token) : true
        };
        
        // Vérifier si le token est valide avec le backend
        if (token) {
            try {
                const response = await fetch(`${api.defaults.baseURL}/auth/validate`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });
                
                results.backendValidation = {
                    success: response.ok,
                    status: response.status,
                    message: response.ok ? 'Token valide' : 'Token invalide'
                };
            } catch (error) {
                results.backendValidation = {
                    success: false,
                    error: error.message,
                    message: `Échec de la validation: ${error.message}`
                };
            }
        }
        
        return results;
    }
    
    /**
     * Analyse un token JWT
     */
    parseJwt(token) {
        try {
            // Extraire la partie payload du token (la deuxième partie)
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
            
            const payload = JSON.parse(jsonPayload);
            
            return {
                ...payload,
                issuedAt: payload.iat ? new Date(payload.iat * 1000).toISOString() : null,
                expiresAt: payload.exp ? new Date(payload.exp * 1000).toISOString() : null
            };
        } catch (error) {
            console.error('Erreur lors de l\'analyse du token JWT:', error);
            return { error: 'Format de token invalide' };
        }
    }
    
    /**
     * Vérifie si un token JWT est expiré
     */
    isTokenExpired(token) {
        try {
            const payload = this.parseJwt(token);
            if (!payload.exp) return true;
            
            // exp est en secondes, Date.now() est en millisecondes
            const expirationDate = payload.exp * 1000;
            return Date.now() >= expirationDate;
        } catch (error) {
            console.error('Erreur lors de la vérification de l\'expiration du token:', error);
            return true; // En cas d'erreur, on considère le token comme expiré
        }
    }
}

export default new DiagnosticService(); 