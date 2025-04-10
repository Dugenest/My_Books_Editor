import store from '../store';

export const authGuard = (to, from, next) => {
  if (!store.getters.isAuthenticated) {
    next('/login');
    return;
  }
  next();
};

export const roleGuard = (roles) => {
  return (to, from, next) => {
    if (!store.getters.isAuthenticated) {
      next('/login');
      return;
    }

    const userRoles = store.getters.userRoles;
    // Afficher les rôles de l'utilisateur pour le débogage
    console.log('Rôles utilisateur:', userRoles);
    console.log('Rôles requis:', roles);
    
    const hasRequiredRole = roles.some(role => userRoles.includes(role));

    if (hasRequiredRole) {
      next();
    } else {
      next('/unauthorized');
    }
  };
};

export const adminGuard = (to, from, next) => {
  roleGuard(['ADMIN'])(to, from, next);
};

export const authorGuard = (to, from, next) => {
  roleGuard(['ADMIN', 'AUTHOR'])(to, from, next);
};

export const editorGuard = (to, from, next) => {
  roleGuard(['ADMIN', 'EDITOR'])(to, from, next);
};

export const userGuard = (to, from, next) => {
  roleGuard(['ADMIN', 'USER', 'AUTHOR', 'EDITOR'])(to, from, next);
}; 