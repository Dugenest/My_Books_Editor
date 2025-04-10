import api from './api';

class AuthService {
    constructor() {
        this.baseUrl = '/auth';
    }

    async login(email, password, rememberMe = false) {
        try {
            console.log('🔍 Tentative de connexion:', email);
            const response = await api.post(`${this.baseUrl}/login`, {
                email,
                password,
                rememberMe
            });

            if (response.data.token) {
                localStorage.setItem('token', response.data.token);
                localStorage.setItem('user', JSON.stringify(response.data.user));
                console.log('✅ Connexion réussie');
                return response.data;
            } else {
                throw new Error('Token non reçu du serveur');
            }
        } catch (error) {
            console.error('❌ Erreur de connexion:', error);
            
            // Gestion spécifique des erreurs
            if (error.response) {
                switch (error.response.status) {
                    case 401:
                        throw new Error('Email ou mot de passe incorrect');
                    case 403:
                        throw new Error('Compte désactivé ou non vérifié');
                    case 404:
                        throw new Error('Service d\'authentification non disponible');
                    default:
                        throw new Error(`Erreur de connexion: ${error.response.data.message || error.message}`);
                }
            } else if (error.request) {
                throw new Error('Impossible de contacter le serveur');
            } else {
                throw error;
            }
        }
    }

    async register(userData) {
        try {
            console.log('📝 Tentative d\'inscription');
            const response = await api.post(`${this.baseUrl}/register`, userData);
            console.log('✅ Inscription réussie');
            return response.data;
        } catch (error) {
            console.error('❌ Erreur d\'inscription:', error);
            if (error.response) {
                throw new Error(error.response.data.message || 'Erreur lors de l\'inscription');
            }
            throw error;
        }
    }

    async confirmEmail(token) {
        try {
            console.log('🔍 Confirmation de l\'email');
            const response = await api.get(`${this.baseUrl}/confirm?token=${token}`);
            console.log('✅ Email confirmé');
            return response.data;
        } catch (error) {
            console.error('❌ Erreur de confirmation d\'email:', error);
            throw new Error('Erreur lors de la confirmation de l\'email');
        }
    }

    async resendConfirmation(email) {
        try {
            console.log('📧 Renvoi de l\'email de confirmation');
            const response = await api.post(`${this.baseUrl}/resend-confirmation`, { email });
            console.log('✅ Email de confirmation renvoyé');
            return response.data;
        } catch (error) {
            console.error('❌ Erreur lors du renvoi de l\'email:', error);
            throw new Error('Erreur lors du renvoi de l\'email de confirmation');
        }
    }

    logout() {
        console.log('👋 Déconnexion');
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }

    getCurrentUser() {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    }

    isAuthenticated() {
        return !!localStorage.getItem('token');
    }
}

export default new AuthService(); 