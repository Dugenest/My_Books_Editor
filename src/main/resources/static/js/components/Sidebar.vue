<template>
  <div class="sidebar">
    <div class="sidebar-header">
      <h3>Menu</h3>
    </div>
    
    <div class="sidebar-menu">
      <!-- Menu pour tous les utilisateurs -->
      <div class="menu-section">
        <h4>Navigation</h4>
        <router-link to="/books" class="menu-item">
          <i class="fas fa-book"></i>
          <span>Livres</span>
        </router-link>
        <router-link to="/authors" class="menu-item">
          <i class="fas fa-user-edit"></i>
          <span>Auteurs</span>
        </router-link>
        <router-link to="/categories" class="menu-item">
          <i class="fas fa-tags"></i>
          <span>Catégories</span>
        </router-link>
      </div>

      <!-- Menu pour les utilisateurs authentifiés -->
      <div v-if="isAuthenticated" class="menu-section">
        <h4>Mon Compte</h4>
        <router-link to="/basket" class="menu-item">
          <i class="fas fa-shopping-cart"></i>
          <span>Panier</span>
        </router-link>
        <router-link to="/orders" class="menu-item">
          <i class="fas fa-clipboard-list"></i>
          <span>Commandes</span>
        </router-link>
        <router-link to="/profile" class="menu-item">
          <i class="fas fa-user"></i>
          <span>Profil</span>
        </router-link>
      </div>

      <!-- Menu pour les auteurs -->
      <div v-if="hasRole('AUTHOR')" class="menu-section">
        <h4>Espace Auteur</h4>
        <router-link to="/dashboard/authors" class="menu-item">
          <i class="fas fa-pen-fancy"></i>
          <span>Mes Livres</span>
        </router-link>
        <router-link to="/dashboard/authors/stats" class="menu-item">
          <i class="fas fa-chart-line"></i>
          <span>Statistiques</span>
        </router-link>
      </div>

      <!-- Menu pour les éditeurs -->
      <div v-if="hasRole('EDITOR')" class="menu-section">
        <h4>Espace Éditeur</h4>
        <router-link to="/dashboard" class="menu-item">
          <i class="fas fa-tachometer-alt"></i>
          <span>Tableau de Bord</span>
        </router-link>
        <router-link to="/dashboard/editors" class="menu-item">
          <i class="fas fa-building"></i>
          <span>Gestion Éditeur</span>
        </router-link>
        <router-link to="/dashboard/books" class="menu-item">
          <i class="fas fa-book"></i>
          <span>Gestion Livres</span>
        </router-link>
      </div>

      <!-- Menu pour les administrateurs -->
      <div v-if="hasRole('ADMIN')" class="menu-section">
        <h4>Administration</h4>
        <router-link to="/dashboard" class="menu-item">
          <i class="fas fa-tachometer-alt"></i>
          <span>Tableau de Bord</span>
        </router-link>
        <router-link to="/dashboard/users" class="menu-item">
          <i class="fas fa-users"></i>
          <span>Utilisateurs</span>
        </router-link>
        <router-link to="/dashboard/authors" class="menu-item">
          <i class="fas fa-user-edit"></i>
          <span>Auteurs</span>
        </router-link>
        <router-link to="/dashboard/editors" class="menu-item">
          <i class="fas fa-building"></i>
          <span>Éditeurs</span>
        </router-link>
        <router-link to="/dashboard/categories" class="menu-item">
          <i class="fas fa-tags"></i>
          <span>Catégories</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  name: 'Sidebar',
  computed: {
    ...mapState({
      user: state => state.user
    }),
    isAuthenticated() {
      return this.user !== null;
    }
  },
  methods: {
    hasRole(role) {
      if (!this.user || !this.user.roles) return false;
      
      // Vérifier avec et sans le préfixe ROLE_
      return this.user.roles.includes(role) || this.user.roles.includes('ROLE_' + role);
    }
  }
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  height: 100vh;
  background-color: #2c3e50;
  color: white;
  position: fixed;
  left: 0;
  top: 0;
  overflow-y: auto;
}

.sidebar-header {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #34495e;
}

.menu-section {
  padding: 15px;
}

.menu-section h4 {
  color: #95a5a6;
  font-size: 0.9em;
  margin-bottom: 10px;
  padding-left: 10px;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  color: #ecf0f1;
  text-decoration: none;
  border-radius: 4px;
  margin-bottom: 5px;
  transition: background-color 0.3s;
}

.menu-item:hover {
  background-color: #34495e;
}

.menu-item i {
  margin-right: 10px;
  width: 20px;
  text-align: center;
}

.menu-item.router-link-active {
  background-color: #3498db;
}

.menu-item.router-link-active:hover {
  background-color: #2980b9;
}
</style> 