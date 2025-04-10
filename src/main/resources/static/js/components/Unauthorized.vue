<template>
  <div class="unauthorized-container">
    <div class="unauthorized-card">
      <div class="unauthorized-icon">
        <i class="fas fa-exclamation-triangle"></i>
      </div>
      <h1>Accès non autorisé</h1>
      <p>Vous n'avez pas les droits suffisants pour accéder à cette page.</p>
      <p>Veuillez contacter l'administrateur si vous pensez qu'il s'agit d'une erreur.</p>
      <div class="unauthorized-actions">
        <router-link to="/" class="btn btn-primary">
          <i class="fas fa-home"></i> Retour à l'accueil
        </router-link>
        <router-link to="/login" class="btn btn-secondary">
          <i class="fas fa-sign-in-alt"></i> Se connecter
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Unauthorized'
}
</script>

<style scoped>
.unauthorized-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  padding: 20px;
}

.unauthorized-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  padding: 40px;
  max-width: 500px;
  text-align: center;
}

.unauthorized-icon {
  font-size: 64px;
  color: #e74c3c;
  margin-bottom: 20px;
}

h1 {
  color: #e74c3c;
  margin-bottom: 20px;
}

p {
  color: #555;
  margin-bottom: 10px;
  font-size: 16px;
}

.unauthorized-actions {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.btn {
  display: inline-block;
  padding: 10px 20px;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover {
  background-color: #2980b9;
}

.btn-secondary {
  background-color: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background-color: #7f8c8d;
}
</style> 