import api from './api';

class UserService {
    constructor() {
        this.baseUrl = '/api/users';
    }

    async getCurrentUser() {
        try {
            const response = await api.get(`${this.baseUrl}/me`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la récupération de l\'utilisateur courant:', error);
            throw error;
        }
    }

    async getUserRole() {
        try {
            const response = await api.get(`${this.baseUrl}/me/role`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la récupération du rôle:', error);
            throw error;
        }
    }

    async updateUser(userId, userData) {
        try {
            const response = await api.put(`${this.baseUrl}/${userId}`, userData);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la mise à jour de l\'utilisateur:', error);
            throw error;
        }
    }

    async updateUserWithAvatar(userId, userData, avatarFile) {
        try {
            const formData = new FormData();
            formData.append('userData', JSON.stringify(userData));
            if (avatarFile) {
                formData.append('avatarFile', avatarFile);
            }

            const response = await api.put(`${this.baseUrl}/${userId}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la mise à jour de l\'utilisateur avec avatar:', error);
            throw error;
        }
    }

    async deleteUser(userId) {
        try {
            await api.delete(`${this.baseUrl}/${userId}`);
        } catch (error) {
            console.error('Erreur lors de la suppression de l\'utilisateur:', error);
            throw error;
        }
    }

    async changePassword(userId, oldPassword, newPassword) {
        try {
            await api.put(`${this.baseUrl}/${userId}/password`, {
                oldPassword,
                newPassword
            });
        } catch (error) {
            console.error('Erreur lors du changement de mot de passe:', error);
            throw error;
        }
    }
}

export default new UserService(); 