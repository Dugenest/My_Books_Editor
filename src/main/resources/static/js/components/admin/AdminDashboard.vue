<template>
  <div class="admin-dashboard">
    <div v-if="loading" class="loading">
      Chargement...
    </div>
    <div v-else-if="error" class="error">
      {{ error }}
    </div>
    <div v-else>
      <!-- Statistiques -->
      <div class="stats-grid">
        <div class="stat-card">
          <h3>Utilisateurs</h3>
          <p class="stat-value">{{ stats.totalUsers }}</p>
          <p class="stat-change" :class="{ 'positive': stats.usersChange > 0, 'negative': stats.usersChange < 0 }">
            {{ stats.usersChange }}%
          </p>
        </div>
        <div class="stat-card">
          <h3>Livres</h3>
          <p class="stat-value">{{ stats.totalBooks }}</p>
          <p class="stat-stock" v-if="stats.outOfStock > 0">
            {{ stats.outOfStock }} en rupture
          </p>
        </div>
        <div class="stat-card">
          <h3>Auteurs</h3>
          <p class="stat-value">{{ stats.totalAuthors }}</p>
        </div>
        <div class="stat-card">
          <h3>Éditeurs</h3>
          <p class="stat-value">{{ stats.totalEditors }}</p>
        </div>
        <div class="stat-card">
          <h3>Catégories</h3>
          <p class="stat-value">{{ stats.totalCategories }}</p>
        </div>
        <div class="stat-card">
          <h3>Commandes</h3>
          <p class="stat-value">{{ stats.totalOrders }}</p>
          <p class="stat-change" :class="{ 'positive': stats.ordersChange > 0, 'negative': stats.ordersChange < 0 }">
            {{ stats.ordersChange }}%
          </p>
        </div>
      </div>

      <!-- Activité récente -->
      <div class="recent-activity">
        <h2>Activité récente</h2>
        <div class="activity-list">
          <div v-for="activity in recentActivity" :key="activity.id" class="activity-item">
            <span class="activity-type">{{ activity.type }}</span>
            <span class="activity-user">{{ activity.user }}</span>
            <span class="activity-details">{{ activity.details }}</span>
            <span class="activity-time">{{ formatDate(activity.timestamp) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../../services/api';
import CategoryService from '../../services/CategoryService';
import BookService from '../../services/BookService';
import UserService from '../../services/UserService';
import AuthorService from '../../services/AuthorService';
import EditorService from '../../services/EditorService';

export default {
  name: 'AdminDashboard',
  data() {
    return {
      loading: true,
      error: null,
      stats: {
        totalUsers: 0,
        totalBooks: 0,
        totalAuthors: 0,
        totalEditors: 0,
        totalCategories: 0,
        totalOrders: 0,
        outOfStock: 0,
        usersChange: 0,
        ordersChange: 0
      },
      recentActivity: []
    };
  },
  methods: {
    async loadDashboardData() {
      try {
        console.log('Chargement des données du tableau de bord...');
        this.loading = true;
        
        // Appel API pour les statistiques
        console.log('Appel API pour les statistiques...');
        const statsResponse = await api.get('/dashboard/stats');
        console.log('Réponse des statistiques:', statsResponse.data);
        
        if (statsResponse.data) {
          this.stats = {
            totalUsers: statsResponse.data.totalUsers || 0,
            totalBooks: statsResponse.data.totalBooks || 0,
            totalAuthors: statsResponse.data.totalAuthors || 0,
            totalEditors: statsResponse.data.totalEditors || 0,
            totalCategories: statsResponse.data.totalCategories || 0,
            totalOrders: statsResponse.data.totalOrders || 0,
            outOfStock: statsResponse.data.outOfStock || 0,
            usersChange: statsResponse.data.usersChange || 0,
            ordersChange: statsResponse.data.ordersChange || 0
          };
        }
        
        // Chargement des catégories
        console.log('Chargement des catégories...');
        const categoriesResponse = await CategoryService.getAllCategories();
        console.log('Catégories chargées:', categoriesResponse.content);
        this.stats.totalCategories = categoriesResponse.totalElements || 0;
        
        // Chargement des livres
        const booksResponse = await BookService.getBooks();
        console.log('Nombre de livres:', booksResponse.totalElements);
        this.stats.totalBooks = booksResponse.totalElements || 0;
        
        // Chargement des utilisateurs
        const usersResponse = await UserService.getUsers(0, 1);
        console.log('Nombre d\'utilisateurs:', usersResponse.totalElements);
        this.stats.totalUsers = usersResponse.totalElements || 0;
        
        // Chargement des auteurs
        const authorsResponse = await AuthorService.getAllAuthors();
        console.log('Nombre d\'auteurs:', authorsResponse.totalElements);
        this.stats.totalAuthors = authorsResponse.totalElements || 0;
        
        // Chargement des éditeurs
        const editorsResponse = await EditorService.getEditors();
        console.log('Nombre d\'éditeurs:', editorsResponse.totalElements);
        this.stats.totalEditors = editorsResponse.totalElements || 0;
        
        // Chargement de l'activité récente
        console.log('Appel API pour l\'activité récente...');
        const activitiesResponse = await api.get('/activities/recent?limit=10');
        console.log('Réponse de l\'activité récente:', activitiesResponse.data);
        this.recentActivity = activitiesResponse.data || [];
        console.log('Activité récente chargée:', this.recentActivity);
        
      } catch (error) {
        console.error('Erreur lors du chargement des données du tableau de bord:', error);
        this.error = 'Erreur lors du chargement des données';
      } finally {
        this.loading = false;
      }
    },
    formatDate(timestamp) {
      return new Date(timestamp).toLocaleString('fr-FR');
    }
  },
  mounted() {
    this.loadDashboardData();
  }
};
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin: 10px 0;
}

.stat-change {
  font-size: 14px;
}

.stat-change.positive {
  color: #4caf50;
}

.stat-change.negative {
  color: #f44336;
}

.stat-stock {
  color: #f44336;
  font-size: 14px;
}

.recent-activity {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.activity-list {
  margin-top: 15px;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.activity-type {
  font-weight: bold;
  margin-right: 10px;
}

.activity-user {
  color: #666;
  margin-right: 10px;
}

.activity-details {
  flex-grow: 1;
}

.activity-time {
  color: #999;
  font-size: 14px;
}

.loading, .error {
  text-align: center;
  padding: 20px;
  font-size: 18px;
}

.error {
  color: #f44336;
}
</style> 