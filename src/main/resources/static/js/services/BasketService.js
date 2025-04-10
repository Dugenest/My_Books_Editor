import api from './api';

class BasketService {
    // Créer un nouveau panier
    async createBasket(basket) {
        try {
            const response = await api.post('/api/basket/create', basket);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la création du panier:', error);
            throw error;
        }
    }

    // Obtenir un panier par son ID
    async getBasketById(id) {
        try {
            const response = await api.get(`/api/basket/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Erreur lors du chargement du panier ${id}:`, error);
            throw error;
        }
    }

    // Ajouter un livre au panier
    async addBookToBasket(basketId, bookId, quantity) {
        try {
            const response = await api.post(`/api/basket-books/baskets/${basketId}/books/${bookId}?quantity=${quantity}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de l\'ajout du livre au panier:', error);
            throw error;
        }
    }

    // Retirer un livre du panier
    async removeBookFromBasket(basketId, bookId) {
        try {
            await api.delete(`/api/basket-books/baskets/${basketId}/books/${bookId}`);
        } catch (error) {
            console.error('Erreur lors de la suppression du livre du panier:', error);
            throw error;
        }
    }

    // Mettre à jour la quantité d'un livre
    async updateBookQuantity(basketId, bookId, quantity) {
        try {
            const response = await api.put(`/api/basket-books/baskets/${basketId}/books/${bookId}?quantity=${quantity}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la mise à jour de la quantité:', error);
            throw error;
        }
    }

    // Obtenir les livres d'un panier
    async getBasketBooks(basketId) {
        try {
            const response = await api.get(`/api/basket-books/baskets/${basketId}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors du chargement des livres du panier:', error);
            throw error;
        }
    }

    // Calculer le total du panier
    async getBasketTotal(basketId) {
        try {
            const response = await api.get(`/api/basket-books/baskets/${basketId}/total`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors du calcul du total du panier:', error);
            throw error;
        }
    }

    // Vider le panier
    async clearBasket(basketId) {
        try {
            await api.delete(`/api/basket-books/baskets/${basketId}/clear`);
        } catch (error) {
            console.error('Erreur lors de la suppression du panier:', error);
            throw error;
        }
    }
}

export default new BasketService(); 