<template>
  <div class="basket">
    <h2>Mon Panier</h2>
    
    <div v-if="loading" class="text-center">
      <div class="spinner-border" role="status">
        <span class="sr-only">Chargement...</span>
      </div>
    </div>

    <div v-else-if="error" class="alert alert-danger" role="alert">
      {{ error }}
    </div>

    <div v-else-if="!basketBooks || basketBooks.length === 0" class="text-center">
      <p>Votre panier est vide</p>
      <router-link to="/books" class="btn btn-primary">
        Parcourir les livres
      </router-link>
    </div>

    <div v-else>
      <div class="basket-items">
        <div v-for="item in basketBooks" :key="item.id" class="basket-item card mb-3">
          <div class="row no-gutters">
            <div class="col-md-2">
              <img :src="item.book.picture || '/img/default-cover.jpg'" 
                   :alt="item.book.title"
                   class="card-img">
            </div>
            <div class="col-md-7">
              <div class="card-body">
                <h5 class="card-title">{{ item.book.title }}</h5>
                <p class="card-text">
                  <small class="text-muted">
                    {{ item.book.author ? `${item.book.author.firstName} ${item.book.author.lastName}` : 'Auteur inconnu' }}
                  </small>
                </p>
                <p class="card-text">{{ formatPrice(item.book.price) }}</p>
              </div>
            </div>
            <div class="col-md-3">
              <div class="card-body text-right">
                <div class="quantity-controls">
                  <button class="btn btn-sm btn-outline-secondary"
                          @click="updateQuantity(item, item.quantity - 1)"
                          :disabled="item.quantity <= 1">
                    -
                  </button>
                  <span class="mx-2">{{ item.quantity }}</span>
                  <button class="btn btn-sm btn-outline-secondary"
                          @click="updateQuantity(item, item.quantity + 1)"
                          :disabled="item.quantity >= item.book.stock">
                    +
                  </button>
                </div>
                <button class="btn btn-sm btn-danger mt-2"
                        @click="removeItem(item)">
                  Supprimer
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="basket-summary card mt-3">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <h5 class="card-title mb-0">Total</h5>
            <h5 class="mb-0">{{ formatPrice(total) }}</h5>
          </div>
          <div class="d-flex justify-content-between mt-3">
            <button class="btn btn-outline-danger" @click="clearBasket">
              Vider le panier
            </button>
            <button class="btn btn-primary" @click="checkout">
              Passer la commande
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BasketService from '../services/BasketService';

export default {
  name: 'Basket',
  data() {
    return {
      basketBooks: [],
      total: 0,
      loading: true,
      error: null
    };
  },
  async created() {
    await this.loadBasket();
  },
  methods: {
    async loadBasket() {
      try {
        this.loading = true;
        this.error = null;
        
        // Récupérer l'ID du panier depuis le localStorage
        const basketId = localStorage.getItem('basketId');
        if (!basketId) {
          this.basketBooks = [];
          this.total = 0;
          this.loading = false;
          return;
        }
        
        // Charger les livres du panier avec le bon endpoint
        const [books, total] = await Promise.all([
          BasketService.getBasketBooks(basketId),
          BasketService.getBasketTotal(basketId)
        ]);
        
        this.basketBooks = books;
        this.total = total;
      } catch (error) {
        console.error('Erreur lors du chargement du panier:', error);
        this.error = 'Une erreur est survenue lors du chargement du panier';
      } finally {
        this.loading = false;
      }
    },
    
    async updateQuantity(item, newQuantity) {
      try {
        if (newQuantity < 1 || newQuantity > item.book.stock) return;
        
        const basketId = localStorage.getItem('basketId');
        await BasketService.updateBookQuantity(basketId, item.book.id, newQuantity);
        await this.loadBasket();
      } catch (error) {
        console.error('Erreur lors de la mise à jour de la quantité:', error);
        this.error = 'Une erreur est survenue lors de la mise à jour de la quantité';
      }
    },
    
    async removeItem(item) {
      try {
        const basketId = localStorage.getItem('basketId');
        await BasketService.removeBookFromBasket(basketId, item.book.id);
        await this.loadBasket();
      } catch (error) {
        console.error('Erreur lors de la suppression du livre:', error);
        this.error = 'Une erreur est survenue lors de la suppression du livre';
      }
    },
    
    async clearBasket() {
      if (!confirm('Êtes-vous sûr de vouloir vider votre panier ?')) return;
      
      try {
        const basketId = localStorage.getItem('basketId');
        await BasketService.clearBasket(basketId);
        await this.loadBasket();
      } catch (error) {
        console.error('Erreur lors de la suppression du panier:', error);
        this.error = 'Une erreur est survenue lors de la suppression du panier';
      }
    },
    
    checkout() {
      // Rediriger vers la page de commande
      this.$router.push('/checkout');
    },
    
    formatPrice(price) {
      return new Intl.NumberFormat('fr-FR', {
        style: 'currency',
        currency: 'EUR'
      }).format(price);
    }
  }
};
</script>

<style scoped>
.basket {
  padding: 20px;
}

.basket-item {
  transition: all 0.3s ease;
}

.basket-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.basket-item img {
  max-height: 150px;
  object-fit: cover;
}

.quantity-controls {
  display: flex;
  align-items: center;
  justify-content: center;
}

.quantity-controls button {
  width: 30px;
  height: 30px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style> 